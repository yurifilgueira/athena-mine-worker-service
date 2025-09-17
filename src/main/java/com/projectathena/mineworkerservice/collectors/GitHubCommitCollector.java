package com.projectathena.mineworkerservice.collectors;

import com.projectathena.mineworkerservice.dto.repo.CommitInfo;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class GitHubCommitCollector {

    private final RestTemplate restTemplate;

    public GitHubCommitCollector(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public CommitInfo[] getCommits(String owner, String repositoryName) {

        String githubBaseUrl = "https://api.github.com";

        String url = githubBaseUrl + "/repos/" + owner + "/" + repositoryName + "/commits";
        ResponseEntity<CommitInfo[]> response = restTemplate.getForEntity(url, CommitInfo[].class);

        return response.getBody();
    }

}
