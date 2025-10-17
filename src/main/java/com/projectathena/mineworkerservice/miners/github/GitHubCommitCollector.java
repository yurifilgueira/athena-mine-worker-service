package com.projectathena.mineworkerservice.miners.github;

import com.projectathena.mineworkerservice.model.dto.commit.Commit;
import com.projectathena.mineworkerservice.model.dto.commit.CommitHistory;
import com.projectathena.mineworkerservice.model.entities.Job;
import com.projectathena.mineworkerservice.service.JobService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.graphql.client.HttpGraphQlClient;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;

@Component
public class GitHubCommitCollector {

    private final JobService jobService;
    private final HttpGraphQlClient graphQlClient;

    public GitHubCommitCollector(@Value("${github.token}") String githubToken, JobService jobService) {
        WebClient webClient = WebClient.builder()
                .baseUrl("https://api.github.com/graphql")
                .defaultHeader("Authorization", "Bearer " + githubToken)
                .build();
        this.graphQlClient = HttpGraphQlClient.builder(webClient).build();
        this.jobService = jobService;
    }

    public List<Commit> getCommits(Job job) {
        List<Commit> allCommits = new ArrayList<>();
        String cursor = null;
        boolean hasNextPage;

        String repoOwner = job.getGitRepositoryOwner();
        String repoName = job.getGitRepositoryName();

        do {
            CommitHistory historyPage = graphQlClient.documentName("getCommits")
                    .variable("owner", repoOwner)
                    .variable("name", repoName)
                    .variable("after", cursor)
                    .retrieveSync("repository.defaultBranchRef.target.history")
                    .toEntity(CommitHistory.class);

            if (historyPage != null && historyPage.nodes() != null) {
                allCommits.addAll(historyPage.nodes());

                hasNextPage = historyPage.pageInfo().hasNextPage();
                cursor = historyPage.pageInfo().endCursor();
            } else {
                hasNextPage = false;
            }

            if (hasNextPage) {
                jobService.updateJobProgress(job, historyPage.pageInfo().endCursor());
            }

        } while (hasNextPage);

        return allCommits;
    }
}