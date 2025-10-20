package com.projectathena.mineworkerservice.model.dto.commit;

import java.time.Instant;
import java.util.Date;

public record Commit (
        String oid,
        GitActor author,
        GitActor committer,
        Boolean authoredByCommitter,
        String message,
        String messageBody,
        String commitUrl,
        int additions,
        int deletions,
        Instant committedDate
) {}