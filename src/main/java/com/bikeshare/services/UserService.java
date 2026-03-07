package com.bikeshare.services;

import com.bikeshare.dto.AuthResponse;
import com.bikeshare.dto.LoginRequest;
import com.bikeshare.dto.RegisterRequest;
import com.bikeshare.models.User;
import com.bikeshare.repositories.UserRepository;
import com.bikeshare.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .phone(request.getPhone())
                .aadharOrPan(request.getAadharOrPan())
                .isVerified(true) // For simplicity, auto-verify in MVP. In real app, verify via OTP/Document check.
                .build();

        userRepository.save(user);
        String jwtToken = jwtService.generateToken(user);
        
        return new AuthResponse(
                jwtToken, 
                user.getId(), 
                user.getName(), 
                user.getEmail(), 
                user.getPhone(), 
                user.getIsVerified()
        );
    }

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));
                
        String jwtToken = jwtService.generateToken(user);
        
        return new AuthResponse(
                jwtToken, 
                user.getId(), 
                user.getName(), 
                user.getEmail(), 
                user.getPhone(), 
                user.getIsVerified()
        );
    }
    
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public void updatePushToken(Long userId, String pushToken) {
        User user = getUserById(userId);
        user.setFcmToken(pushToken);
        userRepository.save(user);
    }
}
