package com.test.myproject.infrastructure.interfaces;

import com.test.myproject.core.role_aggreate.Role;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IRoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String name);
}
