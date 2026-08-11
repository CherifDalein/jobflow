package com.jobflow.jobflow.models;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.jobflow.jobflow.enums.Role;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private Role role;

    @OneToMany(mappedBy = "user")
    @JsonManagedReference
    private List<Application> applications;

    @OneToMany(mappedBy = "user")
    @JsonManagedReference
    private List<Resume> resumes;

}
