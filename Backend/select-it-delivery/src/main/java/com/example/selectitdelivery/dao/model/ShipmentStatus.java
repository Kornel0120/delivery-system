package com.example.selectitdelivery.dao.model;

import com.example.selectitdelivery.dao.entity.ShipmentEntity;
import com.example.selectitdelivery.dao.entity.ShipmentStatusCatalogEntity;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ShipmentStatus {
    private long shipmentStatusId;
    private ShipmentEntity shipment;
    private ShipmentStatusCatalogEntity status;
    private Date pickUpUntil;
}
