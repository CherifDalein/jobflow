package com.jobflow.jobflow.services;

import com.jobflow.jobflow.dto.CreateInterviewRequest;
import com.jobflow.jobflow.dto.UpdateInterviewRequest;
import com.jobflow.jobflow.models.Application;
import com.jobflow.jobflow.models.Interview;
import com.jobflow.jobflow.models.User;
import com.jobflow.jobflow.repositories.ApplicationRepository;
import com.jobflow.jobflow.repositories.InterviewRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

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

    public Interview getInterviewById(Long interviewId, String tokenBearer) throws Exception{
        if(tokenBearer == null || !tokenBearer.startsWith("Bearer ")){
            throw new Exception("Token invalide");
        }
        String token = tokenBearer.substring(7);
        Long userId = jwtService.extractUserId(token);

        Interview interview = interviewRepository.findById(interviewId)
                .orElseThrow(() -> new Exception("Entretien inexistant"));

        Application application = interview.getApplication();
        User user = application.getUser();
        if(!user.getId().equals(userId)){
            throw new Exception("Vous n'avez pas le droit d'acceder a cet entretien");
        }
        return interview;
    }

    public Interview updateInterview(Long id, UpdateInterviewRequest request, String tokenBearer) throws Exception{
        if(tokenBearer == null || !tokenBearer.startsWith("Bearer ")){
            throw new Exception("Token invalide");
        }
        String token = tokenBearer.substring(7);
        Long userId = jwtService.extractUserId(token);

        Interview interview = interviewRepository.findById(id)
                .orElseThrow(() -> new Exception("Entretien inexistant"));

        Application application = interview.getApplication();
        User user = application.getUser();
        if(!user.getId().equals(userId)){
            throw new Exception("Vous n'avez pas le droit de modifier cet entretien");
        }

        interview.setScheduledDate(request.getScheduledDate());
        interview.setNotes(request.getNotes());
        interview.setInterviewType(request.getInterviewType());

        return interviewRepository.save(interview);
    }

    public void deleteInterview(Long id, String tokenBearer) throws Exception{
        if(tokenBearer == null || !tokenBearer.startsWith("Bearer ")){
            throw new Exception("Token invalide");
        }
        String token = tokenBearer.substring(7);
        Long userId = jwtService.extractUserId(token);

        Interview interview = interviewRepository.findById(id)
                .orElseThrow(() -> new Exception("Entretien inexistant"));

        Application application = interview.getApplication();
        User user = application.getUser();
        if(!user.getId().equals(userId)){
            throw new Exception("Vous n'avez pas le droit de supprimer cet entretien");
        }

        interviewRepository.delete(interview);
    }


}
