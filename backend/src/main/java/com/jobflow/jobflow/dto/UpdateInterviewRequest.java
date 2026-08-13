package com.jobflow.jobflow.dto;

import com.jobflow.jobflow.enums.InterviewType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UpdateInterviewRequest {
    private LocalDateTime scheduledDate;
    private String notes;

    @Enumerated(EnumType.STRING)
    private InterviewType interviewType;
}
