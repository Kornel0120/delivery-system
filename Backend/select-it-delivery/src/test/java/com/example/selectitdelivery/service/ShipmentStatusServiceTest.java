package com.example.selectitdelivery.service;

import com.example.selectitdelivery.dao.entity.*;
import com.example.selectitdelivery.dao.model.ShipmentStatus;
import com.example.selectitdelivery.dao.repositories.ShipmentStatusRepository;
import com.example.selectitdelivery.service.exceptions.ShipmentStatusAlreadyExistsException;
import com.example.selectitdelivery.service.exceptions.ShipmentStatusNotFoundException;
import com.example.selectitdelivery.service.implementations.ShipmentStatusService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ShipmentStatusServiceTest {

    @Mock
    ShipmentStatusRepository shipmentStatusRepository;

    @InjectMocks
    ShipmentStatusService shipmentStatusService;

    @Test
    void recordShipmentStatusHappyPath() throws ShipmentStatusAlreadyExistsException {
        ShipmentStatus shipmentStatus = TestDataProvider.getShipmentStatus();
        ShipmentStatusEntity shipmentStatusEntity = TestDataProvider.getShipmentStatusEntity();
        when(shipmentStatusRepository.findById(any())).thenReturn(Optional.empty());
        when(shipmentStatusRepository.save(any())).thenReturn(shipmentStatusEntity);
        ShipmentStatus actual = shipmentStatusService.record(shipmentStatus);
        assertThat(actual).usingRecursiveComparison().isEqualTo(shipmentStatus);
    }

    @Test
    void readByIdHappyPath() throws ShipmentStatusNotFoundException {
        when(shipmentStatusRepository.findById(TestDataProvider.shipmentStatusId))
                .thenReturn(Optional.of(TestDataProvider.getShipmentStatusEntity()));
        ShipmentStatus expected = TestDataProvider.getShipmentStatus();
        ShipmentStatus actual = shipmentStatusService.readById(TestDataProvider.shipmentStatusId);
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    void modifyShipmentStatusHappyPath() throws ShipmentStatusNotFoundException {
        ShipmentStatus shipmentStatus = TestDataProvider.getShipmentStatus();
        ShipmentStatusEntity shipmentStatusEntity = TestDataProvider.getShipmentStatusEntity();
        when(shipmentStatusRepository.findById(shipmentStatus.getShipmentStatusId()))
                .thenReturn(Optional.of(shipmentStatusEntity));
        when(shipmentStatusRepository.save(any())).thenReturn(shipmentStatusEntity);
        ShipmentStatus actual = shipmentStatusService.modify(shipmentStatus);
        assertThat(actual).usingRecursiveComparison().isEqualTo(shipmentStatus);
    }

    @Test
    void readAllHappyPath() {
        List<ShipmentStatusEntity> shipmentStatusEntities = List.of(
                TestDataProvider.getShipmentStatusEntity()
        );

        Collection<ShipmentStatus> expectedShipmentStatuses = List.of(
                TestDataProvider.getShipmentStatus()
        );

        when(shipmentStatusRepository.findAll()).thenReturn(shipmentStatusEntities);
        Collection<ShipmentStatus> actualShipmentStatuses = shipmentStatusService.readAll();
        assertThat(actualShipmentStatuses).usingRecursiveComparison().isEqualTo(expectedShipmentStatuses);
    }

    @Test
    void deleteShipmentStatusHappyPath() throws ShipmentStatusNotFoundException {
        ShipmentStatus shipmentStatus = TestDataProvider.getShipmentStatus();
        ShipmentStatusEntity shipmentStatusEntity = TestDataProvider.getShipmentStatusEntity();
        when(shipmentStatusRepository.findById(shipmentStatus.getShipmentStatusId()))
                .thenReturn(Optional.of(shipmentStatusEntity));
        shipmentStatusService.delete(shipmentStatus);
    }

    @Test
    void recordThrowsShipmentStatusAlreadyExistsException() {
        ShipmentStatus shipmentStatus = TestDataProvider.getShipmentStatus();
        ShipmentStatusEntity shipmentStatusEntity = TestDataProvider.getShipmentStatusEntity();
        when(shipmentStatusRepository.findById(TestDataProvider.shipmentStatusId))
                .thenReturn(Optional.ofNullable(shipmentStatusEntity));
        assertThatThrownBy(() -> shipmentStatusService.record(shipmentStatus))
                .isInstanceOf(ShipmentStatusAlreadyExistsException.class);
    }

    @Test
    void readByIdThrowsShipmentStatusNotFoundException() {
        when(shipmentStatusRepository.findById(TestDataProvider.shipmentStatusId))
                .thenReturn(Optional.empty());
        assertThatThrownBy(() -> shipmentStatusService.readById(TestDataProvider.shipmentStatusId))
                .isInstanceOf(ShipmentStatusNotFoundException.class);
    }

    @Test
    void modifyThrowsShipmentStatusNotFoundException() {
        ShipmentStatus shipmentStatus = TestDataProvider.getShipmentStatus();
        when(shipmentStatusRepository.findById(TestDataProvider.shipmentStatusId))
                .thenReturn(Optional.empty());
        assertThatThrownBy(() -> shipmentStatusService.modify(shipmentStatus))
                .isInstanceOf(ShipmentStatusNotFoundException.class);
    }

    @Test
    void deleteThrowsShipmentStatusNotFoundException() {
        ShipmentStatus shipmentStatus = TestDataProvider.getShipmentStatus();
        assertThatThrownBy(() -> shipmentStatusService.delete(shipmentStatus))
                .isInstanceOf(ShipmentStatusNotFoundException.class);
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

        public static ShipmentStatusEntity getShipmentStatusEntity() {
            return ShipmentStatusEntity.builder()
                    .shipmentStatusId(shipmentStatusId)
                    .shipment(shipment)
                    .status(status)
                    .pickUpUntil(pickUpUntil)
                    .build();
        }
    }
}
