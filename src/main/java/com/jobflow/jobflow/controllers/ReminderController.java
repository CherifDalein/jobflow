package com.jobflow.jobflow.controllers;

import com.jobflow.jobflow.dto.CreateReminderRequest;
import com.jobflow.jobflow.models.Reminder;
import com.jobflow.jobflow.services.ReminderService;
import jakarta.validation.Valid;
import org.springframework.boot.web.server.servlet.context.ServletComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reminders")
public class ReminderController {
    private final ReminderService reminderService;

    public ReminderController(ReminderService reminderService) {
        this.reminderService = reminderService;
    }

    @PostMapping
    public ResponseEntity<?> createReminder(@Valid @RequestBody CreateReminderRequest request,
                                         @RequestHeader("Authorization") String token){
        try{
            Reminder reminder = reminderService.createReminder(request,token);
            return ResponseEntity.status(HttpStatus.CREATED).body(reminder);
        }catch(Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
