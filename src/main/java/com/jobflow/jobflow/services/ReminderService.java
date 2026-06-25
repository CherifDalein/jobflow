package com.jobflow.jobflow.services;

import com.jobflow.jobflow.dto.CreateReminderRequest;
import com.jobflow.jobflow.enums.ReminderStatus;
import com.jobflow.jobflow.models.Application;
import com.jobflow.jobflow.models.Reminder;
import com.jobflow.jobflow.repositories.ApplicationRepository;
import com.jobflow.jobflow.repositories.ReminderRepository;
import org.springframework.stereotype.Service;

@Service
public class ReminderService {
    private final ReminderRepository reminderRepository;
    private final ApplicationRepository applicationRepository;
    private final JwtService jwtService;

    public ReminderService(ReminderRepository reminderRepository,
                           ApplicationRepository applicationRepository,
                           JwtService jwtService) {
        this.reminderRepository = reminderRepository;
        this.applicationRepository = applicationRepository;
        this.jwtService = jwtService;
    }

    public Reminder createReminder(CreateReminderRequest request, String tokenBearer) throws Exception {
        if(tokenBearer == null || !tokenBearer.startsWith("Bearer ")) {
            throw new Exception("Token de sécurité manquant");
        }
        tokenBearer = tokenBearer.substring(7);

        Long userId = jwtService.extractUserId(tokenBearer);

        Application application = applicationRepository.findById(request.getApplicationId())
                .orElseThrow(()-> new Exception("Candidature inexistante"));

        if(!application.getUser().getId().equals(userId))
            throw new Exception("Vous n'avez pas le droit de toucher a cette candidature");

        Reminder reminder = new Reminder();
        reminder.setReminderDate(request.getReminderDate());
        reminder.setMessage(request.getMessage());
        reminder.setReminderStatus(ReminderStatus.PENDING);
        reminder.setApplication(application);

        return   reminderRepository.save(reminder);
    }

}
