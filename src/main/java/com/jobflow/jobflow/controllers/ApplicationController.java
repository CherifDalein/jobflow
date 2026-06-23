package com.jobflow.jobflow.controllers;

import com.jobflow.jobflow.dto.CreateApplicationRequest;
import com.jobflow.jobflow.models.Application;
import com.jobflow.jobflow.services.ApplicationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/applications")
public class ApplicationController {
    private final ApplicationService applicationService;

    public ApplicationController(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    @PostMapping
    public ResponseEntity<?> addApplication(@Valid @RequestBody CreateApplicationRequest request,
                                            @RequestHeader("Authorization") String token) {
        try{
            Application savedApplication = applicationService.createApplication(request, token);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Candidature créée");
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            return  ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
