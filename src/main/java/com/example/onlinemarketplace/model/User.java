package com.example.onlinemarketplace.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder.Default;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "users")
public class User extends BaseEntity {

    @NotBlank
    @Size(max = 20)
    @Column(unique = true, nullable = false)
    private String username;

    @NotBlank
    @Size(max = 50)
    @Email
    @Column(unique = true, nullable = false)
    private String email;

    @NotBlank
    @Size(max = 120)
    private String password;

    @Default
    @ManyToMany(fetch = FetchType.LAZY)
    private Set<Role> roles = new HashSet<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Order> orders;

    @Column(length = 500)
    private String address;

    private String city;

    private String state;

    @Size(max = 20)
    private String phoneNumber;

    public void addRole(Role role) {
        this.roles.add(role);
    }

}