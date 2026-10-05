resource "aws_db_subnet_group" "main" {
  name       = "kickabout"
  subnet_ids = data.aws_subnets.default.ids
}

resource "aws_db_instance" "main" {
  identifier     = "kickabout-db"
  engine         = "postgres"
  engine_version = "17"
  instance_class = "db.t4g.micro"

  allocated_storage = 20
  storage_encrypted = true

  db_name                     = "kickabout"
  username                    = "kickabout"
  manage_master_user_password = true

  db_subnet_group_name   = aws_db_subnet_group.main.name
  vpc_security_group_ids = [aws_security_group.db.id]
  publicly_accessible    = false

  skip_final_snapshot = true
}

output "db_address" {
  value = aws_db_instance.main.address
}

output "db_secret_arn" {
  value = aws_db_instance.main.master_user_secret[0].secret_arn
}