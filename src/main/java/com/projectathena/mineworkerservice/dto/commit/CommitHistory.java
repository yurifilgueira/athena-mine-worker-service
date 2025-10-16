package com.projectathena.mineworkerservice.dto.commit;

import com.projectathena.mineworkerservice.dto.commit.Commit;
import java.util.List;

public record CommitHistory(List<Commit> nodes, PageInfo pageInfo) {
}