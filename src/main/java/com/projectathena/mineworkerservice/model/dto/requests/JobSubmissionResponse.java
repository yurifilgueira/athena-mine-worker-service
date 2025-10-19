package com.projectathena.mineworkerservice.model.dto.requests;

import com.projectathena.mineworkerservice.model.enums.JobStatus;

public record JobSubmissionResponse(
        String jobId,
        JobStatus status,
        String statusUrl
) {
}
