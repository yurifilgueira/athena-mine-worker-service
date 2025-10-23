package com.projectathena.mineworkerservice.controllers;

import com.projectathena.mineworkerservice.model.dto.requests.MiningResultRequest;
import com.projectathena.mineworkerservice.model.entities.MiningCommit;
import com.projectathena.mineworkerservice.model.entities.MiningResult;
import com.projectathena.mineworkerservice.service.MiningResultService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value = "/mining-results")
public class MiningResultController {

    private final MiningResultService miningResultService;

    public MiningResultController(MiningResultService miningResultService) {
        this.miningResultService = miningResultService;
    }

    @GetMapping(produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<MiningCommit> getMiningResult(
            @RequestParam String userName,
            @RequestParam String userEmail,
            @RequestParam String gitRepositoryName,
            @RequestParam String gitRepositoryOwner) {

        MiningResultRequest request = new MiningResultRequest(userName, userEmail, gitRepositoryName, gitRepositoryOwner);
        return miningResultService.findForUserAndRepository(request);
    }

}
