package com.example.selectitdelivery.controller;

import com.example.selectitdelivery.Payload.Request.EmployeeUserRequest;
import com.example.selectitdelivery.Payload.Response.UserModifyResponse;
import com.example.selectitdelivery.Payload.Response.MessageResponse;
import com.example.selectitdelivery.controller.dto.EmployeeDto;
import com.example.selectitdelivery.controller.dto.EmployeeMapper;
import com.example.selectitdelivery.dao.entity.AppUserEntity;
import com.example.selectitdelivery.dao.entity.RoleEntity;
import com.example.selectitdelivery.dao.model.Employee;
import com.example.selectitdelivery.dao.repositories.RoleRepository;
import com.example.selectitdelivery.service.exceptions.EmployeeAlreadyExistsException;
import com.example.selectitdelivery.service.exceptions.EmployeeNotFoundException;
import com.example.selectitdelivery.service.implementations.AppUserService;
import com.example.selectitdelivery.service.implementations.EmployeeService;
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

@RequestMapping("/api/employee")
@RequiredArgsConstructor
@RestController
@ResponseBody
public class EmployeeController {
    private final EmployeeService employeeService;
    private final EmployeeMapper employeeMapper;
    private final AppUserService appUserService;
    @Autowired
    private final RoleRepository roleRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

    @GetMapping(value = "/all")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Collection<EmployeeDto> getEmployees() {
        return employeeService.readAll()
                .stream()
                .map(employeeMapper::employeeToEmployeeDto)
                .collect(Collectors.toList());
    }

    @GetMapping(value = "/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public EmployeeDto readById(@PathVariable long id) {
        try {
            return employeeMapper.employeeToEmployeeDto(employeeService.readById(id));
        } catch (EmployeeNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> createEmployee(@Valid @RequestBody EmployeeUserRequest createEmployeeUserRequest) {
        if(appUserService.existsByEmail(createEmployeeUserRequest.getEmail())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already taken!"));
        }

        Set<RoleEntity> userRoles = new HashSet<>();

        createEmployeeUserRequest.getRole().forEach(role -> {
            switch (role) {
                case "ROLE_ADMIN" -> {
                    RoleEntity adminRole = roleRepository.findByRoleName("ROLE_ADMIN")
                            .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                    userRoles.add(adminRole);
                }
                default -> {
                    RoleEntity userRole = roleRepository.findByRoleName("ROLE_EMPLOYEE")
                            .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                    userRoles.add(userRole);
                }
            }
        });

        AppUserEntity newAppUser = new AppUserEntity(createEmployeeUserRequest.getEmail(),passwordEncoder.encode(createEmployeeUserRequest.getPassword()),userRoles);
        Employee newEmployee = new Employee(newAppUser,createEmployeeUserRequest.getFirstName(),createEmployeeUserRequest.getLastName(),createEmployeeUserRequest.getPhone());

        try {
            employeeService.record(newEmployee);
        } catch (EmployeeAlreadyExistsException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }

        return ResponseEntity.ok(new MessageResponse("Employee registered successfully!"));
    }

    @PutMapping(value = "/modify")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> modify(@Valid @RequestBody EmployeeDto updateRequestEmployeeDto) {
        Employee updateRequestEmployee = employeeMapper.employeeDtoToEmployee(updateRequestEmployeeDto);
        updateRequestEmployee.getUser().setPassword(passwordEncoder.encode(updateRequestEmployee.getUser().getPassword()));

        try {
            Employee updatedEmployee = employeeService.modify(updateRequestEmployee);
            return ResponseEntity.ok(new UserModifyResponse("Employee's account updated successfully.",
                    updatedEmployee.getEmpId(),
                    updatedEmployee.getUser(),
                    updatedEmployee.getFirstName(),
                    updatedEmployee.getLastName(),
                    updatedEmployee.getPhone()));
        } catch (EmployeeNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }

    }

    @DeleteMapping(value = "/delete")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> delete(@RequestParam long id) {
        try {
            employeeService.delete(employeeService.readById(id));
        } catch (EmployeeNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }

        return ResponseEntity.ok(new MessageResponse("Employee deleted successfully!"));
    }
}
