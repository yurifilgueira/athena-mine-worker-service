package com.projectathena.mineworkerservice.repositories;

import com.projectathena.mineworkerservice.model.entities.MiningResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MiningResultRepository extends JpaRepository<MiningResult, String> {
    
    Optional<MiningResult> findByJobId(String jobId);
    
    List<MiningResult> findByRepositoryOwnerAndRepositoryName(String owner, String name);
    
    boolean existsByJobId(String jobId);
}
