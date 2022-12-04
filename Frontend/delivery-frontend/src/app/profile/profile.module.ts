import { NgModule } from '@angular/core';
import { ProfileComponent } from './profile.component';
import { SharedModule } from '../shared';
import { ProfileRoutingModule } from './profile-routing.module';
import { ProfileResolverService } from './profile-resolver.service';



@NgModule({
  imports: [
    SharedModule,
    ProfileRoutingModule
  ],  
  declarations: [
    ProfileComponent
  ],
  providers: [
    ProfileResolverService
  ],
})
export class ProfileModule { }
