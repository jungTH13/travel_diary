resource "aws_security_group_rule" "kibana" {

  type              = "ingress"
  from_port         = 0
  to_port           = 9400
  protocol          = "tcp"
  cidr_blocks       = ["${aws_instance.kibana.private_ip}/32"]
  security_group_id = aws_security_group.elasticSearch.id
}

resource "aws_security_group_rule" "kibana_serve" {
  type              = "ingress"
  from_port         = 5601
  to_port           = 5601
  protocol          = "tcp"
  cidr_blocks       = ["${var.vpc.cidr_block}"]
  security_group_id = aws_security_group.elasticSearch.id
}

resource "aws_lb_target_group_attachment" "kibana" {
  target_group_arn = var.kibana_target_group.arn
  target_id        = aws_instance.kibana.private_ip
  port             = 5601
}


# kibana
resource "aws_instance" "kibana" {
  depends_on = [null_resource.start_es_node]

  ami                    = var.ami_id
  instance_type          = var.kibana_instance_type
  vpc_security_group_ids = [aws_security_group.elasticSearch.id]
  subnet_id              = var.subnet_id
  key_name               = var.key_pair

  tags = {
    Name = "${var.env}-kibana"
    env  = var.env
  }
}

# 탄력적 IP 설정
resource "aws_eip_association" "kibana_eip_assoc" {
  count         = var.kibana_eip_id == "" ? 0 : 1

  instance_id   = aws_instance.kibana.id
  allocation_id = var.kibana_eip_id
}

# kibana 환경 구축 스크립트
resource "null_resource" "start_kibana" {
  depends_on = [aws_instance.kibana, aws_eip_association.kibana_eip_assoc]

  connection {
    host        = aws_instance.kibana.public_ip
    type        = "ssh"
    user        = "ubuntu"
    private_key = file("${path.module}/files/${var.key_pair}.pem")
  }

  provisioner "file" {
    content     = templatefile("${path.module}/files/kibana/docker-compose.yml",{
      es_all_node_public_url_list = "[${join(",", [for instance in aws_instance.es_node : "\"http://${instance.private_ip}:9200\""])}]"
    })
    destination = "./docker-compose.yml"
  }

  provisioner "remote-exec" {
    inline = [
      "sudo apt update",
      "sudo apt update",
      "sudo apt install -y docker.io docker-compose",
      "sudo docker-compose up -d"
    ]
  }
}