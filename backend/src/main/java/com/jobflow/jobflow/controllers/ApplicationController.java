package com.jobflow.jobflow.controllers;

import com.jobflow.jobflow.dto.CreateApplicationRequest;
import com.jobflow.jobflow.dto.UpdateApplicationRequest;
import com.jobflow.jobflow.models.Application;
import com.jobflow.jobflow.services.ApplicationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
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

    @PutMapping("/{id}")
    public ResponseEntity<?> updateApplication(@Valid @PathVariable Long id,
                                               @RequestBody UpdateApplicationRequest  request,
                                               @RequestHeader("Authorization") String token){
        try{
            Application application = applicationService.updateApplication(id, request, token);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Candidature modifiée avec succès");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            return  ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteApplication(@PathVariable Long id,
                                               @RequestHeader("Authorization") String token){
        try{
            applicationService.deleteApplication(id, token);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (Exception e) {
            return  ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> getApplications(@RequestHeader("Authorization") String token){
        try{
            List<Application> applications = applicationService.getAllUserApplications(token);
            return ResponseEntity.status(HttpStatus.OK).body(applications);
        }catch(Exception e){
            return  ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/page")
    public ResponseEntity<?> getMyApplications(@RequestHeader("Authorization") String token,
                                               @RequestParam(value = "page", defaultValue = "0") int page,
                                               @RequestParam(value = "size", defaultValue = "10") int size,
                                               @RequestParam(value = "status", required = false) String status){
        try{
            var applicationsPage = applicationService.getMyApplications(page, size, status, token);
            return ResponseEntity.status(HttpStatus.OK).body(applicationsPage);
        }
        catch (Exception e){
            return  ResponseEntity.badRequest().body(e.getMessage());
        }
    }


}
