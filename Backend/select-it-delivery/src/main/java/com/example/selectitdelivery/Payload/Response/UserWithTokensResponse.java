package com.example.selectitdelivery.Payload.Response;

import com.example.selectitdelivery.security.services.UserDetailsImpl;

import java.util.List;

public class UserWithTokensResponse {
    private long userId;
    private String email;
    private String password;
    String token;
    String refreshToken;
    String type = "Bearer";
    List<String> roles;

    public UserWithTokensResponse(UserDetailsImpl userDetails, String token, String refreshToken, List<String> roles) {
        this.userId = userDetails.getId();
        this.email = userDetails.getEmail();
        this.password = userDetails.getPassword();
        this.token = token;
        this.refreshToken = refreshToken;
        this.roles = roles;
    }
}
