package com.example.selectitdelivery.controller.dto;

import com.example.selectitdelivery.dao.entity.ShipmentEntity;
import com.example.selectitdelivery.dao.entity.ShipmentStatusCatalogEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ShipmentStatusDto {
    private long shipmentStatusId;
    private ShipmentEntity shipment;
    private ShipmentStatusCatalogEntity status;
    private Date pickUpUntil;
}
