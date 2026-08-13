package com.jobflow.jobflow.controllers;

import com.jobflow.jobflow.dto.CreateInterviewRequest;
import com.jobflow.jobflow.dto.UpdateInterviewRequest;
import com.jobflow.jobflow.models.Interview;
import com.jobflow.jobflow.services.InterviewService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/{id}")
    public ResponseEntity<?> getInterviewById(@PathVariable("id") Long id,
                                              @RequestHeader("Authorization")  String token) {
        try{
            Interview interview = interviewService.getInterviewById(id, token);
            return ResponseEntity.status(HttpStatus.OK).body(interview);
        }catch(Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateInterview(@Valid
                                             @PathVariable("id") Long id,
                                             @RequestHeader("Authorization") String token,
                                             @RequestBody UpdateInterviewRequest request) {
        try{
            Interview interview = interviewService.updateInterview(id, request,token);
            return ResponseEntity.status(HttpStatus.OK).body(interview);
        }catch(Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteInterview(@PathVariable Long id,
                                             @RequestHeader("Authorization") String token) {

        try {
            interviewService.deleteInterview(id, token);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/upcoming")
    public ResponseEntity<?> getUpcomingInterviews(@RequestHeader("Authorization") String token) {
        try {
            List<Interview> interviews =  interviewService.getUpcomingInterviews(token);
            return ResponseEntity.status(HttpStatus.OK).body(interviews);

        }catch(Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
