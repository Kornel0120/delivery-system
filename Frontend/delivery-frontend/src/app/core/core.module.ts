import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { authInterceptorProviders } from './interceptors/auth.interceptor';
import { ApiService, AuthGuardService, ProfileService, ShipmentService, ShipmentStatusCatalogService, TokenStorageService, UserService } from './services';



@NgModule({
  imports: [
      CommonModule
  ],
  providers: [
    authInterceptorProviders,
    //{ provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true},
    ApiService,
    AuthGuardService,
    ShipmentService,
    ProfileService,
    ShipmentStatusCatalogService,
    TokenStorageService,
    UserService
  ],
  declarations: []
})
export class CoreModule { }
