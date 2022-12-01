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
import com.example.selectitdelivery.dao.repositories.AppUserRepository;
import com.example.selectitdelivery.dao.repositories.AppUserRoleRepository;
import com.example.selectitdelivery.dao.repositories.RoleRepository;
import com.example.selectitdelivery.security.jwt.JwtUtils;
import com.example.selectitdelivery.security.services.RefreshTokenService;
import com.example.selectitdelivery.security.services.UserDetailsImpl;
import com.example.selectitdelivery.service.exceptions.RefreshTokenException;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping(value = "/api/auth")
@RestController
@Slf4j
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    AppUserRepository appUserRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    AppUserRoleRepository appUserRoleRepository;
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
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
        }

        Set<RoleEntity> userRoles = new HashSet<>();

        signupRequest.getRole().forEach(role -> {
                switch (role) {
                    case "ROLE_ADMIN":
                        RoleEntity adminRole = roleRepository.findByRoleName("ROLE_ADMIN")
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        userRoles.add(adminRole);
                        break;
                    case "ROLE_EMPLOYEE":
                        RoleEntity employeeRole = roleRepository.findByRoleName("ROLE_EMPLOYEE")
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        userRoles.add(employeeRole);
                        break;
                    default:
                        RoleEntity userRole = roleRepository.findByRoleName("ROLE_USER")
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        userRoles.add(userRole);
                }});

        AppUserEntity newAppUserEntity = new AppUserEntity(signupRequest.getEmail(),passwordEncoder.encode(signupRequest.getPassword()),userRoles);

        appUserRepository.save(newAppUserEntity);

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
