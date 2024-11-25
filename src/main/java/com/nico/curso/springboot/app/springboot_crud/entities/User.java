package com.nico.curso.springboot.app.springboot_crud.entities;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.nico.curso.springboot.app.springboot_crud.valid.ExistByUserName.ExistByUserName;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinTable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Data;

@Entity
@Table(name = "users")
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ExistByUserName
    @NotBlank
    @Size(min = 4, max = 20)
    @Column(unique = true)
    private String username;

    @NotBlank
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @JsonIgnoreProperties(value = { "users", "handler", "hibernateLazyInitializer" })
    @ManyToMany
    @JoinTable(name = "users_roles",

            joinColumns = @JoinColumn(name = "user_id"),

            inverseJoinColumns = @JoinColumn(name = "roles_id"),

            uniqueConstraints = @UniqueConstraint(columnNames = { "user_id", "roles_id" }))
    private List<Role> roles;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private boolean enabled;

    @Transient
    @JsonIgnore
    private boolean admin;

    public User() {
        this.roles = new ArrayList<>();
    }

    public User(String username, String password) {
        this();
        this.username = username;
        this.password = password;
    }

    @PrePersist
    public void prePersist() {
        this.enabled = true;
    }

}
