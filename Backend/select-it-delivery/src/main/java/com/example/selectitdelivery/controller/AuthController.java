package com.example.selectitdelivery.controller;

import com.example.selectitdelivery.Payload.Request.LoginRequest;
import com.example.selectitdelivery.Payload.Request.RefreshTokenRequest;
import com.example.selectitdelivery.Payload.Request.SignupRequest;
import com.example.selectitdelivery.Payload.Response.JwtResponse;
import com.example.selectitdelivery.Payload.Response.MessageResponse;
import com.example.selectitdelivery.Payload.Response.RefreshTokenResponse;
import com.example.selectitdelivery.dao.entity.AppUserEntity;
import com.example.selectitdelivery.dao.entity.RefreshToken;
import com.example.selectitdelivery.dao.entity.RoleEntity;
import com.example.selectitdelivery.dao.model.Client;
import com.example.selectitdelivery.dao.repositories.AppUserRepository;
import com.example.selectitdelivery.dao.repositories.RoleRepository;
import com.example.selectitdelivery.security.jwt.JwtUtils;
import com.example.selectitdelivery.security.services.RefreshTokenService;
import com.example.selectitdelivery.security.services.UserDetailsImpl;
import com.example.selectitdelivery.service.exceptions.ClientAlreadyExistsException;
import com.example.selectitdelivery.service.exceptions.RefreshTokenException;
import com.example.selectitdelivery.service.implementations.ClientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping(value = "/api/auth")
@RequiredArgsConstructor
@RestController
@Slf4j
public class AuthController {

    private final ClientService clientService;
    @Autowired
    AuthenticationManager authenticationManager;

    private final AppUserRepository appUserRepository;

    private final RoleRepository roleRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    JwtUtils jwtUtils;
    @Autowired
    RefreshTokenService refreshTokenService;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        String jwt = jwtUtils.generateJwtToken(userDetails);

        List<String> roles = userDetails.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());

        return ResponseEntity.ok(new JwtResponse(
                jwt,
                refreshToken.getRefreshTokenValue(),
                userDetails.getId(),
                userDetails.getEmail(),
                roles));
    }

    @PostMapping("/refreshtoken")
    public ResponseEntity<?> refreshtoken(@Valid @RequestBody RefreshTokenRequest request) {
        String requestRefreshToken = request.getRefreshToken();
        log.info("Token refresh! {}", requestRefreshToken);
        return refreshTokenService.findByToken(requestRefreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    String token = jwtUtils.generateTokenFromEmail(user.getEmail());
                    return ResponseEntity.ok(new RefreshTokenResponse(token, requestRefreshToken));
                })
                .orElseThrow(() -> new RefreshTokenException(requestRefreshToken,
                        "Refresh token is not in database!"));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signupRequest) {
        if(appUserRepository.existsByEmail(signupRequest.getEmail())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already taken!"));
        }

        Set<RoleEntity> userRoles = new HashSet<>();

        userRoles.add(roleRepository.findByRoleName("ROLE_USER").get());

        AppUserEntity newAppUser = new AppUserEntity(signupRequest.getEmail(),passwordEncoder.encode(signupRequest.getPassword()),userRoles);

        Client newClient = new Client(newAppUser,signupRequest.getFirstName(),signupRequest.getLastName(),signupRequest.getPhone());

        try {
            clientService.record(newClient);
        } catch (ClientAlreadyExistsException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }

    @PostMapping("/signout")
    public ResponseEntity<?> logoutUser() {
        log.info("logout");

        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        long userId = userDetails.getId();
        refreshTokenService.deleteByUserId(userId);
        return ResponseEntity.ok(new MessageResponse("Log out successful!"));
    }

}
