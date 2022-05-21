package com.test.myproject.core.services;

import com.test.myproject.core.interfaces.IRoleService;
import com.test.myproject.core.role_aggreate.Role;
import com.test.myproject.core.role_aggreate.exception.RoleAlreadyExistsException;
import com.test.myproject.core.role_aggreate.exception.RoleNotFoundException;
import com.test.myproject.infrastructure.interfaces.IRoleRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class RoleService implements IRoleService {
    @Autowired
    private IRoleRepository roleRepository;

    @Override
    public Role save(Role role) {
        Role roleExists = roleRepository.findByName(role.getName());
        if (roleExists != null) {
            throw new RoleAlreadyExistsException(role.getName());
        }
        return roleRepository.save(role);
    }

    @Override
    public Role one(Long id) {
        return roleRepository.findById(id).orElseThrow(() -> new RoleNotFoundException(id));
    }

    @Override
    public Page<Role> all(Pageable pageable) {
        return roleRepository.findAll(pageable);
    }
}
