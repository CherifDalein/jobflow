package com.jobflow.jobflow.dto;

import com.jobflow.jobflow.enums.InterviewType;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CreateInterviewRequest {
    @NotNull(message = "L'id de l'application est obligatoire")
    private Long applicationId;

    @NotNull(message = "La date prévue pour l'entretien est obligatoire")
    private LocalDateTime scheduledDate;

    @NotNull(message = "Le type de l'entretien est obligatoire")
    private InterviewType interviewType;

    private String notes;

}
