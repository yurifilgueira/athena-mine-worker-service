package com.projectathena.mineworkerservice.dto.repo;

import com.projectathena.mineworkerservice.dto.commit.Author;
import com.projectathena.mineworkerservice.dto.commit.Commit;
import com.projectathena.mineworkerservice.dto.commit.Committer;
import com.projectathena.mineworkerservice.dto.commit.Parent;

import java.util.List;

public record CommitInfo (
     String sha,
     String node_id,
     Commit commit,
     String url,
     String html_url,
     String comments_url,
     Author author,
     Committer committer,
     List<Parent>parents
) {}