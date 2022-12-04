import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, Resolve, Router, RouterStateSnapshot } from '@angular/router';
import { catchError, Observable } from 'rxjs';
import { ShipmentService, ShipmentStatus } from '../core';

@Injectable()
export class ShipmentsResolverService implements Resolve<ShipmentStatus> {

  constructor(
    private shipmentService: ShipmentService,
    private router: Router
  ) {}

  resolve(
    route: ActivatedRouteSnapshot,
     state: RouterStateSnapshot): Observable<any> {
    console.log("route.params[0]: ", route.params[0]);
    return this.shipmentService.getAll()
    .pipe(catchError((err) => this.router.navigateByUrl('/')));
  }

}
