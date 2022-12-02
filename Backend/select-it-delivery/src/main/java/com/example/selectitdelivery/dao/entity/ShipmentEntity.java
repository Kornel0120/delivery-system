package com.example.selectitdelivery.dao.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "shipment")
public class ShipmentEntity {
    @Id
    @Column(name = "shipment_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long shipmentId;

    @OneToOne
    @JoinColumn(name = "emp_id", referencedColumnName = "emp_id")
    private EmployeeEntity employee;

    @OneToOne
    @JoinColumn(name = "client_id", referencedColumnName = "client_id")
    private ClientEntity client;

    @OneToOne
    @JoinColumn(name = "payment_type_id", referencedColumnName = "payment_type_id")
    private PaymentTypeEntity paymentType;

    @OneToOne
    @JoinColumn(name = "shipment_size_id", referencedColumnName = "shipment_size_id")
    private ShipmentSizeEntity shipmentSize;

    @OneToOne
    @JoinColumn(name = "package_point_id", referencedColumnName = "package_point_id")
    private PackagePointEntity packagePoint;

    @Column(name = "products_price")
    private BigDecimal productsPrice;
    @Column(name = "delivery_cost")
    private BigDecimal deliveryCost;
    @Column(name = "final_price")
    private BigDecimal finalPrice;
}
