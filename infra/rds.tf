# Security group for the RDS instance
resource "aws_security_group" "rds_sg" {
  name        = "techchallenge-rds-sg"
  description = "Allow traffic from EKS nodes to RDS"
  vpc_id      = module.vpc.vpc_id

  # Allow inbound traffic from the EKS cluster nodes
  ingress {
    from_port       = 5432
    to_port         = 5432
    protocol        = "tcp"
    security_groups = [module.eks.node_security_group_id]
  }

  # Allow all outbound traffic
  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
}

# Subnet group for the RDS instance
resource "aws_db_subnet_group" "rds_subnet_group" {
  name       = "techchallenge-rds-subnet-group"
  subnet_ids = module.vpc.private_subnets

  tags = {
    Name = "TechChallenge DB Subnet Group"
  }
}

# RDS PostgreSQL instance
resource "aws_db_instance" "postgres_db" {
  identifier        = "techchallenge-db"
  allocated_storage = 20
  engine            = "postgres"
  engine_version         = "15.14"
  instance_class         = "db.t3.micro"
  db_name                = "postgres"
  username               = "postgres"
  password               = "password"
  db_subnet_group_name   = aws_db_subnet_group.rds_subnet_group.name
  vpc_security_group_ids = [aws_security_group.rds_sg.id]
  skip_final_snapshot    = true
  publicly_accessible    = false
}
