output "app" {
  value = aws_codedeploy_app.codedeploy_app
}

output "deploy_role" {
  value = aws_iam_role.codedeploy_iam_role
}