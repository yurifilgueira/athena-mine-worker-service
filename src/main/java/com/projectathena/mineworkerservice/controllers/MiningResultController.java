package com.projectathena.mineworkerservice.controllers;

import com.projectathena.mineworkerservice.model.dto.requests.MiningResultRequest;
import com.projectathena.mineworkerservice.model.entities.MiningResult;
import com.projectathena.mineworkerservice.service.MiningResultService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class MiningResultController {

    private final MiningResultService miningResultService;

    public MiningResultController(MiningResultService miningResultService) {
        this.miningResultService = miningResultService;
    }

    @QueryMapping
    public MiningResult getMiningResult(
            @Argument String userName,
            @Argument String userEmail,
            @Argument String gitRepositoryName,
            @Argument String gitRepositoryOwner) {

        MiningResultRequest request = new MiningResultRequest(userName, userEmail, gitRepositoryName, gitRepositoryOwner);
        var miningResult = miningResultService.findForUserAndRepository(request);

        return miningResult.orElse(null);
    }
}
