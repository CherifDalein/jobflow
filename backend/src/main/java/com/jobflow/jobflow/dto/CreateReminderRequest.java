package com.jobflow.jobflow.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CreateReminderRequest {
    @NotNull(message = "L'id de la candidature est obligatoire")
    private Long applicationId;

    @NotNull(message = "La date de la relance est obligatoire")
    private LocalDate reminderDate;

    @NotBlank(message = "Le message de la relance est obligatoire")
    private String message;
}
