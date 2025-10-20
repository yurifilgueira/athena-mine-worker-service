package com.projectathena.mineworkerservice.controllers;

import com.projectathena.mineworkerservice.model.dto.requests.MiningResultRequest;
import com.projectathena.mineworkerservice.model.entities.MiningResult;
import com.projectathena.mineworkerservice.service.MiningResultService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value = "/mining-results")
public class MiningResultController {

    private final MiningResultService miningResultService;

    public MiningResultController(MiningResultService miningResultService) {
        this.miningResultService = miningResultService;
    }

    @GetMapping("")
    public Mono<ResponseEntity<MiningResult>> getMiningResult(
            @RequestParam String userName,
            @RequestParam String userEmail,
            @RequestParam String gitRepositoryName,
            @RequestParam String gitRepositoryOwner) {

        MiningResultRequest request = new MiningResultRequest(userName, userEmail, gitRepositoryName, gitRepositoryOwner);
        return miningResultService.findForUserAndRepository(request).map(miningResult -> ResponseEntity.ok().body(miningResult))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

}
