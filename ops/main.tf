terraform {
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 5.17"
    }
  }
}

# Configura o mesmo backend, mas apenas para LEITURA do estado
data "terraform_remote_state" "infra" {
  backend = "s3"
  config = {
    bucket = "techchallenge-12soat-terraform-state-bucket"
    key    = "dev/eks-cluster/terraform.tfstate"
    region = "sa-east-1"
  }
}
