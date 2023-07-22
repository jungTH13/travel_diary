
# ecs에서 실행하는 서비스를 배포할 타겟 그룹 로드
data "aws_lb_target_group" "target_group" {
  count = length(var.target_group)

  name = var.target_group[count.index].name
}


# container_definitions 정의 관련 참고 사이트
# https://docs.aws.amazon.com/ko_kr/AmazonECS/latest/developerguide/task-definition-template.html
# https://docs.aws.amazon.com/ko_kr/AmazonECS/latest/developerguide/task_definition_parameters.html
# 예제 : https://docs.aws.amazon.com/ko_kr/AmazonECS/latest/developerguide/example_task_definitions.html
resource "aws_ecs_service" "service" {
  name            						        = var.service_name
  cluster         						        = aws_ecs_cluster.cluster.id
  task_definition 						        = aws_ecs_task_definition.service.arn
  scheduling_strategy   				      = "REPLICA"
  launch_type 							          = "EC2"
  desired_count   						        = 1
  deployment_minimum_healthy_percent 	= 100
  deployment_maximum_percent         	= 200
  force_new_deployment 					      = true
#   iam_role        = aws_iam_role.ecs_service_role.arn
#   depends_on      = [aws_iam_policy.ecs_service_policy]


  health_check_grace_period_seconds = 120
  network_configuration{
    subnets = var.subnet_ids
    security_groups = [var.security_group.id]
    assign_public_ip = false
  }



  ordered_placement_strategy {
    type  = "binpack"
    field = "cpu"
  }

  deployment_controller {
    type = "CODE_DEPLOY"
  }

  load_balancer {
    target_group_arn = data.aws_lb_target_group.target_group[0].arn
    container_name   = var.env_container_name
    container_port   = var.container_port
  }

  lifecycle {
    ignore_changes = [load_balancer,desired_count,task_definition,deployment_controller]
  }
}

# ecs 새성시 기본 실행 task 정의(deploy 시 교체될 원본 container)
resource "aws_ecs_task_definition" "service" {
  family = var.service_name
  network_mode = "awsvpc" # awsvpc
  requires_compatibilities = ["EC2"]
  task_role_arn = var.ecs_service_role.arn
  execution_role_arn = var.ecs_service_role.arn
  cpu                      = var.container_cpu
  memory                   = var.container_memory

  # service가 등록 되었을 경우 해당 task가 바로 실행됨
  container_definitions = jsonencode([
    {
      name      = var.env_container_name
      image     = "${var.aws_id}.dkr.ecr.${var.region}.amazonaws.com/${var.env}-${var.env_container_name}"
      cpu       = var.container_cpu
      memory    = var.container_memory
      essential = true
      portMappings = [
        {
		  protocol = "tcp"
          containerPort = var.container_port
          hostPort      = var.container_port
        }
      ]
	  logConfiguration = {
            logDriver = "awslogs",
            options = {
				awslogs-create-group = "true"
                awslogs-group = "/ecs/${var.env}",
                awslogs-region = "${var.region}",
                awslogs-stream-prefix = "ecs"
            }
        },
    }
  ])

  runtime_platform {
    operating_system_family = "LINUX"
    cpu_architecture        = "X86_64"
  }
}


## ECS 오토스케일링 설정
# https://registry.terraform.io/providers/hashicorp/aws/latest/docs/resources/appautoscaling_policy#policy_type

# # ECS service의 배포 task 개수 오토스케일링 설정
# resource "aws_appautoscaling_target" "ecs_service" {
#   max_capacity       = var.auto_scaling.max
#   min_capacity       = var.auto_scaling.min
#   resource_id        = "service/${aws_ecs_cluster.cluster.name}/${aws_ecs_service.service.name}"
#   scalable_dimension = "ecs:service:DesiredCount"
#   service_namespace  = "ecs"
# }

# # ECS service 오토스케일링 인스턴스 조정 규칙
# resource "aws_appautoscaling_policy" "ecs_up_policy" {
#   name               = "scale-up"
#   policy_type        = "TargetTrackingScaling"
#   resource_id        = aws_appautoscaling_target.ecs_service.resource_id
#   scalable_dimension = aws_appautoscaling_target.ecs_service.scalable_dimension
#   service_namespace  = aws_appautoscaling_target.ecs_service.service_namespace

#   target_tracking_scaling_policy_configuration {
#     predefined_metric_specification {
#       predefined_metric_type = "ECSServiceAverageCPUUtilization"
#     }

#     target_value       = var.auto_scaling.CPUUtilization_level
#     scale_in_cooldown  = 300
#     scale_out_cooldown = 180
#   }
# }

## END