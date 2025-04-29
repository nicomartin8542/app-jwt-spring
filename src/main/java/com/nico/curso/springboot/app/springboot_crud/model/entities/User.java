package com.nico.curso.springboot.app.springboot_crud.model.entities;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.nico.curso.springboot.app.springboot_crud.valid.ExistByUserName.ExistByUserName;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinTable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
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
    @Column(unique = true)
    @Pattern(regexp = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$", message = "Debe ser un email valido")
    private String username;

    @NotBlank(message = "La contraseña no puede estar vacia")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @Transient
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String passwordConfirmation;

    @NotBlank(message = "El nombre no puede estar vacio")
    @Length(min = 3, max = 50, message = "El nombre debe tener entre 3 y 50 caracteres")
    private String name;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private boolean enabled;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private boolean confirm;

    @JsonIgnoreProperties(value = { "users", "handler", "hibernateLazyInitializer" })
    @ManyToMany
    @JoinTable(name = "users_roles",

            joinColumns = @JoinColumn(name = "user_id"),

            inverseJoinColumns = @JoinColumn(name = "roles_id"),

            uniqueConstraints = @UniqueConstraint(columnNames = { "user_id", "roles_id" }))
    private List<Role> roles;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Token> tokens = new ArrayList<>();

    @Transient // Es un atributo que no se guarda en la base de datos
    @JsonIgnore
    private boolean admin;

    @Embedded
    @JsonIgnore
    private Audit audit = new Audit();

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
        this.confirm = false;
        this.audit.prePersist();
    }

    @PreUpdate
    public void preUpdate() {
        this.audit.preUpdate();
    }

}
