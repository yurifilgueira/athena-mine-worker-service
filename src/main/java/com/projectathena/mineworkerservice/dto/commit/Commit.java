package com.projectathena.mineworkerservice.dto.commit;

import java.util.Date;

public record Commit (
        String sha,
        GitActor author,
        GitActor committer,
        Boolean authoredByCommitter,
        String message,
        String messageBody,
        String commitUrl,
        CommitStats commitstats,
        Date commitedDate,
        Date pushedDate
) {}