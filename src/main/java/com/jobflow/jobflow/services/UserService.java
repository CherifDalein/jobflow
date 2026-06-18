package com.jobflow.jobflow.services;


import com.jobflow.jobflow.dto.CreateUserRequest;
import com.jobflow.jobflow.dto.LoginRequest;
import com.jobflow.jobflow.dto.LoginResponse;
import com.jobflow.jobflow.enums.Role;
import com.jobflow.jobflow.models.User;
import com.jobflow.jobflow.repositories.UserRepository;
import jakarta.validation.Valid;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final PasswordEncoder passwordEncoder;
    private UserRepository userRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = new BCryptPasswordEncoder();
        this.passwordEncoder = passwordEncoder;
    }

    public User registerUser(CreateUserRequest request) throws Exception {
        if(userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw  new Exception("Email already exists");
        }
        User user = new User();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setPassword(bCryptPasswordEncoder.encode(request.getPassword()));
        if(request.getRole() == null) {
            user.setRole(Role.USER);
        }else{
            user.setRole(request.getRole());
        }
        userRepository.save(user);
        return user;
    }

    public User loginUser(LoginRequest loginRequest) throws Exception {
        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new Exception("Invalid email or password"));
        if(!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new Exception("Invalid email or password");
        }
        return user;
    }

}
