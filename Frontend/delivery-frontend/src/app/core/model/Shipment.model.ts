import { Client } from "./Client.model";
import { Employee } from "./Employee.model";
import { PackagePoint } from "./PackagePoint.model";
import { PaymentType } from "./PaymentType.model";
import { ShipmentSize } from "./ShipmentSize.model";

export interface Shipment {
    shipmentId: number,
    employee: Employee,
    client: Client,
    paymentType: PaymentType,
    shipmentSize: ShipmentSize,
    packagePoint: PackagePoint,
    productsPrice: number,
    deliveryCost: number,
    finalPrice: number
}