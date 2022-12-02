package com.example.selectitdelivery.dao.model;

import com.example.selectitdelivery.dao.entity.AppUserEntity;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Client {
    private long clientId;
    private AppUserEntity user;
    private String firstName;
    private String lastName;
    private String phone;

    public Client(AppUserEntity user, String firstName, String lastName, String phone) {
        this.user = user;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
    }
}
