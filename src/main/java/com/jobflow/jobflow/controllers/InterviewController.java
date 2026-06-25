package com.jobflow.jobflow.controllers;

import com.jobflow.jobflow.dto.CreateInterviewRequest;
import com.jobflow.jobflow.models.Interview;
import com.jobflow.jobflow.services.InterviewService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/interviews")
public class InterviewController {

    private final InterviewService interviewService;

    public InterviewController(InterviewService interviewService) {
        this.interviewService = interviewService;
    }

    @PostMapping
    public ResponseEntity<?> createInterview(@Valid @RequestBody CreateInterviewRequest request,
                                          @RequestHeader("Authorization") String token) {
        try{
            Interview interview = interviewService.createInterview(request,token);
            return ResponseEntity.status(HttpStatus.CREATED).body(interview);
        }catch(Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
