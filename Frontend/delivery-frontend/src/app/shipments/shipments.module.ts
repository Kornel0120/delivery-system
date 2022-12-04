import { NgModule } from '@angular/core';
import { SharedModule } from '../shared';
import { ShipmentsRoutingModule } from './shipments-routing.module';
import { ShipmentsComponent } from './shipments.component';
import { ShipmentsResolverService } from './shipments-resolver.service';
import { MdbDropdownModule } from 'mdb-angular-ui-kit/dropdown';
import { MatSelectModule } from '@angular/material/select';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';

@NgModule({
  declarations: [ShipmentsComponent],
  imports: [
    SharedModule,
    ShipmentsRoutingModule,
    MdbDropdownModule,
    MatSelectModule,
    MatFormFieldModule,
    MatInputModule
  ],
  providers: [
    ShipmentsResolverService
  ]
})
export class ShipmentsModule { }
