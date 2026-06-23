package com.jobflow.jobflow.services;

import com.jobflow.jobflow.dto.CreateApplicationRequest;
import com.jobflow.jobflow.models.Application;
import com.jobflow.jobflow.models.User;
import com.jobflow.jobflow.repositories.ApplicationRepository;
import com.jobflow.jobflow.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class ApplicationService {
    private final ApplicationRepository applicationRepository;
    private final UserRepository userRepository;
    private final JwtService jwtService;

    public ApplicationService(ApplicationRepository applicationRepository,
                              UserRepository userRepository,
                              JwtService jwtService) {
        this.applicationRepository = applicationRepository;
        this.userRepository = userRepository;
        this.jwtService = jwtService;

    }

    public Application createApplication(CreateApplicationRequest request, String tokenBearer) throws  Exception {
        if(tokenBearer == null || !tokenBearer.startsWith("Bearer ")) {
            throw new Exception("Token de sécurité manquant ou invalide");
        }
        //Nettoyage du token en enlevant le Bearer+espace
        String token = tokenBearer.substring(7);

        Long userId = jwtService.extractUserId(token);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new Exception("Utilisateur non trouvé"));

        Application application = new Application();
        application.setCompanyName(request.getCompanyName());
        application.setLocation(request.getLocation());
        application.setPosition(request.getPosition());
        application.setContractType(request.getContractType());
        if(request.getApplicationDate()==null){
            application.setApplicationDate(LocalDate.now());
        }else{
            application.setApplicationDate(request.getApplicationDate());
        }
        application.setNotes(request.getNotes());
        application.setStatus(request.getStatus());
        application.setUser(user);

        return applicationRepository.save(application);
    }

    public Application updateApplication(CreateApplicationRequest, String tokenBearer) throws  Exception {}

}
