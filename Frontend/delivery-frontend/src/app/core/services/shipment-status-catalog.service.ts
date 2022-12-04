import { Injectable } from '@angular/core';
import { map, Observable } from 'rxjs';
import { ShipmentStatusCatalog } from '../model';
import { ApiService } from './api.service';

@Injectable()
export class ShipmentStatusCatalogService {

  constructor(private apiService: ApiService) { }

  getAll(): Observable<ShipmentStatusCatalog[]> {
    return this.apiService.get('/api/shipmentStatusCatalog/all')
    .pipe(map((data: ShipmentStatusCatalog[]) => data));
  }
}
