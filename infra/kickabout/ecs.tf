resource "aws_ecs_cluster" "main" {
  name = "kickabout"
}

resource "aws_cloudwatch_log_group" "app" {
  name              = "/ecs/kickabout"
  retention_in_days = 7
}
resource "aws_iam_role" "execution" {
  name = "kickabout-ecs-execution"

  assume_role_policy = jsonencode({
    Version = "2012-10-17"
    Statement = [{
      Effect    = "Allow"
      Principal = { Service = "ecs-tasks.amazonaws.com" }
      Action    = "sts:AssumeRole"
    }]
  })
}

resource "aws_iam_role_policy" "execution" {
  name = "least-privilege"
  role = aws_iam_role.execution.id

  policy = jsonencode({
    Version = "2012-10-17"
    Statement = [
      {
        Sid      = "LogInToEcr"
        Effect   = "Allow"
        Action   = "ecr:GetAuthorizationToken"
        Resource = "*"
      },
      {
        Sid    = "PullKickAboutImageOnly"
        Effect = "Allow"
        Action = [
          "ecr:BatchGetImage",
          "ecr:GetDownloadUrlForLayer",
          "ecr:BatchCheckLayerAvailability"
        ]
        Resource = aws_ecr_repository.app.arn
      },
      {
        Sid      = "WriteOwnLogsOnly"
        Effect   = "Allow"
        Action   = ["logs:CreateLogStream", "logs:PutLogEvents"]
        Resource = "${aws_cloudwatch_log_group.app.arn}:*"
      },
      {
        Sid      = "ReadDbPasswordOnly"
        Effect   = "Allow"
        Action   = "secretsmanager:GetSecretValue"
        Resource = aws_db_instance.main.master_user_secret[0].secret_arn
      }
    ]
  })
}
resource "aws_ecs_task_definition" "app" {
  family                   = "kickabout"
  requires_compatibilities = ["FARGATE"]
  network_mode             = "awsvpc"
  cpu                      = 512
  memory                   = 1024
  execution_role_arn       = aws_iam_role.execution.arn

  runtime_platform {
    operating_system_family = "LINUX"
    cpu_architecture        = "X86_64"
  }

  container_definitions = jsonencode([{
    name         = "kickabout"
    image        = "${aws_ecr_repository.app.repository_url}:1.0"
    essential    = true
    portMappings = [{ containerPort = 8080 }]

    environment = [
      { name = "SPRING_DATASOURCE_URL", value = "jdbc:postgresql://${aws_db_instance.main.address}:5432/kickabout" },
      { name = "SPRING_DATASOURCE_USERNAME", value = "kickabout" }
    ]

    secrets = [
      { name = "SPRING_DATASOURCE_PASSWORD", valueFrom = "${aws_db_instance.main.master_user_secret[0].secret_arn}:password::" }
    ]

    logConfiguration = {
      logDriver = "awslogs"
      options = {
        awslogs-group         = aws_cloudwatch_log_group.app.name
        awslogs-region        = "eu-west-2"
        awslogs-stream-prefix = "app"
      }
    }
  }])
}
resource "aws_ecs_service" "app" {
  name            = "kickabout"
  cluster         = aws_ecs_cluster.main.id
  task_definition = aws_ecs_task_definition.app.arn
  desired_count   = 1
  launch_type     = "FARGATE"

  health_check_grace_period_seconds = 120

  network_configuration {
    subnets          = data.aws_subnets.default.ids
    security_groups  = [aws_security_group.app.id]
    assign_public_ip = true
  }

  load_balancer {
    target_group_arn = aws_lb_target_group.app.arn
    container_name   = "kickabout"
    container_port   = 8080
  }

  depends_on = [aws_lb_listener.http]
}