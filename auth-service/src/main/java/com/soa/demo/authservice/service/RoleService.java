package com.soa.demo.authservice.service;

import com.soa.demo.authservice.model.Role;
import com.soa.demo.authservice.repository.RoleRepository;
import com.soa.demo.authservice.util.RoleE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RoleService {
    private final RoleRepository roleRepository;

    @Autowired
    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public Optional<Role> findByName(RoleE name) {
        return roleRepository.findByName(name);
    }
}
