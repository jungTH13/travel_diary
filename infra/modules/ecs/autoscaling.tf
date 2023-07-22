
## auto scaling
resource "aws_autoscaling_group_tag" "ecs_autoscaling_group_tag" {
  depends_on = [aws_autoscaling_group.ecs_autoscaling_group]

  autoscaling_group_name = aws_autoscaling_group.ecs_autoscaling_group.name

  tag {
    key = "env"
    value = var.env
    propagate_at_launch = true
  }

  lifecycle {
    ignore_changes = [
      tag
    ]
  }
}

# TODO : 변수화 하여 최상위단으로 옮겨야함
# 일본리전 Amazon Linux 2 ami-0d5142f63c808d143
# 한국리전 Amazon Linux 2 ami-06b9122710049dfe7
resource "aws_launch_template" "ecs_container_instance_template" {
  name = var.cluster_name
  image_id = "ami-06b9122710049dfe7" # Amazon Linux 2
  instance_type = var.instance_type
  key_name = var.key_pair_name
  user_data = "${base64encode(<<EOF
    #!/bin/bash
    amazon-linux-extras disable docker
    amazon-linux-extras install -y ecs
    yum install ruby -y
    echo ECS_CLUSTER=${aws_ecs_cluster.cluster.id} >> /etc/ecs/ecs.config
    echo ECS_CONTAINER_STOP_TIMEOUT=2s >> /etc/ecs/ecs.config
    echo ECS_CONTAINER_START_TIMEOUT=5s >> /etc/ecs/ecs.config
    echo ECS_CONTAINER_CREATE_TIMEOUT=5s >> /etc/ecs/ecs.config
    systemctl enable --now --no-block ecs
    cd /home/ec2-user
    wget https://aws-codedeploy-${var.region}.s3.${var.region}.amazonaws.com/latest/install
    chmod +x ./install
    ./install auto
    service codedeploy-agent status
  EOF
  )}"

  network_interfaces {
    security_groups = [var.security_group.id]
	  associate_public_ip_address = true
  }

  iam_instance_profile {
    name = var.autoscaling_profile.name
  }

  block_device_mappings {
    device_name = "/dev/sda1"

    ebs {
      volume_type = "gp2" # 범용 SSD
      volume_size = 8
    }
  }

  tag_specifications {
    resource_type = "instance"

    tags = {
      env = var.env
      Name = "${var.cluster_name}-ecs-asg"
    }
  }

  tags = {
    env = var.env
  }
}


resource "aws_autoscaling_group" "ecs_autoscaling_group" {

  name = "ECS-Cluster-${var.cluster_name}"
  vpc_zone_identifier = [var.autoscaling_subnet_id] # var.subnet_ids
  desired_capacity = var.auto_scaling.min
  min_size = var.auto_scaling.min
  max_size = var.auto_scaling.max + 1
  termination_policies = [ "OldestInstance" ] # OldestInstance, NewestInstance, OldestLaunchConfiguration, ClosestToNextInstanceHour, OldestLaunchTemplate, AllocationStrategy

  enabled_metrics = [
    "GroupDesiredCapacity",
  ]
  launch_template {
    id = aws_launch_template.ecs_container_instance_template.id
    version = "$Latest"
  }

  lifecycle {
    ignore_changes = [
      desired_capacity,
      tag
    ]
  }
}

# EC2 오토스케일링 인스턴스 조정 규칙
resource "aws_autoscaling_policy" "ecs_autoscaling_utilization_rate_high_policy" {
  depends_on = [aws_autoscaling_group.ecs_autoscaling_group]

  name = "${var.cluster_name}-UtilizationRateHigh"
  autoscaling_group_name = aws_autoscaling_group.ecs_autoscaling_group.name
  adjustment_type = "ChangeInCapacity"
  scaling_adjustment = 1
  cooldown = 300
}

resource "aws_autoscaling_policy" "ecs_autoscaling_utilization_rate_low_policy" {
  depends_on = [aws_autoscaling_group.ecs_autoscaling_group]

  name = "${var.cluster_name}-UtilizationRateLow"
  autoscaling_group_name = aws_autoscaling_group.ecs_autoscaling_group.name
  adjustment_type = "ChangeInCapacity"
  scaling_adjustment = -1
  cooldown = 300
}