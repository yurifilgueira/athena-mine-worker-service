package com.projectathena.mineworkerservice.dto.commit;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Tree (
    String sha,
    String url
) {}