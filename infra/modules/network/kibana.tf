# 타겟 그룹 설정
resource "aws_lb_target_group" "kibana" {
  count = 2

  name          = "${var.env}-kibana-lb-tg-${count.index}"
  port          = var.kibana.port
  target_type   = "ip"
  protocol      = "HTTP"
  vpc_id        = aws_vpc.main.id

  health_check {
    healthy_threshold   = 3
    unhealthy_threshold = 3
	timeout             = 10
    interval            = 15
    matcher             = var.kibana.health_check.matcher
    path                = var.kibana.health_check.path
    protocol            = "HTTP"
  }
}


# aws 보안 그룹 생성
resource "aws_security_group" "kibana" {
  name = "${var.env}_kibana_security_gorup"
  description = "${var.env}_kibana_security_gorup"
  vpc_id = aws_vpc.main.id

  egress {
    from_port        = 0
    to_port          = 0
    protocol         = "-1"
    cidr_blocks      = ["0.0.0.0/0"]
    ipv6_cidr_blocks = ["::/0"]
  }
}

resource "aws_security_group_rule" "kibana" {
  count = length(local.kibana_security_group_ports)

  type              = "ingress"
  from_port         = local.kibana_security_group_ports[count.index]
  to_port           = local.kibana_security_group_ports[count.index]
  protocol          = "tcp"
  cidr_blocks       = ["0.0.0.0/0"]
  security_group_id = aws_security_group.kibana.id
}



# 리스너 등록
resource "aws_lb_listener" "kibana" {# 기본 접속 요청을 거부
  count = length(var.kibana.listener_ports)

  load_balancer_arn = aws_lb.main.arn
  port              = var.kibana.listener_ports[count.index]
  protocol          = "HTTPS"
  ssl_policy        = "ELBSecurityPolicy-2016-08"
  certificate_arn   = aws_acm_certificate.cert.arn

  default_action {
    type = "fixed-response"

    fixed_response {
      content_type = "text/plain"
      message_body = "connection denied"
      status_code  = "403"
    }
  }
  # lifecycle {
  #   ignore_changes = [default_action]
  # }
}

resource "aws_lb_listener_rule" "kibana" {# 해당 ip로부터의 접속만 라우팅
  count = length(aws_lb_listener.kibana)

  listener_arn = aws_lb_listener.kibana[count.index].arn
  priority     = 100

  action {
    type             = "forward"
    target_group_arn = aws_lb_target_group.kibana[0].arn
  }

  condition {
    source_ip {
      values = [ for ip in var.access_ips : "${ip}/32"]
    }
  }
}

# 타겟 그룹 알람 설정
# resource "aws_cloudwatch_metric_alarm" "kibana_health_check" {
#   count = length(aws_lb_target_group.kibana)

#   alarm_name          = "${aws_lb_target_group.kibana[count.index].name}-kibana is not Healthy"
#   comparison_operator = "GreaterThanThreshold"
#   evaluation_periods  = 1
#   threshold           = 0

#   alarm_description   = "kibana 상태 감시"
#   alarm_actions       = [aws_sns_topic.network_alarms.arn]
#   ok_actions          = [aws_sns_topic.network_alarms.arn]

#   metric_query {
#     id          = "m1"
#     expression  = "SELECT MAX(UnHealthyHostCount) FROM \"AWS/ApplicationELB\" WHERE TargetGroup = '${aws_lb_target_group.kibana[count.index].arn_suffix}'"
#     label       = "test"
#     period      = 60
#     return_data = "true"
#   }
# }