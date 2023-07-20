## IAM role & policy
# codepipline을 위한 권한 설정
resource "aws_iam_role" "codepipeline_role" {
  name = "codepipeline-service-role-${var.env}-${var.group_name}"
  assume_role_policy = file("${path.module}/files/role.json")
  tags = {
    env = var.env
  }
}

resource "aws_iam_policy" "codepipeline_policy" {
  name = "codepipeline-service-policy-${var.env}-${var.group_name}"
  policy = file("${path.module}/files/policy.json")
}

resource "aws_iam_role_policy_attachment" "codepipeline_policy" {
  role = aws_iam_role.codepipeline_role.name
  policy_arn = aws_iam_policy.codepipeline_policy.arn
}

### END

# ## GITHUB
# resource "aws_codestarconnections_connection" "github_connection" {
#   # 수동 설정 요소 : codepipeline 좌측 네비게이션바 -> 설정 -> 연결 -> 해당 connection -> 보류 중인 연결 업데이트
#   name          = "github-connection"
#   provider_type = "GitHub"
# }

# CodePipeline
# https://docs.aws.amazon.com/ko_kr/codepipeline/latest/userguide/action-reference.html
resource "aws_codepipeline" "codepipeline" {
  name     = "${var.env}-${var.group_name}"
  role_arn = aws_iam_role.codepipeline_role.arn

  artifact_store { # required
    location = var.aws_s3_name
    type     = "S3"
  }

  stage {
    name = "Source"

    action {
      name             = "Source"
      category         = "Source"
      owner            = "AWS"
      provider         = "CodeStarSourceConnection"
      version          = "1"
      output_artifacts = ["source"]

      configuration = {
        ConnectionArn    = var.github_connection.arn
        FullRepositoryId = var.github_repository
        BranchName       = var.github_branch
        # 변경 감지 옵션
        DetectChanges = true
      }
    }
  }

  stage {
    name = "Build"

    action {
      name             = "Build"
      category         = "Build"
      owner            = "AWS"
      provider         = "CodeBuild"
      input_artifacts  = ["source"]
      output_artifacts = ["build"]
      version          = "1"

      configuration = {
        ProjectName = "${var.env}-${var.group_name}"
      }
    }
  }

#   stage {
#     name = "Increase"

#     action {
#       name = "Increase"
#       category = "Invoke"
#       owner = "AWS"
#       provider = "Lambda"
#       version = "1"

#       configuration = {
#         FunctionName = "blue_green_ecs_instance_management"
#         UserParameters = "${var.region},${var.env}"
#       }
#     }
#   }

  stage {
    name = "Deploy"

    action {
      name = "Deploy"
      category = "Deploy"
      owner = "AWS"
      provider = "CodeDeployToECS"
      input_artifacts = [ "build" ]
      version = "1"

      configuration = {
        ApplicationName = var.env
        DeploymentGroupName = var.group_name
        AppSpecTemplateArtifact = "build"
        AppSpecTemplatePath = "appspec.yaml"
        TaskDefinitionTemplateArtifact = "build"
        TaskDefinitionTemplatePath = "taskdef.json"
      }
    }
  }

#   stage {
#     name = "Decrease"

#     action {
#       name = "Decrease"
#       category = "Invoke"
#       owner = "AWS"
#       provider = "Lambda"
#       version = "1"

#       configuration = {
#         FunctionName = "blue_green_ecs_instance_management"
#         UserParameters = "${var.region},${var.env}"
#       }
#     }
#   }

  tags = {
    env = var.env
  }
}

# Notification
resource "aws_codestarnotifications_notification_rule" "codepipeline_notification" {
  name = "${var.env}-${var.group_name}-codepipeline-notification"
  resource = aws_codepipeline.codepipeline.arn
  detail_type = "FULL"
  event_type_ids = var.notification_events

  # aws chatbot을 사전에 세팅
  target {
    type = "AWSChatbotSlack"
    address = "arn:aws:chatbot::${var.aws_id}:chat-configuration/slack-channel/${var.notification_name}"
  }

  tags = {
    env = var.env
  }
}