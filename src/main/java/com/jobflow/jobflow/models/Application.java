package com.jobflow.jobflow.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.jobflow.jobflow.enums.ContractType;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@Table(name = "applications")
public class Application {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String companyName;
    private String location;
    private String position;
    @Enumerated(EnumType.STRING)
    private ContractType contractType;
    private LocalDate applicationDate;
    private String status;
    private String notes;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference  // Pour eviter les boubles infinies de Jackson
    private User user;

    @OneToMany(mappedBy = "application")
    @JsonManagedReference
    private List<Interview> interviews;

    @OneToMany(mappedBy = "application")
    @JsonManagedReference
    private List<Reminder> reminders;


}
