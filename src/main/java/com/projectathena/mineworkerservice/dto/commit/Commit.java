package com.projectathena.mineworkerservice.dto.commit;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Commit (
    Author author,
    Committer committer,
    String message,
    Tree tree,
    String url,
    int comment_count,
    Verification verification
) {}