resource "aws_security_group" "elasticSearch" {
  name = "${var.env}-elastic-access"
  vpc_id = var.vpc.id

  egress { # outbound
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }

  tags = {
    env = var.env
  }
}

resource "aws_security_group_rule" "elasticSearch_ext" {
  # count = length(security_access_ips)

  type              = "ingress"
  from_port         = 0
  to_port           = 9400
  protocol          = "tcp"
  cidr_blocks       = [for ip in var.access_ips : "${ip}/32"]
  security_group_id = aws_security_group.elasticSearch.id
}

resource "aws_security_group_rule" "elasticSearch_node_private" {
  depends_on = [ aws_instance.es_node ]

  type              = "ingress"
  from_port         = 0
  to_port           = 9400
  protocol          = "tcp"
  cidr_blocks       = [for instance in aws_instance.es_node : "${instance.private_ip}/32"]
  security_group_id = aws_security_group.elasticSearch.id
}

resource "aws_security_group_rule" "elasticSearch_node_public" {
  depends_on = [ aws_instance.es_node ]

  type              = "ingress"
  from_port         = 0
  to_port           = 9400
  protocol          = "tcp"
  cidr_blocks       = [for instance in aws_instance.es_node : "${instance.public_ip}/32"]
  security_group_id = aws_security_group.elasticSearch.id
}



# elastic search node 생성
# private network 를 중심으로 클러스터링
resource "aws_instance" "es_node" {
  count                  = var.node_count
  ami                    = var.ami_id
  instance_type          = var.node_instance_type
  vpc_security_group_ids = [aws_security_group.elasticSearch.id]
  subnet_id              = var.subnet_id
  key_name               = var.key_pair

  tags = {
    Name = "${var.env}-es${count.index+1}"
    env  = var.env
  }
}

resource "aws_eip_association" "es_eip_assoc" {
  count         = var.node_count <= length(var.node_eip_ids) ? var.node_count : 0

  instance_id   = aws_instance.es_node[count.index].id
  allocation_id = var.node_eip_ids[count.index]
}


resource "null_resource" "start_es_node" {
  # depends_on = [data.template_file.es_node_docker_compose]
  depends_on = [ aws_instance.es_node, aws_eip_association.es_eip_assoc ]
  count = var.node_count

  provisioner "local-exec" {
    command = "echo ${aws_instance.es_node[count.index].public_ip}"
  }

  connection {
    host        = aws_instance.es_node[count.index].public_ip
    type        = "ssh"
    user        = "ubuntu"
    private_key = file("${path.module}/files/${var.key_pair}.pem")
  }



  provisioner "file" {
    content     = templatefile("${path.module}/files/es/docker-compose.yml",{
      es_node_name               = "es${count.index}"
      es_node_public_ip          = aws_instance.es_node[count.index].public_ip
      es_all_node_name_csv       = "[${join(",",[for index in range(0,var.node_count) : "\"es${index}\""])}]" # 클러스터가 최초 실행 될 때 명시된 노드들을 대상으로 마스터 노드를 선출
      es_all_node_public_ip_list = "[${join(",",[for instance in aws_instance.es_node : "\"${instance.private_ip}\""])}]" # 클러스터 구성을 위해 바인딩 할 원격 노드의 IP 또는 도메인 주소
    })
    destination = "./docker-compose.yml"
  }

  # provisioner "file" {
  #   source = "./tools/${var.cw_agent_file_name}"
  #   destination = "${var.ubuntu_main_path}/${var.cw_agent_file_name}"
  # }

  provisioner "remote-exec" {
    inline = [
      "sudo apt update",
      "sudo apt update",
      "sudo apt install -y docker.io docker-compose",
      "sudo sysctl -w vm.max_map_count=262144",
      "mkdir data",
      "sudo docker-compose up -d",
      # "wget https://s3.amazonaws.com/amazoncloudwatch-agent/ubuntu/amd64/latest/amazon-cloudwatch-agent.deb",
      # "sudo dpkg -i -E ./amazon-cloudwatch-agent.deb",
      # "sudo /opt/aws/amazon-cloudwatch-agent/bin/amazon-cloudwatch-agent-ctl -a fetch-config -m ec2 -s -c file:${var.ubuntu_main_path}/${var.cw_agent_file_name}"
    ]
  }
}
