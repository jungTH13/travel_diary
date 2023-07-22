## index 생성 및 feild 정의
resource "null_resource" "index_mapping"{
  depends_on = [ null_resource.start_es_node, null_resource.start_kibana ]

  for_each = fileset("${path.module}/files/index","*.json")

  provisioner "local-exec" {
    command = "curl -X PUT ${aws_instance.es_node[0].public_ip}:9200/${split(".json",each.value)[0]} -d @${path.module}/files/index/${each.value} -H \"Content-Type: application/json\""
  }
}

## END



## elastic env 설정
resource "null_resource" "init_env_start"{
  depends_on = [ null_resource.index_mapping ]

  provisioner "local-exec" {
    command = "rm ${path.module}/files/.env"
  }
}
resource "null_resource" "init_env0"{
  depends_on = [ null_resource.init_env_start ]

  count = length(var.batch_envs)

  provisioner "local-exec" {
    command = "echo ${var.batch_envs[count.index]} >> ${path.module}/files/.env"
  }
}

resource "null_resource" "init_env1"{
  depends_on = [ null_resource.init_env0 ]

  count = length(aws_instance.es_node)
#   for_each = [for instance in aws_instance.es_node : instance.public_ip]

  provisioner "local-exec" {
    command = "echo ES_NODE${count.index+1}_URL=http://${aws_instance.es_node[count.index].public_ip}:9200 >> ${path.module}/files/.env"
  }
}
resource "null_resource" "init_env_end"{
  depends_on = [ null_resource.init_env1 ]
  provisioner "local-exec" {
    command = "echo KIBANA_URL=http://${aws_instance.kibana.public_ip}:5601 >> ${path.module}/files/.env"
  }
  provisioner "local-exec" {
    command = "echo PROJECT_NAME=${var.env} >> ${path.module}/files/.env"
  }
  provisioner "local-exec" {
    command = "echo PROJECT_DATE=${formatdate("YYYY-MM-DD", timestamp())} >> ${path.module}/files/.env"
  }

}
## END



## elasticSearch 인덱스 데이터 초기화
resource "null_resource" "init_batch"{
  depends_on = [ null_resource.init_env_end ]

  provisioner "local-exec" {
    command = "cd ${path.module}/files && npm i"
  }

  provisioner "local-exec" {
    command = "cd ${path.module}/files && node batch.js"
  }
}
## END

