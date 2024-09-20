import requests
import json

def get_latest_tag_and_comment(org_name, token):
    """
    Retrieves the latest tag and release comment for all repositories in the specified organization.

    Args:
        org_name (str): The name of the organization.
        token (str): The GitHub personal access token.

    Returns:
        list: A list of tuples containing the repository name, latest tag, and release comment.
    """

    headers = {
        "Authorization": f"Bearer {token}"
    }

    url = f"https://api.github.com/orgs/{org_name}/repos"

    repos = []
    while True:
        response = requests.get(url, headers=headers)
        if response.status_code != 200:
            raise Exception(f"Error fetching repositories: {response.text}")

        repos.extend(response.json())
        url = response.headers.get("Link", "").split(";")[0]
        if not url or "next" not in url:
            break

    results = []
    for repo in repos:
        repo_name = repo["name"]
        latest_tag = None
        release_comment = None

        # Get tags for the repository
        tags_url = f"https://api.github.com/repos/{org_name}/{repo_name}/tags"
        tags_response = requests.get(tags_url, headers=headers)
        if tags_response.status_code == 200:
            tags = tags_response.json()
            if tags:
                latest_tag = tags[0]["name"]

        # Get releases for the repository
        releases_url = f"https://api.github.com/repos/{org_name}/{repo_name}/releases"
        releases_response = requests.get(releases_url, headers=headers)
        if releases_response.status_code == 200:
            releases = releases_response.json()
            if releases:
                latest_release = releases[0]
                release_comment = latest_release.get("body", "")

        results.append((repo_name, latest_tag, release_comment))

    return results

if __name__ == "__main__":
    org_name = "your-organization-name"  # Replace with your organization's name
    token = "your-github-personal-access-token"  # Replace with your token

    results = get_latest_tag_and_comment(org_name, token)
    for repo_name, latest_tag, release_comment in results:
        print(f"Repository: {repo_name}")
        print(f"Latest Tag: {latest_tag}")
        print(f"Release Comment: {release_comment}")
        print()
