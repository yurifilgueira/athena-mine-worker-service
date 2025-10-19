package com.projectathena.mineworkerservice.miners.github;

import com.projectathena.mineworkerservice.model.dto.commit.Commit;
import com.projectathena.mineworkerservice.model.dto.commit.CommitHistory;
import com.projectathena.mineworkerservice.model.entities.Job;
import com.projectathena.mineworkerservice.service.JobService;
import com.projectathena.mineworkerservice.service.MiningResultService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.graphql.client.HttpGraphQlClient;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

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

    public List<Commit> getCommits(Job job) {
        List<Commit> allCommits = new ArrayList<>();
        String cursor = job.getCursor();
        boolean hasNextPage;

        String repoOwner = job.getGitRepositoryOwner();
        String repoName = job.getGitRepositoryName();

        do {
            logger.info("Mining GitHub commits for {}/{} with cursor: {}", repoOwner, repoName, cursor);

            CommitHistory historyPage = graphQlClient.documentName("getCommits")
                    .variable("owner", repoOwner)
                    .variable("name", repoName)
                    .variable("after", cursor)
                    .retrieveSync("repository.defaultBranchRef.target.history")
                    .toEntity(CommitHistory.class);

            if (historyPage != null && historyPage.nodes() != null) {
                List<Commit> pageCommits = historyPage.nodes();
                allCommits.addAll(pageCommits);

                miningResultService.saveCommitPage(job, pageCommits, historyPage.pageInfo().endCursor());

                hasNextPage = historyPage.pageInfo().hasNextPage();
                cursor = historyPage.pageInfo().endCursor();
            } else {
                hasNextPage = false;
            }

            if (hasNextPage) {
                jobService.updateJobProgress(job, historyPage.pageInfo().endCursor());
            }

        } while (hasNextPage);

        miningResultService.completeMiningResult(job);

        return allCommits;
    }
}