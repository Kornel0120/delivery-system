package com.example.selectitdelivery.Payload.Request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
public class ShipmentStatusModifyRequest {
    @NotNull
    private long shipmentId;
    @NotBlank
    private String shipmentStatusCatalogName;
    @NotNull
    private Date pickUpUntil;
}
