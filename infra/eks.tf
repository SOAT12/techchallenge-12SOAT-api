module "eks" {
  source  = "terraform-aws-modules/eks/aws"
  version = "19.15.3"

  cluster_name    = local.cluster_name
  cluster_version = "1.29"

  vpc_id     = module.vpc.vpc_id
  subnet_ids = module.vpc.public_subnets

  cluster_endpoint_public_access  = true
  cluster_endpoint_private_access = true

  eks_managed_node_groups = {
    main = {
      min_size       = 1
      max_size       = 3
      desired_size   = 2
      instance_types = ["t3.small"]
      associate_public_ip_address = true
      iam_role_additional_policies = {
        ecr_read_only = "arn:aws:iam::aws:policy/AmazonEC2ContainerRegistryReadOnly"
      }
    }
  }

  tags = {
    Terraform   = "true"
    Environment = "dev"
  }
}

resource "aws_iam_role" "aws_load_balancer_controller" {
  name = "${module.eks.cluster_name}-aws-load-balancer-controller"

  assume_role_policy = jsonencode({
    Version = "2012-10-17"
    Statement = [
      {
        Effect = "Allow"
        Principal = {
          Federated = "arn:aws:iam::${data.aws_caller_identity.current.account_id}:oidc-provider/${replace(module.eks.oidc_provider_arn, "arn:aws:iam::${data.aws_caller_identity.current.account_id}:oidc-provider/", "")}"
        }
        Action = "sts:AssumeRoleWithWebIdentity"
        Condition = {
          StringEquals = {
            "${replace(module.eks.oidc_provider_arn, "arn:aws:iam::${data.aws_caller_identity.current.account_id}:oidc-provider/", "")}:sub" = "system:serviceaccount:kube-system:aws-load-balancer-controller"
          }
        }
      }
    ]
  })
}

resource "aws_iam_policy" "aws_load_balancer_controller_policy" {
  name        = "${module.eks.cluster_name}-aws-load-balancer-controller-policy"
  policy      = file("${path.module}/iam_policy_load_balancer_controller.json")
}

resource "aws_iam_role_policy_attachment" "aws_load_balancer_controller" {
  policy_arn = aws_iam_policy.aws_load_balancer_controller_policy.arn
  role       = aws_iam_role.aws_load_balancer_controller.name
}

resource "kubernetes_service_account_v1" "aws_load_balancer_controller" {
  metadata {
    name      = "aws-load-balancer-controller"
    namespace = "kube-system"
    annotations = {
      "eks.amazonaws.com/role-arn" = aws_iam_role.aws_load_balancer_controller.arn
    }
  }
}




data "aws_secretsmanager_secret" "techchallenge_secrets" {
  name = "techchallenge-credentials"
}

data "aws_secretsmanager_secret_version" "techchallenge_secrets_version" {
  secret_id = data.aws_secretsmanager_secret.techchallenge_secrets.id
}

resource "aws_iam_policy" "secrets_manager_read_policy" {
  name        = "${module.eks.cluster_name}-secrets-manager-read-policy"
  policy = jsonencode({
    Version = "2012-10-17",
    Statement = [
      {
        Effect   = "Allow",
        Action   = [
          "secretsmanager:GetSecretValue",
          "secretsmanager:DescribeSecret"
        ],
        Resource = data.aws_secretsmanager_secret.techchallenge_secrets.arn
      }
    ]
  })
}

resource "aws_iam_role_policy_attachment" "secrets_manager_read_attachment" {
  policy_arn = aws_iam_policy.secrets_manager_read_policy.arn
  role       = module.eks.eks_managed_node_groups["main"].iam_role_name
}

resource "aws_iam_role" "techchallenge_app_role" {
  name = "${module.eks.cluster_name}-techchallenge-app-role"

  assume_role_policy = jsonencode({
    Version = "2012-10-17"
    Statement = [
      {
        Effect = "Allow"
        Principal = {
          Federated = "arn:aws:iam::${data.aws_caller_identity.current.account_id}:oidc-provider/${replace(module.eks.oidc_provider_arn, "arn:aws:iam::${data.aws_caller_identity.current.account_id}:oidc-provider/", "")}"
        }
        Action = "sts:AssumeRoleWithWebIdentity"
        Condition = {
          StringEquals = {
            "${replace(module.eks.oidc_provider_arn, "arn:aws:iam::${data.aws_caller_identity.current.account_id}:oidc-provider/", "")}:sub" = "system:serviceaccount:techchallenge:default"
          }
        }
      }
    ]
  })
}

resource "aws_iam_role_policy_attachment" "techchallenge_app_secrets_read_attachment" {
  policy_arn = aws_iam_policy.secrets_manager_read_policy.arn
  role       = aws_iam_role.techchallenge_app_role.name
}


