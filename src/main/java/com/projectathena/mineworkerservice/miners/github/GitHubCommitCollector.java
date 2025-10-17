package com.projectathena.mineworkerservice.miners.github;

import com.projectathena.mineworkerservice.model.dto.commit.Commit;
import com.projectathena.mineworkerservice.model.dto.commit.CommitHistory;
import com.projectathena.mineworkerservice.model.dto.requests.RepositoryMineRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.graphql.client.HttpGraphQlClient;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;

@Component
public class GitHubCommitCollector {

    private final HttpGraphQlClient graphQlClient;

    public GitHubCommitCollector(@Value("${github.token}") String githubToken) {
        WebClient webClient = WebClient.builder()
                .baseUrl("https://api.github.com/graphql")
                .defaultHeader("Authorization", "Bearer " + githubToken)
                .build();
        this.graphQlClient = HttpGraphQlClient.builder(webClient).build();
    }

    public List<Commit> getCommits(String repoName, String repoOwner) {
        List<Commit> allCommits = new ArrayList<>();
        String cursor = null;
        boolean hasNextPage;

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

        } while (hasNextPage);

        return allCommits;
    }
}