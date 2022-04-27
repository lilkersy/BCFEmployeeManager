package com.example.organogram.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import java.util.Set;

@Entity
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String username;
    private String password;
    private String email;

    @ManyToMany
    private Set<Authority> authorities;
}
