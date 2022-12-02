package com.example.selectitdelivery.dao.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "payment_type")
public class PaymentTypeEntity {
    @Id
    @Column(name = "payment_type_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long paymentTypeId;
    @Column(name = "payment_type_name")
    private String paymentTypeName;
}
