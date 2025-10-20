package com.projectathena.mineworkerservice.controllers;

import com.projectathena.mineworkerservice.model.dto.requests.PublishJobRequest;
import com.projectathena.mineworkerservice.model.dto.responses.JobStatusResponse;
import com.projectathena.mineworkerservice.model.dto.responses.JobSubmissionResponse;
import com.projectathena.mineworkerservice.service.JobService;
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

    @PostMapping(value = "/publish")
    public Mono<ResponseEntity<JobSubmissionResponse>> publishJob(@RequestBody Mono<PublishJobRequest> request) {
        return request
                .flatMap(jobService::publishJob)
                .map(response -> ResponseEntity.accepted().body(response));
    }

    @GetMapping(value = "/status/{id}")
    public Mono<ResponseEntity<JobStatusResponse>> getJobStatus(@PathVariable UUID id) {
        return jobService.findJobStatusById(id)
                .map(jobStatus -> ResponseEntity.ok().body(jobStatus))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}