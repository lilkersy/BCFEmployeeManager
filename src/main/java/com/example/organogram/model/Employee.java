package com.example.organogram.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Employee {
    @Id
    @GeneratedValue
    @EqualsAndHashCode.Include
    private Long id;
    private String name;
    @ManyToOne(fetch = FetchType.LAZY)
    private Employee supervisor;
}
