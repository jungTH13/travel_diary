resource "aws_codedeploy_app" "codedeploy_app" {
  name = var.env
  compute_platform = "ECS"

  tags = {
    env = var.env
  }
}

## IAM role & policy
resource "aws_iam_role" "codedeploy_iam_role" {
  name                = "${var.env}-ecs-deploy-role"
  assume_role_policy  = file("${path.module}/files/role.json")
}

resource "aws_iam_policy" "codedeploy_iam_policy" {
  name = "${var.env}-ecs-deploy-policy"
  policy = file("${path.module}/files/policy.json")
}

resource "aws_iam_role_policy_attachment" "codepipeline_policy" {
  role = aws_iam_role.codedeploy_iam_role.name
  policy_arn = aws_iam_policy.codedeploy_iam_policy.arn
}

### END

