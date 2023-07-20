## IAM role & policy
resource "aws_iam_role" "codedeploy_iam_role" {
  name                = "ecs-deploy-role"
  assume_role_policy  = file("${path.module}/files/role.json")
}

resource "aws_iam_policy" "codedeploy_iam_policy" {
  name = "ecs-deploy-policy"
  policy = file("${path.module}/files/policy.json")
}

resource "aws_iam_role_policy_attachment" "codepipeline_policy" {
  role = aws_iam_role.codedeploy_iam_role.name
  policy_arn = aws_iam_policy.codedeploy_iam_policy.arn
}

### END

## 생성된 network 정보 로드
# data "aws_lb" "ecs_alb" {
#   name = var.env
# }

data "aws_lb_listener" "listener" {
  load_balancer_arn = var.aws_lb.arn
  port = var.listener_port
}

### END

resource "aws_codedeploy_app" "codedeploy_app" {
  name = var.env
  compute_platform = "ECS"

  tags = {
    env = var.env
  }
}

resource "aws_codedeploy_deployment_group" "codedeploy_deployment_group" {
    depends_on = [ aws_codedeploy_app.codedeploy_app ]

    app_name                = aws_codedeploy_app.codedeploy_app.name
    deployment_group_name   = var.group_name
    service_role_arn        = aws_iam_role.codedeploy_iam_role.arn
    deployment_config_name  = "CodeDeployDefault.ECSAllAtOnce"

    auto_rollback_configuration {
        enabled = true
        events = [ "DEPLOYMENT_FAILURE" ]
    }

    blue_green_deployment_config {
      deployment_ready_option {
        action_on_timeout = "CONTINUE_DEPLOYMENT"
      }

      terminate_blue_instances_on_deployment_success {
        action                           = "TERMINATE"
        termination_wait_time_in_minutes = var.termination_wait_time_in_minutes
      }
    }

    deployment_style {
      deployment_option = "WITH_TRAFFIC_CONTROL"
      deployment_type   = "BLUE_GREEN"
    }

    ecs_service {
      cluster_name = var.env
      service_name = var.ecs_service_name
    }

    load_balancer_info {
      target_group_pair_info {
        prod_traffic_route {
          listener_arns = [data.aws_lb_listener.listener.arn]
          # listener_arns = [ for listener in data.aws_lb_listener.listener : listener.arn ]
        }

        target_group {
          name = var.target_groups[0].name
        }

        target_group {
          name = var.target_groups[1].name
        }
      }
    }

    tags = {
      name = var.env
    }
}


