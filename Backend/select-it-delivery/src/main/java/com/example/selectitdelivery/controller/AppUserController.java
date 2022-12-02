package com.example.selectitdelivery.controller;

import com.example.selectitdelivery.Payload.Request.EmployeeUserRequest;
import com.example.selectitdelivery.Payload.Response.MessageResponse;
import com.example.selectitdelivery.controller.dto.AppUserDto;
import com.example.selectitdelivery.controller.dto.AppUserMapper;
import com.example.selectitdelivery.dao.entity.RoleEntity;
import com.example.selectitdelivery.dao.model.AppUser;
import com.example.selectitdelivery.dao.repositories.RoleRepository;
import com.example.selectitdelivery.service.exceptions.AppUserAlreadyExistsException;
import com.example.selectitdelivery.service.exceptions.AppUserNotFoundException;
import com.example.selectitdelivery.service.implementations.AppUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@RequestMapping("/api/appUser")
@RequiredArgsConstructor
@RestController
@ResponseBody
public class AppUserController {

    private final AppUserService appUserService;
    private final AppUserMapper appUserMapper;

    @Autowired
    private final RoleRepository roleRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

    @GetMapping(value = "/all")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Collection<AppUserDto> getUsers() {
        return appUserService.readAll()
                .stream()
                .map(appUserMapper::appUserToAppUserDto)
                .collect(Collectors.toList());
    }

    @GetMapping(value = "/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public AppUserDto readById(@PathVariable long id) {
        try {
            return appUserMapper.appUserToAppUserDto(appUserService.readById(id));
        } catch (AppUserNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    /*@PostMapping(value = "/create")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public AppUserDto create(@Valid @RequestBody AppUserDto createRequestAppUserDto) {
        AppUser createRequestAppUser = appUserMapper.appUserDtoToAppUser(createRequestAppUserDto);
        try {
            AppUser createdAppUser = appUserService.record(createRequestAppUser);
            return appUserMapper.appUserToAppUserDto(createdAppUser);
        } catch (AppUserAlreadyExistsException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }*/

    /*@PostMapping("/create")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> createUser(@Valid @RequestBody EmployeeUserRequest createEmployeeUserRequest) {
        if(appUserService.existsByEmail(createEmployeeUserRequest.getEmail())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
        }

        Set<RoleEntity> userRoles = new HashSet<>();

        createEmployeeUserRequest.getRole().forEach(role -> {
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

        AppUser newAppUser = new AppUser(createEmployeeUserRequest.getEmail(),passwordEncoder.encode(createEmployeeUserRequest.getPassword()),userRoles);

        try {
            appUserService.record(newAppUser);
        } catch (AppUserAlreadyExistsException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }*/

    @PutMapping(value = "/modify")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public AppUserDto modify(@Valid @RequestBody AppUserDto updateRequestAppUserDto) {
        AppUser updateRequestAppUser = appUserMapper.appUserDtoToAppUser(updateRequestAppUserDto);

        try {
            AppUser updatedAppUser = appUserService.modify(updateRequestAppUser);
            return appUserMapper.appUserToAppUserDto(updatedAppUser);
        } catch (AppUserNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @DeleteMapping(value = "/delete")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void delete(@RequestParam long id) {
        try {
            appUserService.delete(appUserService.readById(id));
        } catch (AppUserNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
