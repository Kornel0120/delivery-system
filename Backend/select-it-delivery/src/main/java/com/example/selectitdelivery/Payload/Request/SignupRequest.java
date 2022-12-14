package com.example.selectitdelivery.Payload.Request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
public class SignupRequest {
    @NotBlank
    private String email;
    @NotBlank
    @Size(min = 4, max = 40)
    private String password;
    private String firstName;
    private String lastName;
    private String phone;
}
