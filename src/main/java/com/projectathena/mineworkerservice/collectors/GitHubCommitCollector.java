package com.projectathena.mineworkerservice.collectors;

import com.projectathena.mineworkerservice.dto.repo.CommitInfo;
import com.projectathena.mineworkerservice.dto.requests.RepositoryMineRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class GitHubCommitCollector {

    private final RestTemplate restTemplate;

    public GitHubCommitCollector(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public CommitInfo[] getCommits(RepositoryMineRequest request) {

        String githubBaseUrl = "https://api.github.com";

        String url = githubBaseUrl + "/repos/" + request.organizationName() + "/" + request.projectName() + "/commits";
        ResponseEntity<CommitInfo[]> response = restTemplate.getForEntity(url, CommitInfo[].class);

        return response.getBody();
    }

}
