import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Routes } from '@angular/router';
import { AuthComponent } from './auth.component';
import { NoAuthGuardService } from './no-auth-guard.service';

const routes: Routes = [
  {
    path: 'login',
    component: AuthComponent,
    canActivate: [NoAuthGuardService]
  },
  {
    path: 'register',
    component: AuthComponent,
    canActivate: [NoAuthGuardService]
  }
]

@NgModule({
  declarations: [],
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class AuthRoutingModule { }
