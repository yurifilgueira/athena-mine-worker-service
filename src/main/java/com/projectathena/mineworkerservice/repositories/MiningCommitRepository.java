package com.projectathena.mineworkerservice.repositories;

import com.projectathena.mineworkerservice.model.entities.MiningCommit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MiningCommitRepository extends JpaRepository<MiningCommit, String> {
    
    List<MiningCommit> findByMiningResultId(String miningResultId);
    
    Optional<MiningCommit> findByOid(String oid);
    
    boolean existsByOid(String oid);
}
