import { TestBed } from '@angular/core/testing';

import { ShipmentStatusCatalogService } from './shipment-status-catalog.service';

describe('ShipmentStatusCatalogService', () => {
  let service: ShipmentStatusCatalogService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ShipmentStatusCatalogService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
