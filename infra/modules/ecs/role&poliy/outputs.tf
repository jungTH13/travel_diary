output "service_role" {
  value = aws_iam_role.ecs_service_role
}

output "ecs_agent_iam_instance_profile" {
  value = aws_iam_instance_profile.ecs_agent
}