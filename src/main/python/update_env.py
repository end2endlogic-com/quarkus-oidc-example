import json

def load_env_file(env_file_path):
    env_vars = {}
    try:
        with open(env_file_path, 'r') as env_file:
            for line in env_file:
                if '=' in line:
                    key, value = line.strip().split('=', 1)
                    env_vars[key] = value
    except FileNotFoundError:
        pass
    return env_vars

def update_env_file(json_file_path, env_file_path):
    with open(json_file_path, 'r') as json_file:
        data = json.load(json_file)

    env_vars = load_env_file(env_file_path)

    # Update or add the specific environment variables
    env_vars['APPLICATION_CLIENT_ID'] = data['app_client_id']['value']
    env_vars['USER_POOL_ID'] = data['user_pool_id']['value']

    with open(env_file_path, 'w') as env_file:
        for key, value in env_vars.items():
            env_file.write(f"{key}={value}\n")

if __name__ == "__main__":
    json_file_path = 'terraform/terraform-output.json'
    env_file_path = '.env'
    update_env_file(json_file_path, env_file_path)
    print(f".env file updated at {env_file_path}")