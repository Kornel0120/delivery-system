package com.example.selectitdelivery.controller;

import com.example.selectitdelivery.Payload.Request.ShipmentStatusModifyRequest;
import com.example.selectitdelivery.Payload.Response.ShipmentStatusModifyResponse;
import com.example.selectitdelivery.controller.dto.ShipmentStatusDto;
import com.example.selectitdelivery.controller.dto.ShipmentStatusMapper;
import com.example.selectitdelivery.dao.entity.*;
import com.example.selectitdelivery.dao.model.ShipmentStatus;
import com.example.selectitdelivery.dao.model.ShipmentStatusCatalog;
import com.example.selectitdelivery.service.exceptions.ShipmentStatusAlreadyExistsException;
import com.example.selectitdelivery.service.exceptions.ShipmentStatusCatalogNotFoundException;
import com.example.selectitdelivery.service.exceptions.ShipmentStatusNotFoundException;
import com.example.selectitdelivery.service.implementations.ShipmentStatusService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ShipmentStatusControllerTest {
    @Mock
    ShipmentStatusService shipmentStatusService;

    @Mock
    ShipmentStatusMapper shipmentStatusMapper;

    @InjectMocks
    ShipmentStatusController shipmentStatusController;

    @Test
    void readAllHappyPath() {
        when(shipmentStatusService.readAll()).thenReturn(List.of(TestDataProvider.getShipmentStatus()));
        when(shipmentStatusMapper.shipmentStatusToShipmentStatusDto(any())).thenReturn(TestDataProvider.getShipmentStatusDto());
        Collection<ShipmentStatusDto> expected = List.of(TestDataProvider.getShipmentStatusDto());
        Collection<ShipmentStatusDto> actual = shipmentStatusController.getUsers();
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    void readByIdHappyPath() throws ShipmentStatusNotFoundException {
        when(shipmentStatusService.readById(TestDataProvider.getShipmentStatus().getShipmentStatusId()))
                .thenReturn(TestDataProvider.getShipmentStatus());
        ShipmentStatusDto expected = TestDataProvider.getShipmentStatusDto();
        when(shipmentStatusMapper.shipmentStatusToShipmentStatusDto(any()))
                .thenReturn(TestDataProvider.getShipmentStatusDto());

        ShipmentStatusDto actual = shipmentStatusController.readById(TestDataProvider.getShipmentStatus().getShipmentStatusId());
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    void createShipmentStatusHappyPath() throws ShipmentStatusAlreadyExistsException {
        ShipmentStatus shipmentStatus = TestDataProvider.getShipmentStatus();
        ShipmentStatusDto shipmentStatusDto = TestDataProvider.getShipmentStatusDto();

        when(shipmentStatusMapper.shipmentStatusDtoToShipmentStatus(shipmentStatusDto)).thenReturn(shipmentStatus);
        when(shipmentStatusService.record(shipmentStatus)).thenReturn(shipmentStatus);
        when(shipmentStatusMapper.shipmentStatusToShipmentStatusDto(shipmentStatus)).thenReturn(shipmentStatusDto);

        ShipmentStatusDto actual = shipmentStatusController.create(shipmentStatusDto);
        assertThat(actual).usingRecursiveComparison().isEqualTo(shipmentStatusDto);
    }

    @Test
    void updateShipmentStatusHappyPath() throws ShipmentStatusNotFoundException, ShipmentStatusCatalogNotFoundException {
        ShipmentStatusDto shipmentStatusDto = TestDataProvider.getShipmentStatusDto();
        ShipmentStatus shipmentStatus = TestDataProvider.getShipmentStatus();

        when(shipmentStatusService.modify(shipmentStatus)).thenReturn(shipmentStatus);
        when(shipmentStatusService.readStatusCatalogByStatusName(TestDataProvider.modifyRequest.getShipmentStatusCatalogName())).thenReturn(TestDataProvider.shipmentStatusCatalog);
        when(shipmentStatusService.readById(TestDataProvider.modifyRequest.getShipmentId())).thenReturn(TestDataProvider.getShipmentStatus());

        ResponseEntity<?> expected = TestDataProvider.expectedModifyResponse;
        ResponseEntity<?> actual = shipmentStatusController.modify(TestDataProvider.modifyRequest);
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    void deleteHappyPath() throws ShipmentStatusNotFoundException {
        ShipmentStatus shipmentStatus = TestDataProvider.getShipmentStatus();
        when(shipmentStatusService.readById(TestDataProvider.shipmentStatusId)).thenReturn(shipmentStatus);
        doNothing().when(shipmentStatusService).delete(shipmentStatus);

        shipmentStatusController.delete(TestDataProvider.shipmentStatusId);
    }

    @Test
    void readByIdThrowsResponseStatusException() throws ShipmentStatusNotFoundException {
        when(shipmentStatusService.readById(TestDataProvider.getShipmentStatus().getShipmentStatusId()))
                .thenThrow(new ShipmentStatusNotFoundException());

        assertThatThrownBy(() -> {shipmentStatusController.readById(TestDataProvider.getShipmentStatus().getShipmentStatusId());})
                .isInstanceOf(ResponseStatusException.class);
    }

    @Test
    void updateThrowsResponseStatusException() throws ShipmentStatusNotFoundException, ShipmentStatusCatalogNotFoundException {
        ShipmentStatusDto shipmentStatusDto = TestDataProvider.getShipmentStatusDto();
        ShipmentStatus shipmentStatus = TestDataProvider.getShipmentStatus();

        when(shipmentStatusService.modify(shipmentStatus)).thenReturn(shipmentStatus);
        when(shipmentStatusService.readStatusCatalogByStatusName(TestDataProvider.modifyRequest.getShipmentStatusCatalogName())).thenReturn(TestDataProvider.shipmentStatusCatalog);
        when(shipmentStatusService.readById(TestDataProvider.modifyRequest.getShipmentId())).thenReturn(TestDataProvider.getShipmentStatus());
        when(shipmentStatusService.modify(shipmentStatus)).thenThrow(new ShipmentStatusNotFoundException());

        assertThatThrownBy(() -> {shipmentStatusController.modify(TestDataProvider.modifyRequest);})
                .isInstanceOf(ResponseStatusException.class);
    }

    @Test
    void createThrowsResponseStatusException() throws ShipmentStatusAlreadyExistsException {
        ShipmentStatusDto shipmentStatusDto = TestDataProvider.getShipmentStatusDto();
        ShipmentStatus shipmentStatus = TestDataProvider.getShipmentStatus();

        when(shipmentStatusMapper.shipmentStatusDtoToShipmentStatus(shipmentStatusDto)).thenReturn(shipmentStatus);
        when(shipmentStatusService.record(shipmentStatus)).thenThrow(new ShipmentStatusAlreadyExistsException());

        assertThatThrownBy(() -> {shipmentStatusController.create(shipmentStatusDto);})
                .isInstanceOf(ResponseStatusException.class);
    }

    @Test
    void deleteThrowsResponseStatusException() throws ShipmentStatusNotFoundException {
        final long notFoundShipmentStatusId = TestDataProvider.shipmentStatusId;

        doThrow(new ShipmentStatusNotFoundException()).when(shipmentStatusService).readById(notFoundShipmentStatusId);

        assertThatThrownBy(() -> {shipmentStatusController.delete(notFoundShipmentStatusId);})
                .isInstanceOf(ResponseStatusException.class);
    }

    private static class TestDataProvider {
        public static final long shipmentStatusId = 9999;
        public static final ShipmentEntity shipment = ShipmentEntity.builder()
                .shipmentId(9999L)
                .employee(new EmployeeEntity(9999,
                        new AppUserEntity("TestShipment@employee.com","12345678", Set.of(new RoleEntity(2,"ROLE_EMPLOYEE"))),
                        "Test", "ShipmentStatus", "00000000000"))
                .client(new ClientEntity(9999,
                        new AppUserEntity("TestShipment@user.com","12345678", Set.of(new RoleEntity(3,"ROLE_USER"))),
                        "Test", "ShipmentStatus", "10000000000"))
                .paymentType(new PaymentTypeEntity(9999,"TestPayment"))
                .shipmentSize(new ShipmentSizeEntity(9999,'C'))
                .packagePoint(new PackagePointEntity(9999,"0000","TestCity","TestAddress"))
                .productsPrice(new BigDecimal("10000.00"))
                .deliveryCost(new BigDecimal("1000.00"))
                .finalPrice(new BigDecimal("11000.00"))
                .build();

        public static final ShipmentStatusCatalogEntity status = ShipmentStatusCatalogEntity.builder()
                .statusCatalogId(9999)
                .statusName("TestStatus")
                .build();
        public static final Date pickUpUntil = new Date(System.currentTimeMillis() + 600000 * 200);

        public static ShipmentStatus getShipmentStatus() {
            return new ShipmentStatus(
                    shipmentStatusId,
                    shipment,
                    status,
                    pickUpUntil);
        }

        public static ShipmentStatusDto getShipmentStatusDto() {
            return ShipmentStatusDto.builder()
                    .shipmentStatusId(shipmentStatusId)
                    .shipment(shipment)
                    .status(status)
                    .pickUpUntil(pickUpUntil)
                    .build();
        }

        public static ShipmentStatusModifyRequest modifyRequest = new ShipmentStatusModifyRequest(1,"Under delivery", TestDataProvider.pickUpUntil);

        public static ShipmentStatusCatalog shipmentStatusCatalog = new ShipmentStatusCatalog(status.getStatusCatalogId(), status.getStatusName());

        public static ResponseEntity<?> expectedModifyResponse = ResponseEntity.ok(new ShipmentStatusModifyResponse("Shipment status updated successfully!",
                getShipmentStatus().getShipment().getEmployee().getFirstName(),
                getShipmentStatus().getShipment().getEmployee().getLastName(),
                getShipmentStatus().getShipment().getEmployee().getPhone(),
                getShipmentStatus().getShipment().getClient().getFirstName(),
                getShipmentStatus().getShipment().getClient().getLastName(),
                getShipmentStatus().getShipment().getClient().getPhone(),
                getShipmentStatus().getShipment().getPaymentType().getPaymentTypeName(),
                getShipmentStatus().getShipment().getShipmentSize().getShipmentSizeName(),
                getShipmentStatus().getShipment().getPackagePoint().getPostalCode(),
                getShipmentStatus().getShipment().getPackagePoint().getCity(),
                getShipmentStatus().getShipment().getPackagePoint().getAddress(),
                getShipmentStatus().getShipment().getProductsPrice(),
                getShipmentStatus().getShipment().getDeliveryCost(),
                getShipmentStatus().getShipment().getProductsPrice().add(getShipmentStatus().getShipment().getDeliveryCost()),
                getShipmentStatus().getStatus().getStatusName(),
                getShipmentStatus().getPickUpUntil()));
    }
}
