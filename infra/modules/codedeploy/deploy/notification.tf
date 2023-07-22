# Notification
# resource "aws_codestarnotifications_notification_rule" "codedeploy_notification" {
#   name = "${var.env}-${var.group_name}-codedeploy-notification"
#   resource = aws_codedeploy_deployment_group.codedeploy_deployment_group.arn
#   detail_type = "FULL"
#   event_type_ids = var.notification_events

#   # aws chatbot을 사전에 세팅
#   target {
#     type = "AWSChatbotSlack"
#     address = "arn:aws:chatbot::${var.aws_id}:chat-configuration/slack-channel/${var.notification_name}"
#   }

#   tags = {
#     env = var.env
#   }
# }