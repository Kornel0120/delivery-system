package com.example.selectitdelivery.Payload.Request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class SignupRequest {
    @NotBlank
    private String email;
    @NotBlank
    @Size(min = 4, max = 40)
    private String password;
    private Set<String> role = new HashSet<>();
}
