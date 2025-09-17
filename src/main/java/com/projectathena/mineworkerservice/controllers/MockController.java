package com.projectathena.mineworkerservice.controllers;

import com.projectathena.mineworkerservice.collectors.GitHubCommitCollector;
import com.projectathena.mineworkerservice.dto.repo.CommitInfo;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/mock")
public class MockController {

    private final GitHubCommitCollector gitHubCommitCollector;

    public MockController(GitHubCommitCollector gitHubCommitCollector) {
        this.gitHubCommitCollector = gitHubCommitCollector;
    }

    @GetMapping(value = "/commits")
    public ResponseEntity<CommitInfo[]> getCommits(@RequestParam String owner, @RequestParam String repositoryName) {

        var commits = gitHubCommitCollector.getCommits(owner, repositoryName);

        return ResponseEntity.ok(commits);
    }

}
