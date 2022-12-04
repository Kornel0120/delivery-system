package com.example.selectitdelivery.controller;

import com.example.selectitdelivery.Payload.Request.LoginRequest;
import com.example.selectitdelivery.Payload.Response.MessageResponse;
import com.example.selectitdelivery.Payload.Response.ProfileResponse;
import com.example.selectitdelivery.Payload.Response.UserWithTokensResponse;
import com.example.selectitdelivery.controller.dto.AppUserMapper;
import com.example.selectitdelivery.controller.dto.ClientDto;
import com.example.selectitdelivery.controller.dto.ClientMapper;
import com.example.selectitdelivery.controller.dto.EmployeeMapper;
import com.example.selectitdelivery.dao.entity.RoleEntity;
import com.example.selectitdelivery.dao.model.AppUser;
import com.example.selectitdelivery.dao.model.Client;
import com.example.selectitdelivery.dao.model.Employee;
import com.example.selectitdelivery.service.exceptions.AppUserNotFoundException;
import com.example.selectitdelivery.service.exceptions.ClientNotFoundException;
import com.example.selectitdelivery.service.exceptions.EmployeeNotFoundException;
import com.example.selectitdelivery.service.implementations.AppUserService;
import com.example.selectitdelivery.service.implementations.ClientService;
import com.example.selectitdelivery.service.implementations.EmployeeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;


@RequestMapping("/api/appUser")
@RequiredArgsConstructor
@RestController
@ResponseBody
public class AppUserController {

    private final AppUserService appUserService;
    private final EmployeeService employeeService;
    private final ClientService clientService;

    private final PasswordEncoder passwordEncoder;

    @GetMapping(value = "/profile/:{email:.+}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_EMPLOYEE') or hasRole('ROLE_USER')")
    public ResponseEntity<?> getProfile(@PathVariable String email) {
        try {
            AppUser requestedAppUser = appUserService.readByEmail(email);

            if(!requestedAppUser.getRole().contains("ROLE_USER")) {
                Employee requestedEmployee = employeeService.readByUserId(requestedAppUser.getUserId());
                return ResponseEntity.ok(new ProfileResponse(
                        requestedAppUser.getEmail(),
                        requestedAppUser.getPassword(),
                        requestedEmployee.getFirstName(),
                        requestedEmployee.getLastName(),
                        requestedEmployee.getPhone()));
            } else if(!requestedAppUser.getRole().isEmpty()) {
                Client requestedClient = clientService.readByUserId(requestedAppUser.getUserId());
                return ResponseEntity.ok(new ProfileResponse(
                        requestedAppUser.getEmail(),
                        requestedAppUser.getPassword(),
                        requestedClient.getFirstName(),
                        requestedClient.getLastName(),
                        requestedClient.getPhone()));
            }

            return ResponseEntity.badRequest().body(new MessageResponse("No role has been specified to perform this process!"));

        } catch (ClientNotFoundException | EmployeeNotFoundException | AppUserNotFoundException e ) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @GetMapping(value = "/validateUser/")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_EMPLOYEE') or hasRole('ROLE_USER')")
    public ResponseEntity<?> validateUser(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            AppUser appUser = appUserService.readByEmail(loginRequest.getEmail());
            String encodedPassword = passwordEncoder.encode(loginRequest.getPassword());
            if(encodedPassword.equals(appUser.getPassword())) {
                return ResponseEntity.ok(new MessageResponse("Successful validation!"));
            }
            return ResponseEntity.badRequest().body(new MessageResponse("Validation failed!"));
        } catch (AppUserNotFoundException e ) {
        throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
