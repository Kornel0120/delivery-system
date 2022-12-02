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
@Entity(name = "shipment_size")
public class ShipmentSizeEntity {
    @Id
    @Column(name = "shipment_size_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long shipmentSizeId;
    @Column(name = "shipment_size_name")
    private char shipmentSizeName;
}
