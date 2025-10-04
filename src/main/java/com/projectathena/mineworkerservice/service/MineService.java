package com.projectathena.mineworkerservice.service;

import com.projectathena.mineworkerservice.collectors.GitHubCommitCollector;
import com.projectathena.mineworkerservice.dto.commit.Commit;
import com.projectathena.mineworkerservice.dto.requests.RepositoryMineRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MineService {

    private GitHubCommitCollector gitHubCommitCollector;


    public MineService(GitHubCommitCollector gitHubCommitCollector) {
        this.gitHubCommitCollector = gitHubCommitCollector;
    }

    public List<Commit> mineCommits(RepositoryMineRequest request){
        return gitHubCommitCollector.getCommits(request);
    }
}
