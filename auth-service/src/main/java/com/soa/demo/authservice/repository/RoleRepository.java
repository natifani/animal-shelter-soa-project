package com.soa.demo.authservice.repository;

import com.soa.demo.authservice.model.Role;
import com.soa.demo.authservice.util.RoleE;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleE name);
}