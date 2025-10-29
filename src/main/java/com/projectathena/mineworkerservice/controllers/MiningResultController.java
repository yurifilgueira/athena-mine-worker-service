package com.projectathena.mineworkerservice.controllers;

import com.projectathena.mineworkerservice.model.dto.requests.MiningResultRequest;
import com.projectathena.mineworkerservice.model.entities.MiningResult;
import com.projectathena.mineworkerservice.service.MiningResultService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/mining-results")
public class MiningResultController {

    private final MiningResultService miningResultService;

    public MiningResultController(MiningResultService miningResultService) {
        this.miningResultService = miningResultService;
    }

    @GetMapping
    public ResponseEntity<?> getMiningResult(
            @RequestParam String userName,
            @RequestParam String userEmail,
            @RequestParam String gitRepositoryName,
            @RequestParam String gitRepositoryOwner) {

        MiningResultRequest request = new MiningResultRequest(userName, userEmail, gitRepositoryName, gitRepositoryOwner);
        var miningResult = miningResultService.findForUserAndRepository(request);

        if (miningResult.isPresent()) {
            return ResponseEntity.ok().body(miningResult.get());
        }

        return ResponseEntity.notFound().build();
    }

}
