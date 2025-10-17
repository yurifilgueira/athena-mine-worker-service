package com.projectathena.mineworkerservice.controllers;

import com.projectathena.mineworkerservice.model.entities.Job;
import com.projectathena.mineworkerservice.service.JobService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping(value = "/jobs")
public class JobController {

    private final JobService jobService;

    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    @GetMapping(value = "/status/{id}")
    public ResponseEntity<?> getJobStatus(@PathVariable String id) {
        Optional<Job> job = jobService.findById(id);
        if (job.isPresent()) {
            return ResponseEntity.ok().body(job.get().getJobStatus());
        }else {
            return ResponseEntity.notFound().build();
        }
    }

}
