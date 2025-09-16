package com.projectathena.mineworkerservice.dto.commit;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Parent (
     String sha,
     String url,
     String html_url
) {}