package com.henheang.authhub.domain;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "users")
@Table
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class User {

    @Id
    private Long id;

    private String email;

    private String name;

    private String password;

    private String imageUrl;

    @Column(name = "email_verified", nullable = false)
    private Boolean emailVerified;

}
