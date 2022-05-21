package com.test.myproject.web.api_models;

import java.util.Collection;

import com.test.myproject.core.role_aggreate.Role;

import lombok.Data;

@Data
public class UserDTO {
    private Long id;
    private String email;
    private String fullname;

    private Collection<Role> roles;
}
