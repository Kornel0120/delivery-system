package com.example.selectitdelivery.service.interfaces;

import com.example.selectitdelivery.dao.model.ShipmentStatus;
import com.example.selectitdelivery.dao.model.ShipmentStatusCatalog;
import com.example.selectitdelivery.service.exceptions.ShipmentStatusAlreadyExistsException;
import com.example.selectitdelivery.service.exceptions.ShipmentStatusCatalogNotFoundException;
import com.example.selectitdelivery.service.exceptions.ShipmentStatusNotFoundException;

import java.util.Collection;

public interface IShipmentStatusService {
    ShipmentStatus record(ShipmentStatus shipmentStatus) throws ShipmentStatusAlreadyExistsException;
    ShipmentStatus readById(long id) throws ShipmentStatusNotFoundException;
    Collection<ShipmentStatus> readAll();
    ShipmentStatus modify(ShipmentStatus shipmentStatus) throws ShipmentStatusNotFoundException;
    void delete(ShipmentStatus shipmentStatus) throws ShipmentStatusNotFoundException;

    ShipmentStatusCatalog readStatusCatalogByStatusName(String statusName) throws ShipmentStatusCatalogNotFoundException;
}
