package com.jobflow.jobflow.dto;

import com.jobflow.jobflow.enums.ContractType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CreateApplicationRequest {
    @NotBlank(message = "Le nom de l'entreprise est obligatoire")
    @Size(max = 100, message = "Le nom de l'entreprise ne doit pas depasser 100 caractères")

    private String companyName;

    private String location;

    @NotBlank(message = "Le nom du poste est obligatoire")
    private String position;

    private ContractType contractType;

    @PastOrPresent(message = "La date de candidature ne peut pas être une date future")
    private LocalDate applicationDate;

    private String status;

    private String notes;

}
