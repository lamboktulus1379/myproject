package com.test.myproject.core.interfaces;

import com.test.myproject.core.user_aggreate.User;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IUserService {
    public Page<User> all(Pageable pageable);

    public User save(User user);

    public User one(Long id);

    public User findAndUpdate(User user, Long id);

    public void deleteById(Long id);

    public User getUserByEmail(String email);

    public void addRoleToUser(String email, String role);

    public Page<User> allByFullnameLike(String fullname, Pageable pageable);
}
