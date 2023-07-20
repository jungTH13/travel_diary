
## IAM role & policy
# codedeploy를 위한 권한 설정
resource "aws_iam_role" "codebuild_role" {
  name               = "codebuild-service-role-${local.ecr_name}"
  assume_role_policy = file("${path.module}/files/role.json")

  tags = {
    env = var.env
  }
}

resource "aws_iam_policy" "codebuild_policy" {
  name = "codebuild-service-policy-${local.ecr_name}"
  policy = templatefile("${path.module}/files/policy.json", {
    region        = var.region
    aws_id        = var.aws_id
	  aws_s3_name   = var.aws_s3_name
    resource_name = local.ecr_name
    role_name     = "codebuild-${var.env}-service-role"
  })

  tags = {
    env = var.env
  }
}

resource "aws_iam_role_policy_attachment" "ecr_policy" {
  role       = aws_iam_role.codebuild_role.name
  policy_arn = aws_iam_policy.codebuild_policy.arn
}

### END

## ECR
resource "aws_ecr_repository" "ecr" {
  name = local.ecr_name

  tags = {
    env = var.env
  }
}

### END

## S3 Upload
# Local의 infra관련 파일 Bucket으로 업로드
resource "aws_s3_bucket_object" "infra-files" {
    for_each = fileset("${path.module}/files/", "**")
        # fileset : 지정 경로에 대한 파일 이름 집합 열거
            # ** : fileset 함수에서 재귀 검색 패턴 적용
            # * : 재귀 검색 없이 단일 검색 패턴으로 적용 시 폴더는 업로드 되지 않음
        # for_each : fileset 함수에서 반환된 문서에 대한 반복을 수행하는 인수

    bucket = "${var.aws_s3_id}/${var.directory_name}/"
        # 업로드 할 Bucket 지정
            # .id 를 통해 생성하기로 한 버킷에 대한 자동 인식
    key = each.value
        # key = bucket에 업로드 시, 지정되는 객체의 이름
            # each.value : 업로드 할 각 파일(폴더)명을 그대로 식별하도록 지정
    content = file("${path.module}/files/${each.value}")
        # 업로드 대상에 대한 출처(경로) 지정 및 내부 파일(폴더)들을 각각 지정
}

### END


## CodeBuild
resource "aws_codebuild_project" "codebuild" {
  name           = local.ecr_name
  source_version = var.github_branch
  service_role   = aws_iam_role.codebuild_role.arn
  project_visibility = "PRIVATE"
#   resource_access_role = aws_iam_role.codebuild_role.arn
  build_timeout  = "60"  # 단위(분)
  queued_timeout = "480" # 단위(분)
  # 해당 terraform 버전에서 project_visibility 변경에 대한 이슈가 발생
  # apply시 해당 요소에 대한 변경사항 체크를 무시하도록 설정
  lifecycle {
	create_before_destroy = true
	ignore_changes = [project_visibility]
  }

  source {
    type            = "GITHUB"
    location        = "https://github.com/${var.github_repository}.git"
    git_clone_depth = 1
    buildspec       = file("${path.module}/files/buildspec.yml")
  }

  cache {
    type     = "S3"
    location = "${var.aws_s3_name}/${var.directory_name}"
  }

  artifacts {
    type = "NO_ARTIFACTS"
  }

  environment {
    image           = "aws/codebuild/amazonlinux2-x86_64-standard:4.0"
    type            = "LINUX_CONTAINER"
    privileged_mode = true # 권한 부여 여부 설정 (계정 권한)
    compute_type    = var.compute_type

    # ./files/buildspec.yml 참고
    environment_variable {
      name  = "REGION"
      value = "${var.region}"
      type  = "PLAINTEXT"
    }

	environment_variable {
	  name = "AWS_S3_NAME"
	  value = var.aws_s3_name
	  type  = "PLAINTEXT"
	}

    environment_variable {
      name  = "AWS_ID"
      value = var.aws_id
      type  = "PLAINTEXT"
    }

	environment_variable {
      name  = "AWS_ACCESS_KEY"
      value = var.aws_access_key
      type  = "PLAINTEXT"
    }

	environment_variable {
      name  = "AWS_SECRET_KEY"
      value = var.aws_secret_key
      type  = "PLAINTEXT"
    }

	environment_variable {
	  name = "ECR_NAME"
	  value = local.ecr_name
	}

    environment_variable {
      name = "ENV"
      value = var.env
      type = "PLAINTEXT"
    }

	environment_variable {
	  name = "GROUP_NAME"
	  value = var.group_name
	}

	environment_variable {
	  name = "CONTRAINER_PORT"
	  value = var.container_port
	}

	environment_variable {
	  name = "CONTRAINER_MEMORY"
	  value = var.container_memory
	}
	environment_variable {
	  name = "CONTRAINER_CPU"
	  value = var.container_cpu
	}
    environment_variable {
      name = "INFRA_FILE_PATH"
      value = "${var.aws_s3_name}/${local.infra_file_path}"
      type = "PLAINTEXT"
    }
  }

  tags = {
    env = var.env
  }
}