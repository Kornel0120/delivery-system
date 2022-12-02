package com.example.selectitdelivery.controller.dto;

import com.example.selectitdelivery.dao.entity.AppUserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeDto {
    private long empId;
    private AppUserEntity user;
    private String firstName;
    private String lastName;
    private String phone;
}
