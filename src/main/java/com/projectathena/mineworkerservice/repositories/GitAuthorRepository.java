package com.projectathena.mineworkerservice.repositories;

import com.projectathena.mineworkerservice.model.entities.GitAuthor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GitAuthorRepository extends JpaRepository<GitAuthor, String> {
    
    Optional<GitAuthor> findByEmail(String email);
    
    Optional<GitAuthor> findByUserId(String userId);
}
