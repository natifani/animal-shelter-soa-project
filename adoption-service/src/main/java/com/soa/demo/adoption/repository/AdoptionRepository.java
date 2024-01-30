package com.soa.demo.adoption.repository;

import com.soa.demo.adoption.model.AdoptionRequest;
import com.soa.demo.adoption.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface AdoptionRepository extends JpaRepository<AdoptionRequest, Long> {
    Page<AdoptionRequest> findByUserId(Pageable pageable, Long userId);
    Long countByUser(User user);
}
