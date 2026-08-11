package com.jobflow.jobflow.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.jobflow.jobflow.enums.InterviewType;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "interviews")
public class Interview {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime scheduledDate;
    private String notes;

    @Enumerated(EnumType.STRING)
    private InterviewType  interviewType;

    @ManyToOne
    @JoinColumn(name = "application_id")
    @JsonBackReference
    private Application application;
}
