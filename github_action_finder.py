import os
import yaml
import re
from tabulate import tabulate

# Directory where your YAML files are stored
repo_directory = 'reusable-workflows'

# Pattern to match the "uses: user/repo@version"
action_pattern = re.compile(r'uses:\s*([^\s@]+)@([^\s]+)')

# Function to extract action names and versions from a YAML file
def extract_actions_from_yaml(file_path):
    actions = []
    with open(file_path, 'r') as file:
        try:
            # Parse YAML content
            content = yaml.safe_load(file)
            # Convert YAML content to string to search for 'uses'
            yaml_str = yaml.dump(content)
            # Find all matches for the "uses" pattern
            matches = action_pattern.findall(yaml_str)
            actions.extend(matches)
        except yaml.YAMLError as e:
            print(f"Error parsing {file_path}: {e}")
    return actions

# Recursively search for YAML files and extract actions
def collect_all_actions(repo_directory):
    all_actions = []
    for root, dirs, files in os.walk(repo_directory):
        for file in files:
            if file.endswith('.yml') or file.endswith('.yaml'):
                file_path = os.path.join(root, file)
                actions = extract_actions_from_yaml(file_path)
                for action, version in actions:
                    all_actions.append([file, action, version])
    return all_actions

# Main script
if __name__ == "__main__":
    actions = collect_all_actions(repo_directory)

    if actions:
        # Sort actions alphabetically by the "Action" name (2nd column)
        actions.sort(key=lambda x: x[1])
        
        # Print actions in a table format
        headers = ["YAML File", "Action", "Version"]
        print(tabulate(actions, headers, tablefmt="pretty"))
    else:
        print("No actions found in the YAML files.")
