
## autoscaling group 알람

# metric_name 종류 참고 : https://docs.aws.amazon.com/ko_kr/AmazonCloudWatch/latest/monitoring/aws-services-cloudwatch-metrics.html
# resource "aws_cloudwatch_metric_alarm" "ec2_autoscailing_up" {
#   alarm_name          = "${var.cluster_name}-ec2_autocailing_instance_up"
#   comparison_operator = "GreaterThanOrEqualToThreshold"
#   evaluation_periods  = 1
#   period              = 60
#   threshold           = 100

#   alarm_description = "autoscaling group의 여유 인스턴스 관리(up)"
#   alarm_actions     = [aws_autoscaling_policy.ecs_autoscaling_utilization_rate_high_policy.arn]

#   metric_name         = "CPUReservation"
#   namespace           = "AWS/ECS"
#   statistic           = "Maximum"

#   dimensions = {
#     ClusterName = aws_ecs_cluster.cluster.name
#   }
# }

resource "aws_cloudwatch_metric_alarm" "ec2_autoscailing_down" {
  alarm_name          = "${var.cluster_name}-ec2_autocailing_instance_down"
  comparison_operator = "GreaterThanOrEqualToThreshold"
  evaluation_periods  = 10
  threshold           = 1

  alarm_description = "autoscaling group의 여유 인스턴스 관리(down)"
  alarm_actions     = [aws_autoscaling_policy.ecs_autoscaling_utilization_rate_low_policy.arn]

  metric_query {
    id          = "e1"
    expression  = "m1 - (m2/100) * m1"
    label       = "여유분 ec2 개수"
    return_data = "true"
  }

  metric_query {
    id = "m1"

    metric {
      metric_name = "GroupDesiredCapacity"
      namespace   = "AWS/AutoScaling"
      period      = 60
      stat        = "Average"
      # unit        = "Count" <- 해당 값으로 인해 지표 싱크가 틀어지기 때문에 입력 X

      dimensions = {
        AutoScalingGroupName = aws_autoscaling_group.ecs_autoscaling_group.name
      }
    }
  }

  metric_query {
    id = "m2"

    metric {
      metric_name = "CPUReservation"
      namespace   = "AWS/ECS"
      period      = 60
      stat        = "Average"

      dimensions = {
        ClusterName = aws_ecs_cluster.cluster.name
      }
    }
  }

}

# resource "aws_cloudwatch_metric_alarm" "ec2_autoscailing_up_task_count" {
#   alarm_name          = "${var.cluster_name}-ec2_autocailing_instance_up_task_count"
#   comparison_operator = "GreaterThanOrEqualToThreshold"
#   evaluation_periods  = 1
#   threshold           = 1

#   alarm_description = "autoscaling group의 여유 인스턴스 관리(up)"
#   alarm_actions     = [aws_autoscaling_policy.ecs_autoscaling_utilization_rate_high_policy.arn]

#   metric_query {
#     id          = "e1"
#     expression  = "m2 - m1"
#     label       = "setTask 개수 대비 ec2 부족 개수 "
#     return_data = "true"
#   }

#   metric_query {
#     id = "m1"

#     metric {
#       metric_name = "GroupDesiredCapacity"
#       namespace   = "AWS/AutoScaling"
#       period      = 60
#       stat        = "Maximum"
#       # unit        = "Count" <- 해당 값으로 인해 지표 싱크가 틀어지기 때문에 입력 X

#       dimensions = {
#         AutoScalingGroupName = aws_autoscaling_group.ecs_autoscaling_group.name
#       }
#     }
#   }

#   metric_query {
#     id = "m2"

#     metric {
#       metric_name = "TaskSetCount"
#       namespace   = "ECS/ContainerInsights"
#       period      = 60
#       stat        = "Maximum"

#       dimensions = {
#         ClusterName = aws_ecs_cluster.cluster.name
#         ServiceName = aws_ecs_service.service.name
#       }
#     }
#   }

# }

resource "aws_cloudwatch_metric_alarm" "ec2_autoscailing_up_build_count" {
  alarm_name          = "${var.cluster_name}-ec2_autocailing_instance_up_build_count"
  comparison_operator = "GreaterThanOrEqualToThreshold"
  evaluation_periods  = 1
  period              = 60
  threshold           = 1
  treat_missing_data  = "notBreaching"

  alarm_description = "autoscaling group의 여유 인스턴스 관리(up)"
  alarm_actions     = [aws_autoscaling_policy.ecs_autoscaling_utilization_rate_high_policy.arn]

  metric_name         = "CPUUtilized"
  namespace           = "AWS/CodeBuild"
  statistic           = "Maximum"

  dimensions = {
    ProjectName = aws_ecs_cluster.cluster.name
  }
}


## END

## ECS cpu 사용량 알람

# aws-sns-topic (sns 주제 생성)
resource "aws_cloudwatch_metric_alarm" "ecs_autoscailing_up" {
  alarm_name          = "${var.cluster_name} CPU Usage High"
  comparison_operator = "GreaterThanOrEqualToThreshold"
  evaluation_periods  = 2
  period              = 60
  threshold           = 80

  alarm_description   = "ECS CPU Uasge 감시"
  alarm_actions       = [var.sns_topic.arn]
  ok_actions          = [var.sns_topic.arn]

  metric_name         = "CPUUtilization"
  namespace           = "AWS/ECS"
  statistic           = "Average"

  dimensions = {
    ClusterName = aws_ecs_cluster.cluster.name
    ServiceName = aws_ecs_service.service.name
  }
}

## END