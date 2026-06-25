package com.jobflow.jobflow.services;

import com.jobflow.jobflow.dto.CreateInterviewRequest;
import com.jobflow.jobflow.models.Application;
import com.jobflow.jobflow.models.Interview;
import com.jobflow.jobflow.repositories.ApplicationRepository;
import com.jobflow.jobflow.repositories.InterviewRepository;
import org.springframework.stereotype.Service;

@Service
public class InterviewService {
    private final InterviewRepository interviewRepository;
    private final ApplicationRepository applicationRepository;
    private final JwtService jwtService;

    public InterviewService(InterviewRepository interviewRepository,
                            ApplicationRepository applicationRepository,
                            JwtService jwtService){
        this.interviewRepository = interviewRepository;
        this.applicationRepository = applicationRepository;
        this.jwtService = jwtService;
    }

    public Interview createInterview(CreateInterviewRequest request, String tokenBearer) throws Exception{
        if(tokenBearer == null || !tokenBearer.startsWith("Bearer ")){
            throw new Exception("Token de sécurtité manquant");
        }
        String token = tokenBearer.substring(7);

        Long userId = jwtService.extractUserId(token);

        Application application = applicationRepository.findById(request.getApplicationId())
                .orElseThrow(() -> new Exception("Candidature inexistante"));

        if(!application.getUser().getId().equals(userId)){
            throw new Exception("Vous n'avez pas le droit de toucher a cette candidature");
        }
        Interview interview = new Interview();
        interview.setScheduledDate(request.getScheduledDate());
        interview.setNotes(request.getNotes());
        interview.setInterviewType(request.getInterviewType());
        interview.setApplication(application);

        return interviewRepository.save(interview);
    }


}
