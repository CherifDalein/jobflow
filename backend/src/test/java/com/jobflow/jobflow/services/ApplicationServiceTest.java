package com.jobflow.jobflow.services;

import com.jobflow.jobflow.dto.CreateApplicationRequest;
import com.jobflow.jobflow.dto.UpdateApplicationRequest;
import com.jobflow.jobflow.enums.ContractType;
import com.jobflow.jobflow.models.Application;
import com.jobflow.jobflow.models.User;
import com.jobflow.jobflow.repositories.ApplicationRepository;
import com.jobflow.jobflow.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ApplicationServiceTest {
    @Mock
    ApplicationRepository applicationRepository;
    @Mock
    UserRepository userRepository;
    @Mock
    JwtService jwtService;

    @InjectMocks
    ApplicationService applicationService;

    private User mockUser;
    private CreateApplicationRequest request;
    private final String validTokenBearer = "Bearer dummy_jwt_token";
    private final String rawToken = "dummy_jwt_token";

    @BeforeEach
    public void setUp() {
        mockUser = new User();
        mockUser.setId(1L);
        mockUser.setEmail("test@jobflow.com");

        request = new CreateApplicationRequest();
        request.setCompanyName("Inetum");
        request.setPosition("Développeur Java");
        request.setLocation("Rennes");
        request.setContractType(ContractType.ALTERNANCE);
        request.setStatus("APPLIED");
        request.setNotes("Déposé sur LinkedIn");
    }

    @Test
    void createApplication_ShouldSuccess_WhenTokenAndUserAreValid() throws Exception {
        when(jwtService.extractUserId(rawToken)).thenReturn(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));
        when(applicationRepository.save(any(Application.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Application createdApplication = applicationService.createApplication(request, validTokenBearer);

        assertNotNull(createdApplication);
        assertEquals("Inetum", createdApplication.getCompanyName());
        assertEquals("Développeur Java", createdApplication.getPosition());
        assertEquals(mockUser, createdApplication.getUser());
        assertEquals(LocalDate.now(), createdApplication.getApplicationDate());
        assertEquals(ContractType.ALTERNANCE, createdApplication.getContractType());

        verify(applicationRepository, times(1)).save(any(Application.class));
    }

    @Test
    void createApplication_ShouldThrowException_WhenTokenIsMissingOrInvalid() {
        String invalidToken = "jwt_token";

        Exception exception = assertThrows(Exception.class, () -> {
            applicationService.createApplication(request, invalidToken);
        });

        assertEquals("Token de sécurité manquant ou invalide", exception.getMessage());

        verify(userRepository, never()).findById(anyLong());
        verify(applicationRepository, never()).save(any(Application.class));
    }

    @Test
    void createApplication_ShouldThrowException_WhenUserIsNull() {
        when(jwtService.extractUserId(rawToken)).thenReturn(99L);
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(Exception.class, () -> {
            applicationService.createApplication(request, validTokenBearer);
        });

        assertEquals("Utilisateur non trouvé", exception.getMessage());
        verify(applicationRepository, never()).save(any(Application.class));
    }

    @Test
    void updateApplication_ShouldSuccess_WhenUserIsOwner() throws Exception {
        Long applicationId = 10L;

        Application existingApplication = new Application();
        existingApplication.setId(applicationId);
        existingApplication.setUser(mockUser);
        existingApplication.setCompanyName("Ancien nom");

        UpdateApplicationRequest request = new UpdateApplicationRequest();
        request.setCompanyName("Nouveau nom");
        request.setPosition("Lead Dev Java");
        request.setStatus("INTERVIEW");

        when(jwtService.extractUserId(rawToken)).thenReturn(1L);
        when(applicationRepository.findById(applicationId)).thenReturn(Optional.of(existingApplication));
        when(applicationRepository.save(any(Application.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Application updatedApplication = applicationService.updateApplication(applicationId, request, validTokenBearer);

        assertNotNull(updatedApplication);
        assertEquals("Nouveau nom", updatedApplication.getCompanyName());
        assertEquals("Lead Dev Java", updatedApplication.getPosition());
        assertEquals("INTERVIEW", updatedApplication.getStatus());
        assertEquals(mockUser, updatedApplication.getUser());

        verify(applicationRepository, times(1)).save(any(Application.class));
    }

    @Test
    void updateApplication_ShouldThrowException_WhenUserIsNotOwner() {
        Long applicationId = 10L;

        User hackerUser = new User();
        hackerUser.setId(99L);

        Application existingApplication = new Application();
        existingApplication.setId(applicationId);
        existingApplication.setUser(hackerUser);

        when(jwtService.extractUserId(rawToken)).thenReturn(1L);
        when(applicationRepository.findById(applicationId)).thenReturn(Optional.of(existingApplication));

        UpdateApplicationRequest updatedApplication = new UpdateApplicationRequest();

        Exception exception = assertThrows(Exception.class, () -> {
            applicationService.updateApplication(applicationId, updatedApplication, validTokenBearer);
        });

        assertEquals("Vous n'etes pas autorisé à modifier cette candidature", exception.getMessage());

        verify(applicationRepository, never()).save(any(Application.class));
    }

    @Test
    void updateApplication_ShouldThrowException_WhenApplicationNotFound() {
        Long unknownId = 404L;
        UpdateApplicationRequest updatedRequest= new UpdateApplicationRequest();

        when(jwtService.extractUserId(rawToken)).thenReturn(1L);
        when(applicationRepository.findById(unknownId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(Exception.class, () -> {
            applicationService.updateApplication(unknownId, updatedRequest, validTokenBearer);
        });

        assertEquals("Candidature non trouvée", exception.getMessage());
        verify(applicationRepository, never()).save(any(Application.class));
    }
}
