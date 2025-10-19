package com.projectathena.mineworkerservice.model.dto.responses;

import com.projectathena.mineworkerservice.model.enums.JobStatus;

public record JobSubmissionResponse(
        String jobId,
        JobStatus status,
        String statusUrl
) {
}
