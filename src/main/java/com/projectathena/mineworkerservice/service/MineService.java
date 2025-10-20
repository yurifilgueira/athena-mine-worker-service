package com.projectathena.mineworkerservice.service;

import com.projectathena.mineworkerservice.miners.github.GitHubCommitCollector;
import com.projectathena.mineworkerservice.model.entities.Job;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class MineService {

    private static final Logger logger = LoggerFactory.getLogger(MineService.class);

    private final GitHubCommitCollector gitHubCommitCollector;

    public MineService(GitHubCommitCollector gitHubCommitCollector) {
        this.gitHubCommitCollector = gitHubCommitCollector;
    }

    public Mono<Void> mineCommits(Job job){
        return gitHubCommitCollector.getCommits(job).then();
    }
}
