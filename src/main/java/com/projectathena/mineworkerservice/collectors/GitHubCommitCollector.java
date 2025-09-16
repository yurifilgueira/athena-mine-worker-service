package com.projectathena.mineworkerservice.collectors;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class GitHubCommitCollector {

    private RestTemplate restTemplate;

    public GitHubCommitCollector(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    // TODO
    public void getCommits(String owner, String repositoryName) {
    }

}
