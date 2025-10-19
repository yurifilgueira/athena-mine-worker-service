package com.projectathena.mineworkerservice.model.dto.requests;

public record PublishJobRequest (
    String userName,
    String userEmail,
    String gitRepositoryName,
    String gitRepositoryOwner
){
}
