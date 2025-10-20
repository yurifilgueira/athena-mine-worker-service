package com.projectathena.mineworkerservice.miners.github;

import com.projectathena.mineworkerservice.model.dto.commit.CommitHistory;
import com.projectathena.mineworkerservice.model.entities.Job;
import com.projectathena.mineworkerservice.service.JobService;
import com.projectathena.mineworkerservice.service.MiningResultService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.graphql.client.HttpGraphQlClient;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Component
public class GitHubCommitCollector {

    private final JobService jobService;
    private final MiningResultService miningResultService;
    private final HttpGraphQlClient graphQlClient;
    Logger logger = org.slf4j.LoggerFactory.getLogger(GitHubCommitCollector.class);

    public GitHubCommitCollector(@Value("${github.token}") String githubToken,
                                 JobService jobService,
                                 MiningResultService miningResultService) {
        WebClient webClient = WebClient.builder()
                .baseUrl("https://api.github.com/graphql")
                .defaultHeader("Authorization", "Bearer " + githubToken)
                .build();
        this.graphQlClient = HttpGraphQlClient.builder(webClient).build();
        this.jobService = jobService;
        this.miningResultService = miningResultService;
    }

    public Mono<Void> getCommits(Job job) {
        String repoOwner = job.getGitRepositoryOwner();
        String repoName = job.getGitRepositoryName();

        return fetchPage(repoOwner, repoName, job.getCursor())
                .expand(historyPage -> {
                    if (historyPage.pageInfo().hasNextPage()) {
                        logger.debug("Expanding to next page with cursor: {}", historyPage.pageInfo().endCursor());
                        return fetchPage(repoOwner, repoName, historyPage.pageInfo().endCursor());
                    } else {
                        return Mono.empty();
                    }
                })
                .flatMap(historyPage -> processAndSavePage(job, historyPage).thenReturn(historyPage))

                .then(miningResultService.completeMiningResult(job));
    }

    private Mono<CommitHistory> fetchPage(String owner, String name, String cursor) {
        logger.debug("Executing GraphQL query for {}/{} with cursor: {}", owner, name, cursor);

        return graphQlClient.documentName("getCommits")
                .variable("owner", owner)
                .variable("name", name)
                .variable("after", cursor)
                .retrieve("repository.defaultBranchRef.target.history")
                .toEntity(CommitHistory.class);
    }

    private Mono<Void> processAndSavePage(Job job, CommitHistory historyPage) {
        if (historyPage == null || historyPage.nodes() == null || historyPage.nodes().isEmpty()) {
            return Mono.empty();
        }

        return miningResultService.saveCommitPage(job, historyPage.nodes(), historyPage.pageInfo().endCursor())
                .then(jobService.updateJobProgress(job, historyPage.pageInfo().endCursor()));
    }
}