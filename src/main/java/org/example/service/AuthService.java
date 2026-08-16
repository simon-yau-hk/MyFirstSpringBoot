package org.example.service;

import org.example.dto.auth.AuthResponse;
import org.example.dto.auth.AuthUserResponse;
import org.example.dto.auth.LoginRequest;
import org.example.dto.auth.RegisterRequest;
import org.example.entity.User;
import org.example.exception.DuplicateResourceException;
import org.example.repository.UserRepository;
import org.example.security.JwtService;
import org.example.security.UserPrincipal;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService,
            AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new DuplicateResourceException("User already exists with email: " + request.getEmail());
        }

        User user = new User(request.getName(), request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        User savedUser = userRepository.save(user);

        UserPrincipal principal = UserPrincipal.fromUser(savedUser);
        String token = jwtService.generateToken(principal);

        return buildAuthResponse(token, savedUser);
    }

    public AuthResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()));

        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
        String token = jwtService.generateToken(principal);

        User user = userRepository.findByEmail(principal.getUsername())
                .orElseThrow();

        return buildAuthResponse(token, user);
    }

    public AuthUserResponse getCurrentUser(UserPrincipal principal) {
        return new AuthUserResponse(
                principal.getId(),
                principal.getName(),
                principal.getUsername());
    }

    private AuthResponse buildAuthResponse(String token, User user) {
        AuthUserResponse userResponse = new AuthUserResponse(
                user.getId(),
                user.getName(),
                user.getEmail());
        return new AuthResponse(token, jwtService.getExpirationMs(), userResponse);
    }
}
