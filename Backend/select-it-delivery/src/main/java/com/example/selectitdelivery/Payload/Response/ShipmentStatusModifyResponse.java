package com.example.selectitdelivery.Payload.Response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
public class ShipmentStatusModifyResponse {
    private String message;
    private String empFirstName;
    private String empLastName;
    private String empPhone;
    private String clientFirstName;
    private String clientLastName;
    private String clientPhone;
    private String paymentType;
    private char shipmentSize;
    private String packagePointPostalCode;
    private String packagePointCity;
    private String packagePointAddress;
    private BigDecimal productsPrice;
    private BigDecimal deliveryCost;
    private BigDecimal finalPrice;
    private String statusName;
    private Date pickUpUntil;
}
