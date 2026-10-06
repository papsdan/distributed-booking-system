terraform {
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 6.0"
    }
  }
}

provider "aws" {
  region = "eu-west-2"

  default_tags {
    tags = {
      project    = "kickabout"
      component  = "github-ci"
      managed_by = "terraform"
    }
  }
}

# 1. Tell AWS to trust ID cards issued by GitHub Actions
resource "aws_iam_openid_connect_provider" "github" {
  url            = "https://token.actions.githubusercontent.com"
  client_id_list = ["sts.amazonaws.com"]
}

# 2. A role that ONLY my repo's pipeline can use
resource "aws_iam_role" "github_plan" {
  name = "kickabout-github-plan"

  assume_role_policy = jsonencode({
    Version = "2012-10-17"
    Statement = [{
      Effect    = "Allow"
      Principal = { Federated = aws_iam_openid_connect_provider.github.arn }
      Action    = "sts:AssumeRoleWithWebIdentity"
      Condition = {
        StringEquals = {
          "token.actions.githubusercontent.com:aud" = "sts.amazonaws.com"
          "token.actions.githubusercontent.com:sub" = [
            "repo:papsdan/distributed-booking-system:ref:refs/heads/master",
            "repo:papsdan/distributed-booking-system:pull_request"
          ]
        }
      }
    }]
  })
}

# 3. What the role can do: look, but not touch
resource "aws_iam_role_policy_attachment" "read_only" {
  role       = aws_iam_role.github_plan.name
  policy_arn = "arn:aws:iam::aws:policy/ReadOnlyAccess"
}

output "role_arn" {
  value = aws_iam_role.github_plan.arn
}