package com.projectathena.mineworkerservice.dto.commit;

import java.time.LocalDateTime;

public record GitActor(
        String avatarUrl,
        LocalDateTime date,
        String email,
        String name
) {
}
