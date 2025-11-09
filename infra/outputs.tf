output "rds_endpoint" {
  description = "The endpoint of the RDS instance"
  value       = aws_db_instance.postgres_db.endpoint
}

output "cluster_name" {
  description = "The name of the EKS cluster"
  value = module.eks.cluster_name
}

output "configure_kubectl" {
  description = "Command to configure kubectl for the EKS cluster"
  value = "aws eks update-kubeconfig --name ${module.eks.cluster_name} --region sa-east-1"
}
