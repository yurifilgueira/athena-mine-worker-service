package com.projectathena.mineworkerservice.controllers;

import com.projectathena.mineworkerservice.model.dto.requests.PublishJobRequest;
import com.projectathena.mineworkerservice.service.JobService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/jobs")
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

    @GetMapping(value = "/status/{id}")
    public ResponseEntity<?> getJobStatus(@PathVariable String id){
        var job = jobService.findJobStatusById(id);
        if(job != null){
            return ResponseEntity.ok().body(job);
        }else {
            return ResponseEntity.notFound().build();
        }
    }

}
