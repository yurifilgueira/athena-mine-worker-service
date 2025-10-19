package com.projectathena.mineworkerservice.model.dto.responses;

import com.projectathena.mineworkerservice.model.enums.JobStatus;

public record JobStatusResponse(
        String id,
        JobStatus status
) {
}
