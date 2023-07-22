# Notification
# resource "aws_codestarnotifications_notification_rule" "codebuild_notification" {
#   name        = "${local.ecr_name}-codebuild-notification"
#   resource    = aws_codebuild_project.codebuild.arn
#   detail_type = "FULL"
#   event_type_ids = var.notification_events

#   # aws chatbot을 사전에 세팅
#   target {
#     type    = "AWSChatbotSlack"
#     address = "arn:aws:chatbot::${var.aws_id}:chat-configuration/slack-channel/${var.notification_name}"
#   }

#   tags = {
#     env = var.env
#   }
# }