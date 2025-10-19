package com.projectathena.mineworkerservice.service;

import com.projectathena.mineworkerservice.miners.github.GitHubCommitCollector;
import com.projectathena.mineworkerservice.model.dto.commit.Commit;
import com.projectathena.mineworkerservice.model.entities.Job;
import com.projectathena.mineworkerservice.model.entities.MiningResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MineService {

    private static final Logger logger = LoggerFactory.getLogger(MineService.class);

    private final GitHubCommitCollector gitHubCommitCollector;
    private final MiningResultService miningResultService;

    public MineService(GitHubCommitCollector gitHubCommitCollector, MiningResultService miningResultService) {
        this.gitHubCommitCollector = gitHubCommitCollector;
        this.miningResultService = miningResultService;
    }

    public List<Commit> mineCommits(Job job){
        return gitHubCommitCollector.getCommits(job);
    }
}
