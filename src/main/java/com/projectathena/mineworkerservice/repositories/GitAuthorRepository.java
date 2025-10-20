package com.projectathena.mineworkerservice.repositories;

import com.projectathena.mineworkerservice.model.entities.GitAuthor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface GitAuthorRepository extends ReactiveCrudRepository<GitAuthor, UUID> {
    Mono<GitAuthor> findByEmail(String email);
    Mono<GitAuthor> findByUserId(UUID userId);
}
