package com.example.selectitdelivery.service.implementations;

import com.example.selectitdelivery.dao.entity.ShipmentStatusCatalogEntity;
import com.example.selectitdelivery.dao.entity.ShipmentStatusEntity;
import com.example.selectitdelivery.dao.model.ShipmentStatus;
import com.example.selectitdelivery.dao.model.ShipmentStatusCatalog;
import com.example.selectitdelivery.dao.repositories.ShipmentStatusCatalogRepository;
import com.example.selectitdelivery.dao.repositories.ShipmentStatusRepository;
import com.example.selectitdelivery.service.exceptions.ShipmentStatusAlreadyExistsException;
import com.example.selectitdelivery.service.exceptions.ShipmentStatusCatalogNotFoundException;
import com.example.selectitdelivery.service.exceptions.ShipmentStatusNotFoundException;
import com.example.selectitdelivery.service.interfaces.IShipmentStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ShipmentStatusService implements IShipmentStatusService {
    private final ShipmentStatusRepository shipmentStatusRepository;

    private final ShipmentStatusCatalogRepository shipmentStatusCatalogRepository;

    private static ShipmentStatus convertEntityToModel(ShipmentStatusEntity shipmentStatusEntity) {
        return new ShipmentStatus(
                shipmentStatusEntity.getShipmentStatusId(),
                shipmentStatusEntity.getShipment(),
                shipmentStatusEntity.getStatus(),
                shipmentStatusEntity.getPickUpUntil()
        );
    }

    private static ShipmentStatusEntity convertModelToEntity(ShipmentStatus shipmentStatus) {
        return ShipmentStatusEntity.builder()
                .shipmentStatusId(shipmentStatus.getShipmentStatusId())
                .shipment(shipmentStatus.getShipment())
                .status(shipmentStatus.getStatus())
                .pickUpUntil(shipmentStatus.getPickUpUntil())
                .build();
    }

    @Override
    public ShipmentStatus record(ShipmentStatus shipmentStatus) throws ShipmentStatusAlreadyExistsException {
        if(shipmentStatusRepository.findById(shipmentStatus.getShipment().getShipmentId()).isPresent()) {
            throw new ShipmentStatusAlreadyExistsException();
        }

        ShipmentStatusEntity recordShipmentStatusEntity = shipmentStatusRepository.save(convertModelToEntity(shipmentStatus));

        return convertEntityToModel(recordShipmentStatusEntity);
    }

    @Override
    public ShipmentStatus readById(long id) throws ShipmentStatusNotFoundException {
        if(shipmentStatusRepository.findById(id).isEmpty()) {
            throw new ShipmentStatusNotFoundException();
        }

        ShipmentStatusEntity request = shipmentStatusRepository.findById(id).get();
        return convertEntityToModel(request);
    }

    @Override
    public Collection<ShipmentStatus> readAll() {
        return shipmentStatusRepository.findAll()
                .stream()
                .map(ShipmentStatusService::convertEntityToModel)
                .collect(Collectors.toList());
    }

    @Override
    public ShipmentStatus modify(ShipmentStatus shipmentStatus) throws ShipmentStatusNotFoundException {
        if(shipmentStatusRepository.findById(shipmentStatus.getShipmentStatusId()).isEmpty()) {
            throw new ShipmentStatusNotFoundException();
        } else {
            ShipmentStatusEntity shipmentStatusEntity = convertModelToEntity(shipmentStatus);
            return convertEntityToModel(shipmentStatusRepository.save(shipmentStatusEntity));
        }
    }

    @Override
    public void delete(ShipmentStatus shipmentStatus) throws ShipmentStatusNotFoundException {
        if(shipmentStatusRepository.findById(shipmentStatus.getShipment().getShipmentId()).isEmpty()) {
            throw new ShipmentStatusNotFoundException();
        }

        shipmentStatusRepository.delete(convertModelToEntity(shipmentStatus));
    }

    @Override
    public ShipmentStatusCatalog readStatusCatalogByStatusName(String statusName) throws ShipmentStatusCatalogNotFoundException {
        if(shipmentStatusCatalogRepository.findByStatusName(statusName).isEmpty()) {
            throw new ShipmentStatusCatalogNotFoundException();
        }

        ShipmentStatusCatalogEntity requestedShipmentStatusCatalog = shipmentStatusCatalogRepository.findByStatusName(statusName).get();
        return new ShipmentStatusCatalog(
                requestedShipmentStatusCatalog.getStatusCatalogId(),
                requestedShipmentStatusCatalog.getStatusName()
        );
    }
}
