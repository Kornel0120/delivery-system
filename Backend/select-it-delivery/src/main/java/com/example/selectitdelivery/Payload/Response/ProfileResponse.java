package com.example.selectitdelivery.Payload.Response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ProfileResponse {
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String phone;
}
