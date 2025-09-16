package com.projectathena.mineworkerservice.dto.commit;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record GitHubCommitInfo (
     String sha,
     String node_id,
     Commit commit,
     String url,
     String html_url,
     String comments_url,
     Author author,
     Committer committer,
     List<Parent> parents
) {}
