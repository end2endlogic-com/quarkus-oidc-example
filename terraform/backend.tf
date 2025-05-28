
terraform {
  backend "s3" {
    bucket = "quarkus-terraform-state"
    key    = "cognito/terraform.tfstate"
    region = "us-east-1"
  }
}
