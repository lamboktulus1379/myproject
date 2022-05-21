package com.test.myproject.infrastructure.interfaces;

import com.test.myproject.core.user_aggreate.User;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IUserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);

    Page<User> findByFullnameLike(String fullname, Pageable pageable);
}
