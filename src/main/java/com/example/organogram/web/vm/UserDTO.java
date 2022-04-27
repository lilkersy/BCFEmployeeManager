package com.example.organogram.web.vm;

import com.example.organogram.model.Authority;
import com.example.organogram.model.User;
import lombok.Getter;

import java.util.Set;
import java.util.stream.Collectors;

@Getter
public class UserDTO {

    private Long id;
    private String username;

    private String name;

    private String email;

    private Set<String> authorities;

    public UserDTO() {
    }

    public UserDTO(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.name = user.getName();
        this.email = user.getEmail();
        this.authorities = user.getAuthorities().stream()
            .map(Authority::getName)
            .collect(Collectors.toSet());
    }
}
