package com.test.myproject.core.interfaces;

import com.test.myproject.core.role_aggreate.Role;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IRoleService {
    public Page<Role> all(Pageable pageable);

    public Role save(Role role);

    public Role one(Long id);
}
