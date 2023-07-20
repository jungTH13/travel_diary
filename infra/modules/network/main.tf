#참고 : https://bosungtea9416.tistory.com/11

## Security Goroup
# 해당 이름의 그룹이 유일하지 않은 경우 id = "<SECURITY_GROUP_ID>" 를 추가해야한다.

resource "aws_vpc" "main" {
  cidr_block = "192.168.0.0/16"
  tags = {
    Name = var.env
  }
}

## 인터넷 통신을 위한 인터넷 게이트웨이
resource "aws_internet_gateway" "IGW" {
  vpc_id = aws_vpc.main.id
  tags = {
    Name = "${var.env}_vpc_IGW"
  }
}

resource "aws_nat_gateway" "NAT" {
  depends_on = [aws_internet_gateway.IGW, aws_subnet.public_subnet1]

  allocation_id = var.NAT_allocation_id
  subnet_id     = aws_subnet.public_subnet1.id

  tags = {
    Name = "${var.env}_vpc_NAT"
  }
}

### END


## 서브넷 설정
# 공개 서브넷 설정 (인터넷 게이트웨이와 연결)
resource "aws_subnet" "public_subnet1" {
  vpc_id = aws_vpc.main.id
  cidr_block = "192.168.0.0/24"

  availability_zone = "${var.aws_region}a"

  map_public_ip_on_launch = true

  tags = {
    Name = "${var.env}_public_subnet1"
  }
}

resource "aws_subnet" "public_subnet2" {
  vpc_id = aws_vpc.main.id
  cidr_block = "192.168.1.0/24"

  availability_zone = "${var.aws_region}c"

  map_public_ip_on_launch = true

  tags = {
    Name = "${var.env}_public_subnet2"
  }
}

# 비공개 서브넷 설정 (NAT를 통해서 인터넷 게이트웨이에 연결)
# - public ip가 없을 경우 외부 통신 불가하기 때문에 NAT 사용
resource "aws_subnet" "private_subnet" {
  vpc_id = aws_vpc.main.id
  cidr_block = "192.168.2.0/24"

  availability_zone = "${var.aws_region}a"

  map_public_ip_on_launch = true

  tags = {
    Name = "${var.env}_private_subnet"
  }
}

### END

## 라우팅 테이블 생성
resource "aws_route_table" "public_rt" {
  vpc_id = aws_vpc.main.id

  route {
    cidr_block = "0.0.0.0/0"
    gateway_id = aws_internet_gateway.IGW.id
  }

  tags = {
    Name = "${var.env}_public_rt"
  }
}

# private 라우팅 테이블 생성
resource "aws_route_table" "private_rt" {
  vpc_id = aws_vpc.main.id

  route {
    cidr_block = "0.0.0.0/0"
    gateway_id = aws_nat_gateway.NAT.id
  }

  tags = {
    Name = "${var.env}_private_rt"
  }
}

# 서브넷을 라우팅 테이블에 연결
resource "aws_route_table_association" "public_rt_association_1" {
  subnet_id      = aws_subnet.public_subnet1.id
  route_table_id = aws_route_table.public_rt.id
}

resource "aws_route_table_association" "public_rt_association_2" {
  subnet_id      = aws_subnet.public_subnet2.id
  route_table_id = aws_route_table.public_rt.id
}

# private 서브넷을 라우팅 테이블에 연결
resource "aws_route_table_association" "public_rt_association_3" {
  subnet_id      = aws_subnet.private_subnet.id
  route_table_id = aws_route_table.private_rt.id
}

### END


## 로드 밸런서 설정

resource "aws_lb" "main" {
  name               = var.env
  internal           = false
  load_balancer_type = "application"
  security_groups    = [
    aws_security_group.server.id,
    aws_security_group.client.id,
    aws_security_group.admin.id,
    aws_security_group.kibana.id
  ]
  subnets            = [
    aws_subnet.public_subnet1.id,
    aws_subnet.public_subnet2.id,
    # aws_subnet.private_subnet.id
  ]

  enable_deletion_protection = false

  # access_logs {
  #   bucket  = var.aws.s3_name
  #   prefix  = "main"
  #   enabled = true
  # }
}

## END

# 인증서 발급
resource "aws_acm_certificate" "cert" {
  domain_name       = var.domain_name
  validation_method = "DNS"

  lifecycle {
    create_before_destroy = true
  }
}

## 알람 설정

# aws-sns-topic (sns 주제 생성)
resource "aws_sns_topic" "network_alarms" {
  name            = "${var.env}-network-alarms-topic"
  delivery_policy = <<EOF
{
  "http": {
    "defaultHealthyRetryPolicy": {
      "minDelayTarget": 20,
      "maxDelayTarget": 20,
      "numRetries": 3,
      "numMaxDelayRetries": 0,
      "numNoDelayRetries": 0,
      "numMinDelayRetries": 0,
      "backoffFunction": "linear"
    },
    "disableSubscriptionOverrides": false,
    "defaultThrottlePolicy": {
      "maxReceivesPerSecond": 1
    }
  }
}
EOF
}

# 로드 밸런스 에러률 감시
resource "aws_cloudwatch_metric_alarm" "load_balancer_error_rate" {
  alarm_name                = "ALB-${aws_lb.main.name} Request error rate has exceeded 10%"
  comparison_operator       = "GreaterThanOrEqualToThreshold"
  evaluation_periods        = 2
  threshold                 = 10
  alarm_description         = "Request error rate has exceeded 10%"
  alarm_actions             = [aws_sns_topic.network_alarms.arn]
  ok_actions                = [aws_sns_topic.network_alarms.arn]

  metric_query {
    id          = "e1"
    expression  = "m2/m1*100"
    label       = "Error Rate"
    return_data = "true"
  }

  metric_query {
    id = "m1"

    metric {
      metric_name = "RequestCount"
      namespace   = "AWS/ApplicationELB"
      period      = 120
      stat        = "Sum"

      dimensions = {
        LoadBalancer = aws_lb.main.arn_suffix
      }
    }
  }

  metric_query {
    id = "m2"

    metric {
      metric_name = "HTTPCode_ELB_5XX_Count"
      namespace   = "AWS/ApplicationELB"
      period      = 120
      stat        = "Sum"

      dimensions = {
        LoadBalancer = aws_lb.main.arn_suffix
      }
    }
  }
}


## END