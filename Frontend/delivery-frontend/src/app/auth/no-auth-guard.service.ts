import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, Router, RouterStateSnapshot } from '@angular/router';
import { map, Observable, take } from 'rxjs';
import { UserService } from '../core/services/user.service';

@Injectable()
export class NoAuthGuardService {

  constructor(
    private router: Router,
    private userService: UserService
  ) { }


  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<boolean> {
    console.log("AuthGuard");
    this.userService.populate();
    return this.userService.isAuthenticated.pipe(take(1), map(isAuth => !isAuth));
  }
}
