package com.example.selectitdelivery.Payload.Request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
public class EmployeeUserRequest {
    @NotBlank
    private String email;
    @NotBlank
    @Size(min = 4, max = 40)
    private String password;
    private Set<String> role = new HashSet<>();
    private String firstName;
    private String lastName;
    private String phone;
}
