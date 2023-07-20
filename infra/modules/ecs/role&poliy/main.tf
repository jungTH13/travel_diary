## IAM role & policy
resource "aws_iam_policy" "ecs_service_policy" {
  name = "${var.env}-ecs-task-execution-policy"
  policy = file("${path.module}/files/policy.json")
}

resource "aws_iam_role" "ecs_service_role" {
  name = "${var.env}-ecs-task-execution-role"
  assume_role_policy = file("${path.module}/files/role.json")
}

resource "aws_iam_role_policy_attachment" "ecs_service_policy" {
  role = aws_iam_role.ecs_service_role.name
  policy_arn = aws_iam_policy.ecs_service_policy.arn
}



# ec2 파일 생성을 위한 권한 설정
data "aws_iam_policy_document" "ecs_agent" {
  statement {
    actions = ["sts:AssumeRole"]

    principals {
      type        = "Service"
      identifiers = ["ec2.amazonaws.com"]
    }
  }
}

resource "aws_iam_role" "ecs_agent" {
  name               = "${var.env}-ecs-agent"
  assume_role_policy = data.aws_iam_policy_document.ecs_agent.json
}


resource "aws_iam_role_policy_attachment" "ecs_agent" {
  role       = aws_iam_role.ecs_agent.name
  policy_arn = "arn:aws:iam::aws:policy/service-role/AmazonEC2ContainerServiceforEC2Role"
}

resource "aws_iam_instance_profile" "ecs_agent" {
  name = "${var.env}-ecs-agent"
  role = aws_iam_role.ecs_agent.name
}

### END

