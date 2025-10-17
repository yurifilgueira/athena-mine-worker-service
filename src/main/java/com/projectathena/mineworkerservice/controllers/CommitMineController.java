package com.projectathena.mineworkerservice.controllers;

import com.projectathena.mineworkerservice.model.dto.requests.RepositoryMineRequest;
import com.projectathena.mineworkerservice.service.MineService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/commits")
public class CommitMineController {

    private final MineService mineService;

    public CommitMineController(MineService mineService) {
        this.mineService = mineService;
    }

    @GetMapping(value = "/mine")
    public ResponseEntity<?> getCommits(@RequestBody RepositoryMineRequest request) {

        var commits = mineService.mineCommits(request.projectName(), request.owner());

        return ResponseEntity.ok(commits);
    }

}
