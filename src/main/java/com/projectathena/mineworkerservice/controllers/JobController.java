package com.projectathena.mineworkerservice.controllers;

import com.projectathena.mineworkerservice.model.dto.requests.PublishJobRequest;
import com.projectathena.mineworkerservice.model.entities.Job;
import com.projectathena.mineworkerservice.service.JobService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class JobController {

    private final JobService jobService;

    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    @PostMapping(value = "/publish")
    public ResponseEntity<?> publishJob(@RequestBody PublishJobRequest request){

        var jobSubmissionResponse = jobService.publishJob(request);

        return ResponseEntity.accepted().body(jobSubmissionResponse);
    }

    @QueryMapping
    public Job getJob(@Argument String id){
        return jobService.findById(id).orElse(null);
    }

}
