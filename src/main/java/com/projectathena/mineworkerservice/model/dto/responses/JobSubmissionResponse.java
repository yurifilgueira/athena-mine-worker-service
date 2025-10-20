package com.projectathena.mineworkerservice.model.dto.responses;

import com.projectathena.mineworkerservice.model.enums.JobStatus;

import java.util.UUID;

public record JobSubmissionResponse(
        UUID jobId,
        JobStatus status,
        String statusUrl
) {
}
