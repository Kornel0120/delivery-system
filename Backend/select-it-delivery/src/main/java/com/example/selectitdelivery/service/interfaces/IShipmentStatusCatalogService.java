package com.example.selectitdelivery.service.interfaces;

import com.example.selectitdelivery.dao.model.ShipmentStatusCatalog;

import java.util.Collection;

public interface IShipmentStatusCatalogService {
    Collection<ShipmentStatusCatalog> readAll();
}
