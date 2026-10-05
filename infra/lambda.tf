# Two files in the bucket: one the Lambda may read, one it may not
resource "aws_s3_object" "welcome" {
  bucket       = aws_s3_bucket.practice.id
  key          = "welcome.txt"
  content      = "Welcome to KickAbout! Next game: Saturday 10am."
  content_type = "text/plain"
}

resource "aws_s3_object" "secret" {
  bucket       = aws_s3_bucket.practice.id
  key          = "secret.txt"
  content      = "Top secret: the admin password is not here."
  content_type = "text/plain"
}

resource "aws_s3_object" "rules" {
  bucket       = aws_s3_bucket.practice.id
  key          = "rules.txt"
  content      = "First rule of KickAbout is there are no rules!"
  content_type = "text/plain"
}

# Zip up the Python code
data "archive_file" "hello" {
  type        = "zip"
  source_file = "${path.module}/lambda/hello.py"
  output_path = "${path.module}/build/hello.zip"
}

# Where the Lambda's logs go, kept for 7 days
resource "aws_cloudwatch_log_group" "hello" {
  name              = "/aws/lambda/kickabout-hello"
  retention_in_days = 7
}

# The role the Lambda wears
resource "aws_iam_role" "hello" {
  name = "kickabout-hello-lambda"

  assume_role_policy = jsonencode({
    Version = "2012-10-17"
    Statement = [{
      Effect    = "Allow"
      Principal = { Service = "lambda.amazonaws.com" }
      Action    = "sts:AssumeRole"
    }]
  })
}

# What the role is allowed to do, and nothing else
resource "aws_iam_role_policy" "hello" {
  name = "least-privilege"
  role = aws_iam_role.hello.id

  policy = jsonencode({
    Version = "2012-10-17"
    Statement = [
      {
        Sid      = "ReadWelcomeFileOnly"
        Effect   = "Allow"
        Action   = "s3:GetObject"
        Resource = [
          "${aws_s3_bucket.practice.arn}/welcome.txt",
          "${aws_s3_bucket.practice.arn}/rules.txt",
          ]
      },
      {
        Sid      = "WriteOwnLogsOnly"
        Effect   = "Allow"
        Action   = ["logs:CreateLogStream", "logs:PutLogEvents"]
        Resource = "${aws_cloudwatch_log_group.hello.arn}:*"
      }
    ]
  })
}

# The Lambda itself
resource "aws_lambda_function" "hello" {
  function_name    = "kickabout-hello"
  role             = aws_iam_role.hello.arn
  runtime          = "python3.13"
  handler          = "hello.handler"
  filename         = data.archive_file.hello.output_path
  source_code_hash = data.archive_file.hello.output_base64sha256
  timeout          = 10

  environment {
    variables = {
      BUCKET_NAME = aws_s3_bucket.practice.bucket
    }
  }

  depends_on = [aws_cloudwatch_log_group.hello, aws_iam_role_policy.hello]
}

output "lambda_name" {
  value = aws_lambda_function.hello.function_name
}