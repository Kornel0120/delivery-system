package com.example.selectitdelivery.Payload.Response;

import com.example.selectitdelivery.dao.entity.AppUserEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserModifyResponse {
    private String message;
    private long id;
    private AppUserEntity user;
    private String firstName;
    private String lastName;
    private String phone;
}
