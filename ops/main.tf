terraform {
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 5.17"
    }
  }
}

# State for EKS Cluster
data "terraform_remote_state" "eks" {
  backend = "s3"
  config = {
    bucket = "techchallenge-soat12-db-state-db"
    key = "infra/terraform.tfstate"
    region = "us-east-1"
  }
}

# State for RDS Database
data "terraform_remote_state" "rds" {
  backend = "s3"
  config = {
    bucket = "techchallenge-soat12-db-state-db"
    key    = "rds/terraform.tfstate"
    region = "us-east-1"
  }
}