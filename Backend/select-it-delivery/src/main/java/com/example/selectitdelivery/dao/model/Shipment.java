package com.example.selectitdelivery.dao.model;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Shipment {
    private long shipmentId;
    private long empId;
    private long clientId;
    private long paymentTypeId;
    private long shipmentSizeId;
    private long packagePointId;
    private BigDecimal productsPrice;
    private BigDecimal deliveryCost;
    private BigDecimal finalPrice;
}
