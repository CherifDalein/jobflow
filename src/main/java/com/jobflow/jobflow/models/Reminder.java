package com.jobflow.jobflow.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.jobflow.jobflow.enums.ReminderStatus;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "reminders")
public class Reminder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate reminderDate;
    private String message;

    @Enumerated(EnumType.STRING)
    private ReminderStatus reminderStatus;

    @ManyToOne
    @JoinColumn(name = "application_id")
    @JsonBackReference
    private Application application;
}
