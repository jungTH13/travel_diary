output "vpc" {
  value = aws_vpc.main
}

output "lb" {
  value = aws_lb.main
}

# 서브넷
output "public_subnet1" {
  value = aws_subnet.public_subnet1
}

output "public_subnet2" {
  value = aws_subnet.public_subnet2
}

output "private_subnet" {
  value = aws_subnet.private_subnet
}


# 대상 그룹
output "target_groups_server" {
  value = aws_lb_target_group.server
}

output "target_groups_client" {
  value = aws_lb_target_group.client
}

output "target_groups_admin" {
  value = aws_lb_target_group.admin
}

output "target_groups_kibana" {
  value = aws_lb_target_group.kibana
}


# 보안 그룹
output "security_group_server" {
  value = aws_security_group.server
}

output "security_group_client" {
  value = aws_security_group.client
}

output "security_group_admin" {
  value = aws_security_group.admin
}

output "security_group_kibana" {
  value = aws_security_group.kibana
}