package com.test.myproject.core.services;

import java.util.ArrayList;
import java.util.Collection;

import com.test.myproject.core.interfaces.IUserService;
import com.test.myproject.core.role_aggreate.Role;
import com.test.myproject.core.user_aggreate.User;
import com.test.myproject.core.user_aggreate.exception.EmailAlreadyExistsException;
import com.test.myproject.core.user_aggreate.exception.UserNotFoundException;
import com.test.myproject.infrastructure.interfaces.IRoleRepository;
import com.test.myproject.infrastructure.interfaces.IUserRepository;

import org.hibernate.annotations.Cache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
@CacheConfig(cacheNames = "user")
public class UserService implements IUserService, UserDetailsService {
    @Autowired
    private final PasswordEncoder passwordEncoder;

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private IRoleRepository roleRepository;

    @Override
    @Caching(evict = {
            @CacheEvict(value = "usercache")
    })
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username);
        if (user == null) {
            log.error("User not found");
            throw new UsernameNotFoundException("User not found");
        } else {
            log.info("User found: {}", username);
        }

        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        user.getRoles().forEach(role -> {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        });

        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), authorities);
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "usercache")
    })
    public User save(User user) {
        User isExist = this.getUserByEmail(user.getEmail());
        if (isExist != null) {
            throw new EmailAlreadyExistsException(user.getEmail());
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        Role role = roleRepository.findByName("ROLE_USER");
        user.getRoles().add(role);
        return userRepository.save(user);
    }

    @Override
    public Page<User> all(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @Override
    public User one(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
    }

    @Override
    public User findAndUpdate(User newUser, Long id) {
        return userRepository.findById(id).map(user -> {
            user.setEmail(newUser.getEmail());
            user.setFullname(newUser.getFullname());
            user.setPassword(passwordEncoder.encode(newUser.getPassword()));
            return userRepository.save(user);
        }).orElseGet(() -> {
            newUser.setId(id);
            return userRepository.save(newUser);
        });
    }

    @Override
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    @Cacheable(value = "usercache", key = "#email")
    public User getUserByEmail(String email) {
        log.info("Load user with email " + email);
        return userRepository.findByEmail(email);
    }

    @Override
    public void addRoleToUser(String email, String roleName) {
        User user = userRepository.findByEmail(email);
        Role role = roleRepository.findByName(roleName);
        user.getRoles().add(role);
    }

    @Override
    public Page<User> allByFullnameLike(String fullname, Pageable pageable) {
        return userRepository.findByFullnameLike(fullname, pageable);
    }

}
