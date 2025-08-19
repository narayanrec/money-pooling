package com.startup.usergroup.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
public class PoolGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String name;

    @ManyToOne
    private User createdBy;

    // Getters and Setters
}