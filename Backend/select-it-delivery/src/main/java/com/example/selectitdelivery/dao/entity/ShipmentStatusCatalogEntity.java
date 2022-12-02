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
@Entity(name = "shipment_status_catalog")
public class ShipmentStatusCatalogEntity {
    @Id
    @Column(name = "status_catalog_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long statusCatalogId;
    @Column(name = "status_name")
    private String statusName;
}
