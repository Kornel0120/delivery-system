import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { tap } from 'rxjs';
import { AppUser, ShipmentService, ShipmentStatus, ShipmentStatusCatalog, UserService } from '../core';
import { ShipmentStatusModifyRequest } from '../core/model/ShipmentStatusModifyRequest.model';
import { ShipmentStatusCatalogService } from '../core/services/shipment-status-catalog.service';

enum PickUpDateMethod {
  DefaultDays = "DefaultDays",
  SpecificDays = "SpecificDays",
  FromCalendar = "FromCalendar",
  AsAString = "AsAString"
}

enum ShipmentStatuses {
  UnderDelivery = "Under delivery",
  Delivered = "Delivered",
  FailedDelivery = "Failed delivery",
  PickedUp = "Picked up"
}

@Component({
  selector: 'app-shipments',
  templateUrl: './shipments.component.html',
  styleUrls: ['./shipments.component.css']
})
export class ShipmentsComponent implements OnInit {
  today: number = Date.now();
  shipments: ShipmentStatus[] | undefined;
  shipmentStatus: ShipmentStatus | undefined;
  editShipmentStatus: ShipmentStatus | undefined;
  shipmentStatusModifyRequest: ShipmentStatusModifyRequest = {shipmentId: 0, shipmentStatusCatalogName: "", pickUpUntil: new Date()};
  currentUser: AppUser | undefined;
  isShipmentForThisEmployee: boolean | undefined;
  shipmentStatuses: ShipmentStatusCatalog[] | undefined;
  isSubmitting = false;
  availableMethods = [PickUpDateMethod.DefaultDays, PickUpDateMethod.SpecificDays, PickUpDateMethod.AsAString, PickUpDateMethod.FromCalendar];
  pickUpDateMethod = PickUpDateMethod.DefaultDays;
  pickUpDays: number = 5;
  stringDate: string | undefined;

  constructor(
    private router: Router,
    private route: ActivatedRoute,
    private userService: UserService,
    private shipmentService: ShipmentService,
    private shipmentStatusCatalogService: ShipmentStatusCatalogService
  ) { }

  ngOnInit() {
    this.getShipments();
    this.userService.currentUser.subscribe(
      (userData: AppUser) => {
        this.currentUser = userData;
      }
    );
  }

  public getShipment() {
    this.shipmentService.get(this.route.snapshot.data['id']).subscribe((response:ShipmentStatus) => {
      this.shipmentStatus = response;
    });
    return this.userService.currentUser.pipe(tap(
      (userData: AppUser) => {
        this.currentUser = userData;
        this.isShipmentForThisEmployee = (this.currentUser?.email === this.shipmentStatus?.shipment.employee.user.email);
      }
    ));
  }

  public getShipments() {
    this.shipmentService.getAll().subscribe({
      next: data => {
        this.shipments = data;
      },
      error: err => {
        alert(err.message);
      }
    })
  }

  public getShipmentStatuses() {
    this.shipmentStatusCatalogService.getAll().subscribe({
      next: data => {
        this.shipmentStatuses = data;
      },
      error: err => {
        alert(err.message);
      }
    })
  }

  public onUpdateShipment(shipmentStatusModifyRequest: ShipmentStatusModifyRequest) : void {   
    this.shipmentService.modify(shipmentStatusModifyRequest.shipmentId, shipmentStatusModifyRequest.shipmentStatusCatalogName, shipmentStatusModifyRequest.pickUpUntil).subscribe({
      next: data => {
        this.getShipments();
      },
      error: err => {
        alert(err.message);
      }
    })
  }

  public onPickUpPackage(shipmentStatus: ShipmentStatus): void {
    this.shipmentStatusModifyRequest.pickUpUntil = new Date();
    this.shipmentStatusModifyRequest.shipmentId = shipmentStatus.shipment.shipmentId;
    this.shipmentStatusModifyRequest.shipmentStatusCatalogName = ShipmentStatuses.PickedUp;

    this.onUpdateShipment(this.shipmentStatusModifyRequest)
  }

  public onOpenModal(shipmentStatus: ShipmentStatus | undefined, mode: string): void {
    const container = document.getElementById('main-container')
    const button = document.createElement('button');
    button.type = 'button';
    button.style.display = 'none';
    button.setAttribute('data-toggle', 'modal');
    this.getShipmentStatuses();
   
    console.log('deliveryMethod: ', PickUpDateMethod[this.pickUpDateMethod])
    if (mode === 'deliver') {
      this.editShipmentStatus = shipmentStatus;
    }

    container?.appendChild(button);
    button.click();
  }

  public getModalValue(shipmentStatusModifyRequest: ShipmentStatusModifyRequest): void {
    if(this.pickUpDateMethod === PickUpDateMethod.DefaultDays) {
      shipmentStatusModifyRequest.pickUpUntil = this.addDay(this.pickUpDays);
    } else if (this.pickUpDateMethod === PickUpDateMethod.SpecificDays) {
      shipmentStatusModifyRequest.pickUpUntil = this.addDay(this.pickUpDays);
    } else if (this.pickUpDateMethod === PickUpDateMethod.AsAString && this.stringDate !== undefined) {
      shipmentStatusModifyRequest.pickUpUntil = this.convertStringToDay(this.stringDate);
    } else {
      shipmentStatusModifyRequest.pickUpUntil = shipmentStatusModifyRequest.pickUpUntil;
    }

    this.onUpdateShipment(shipmentStatusModifyRequest);
  }

  public addDay(numberOfDays: number, date = new Date()):any {
     date.setDate(date.getDate() + numberOfDays);
     return date;
  }

  public convertStringToDay(dateInput: string):any {
    const date = new Date(dateInput);
    console.log(date);
    return date;
  }

  public get pickUpDateMethodEnum(): typeof PickUpDateMethod {
    return PickUpDateMethod; 
  }

  public get shipmentStatusesEnum(): typeof ShipmentStatuses {
    return ShipmentStatuses; 
  }

  getMethodLabel(method: PickUpDateMethod) {
    switch (method) {
      case PickUpDateMethod.DefaultDays:
        return "Default pickup date. (5 days from delivery date)";
      case PickUpDateMethod.SpecificDays:
        return "Specific pickup date. (With the given days from delivery date)";
      case PickUpDateMethod.FromCalendar:
        return "Select date from a calendar.";
      case PickUpDateMethod.AsAString:
        return "Specify pickup date as a text. (for example 2022-12-04 or 2022. 12. 04.)";
      default:
        throw new Error("Unsupported option");
    }
  }
}
