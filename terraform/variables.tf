
variable "region" {
  default = "us-east-1"
}

variable "callback_url" {
  description = "The callback URL for the Cognito user pool client"
  default     = "http://localhost:8080/callback"
}
