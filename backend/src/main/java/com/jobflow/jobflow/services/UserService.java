package com.jobflow.jobflow.services;


import com.jobflow.jobflow.dto.CreateUserRequest;
import com.jobflow.jobflow.dto.LoginRequest;
import com.jobflow.jobflow.dto.LoginResponse;
import com.jobflow.jobflow.dto.UserProfileResponse;
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
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtService jwtService;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
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

    public UserProfileResponse me(String tokenBearer) throws Exception {
        if(tokenBearer == null || !tokenBearer.startsWith("Bearer ")) {
            throw new Exception("Invalid token");
        }
        String token = tokenBearer.substring(7);

        Long userId = jwtService.extractUserId(token);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new Exception("Invalid user ID"));

        UserProfileResponse userProfileResponse = new UserProfileResponse();
        userProfileResponse.setId(user.getId());
        userProfileResponse.setFirstName(user.getFirstName());
        userProfileResponse.setLastName(user.getLastName());
        userProfileResponse.setEmail(user.getEmail());
        userProfileResponse.setRole(user.getRole());

        return userProfileResponse;
    }

}
