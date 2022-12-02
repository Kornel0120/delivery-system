package com.example.selectitdelivery.dao.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PackagePoint {
    private long packagePointId;
    private String postalCode;
    private String city;
    private String address;
}
