package com.example.selectitdelivery.dao.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "package_point")
public class PackagePointEntity {
    @Id
    @Column(name = "package_point_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long packagePointId;
    @Column(name = "postal_code")
    private String postalCode;
    @Column(name = "city")
    private String city;
    @Column(name = "address")
    private String address;
}
