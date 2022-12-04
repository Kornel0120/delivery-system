package com.example.selectitdelivery.service.implementations;

import com.example.selectitdelivery.dao.entity.ShipmentStatusCatalogEntity;
import com.example.selectitdelivery.dao.model.ShipmentStatusCatalog;
import com.example.selectitdelivery.dao.repositories.ShipmentStatusCatalogRepository;
import com.example.selectitdelivery.service.interfaces.IShipmentStatusCatalogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ShipmentStatusCatalogService implements IShipmentStatusCatalogService {
    private final ShipmentStatusCatalogRepository shipmentStatusCatalogRepository;

    private static ShipmentStatusCatalog convertEntityToModel(ShipmentStatusCatalogEntity shipmentStatusCatalogEntity) {
        return new ShipmentStatusCatalog(
                shipmentStatusCatalogEntity.getStatusCatalogId(),
                shipmentStatusCatalogEntity.getStatusName()
        );
    }

    private static ShipmentStatusCatalogEntity convertModelToEntity(ShipmentStatusCatalog shipmentStatusCatalog) {
        return ShipmentStatusCatalogEntity.builder()
                .statusCatalogId(shipmentStatusCatalog.getStatusCatalogId())
                .statusName(shipmentStatusCatalog.getStatusName())
                .build();
    }

    @Override
    public Collection<ShipmentStatusCatalog> readAll() {
        return shipmentStatusCatalogRepository.findAll()
                .stream()
                .map(ShipmentStatusCatalogService::convertEntityToModel)
                .collect(Collectors.toList());
    }
}
