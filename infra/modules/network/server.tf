# 타겟 그룹 설정
resource "aws_lb_target_group" "server" {
  count = 2

  name          = "${var.env}-server-lb-tg-${count.index}"
  port          = var.server.port
  target_type   = "ip" # ecs에서 network_mode = "awsvpc" 와 호환을 위해 사용
  protocol      = "HTTP"
  vpc_id        = aws_vpc.main.id

  health_check {
    healthy_threshold   = 3
    unhealthy_threshold = 3
	timeout             = 10
    interval            = 15
    matcher             = var.server.health_check.matcher
    path                = var.server.health_check.path
    protocol            = "HTTP"
  }
}

# aws 보안 그룹 생성
resource "aws_security_group" "server" {
  name = "${var.env}_server_security_gorup"
  description = "${var.env}_server_security_gorup"
  vpc_id = aws_vpc.main.id

  egress {
    from_port        = 0
    to_port          = 0
    protocol         = "-1"
    cidr_blocks      = ["0.0.0.0/0"]
    ipv6_cidr_blocks = ["::/0"]
  }
}

resource "aws_security_group_rule" "server" {
  count = length(local.server_security_group_ports)

  type              = "ingress"
  from_port         = local.server_security_group_ports[count.index]
  to_port           = local.server_security_group_ports[count.index]
  protocol          = "tcp"
  cidr_blocks       = ["0.0.0.0/0"]
  security_group_id = aws_security_group.server.id
}

# 리스너 등록
resource "aws_lb_listener" "server" {
  count = length(var.server.listener_ports)

  load_balancer_arn = aws_lb.main.arn
  port              = var.server.listener_ports[count.index]
  protocol          = "HTTPS"
  ssl_policy        = "ELBSecurityPolicy-2016-08"
  certificate_arn   = aws_acm_certificate.cert.arn
  default_action {
    type = "forward"
    
    forward {
      target_group{
		arn = aws_lb_target_group.server[0].arn
		weight = 100
      }
      target_group{
		arn = aws_lb_target_group.server[1].arn
		weight = 0
      }

      stickiness {
		enabled = false
		duration = 1
      }
    }
  }
  lifecycle {
    ignore_changes = [default_action]
  }
}

# 타겟 그룹 알람 설정
resource "aws_cloudwatch_metric_alarm" "server_health_check" {
  count = length(aws_lb_target_group.server)

  alarm_name          = "${aws_lb_target_group.server[count.index].name} is not Healthy"
  comparison_operator = "GreaterThanThreshold"
  evaluation_periods  = 1
  threshold           = 0

  alarm_description   = "server 상태 감시"
  alarm_actions       = [aws_sns_topic.network_alarms.arn]
  ok_actions          = [aws_sns_topic.network_alarms.arn]

  metric_query {
    id          = "m1"
    expression  = "SELECT MAX(UnHealthyHostCount) FROM \"AWS/ApplicationELB\" WHERE TargetGroup = '${aws_lb_target_group.server[count.index].arn_suffix}'"
    label       = "test"
    period      = 60
    return_data = "true"
  }
}