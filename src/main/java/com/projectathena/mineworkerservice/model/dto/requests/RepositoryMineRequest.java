package com.projectathena.mineworkerservice.model.dto.requests;

import java.time.ZonedDateTime;

public record RepositoryMineRequest(
        String requestedBy,
        String projectName,
        String owner,
        ZonedDateTime requestedAt
) {
}
