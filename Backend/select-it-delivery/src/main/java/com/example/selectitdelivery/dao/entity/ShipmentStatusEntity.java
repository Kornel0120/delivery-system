package com.example.selectitdelivery.dao.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "shipment_status")
public class ShipmentStatusEntity {
    @Id
    @Column(name = "shipment_status_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long shipmentStatusId;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "shipment_id", referencedColumnName = "shipment_id")
    private ShipmentEntity shipment;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "status_catalog_id", referencedColumnName = "status_catalog_id")
    private ShipmentStatusCatalogEntity status;
    @Column(name = "pick_up_until")
    private Date pickUpUntil;
}
