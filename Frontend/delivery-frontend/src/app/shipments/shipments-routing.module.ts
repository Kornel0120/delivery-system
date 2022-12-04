import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ShipmentsComponent } from './shipments.component';
import { ShipmentsResolverService } from './shipments-resolver.service';


const routes: Routes = [
  {
    path: '',
    component: ShipmentsComponent,
    resolve: {
      profile: ShipmentsResolverService
    },
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ShipmentsRoutingModule { }
