package com.projectathena.mineworkerservice.controllers;

import com.projectathena.mineworkerservice.model.dto.requests.PublishJobRequest;
import com.projectathena.mineworkerservice.model.dto.responses.JobStatusResponse;
import com.projectathena.mineworkerservice.model.dto.responses.JobSubmissionResponse;
import com.projectathena.mineworkerservice.model.entities.Job;
import com.projectathena.mineworkerservice.service.JobService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequestMapping(value = "/jobs")
public class JobController {

    private final JobService jobService;

    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    @MutationMapping
    public Mono<Job> publishJob(@Argument PublishJobRequest request) {
        return jobService.publishJob(request);
    }

    @QueryMapping
    public Mono<Job> getJob(@Argument UUID id) {
        return jobService.findById(id)
                .map(job -> job);
    }
}