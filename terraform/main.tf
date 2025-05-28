
provider "aws" {
  region = "us-east-1"
}

resource "aws_cognito_user_pool" "user_pool" {
  name = "quarkus-auth-user-pool"

  schema {
    name                = "email"
    attribute_data_type = "String"
    required            = true
    mutable             = false
  }
}

resource "aws_cognito_user_pool_client" "app_client" {
  name         = "quarkus-app-client"
  user_pool_id = aws_cognito_user_pool.user_pool.id
  generate_secret = false
  allowed_oauth_flows = ["code"]
  allowed_oauth_flows_user_pool_client = true
  allowed_oauth_scopes = ["phone", "email", "openid", "profile"]
  callback_urls = [var.callback_url]
  explicit_auth_flows = ["ALLOW_ADMIN_USER_PASSWORD_AUTH", "ALLOW_REFRESH_TOKEN_AUTH"]
}

output "user_pool_id" {
  value = aws_cognito_user_pool.user_pool.id
}

output "app_client_id" {
  value = aws_cognito_user_pool_client.app_client.id
}
