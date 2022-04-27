package com.example.organogram.security;

import com.example.organogram.model.User;
import com.example.organogram.model.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.internal.constraintvalidators.hv.EmailValidator;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * Authenticate a user from the database.
 */
@Component("userDetailsService")
@RequiredArgsConstructor
@Slf4j
public class DomainUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(final String username) {
        log.debug("Authenticating {}", username);

        if (new EmailValidator().isValid(username, null)) {
            return userRepository
                .findOneWithAuthoritiesByEmailIgnoreCase(username)
                .map(user -> createSpringSecurityUser(username, user))
                .orElseThrow(() -> new UsernameNotFoundException("User with email " + username + " was not found in the database"));
        }

        String lowercaseLogin = username.toLowerCase(Locale.ENGLISH);
        return userRepository
            .findOneWithAuthoritiesByUsername(lowercaseLogin)
            .map(user -> createSpringSecurityUser(lowercaseLogin, user))
            .orElseThrow(() -> new UsernameNotFoundException("User " + lowercaseLogin + " was not found in the database"));
    }

    private org.springframework.security.core.userdetails.User createSpringSecurityUser(String lowercaseLogin, User user) {
        List<GrantedAuthority> grantedAuthorities = user
            .getAuthorities()
            .stream()
            .map(authority -> new SimpleGrantedAuthority(authority.getName()))
            .collect(Collectors.toList());
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), grantedAuthorities);
    }
}
