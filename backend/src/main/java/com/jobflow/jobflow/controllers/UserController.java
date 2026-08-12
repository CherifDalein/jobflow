package com.jobflow.jobflow.controllers;


import com.jobflow.jobflow.dto.*;
import com.jobflow.jobflow.models.User;
import com.jobflow.jobflow.services.JwtService;
import com.jobflow.jobflow.services.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api/auth")
public class UserController {
    private final UserService userService;
    private final JwtService jwtService;

    public UserController(UserService userService, JwtService jwtService) {
        this.userService = userService;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody CreateUserRequest request) {
        try{
            User savedUser = userService.registerUser(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        try{
            User user = userService.loginUser(request);

            String token = jwtService.generateToken(user);

            LoginResponse response = new LoginResponse(token);

            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(@RequestHeader("Authorization") String token) {
        try{
            UserProfileResponse user = userService.me(token);
            return ResponseEntity.status(HttpStatus.OK).body(user);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    @PutMapping("/me")
    public ResponseEntity<?> updateProfile(@Valid
                                           @RequestHeader("Authorization") String token,
                                           @RequestBody UpdateProfileRequest request){
        try{
            UserProfileResponse user = userService.updateProfile(token, request);
            return ResponseEntity.status(HttpStatus.OK).body(user);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    @PutMapping("/me/password")
    public ResponseEntity<?> updatePassword(@Valid
                                            @RequestHeader("Authorization") String token,
                                            @RequestBody ChangePasswordRequest request){
        try{
            userService.updatePassword(token, request);
            return ResponseEntity.ok().build();
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }


}
