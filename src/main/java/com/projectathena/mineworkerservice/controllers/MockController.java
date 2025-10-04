package com.projectathena.mineworkerservice.controllers;

import com.projectathena.mineworkerservice.collectors.GitHubCommitCollector;
import com.projectathena.mineworkerservice.dto.requests.RepositoryMineRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/mock")
public class MockController {

    private final GitHubCommitCollector gitHubCommitCollector;

    public MockController(GitHubCommitCollector gitHubCommitCollector) {
        this.gitHubCommitCollector = gitHubCommitCollector;
    }

    @GetMapping(value = "/commits")
    public ResponseEntity<?> getCommits(@RequestBody RepositoryMineRequest request) {

        var commits = gitHubCommitCollector.getCommits(request);

        return ResponseEntity.ok(commits);
    }

}
