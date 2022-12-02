package com.example.selectitdelivery.controller.dto;

import com.example.selectitdelivery.dao.model.ShipmentStatus;
import org.mapstruct.Mapper;

@Mapper(componentModel = "Spring")
public interface ShipmentStatusMapper {
    ShipmentStatusDto shipmentStatusToShipmentStatusDto(ShipmentStatus shipmentStatus);

    ShipmentStatus shipmentStatusDtoToShipmentStatus(ShipmentStatusDto shipmentStatusDto);
}
