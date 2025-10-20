package com.projectathena.mineworkerservice.repositories;

import com.projectathena.mineworkerservice.model.entities.MiningResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MiningResultRepository extends JpaRepository<MiningResult, String> {
    
    Optional<MiningResult> findByJobId(String jobId);
    
    boolean existsByJobId(String jobId);
    @Query("SELECT mr FROM MiningResult mr JOIN Job j ON mr.jobId = j.id " +
            "WHERE j.requestedBy.email = :userEmail "
            + "AND mr.repositoryOwner = :owner "
            + "AND mr.repositoryName = :name")
    Optional<MiningResult> findForUserAndRepository(
            @Param("userEmail") String userEmail,
            @Param("owner") String owner,
            @Param("name") String name
    );
}
