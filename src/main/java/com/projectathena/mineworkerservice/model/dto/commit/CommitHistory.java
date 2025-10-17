package com.projectathena.mineworkerservice.model.dto.commit;

import java.util.List;

public record CommitHistory(List<Commit> nodes, PageInfo pageInfo) {
}