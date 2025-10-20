package com.projectathena.mineworkerservice.repositories;

import com.projectathena.mineworkerservice.model.entities.MiningCommit;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MiningCommitRepository extends ReactiveCrudRepository<MiningCommit, UUID> {
    Flux<MiningCommit> findByMiningResultId(UUID miningResultId);
}
