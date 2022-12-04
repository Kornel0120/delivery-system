import { Shipment } from "./Shipment.model";
import { ShipmentStatusCatalog } from "./ShipmentStatusCatalog.model";

export interface ShipmentStatus {
    shipmentStatusId: number,
    shipment: Shipment,
    status: ShipmentStatusCatalog,
    pickUpUntil: Date
}