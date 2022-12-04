import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, Resolve, Router, RouterStateSnapshot } from '@angular/router';
import { catchError, Observable } from 'rxjs';
import { Profile, ProfileService, UserService } from '../core';

@Injectable()
export class ProfileResolverService  implements Resolve<Profile>  {

  constructor(
    private profileServie: ProfileService,
    private router: Router
  ) {}

  resolve(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Observable<any> {
      console.log("route.params['email']: ", route.params);
    return this.profileServie.get(route.params['email'])
    .pipe(catchError((err) => this.router.navigateByUrl('/')));
  }
}