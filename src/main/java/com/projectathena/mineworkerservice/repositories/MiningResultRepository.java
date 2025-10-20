package com.projectathena.mineworkerservice.repositories;

import com.projectathena.mineworkerservice.model.entities.MiningResult;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MiningResultRepository extends ReactiveCrudRepository<MiningResult, UUID> {
    Mono<MiningResult> findByJobId(UUID jobId);

    @Query("""
        SELECT mr.* FROM mining_results mr 
        INNER JOIN jobs j ON mr.job_id = j.id 
        INNER JOIN users u ON j.requested_by_id = u.id 
        WHERE u.email = :userEmail 
          AND mr.repository_owner = :repositoryOwner 
          AND mr.repository_name = :repositoryName
    """)
    Mono<MiningResult> findByJobUserAndRepository(
            @Param("userEmail") String email,
            @Param("repositoryOwner") String owner,
            @Param("repositoryName") String name
    );
}
