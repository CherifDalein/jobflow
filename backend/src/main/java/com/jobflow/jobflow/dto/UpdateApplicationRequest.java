package com.jobflow.jobflow.dto;

import com.jobflow.jobflow.models.Application;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateApplicationRequest {
    @NotBlank(message = "Le nom de l'entreprise est obligatoire")
    @Size(max = 100, message = "Le nom de l'entreprise ne doit pas depasser 100 caractères")
    private String companyName;

    private String location;

    @NotBlank(message = "Le nom du poste est obligatoire")
    private String position;

    private String status;

    private String notes;
}
