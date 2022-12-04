package com.example.selectitdelivery.controller;

import com.example.selectitdelivery.controller.dto.ShipmentStatusCatalogDto;
import com.example.selectitdelivery.controller.dto.ShipmentStatusCatalogMapper;
import com.example.selectitdelivery.service.implementations.ShipmentStatusCatalogService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.stream.Collectors;

@RequestMapping("/api/shipmentStatusCatalog")
@RequiredArgsConstructor
@RestController
@ResponseBody
public class ShipmentStatusCatalogController {
    private final ShipmentStatusCatalogService shipmentStatusCatalogService;
    private final ShipmentStatusCatalogMapper shipmentStatusCatalogMapper;

    @GetMapping(value = "/all")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_EMPLOYEE')")
    public Collection<ShipmentStatusCatalogDto> getShipments() {
        return shipmentStatusCatalogService.readAll()
                .stream()
                .map(shipmentStatusCatalogMapper::ShipmentStatusCatalogToShipmentStatusCatalogDto)
                .collect(Collectors.toList());
    }
}
