package com.projectathena.mineworkerservice.model.dto.requests;

public record MiningResultRequest(
    String userName,
    String userEmail,
    String gitRepositoryName,
    String gitRepositoryOwner
){
}
