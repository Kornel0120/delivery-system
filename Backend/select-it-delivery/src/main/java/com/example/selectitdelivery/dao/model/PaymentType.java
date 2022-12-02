package com.example.selectitdelivery.dao.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PaymentType {
    private long paymentTypeId;
    private String paymentTypeName;
}
