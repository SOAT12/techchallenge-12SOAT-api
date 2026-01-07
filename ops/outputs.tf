output "cluster_name" {
  value = data.terraform_remote_state.eks.outputs.cluster_name
}

output "cluster_endpoint" {
  value = data.terraform_remote_state.eks.outputs.cluster_endpoint
}

output "cluster_certificate_authority_data" {
  value = data.terraform_remote_state.eks.outputs.cluster_certificate_authority_data
}

output "rds_endpoint" {
  description = "The endpoint of the RDS database."
  value       = data.terraform_remote_state.rds.outputs.rds_endpoint
}

output "rds_db_name" {
  description = "The name of the database in RDS."
  value       = data.terraform_remote_state.rds.outputs.db_name
}

output "rds_secret_name" {
  description = "The name of the Secrets Manager secret containing DB credentials."
  value       = data.terraform_remote_state.rds.outputs.secret_name
}
