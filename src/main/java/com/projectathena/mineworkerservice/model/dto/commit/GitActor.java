package com.projectathena.mineworkerservice.model.dto.commit;

import java.util.Date;

public record GitActor(
        String avatarUrl,
        Date date,
        String email,
        String name,
        User user
) {
}
