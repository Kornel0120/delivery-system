export interface ShipmentStatusModifyRequest {
    shipmentId: number,
    shipmentStatusCatalogName: String,
    pickUpUntil: Date
}