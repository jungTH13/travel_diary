terraform {
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 4.4"
    }
  }
}

provider "aws" {
  region      = var.aws_data.region
  access_key  = var.aws_data.access_key
  secret_key  = var.aws_data.secret_key
}

module "infra" {
  source = "../"

  infra_names         = var.infra_names
  project             = var.project
  network             = var.network
  aws_data            = var.aws_data
  github_repository   = var.github_repository
  github_branch       = var.github_branch
  ECS                 = local.ECS
  ELK                 = var.ELK
}