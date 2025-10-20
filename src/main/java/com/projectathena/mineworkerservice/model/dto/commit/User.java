package com.projectathena.mineworkerservice.model.dto.commit;

import java.util.UUID;

public record User(
        String id,
        String login
) {
}