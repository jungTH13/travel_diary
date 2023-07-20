resource "aws_s3_bucket" "s3_infra" {
  bucket = local.aws.s3_infra
}

data "aws_eip" "private_network_allocation" {
  id = var.aws_data.allocation_id
}

resource "aws_codestarconnections_connection" "github_connection" {
  # 수동 설정 요소 : codepipeline 좌측 네비게이션바 -> 설정 -> 연결 -> 해당 connection -> 보류 중인 연결 업데이트
  name          = "github-connection"
  provider_type = "GitHub"
}

### END

module "network" {
  source                = "./modules/network"
  aws_region            = local.aws.region
  domain_name           = var.project.domain_name
  env                   = var.project.name
  aws_id                = local.aws.id
  alarm_name            = var.alarm.name

  server                = var.network.server
  client                = var.network.client
  admin                 = var.network.admin
  kibana                = var.network.kibana

  NAT_allocation_id     = var.aws_data.allocation_id
  access_ips            = var.project.access_ips
}

# ECS
module "ecs_role_policy" {
  source = "./modules/ecs/role&poliy"

  env    = var.project.name
}

module "ecs_sns_topic" {
  source = "./modules/ecs/sns_topic"

  env    = var.project.name
}
module "ecs_service" {
  count = length(var.infra_names)

  depends_on = [ module.network, module.ecs_role_policy ]

  source = "./modules/ecs"

  env                   = var.project.name
  region                = local.aws.region
  aws_id                = local.aws.id

  cluster_name          = var.ECS.container_spec[var.infra_names[count.index]].cluster_name
  service_name          = var.ECS.container_spec[var.infra_names[count.index]].service_name
  env_container_name    = var.ECS.container_spec[var.infra_names[count.index]].service_name

  instance_type         = var.ECS.container_spec[var.infra_names[count.index]].instance_type
  container_port		    = var.ECS.container_spec[var.infra_names[count.index]].port
  container_memory      = var.ECS.container_spec[var.infra_names[count.index]].memory
  container_cpu         = var.ECS.container_spec[var.infra_names[count.index]].cpu
  auto_scaling          = var.ECS.container_spec[var.infra_names[count.index]].auto_scaling

  target_group     		  = local.network_outputs["target_groups_${var.infra_names[count.index]}"] # local.network_outputs.target_groups_server
  security_group        = local.network_outputs["security_group_${var.infra_names[count.index]}"] # local.network_outputs.security_group_server


  subnet_ids            = [local.network_outputs.private_subnet.id]
  autoscaling_subnet_id = local.network_outputs.public_subnet1.id

  ecs_service_role      = module.ecs_role_policy.service_role
  autoscaling_profile   = module.ecs_role_policy.ecs_agent_iam_instance_profile
  sns_topic             = module.ecs_sns_topic.sns_topic

  key_pair_name         = var.aws_data.key_pair_name
}

# CodeBuild
module "codebuild" {
  count = length(var.infra_names)

  depends_on = [ aws_s3_bucket.s3_infra ]

  source                = "./modules/codebuild"
  region                = local.aws.region
  env                   = var.project.name

  group_name            = var.infra_names[count.index]
  container_port		    = var.ECS.container_spec[var.infra_names[count.index]].port
  container_memory      = var.ECS.container_spec[var.infra_names[count.index]].memory
  container_cpu         = var.ECS.container_spec[var.infra_names[count.index]].cpu

  aws_id                = local.aws.id
  aws_access_key        = local.aws.access_key
  aws_secret_key        = local.aws.secret_key
  aws_s3_name           = local.aws.s3_infra
  aws_s3_id             = aws_s3_bucket.s3_infra.id

  github_token          = var.github_token
  github_repository     = var.github_repository[var.infra_names[count.index]]
  github_branch         = var.github_branch[var.infra_names[count.index]]

  notification_events   = var.notification_events["codebuild"]
  notification_name     = var.notification_events.name
}

# CodeDeploy
module "codedeploy_app" {
  source = "./modules/codedeploy/app"

  env    = var.project.name
}

module "codedeploy_deploy" {
  count = length(var.infra_names)

  depends_on                        = [module.ecs_service, module.network, module.codedeploy_app]

  source                            = "./modules/codedeploy/deploy"
  env                               = var.project.name
  group_name                        = var.infra_names[count.index]
  ecs_cluster_name                  = var.ECS.container_spec[var.infra_names[count.index]].cluster_name
  ecs_service_name                  = var.ECS.container_spec[var.infra_names[count.index]].service_name

  target_groups                     = local.network_outputs["target_groups_${var.infra_names[count.index]}"] # local.network_outputs.target_groups_server
  aws_id                            = local.aws.id
  aws_lb                            = local.network_outputs.lb
  listener_port                     = var.ECS.container_spec[var.infra_names[count.index]].port

  codedeploy_app                    = module.codedeploy_app.app
  codedeploy_deploy_role            = module.codedeploy_app.deploy_role
  termination_wait_time_in_minutes  = var.ECS.termination_wait_time_in_minutes

  notification_events               = var.notification_events["codedeploy"]
  notification_name                 = var.notification_events.name
}

# CodePipeline
module "codepipeline" {
  count = length(var.infra_names)

  depends_on = [module.codebuild,module.codedeploy_deploy]

  source               = "./modules/codepipeline"
  region               = local.aws.region
  env                  = var.project.name
  group_name           = var.infra_names[count.index]

  aws_id               = local.aws.id
  aws_s3_name          = local.aws.s3_infra
  aws_s3_id            = aws_s3_bucket.s3_infra.id

  github_token         = var.github_token
  github_repository    = var.github_repository[var.infra_names[count.index]]
  github_branch        = var.github_branch[var.infra_names[count.index]]
  github_connection    = aws_codestarconnections_connection.github_connection

  notification_events  = var.notification_events["codepipeline"]
  notification_name     = var.notification_events.name
}