package com.projectathena.mineworkerservice.collectors;

import com.projectathena.mineworkerservice.dto.commit.Commit;
import com.projectathena.mineworkerservice.dto.requests.RepositoryMineRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.graphql.client.HttpGraphQlClient;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Component
public class GitHubCommitCollector {

    @Value("${github.token}")
    private String githubToken;

    public List<Commit> getCommits(RepositoryMineRequest request) {

        String githubBaseUrl = "https://api.github.com/graphql";

        WebClient webClient = WebClient.builder().baseUrl(githubBaseUrl).build();
        HttpGraphQlClient graphQlClient = HttpGraphQlClient.builder(webClient)
                .headers(header -> header.setBearerAuth(githubToken))
                .build();

        List<Commit> commits = graphQlClient.documentName("getCommits")
                .variable("owner", request.owner())
                .variable("name", request.projectName())
                .retrieveSync("repository.defaultBranchRef.target.history.nodes")
                .toEntityList(Commit.class);

        return commits;
    }
}
