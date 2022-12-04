import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AuthRoutingModule } from './auth-routing.module';
import { AuthComponent } from './auth.component';
import { NoAuthGuardService } from './no-auth-guard.service';
import { SharedModule } from '../shared';



@NgModule({
  imports: [
    SharedModule,
    AuthRoutingModule
  ],
  declarations: [
    AuthComponent
  ],
  providers: [
    NoAuthGuardService
  ]
})
export class AuthModule { }
