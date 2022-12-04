import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ProfileComponent } from './profile.component';
import { ProfileResolverService } from './profile-resolver.service';

const routes: Routes = [
  {
    path: ':email',
    component: ProfileComponent,
    resolve: {
      profile: ProfileResolverService
    },
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ProfileRoutingModule { }
