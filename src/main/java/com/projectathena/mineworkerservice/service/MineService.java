package com.projectathena.mineworkerservice.service;

import com.projectathena.mineworkerservice.miners.github.GitHubCommitCollector;
import com.projectathena.mineworkerservice.model.dto.commit.Commit;
import com.projectathena.mineworkerservice.model.dto.requests.RepositoryMineRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MineService {

    private GitHubCommitCollector gitHubCommitCollector;


    public MineService(GitHubCommitCollector gitHubCommitCollector) {
        this.gitHubCommitCollector = gitHubCommitCollector;
    }

    public List<Commit> mineCommits(String repoName, String repoOwner){
        return gitHubCommitCollector.getCommits(repoName, repoOwner);
    }
}
