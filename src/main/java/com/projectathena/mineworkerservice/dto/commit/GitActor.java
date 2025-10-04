package com.projectathena.mineworkerservice.dto.commit;

import java.util.Date;

public record GitActor(
        String avatarUrl,
        Date date,
        String email,
        String name,
        User user
) {
}
