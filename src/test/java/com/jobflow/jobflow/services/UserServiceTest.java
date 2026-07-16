package com.jobflow.jobflow.services;


import com.jobflow.jobflow.dto.CreateUserRequest;
import com.jobflow.jobflow.enums.Role;
import com.jobflow.jobflow.models.User;
import com.jobflow.jobflow.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private CreateUserRequest  request;

    @BeforeEach
    public void setUp() {
        request = new CreateUserRequest();
        request.setEmail("mcd2020etude@gimail.com");
        request.setPassword("password");
        request.setFirstName("Cherif");
        request.setLastName("Diallo");
    }

    @Test
    void registerUser_ShouldSuccess_WhenEmailDoesNotExist() throws Exception {
        // 1 - ARRANGE
        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());

        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // 2 - ACT
        User savedUser = userService.registerUser(request);

        // 3 - ASSERT
        assertNotNull(savedUser);
        assertEquals("Cherif", savedUser.getFirstName());
        assertEquals("Diallo", savedUser.getLastName());
        assertEquals("mcd2020etude@gimail.com", savedUser.getEmail());
        assertEquals(Role.USER,  savedUser.getRole());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void registerUser_ShouldThrowException_WhenEmailAlreadyExists() throws Exception {
        User existingUser = new User();
        existingUser.setEmail(request.getEmail());
        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(existingUser));

        Exception exception = assertThrows(Exception.class,() -> userService.registerUser(request));

        assertEquals("Email already exists", exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }


}
