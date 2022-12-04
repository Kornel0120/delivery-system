package com.example.selectitdelivery.controller.dto;

import com.example.selectitdelivery.dao.model.ShipmentStatusCatalog;
import org.mapstruct.Mapper;

@Mapper(componentModel = "Spring")
public interface ShipmentStatusCatalogMapper {
    ShipmentStatusCatalogDto ShipmentStatusCatalogToShipmentStatusCatalogDto(ShipmentStatusCatalog shipmentStatusCatalog);

    ShipmentStatusCatalog ShipmentStatusCatalogDtoToShipmentStatusCatalog(ShipmentStatusCatalogDto shipmentStatusCatalogDto);
}
