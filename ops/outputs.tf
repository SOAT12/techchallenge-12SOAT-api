output "cluster_name" {
  value = data.terraform_remote_state.infra.outputs.cluster_name
}

output "cluster_endpoint" {
  value = data.terraform_remote_state.infra.outputs.cluster_endpoint
}

output "cluster_certificate_authority_data" {
  value = data.terraform_remote_state.infra.outputs.cluster_certificate_authority_data
}

output "rds_endpoint" {
  description = "The endpoint of the RDS database."
  value       = data.terraform_remote_state.infra.outputs.rds_endpoint
}

output "rds_db_name" {
  description = "The name of the database in RDS."
  value       = data.terraform_remote_state.infra.outputs.rds_db_name
}

output "rds_username" {
  description = "The username for the RDS database."
  value       = data.terraform_remote_state.infra.outputs.rds_username
  sensitive   = true
}

output "rds_password" {
  description = "The password for the RDS database."
  value       = data.terraform_remote_state.infra.outputs.rds_password
  sensitive   = true
}
