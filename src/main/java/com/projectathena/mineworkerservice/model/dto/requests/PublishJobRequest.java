package com.projectathena.mineworkerservice.model.dto.requests;

public record PublishJobRequest (
         String requestedBy,
         String gitRepositoryName,
         String gitRepositoryOwner
){
}
