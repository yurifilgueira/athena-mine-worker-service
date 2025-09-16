package com.projectathena.mineworkerservice.dto.commit;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Author (
     String name,
     String email,
     Date date,
     String login,
     int id,
     String node_id,
     String avatar_url,
     String gravatar_id,
     String url,
     String html_url,
     String followers_url,
     String following_url,
     String gists_url,
     String starred_url,
     String subscriptions_url,
     String organizations_url,
     String repos_url,
     String events_url,
     String received_events_url,
     String type,
     boolean site_admin
){}