package com.example.selectitdelivery.controller;

import com.example.selectitdelivery.Payload.Request.ShipmentStatusModifyRequest;
import com.example.selectitdelivery.Payload.Response.ShipmentStatusModifyResponse;
import com.example.selectitdelivery.controller.dto.ShipmentStatusDto;
import com.example.selectitdelivery.controller.dto.ShipmentStatusMapper;
import com.example.selectitdelivery.dao.entity.ShipmentStatusCatalogEntity;
import com.example.selectitdelivery.dao.model.ShipmentStatus;
import com.example.selectitdelivery.dao.model.ShipmentStatusCatalog;
import com.example.selectitdelivery.service.exceptions.ShipmentStatusAlreadyExistsException;
import com.example.selectitdelivery.service.exceptions.ShipmentStatusCatalogNotFoundException;
import com.example.selectitdelivery.service.exceptions.ShipmentStatusNotFoundException;
import com.example.selectitdelivery.service.implementations.ShipmentStatusService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collection;
import java.util.stream.Collectors;

@RequestMapping("/api/shipmentStatus")
@RequiredArgsConstructor
@RestController
@ResponseBody
public class ShipmentStatusController {
    private final ShipmentStatusService shipmentStatusService;
    private final ShipmentStatusMapper shipmentStatusMapper;

    @GetMapping(value = "/all")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_EMPLOYEE')")
    public Collection<ShipmentStatusDto> getUsers() {
        return shipmentStatusService.readAll()
                .stream()
                .map(shipmentStatusMapper::shipmentStatusToShipmentStatusDto)
                .collect(Collectors.toList());
    }

    @GetMapping(value = "/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_EMPLOYEE')")
    public ShipmentStatusDto readById(@PathVariable long id) {
        try {
            return shipmentStatusMapper.shipmentStatusToShipmentStatusDto(shipmentStatusService.readById(id));
        } catch (ShipmentStatusNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @PostMapping(value = "/create")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_EMPLOYEE')")
    public ShipmentStatusDto create(@Valid @RequestBody ShipmentStatusDto recordRequestShipmentStatusDto) {
        ShipmentStatus shipmentStatus = shipmentStatusMapper.shipmentStatusDtoToShipmentStatus(recordRequestShipmentStatusDto);
        try {
            ShipmentStatus recordedShipmentStatus = shipmentStatusService.record(shipmentStatus);
            return shipmentStatusMapper.shipmentStatusToShipmentStatusDto(recordedShipmentStatus);
        } catch (ShipmentStatusAlreadyExistsException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @PutMapping(value = "/modify")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_EMPLOYEE')")
    public ResponseEntity<?> modify(@Valid @RequestBody ShipmentStatusModifyRequest updateRequestShipmentStatusModifyRequest) {

        try {
            ShipmentStatus request = shipmentStatusService.readById(updateRequestShipmentStatusModifyRequest.getShipmentId());
            ShipmentStatusCatalog requestedShipmentStatusCatalog = shipmentStatusService.readStatusCatalogByStatusName(updateRequestShipmentStatusModifyRequest.getShipmentStatusCatalogName());

            request.setStatus(ShipmentStatusCatalogEntity.builder()
                    .statusCatalogId(requestedShipmentStatusCatalog.getStatusCatalogId())
                    .statusName(requestedShipmentStatusCatalog.getStatusName())
                    .build());

            request.setPickUpUntil(updateRequestShipmentStatusModifyRequest.getPickUpUntil());
            ShipmentStatus updatedShipmentStatus = shipmentStatusService.modify(request);

            return ResponseEntity.ok(new ShipmentStatusModifyResponse("Shipment status updated successfully!",
                    updatedShipmentStatus.getShipment().getEmployee().getFirstName(),
                    updatedShipmentStatus.getShipment().getEmployee().getLastName(),
                    updatedShipmentStatus.getShipment().getEmployee().getPhone(),
                    updatedShipmentStatus.getShipment().getClient().getFirstName(),
                    updatedShipmentStatus.getShipment().getClient().getLastName(),
                    updatedShipmentStatus.getShipment().getClient().getPhone(),
                    updatedShipmentStatus.getShipment().getPaymentType().getPaymentTypeName(),
                    updatedShipmentStatus.getShipment().getShipmentSize().getShipmentSizeName(),
                    updatedShipmentStatus.getShipment().getPackagePoint().getPostalCode(),
                    updatedShipmentStatus.getShipment().getPackagePoint().getCity(),
                    updatedShipmentStatus.getShipment().getPackagePoint().getAddress(),
                    updatedShipmentStatus.getShipment().getProductsPrice(),
                    updatedShipmentStatus.getShipment().getDeliveryCost(),
                    updatedShipmentStatus.getShipment().getProductsPrice().add(updatedShipmentStatus.getShipment().getDeliveryCost()),
                    updatedShipmentStatus.getStatus().getStatusName(),
                    updatedShipmentStatus.getPickUpUntil()));

        } catch (ShipmentStatusNotFoundException | ShipmentStatusCatalogNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @DeleteMapping(value = "/delete")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_EMPLOYEE')")
    public void delete(@RequestParam long id) {
        try {
            shipmentStatusService.delete(shipmentStatusService.readById(id));
        } catch (ShipmentStatusNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
