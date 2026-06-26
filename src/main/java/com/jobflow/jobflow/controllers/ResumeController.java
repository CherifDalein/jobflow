package com.jobflow.jobflow.controllers;

import com.jobflow.jobflow.models.Resume;
import com.jobflow.jobflow.services.ResumeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/resumes")
public class ResumeController {
    private final ResumeService resumeService;

    public ResumeController(ResumeService resumeService) {
        this.resumeService = resumeService;
    }
    @PostMapping("/upload")
    public ResponseEntity<?> addResume(@RequestParam("file") MultipartFile file,
                                       @RequestHeader("Authorization") String tokenBearer) throws Exception{
        try{
            Resume resume = resumeService.addResume(file,tokenBearer);
            return ResponseEntity.status(HttpStatus.CREATED).body(resume);
        }catch(Exception e){
            return  ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
