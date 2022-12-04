import { Injectable } from '@angular/core';
import { map, Observable } from 'rxjs';
import { ShipmentStatus } from '../model';
import { ShipmentStatusModifyRequest } from '../model/ShipmentStatusModifyRequest.model';
import { ApiService } from './api.service';

@Injectable()
export class ShipmentService {
  constructor(private apiService: ApiService) { }

  get(id: number): Observable<ShipmentStatus> {
    console.log("ShipmentService!")
    return this.apiService.get('/api/shipmentStatus/' + id)
      .pipe(map((data: ShipmentStatus) => data));
  }

  getAll(): Observable<ShipmentStatus[]> {
    return this.apiService.get('/api/shipmentStatus/all')
    .pipe(map((data: ShipmentStatus[]) => data));
  }

  modify(shipmentId: number, shipmentStatusCatalogName: String, pickUpUntil: Date): Observable<any> {
    return this.apiService.put('/api/shipmentStatus/modify', {
      shipmentId,
      shipmentStatusCatalogName,
      pickUpUntil})
      .pipe(map((data: ShipmentStatusModifyRequest) => data));
  }
}
