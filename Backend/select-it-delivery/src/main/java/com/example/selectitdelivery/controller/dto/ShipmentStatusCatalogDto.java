package com.example.selectitdelivery.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ShipmentStatusCatalogDto {
    private long statusCatalogId;
    private String statusName;
}
