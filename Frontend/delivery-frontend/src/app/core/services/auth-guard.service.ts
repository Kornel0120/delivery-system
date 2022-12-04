import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, Router, RouterStateSnapshot } from '@angular/router';
import { Observable, take } from 'rxjs';
import { UserService } from './user.service';

@Injectable()
export class AuthGuardService {

  constructor(
    private router: Router,
    private userService: UserService
    ) { }

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<boolean> {
    return this.userService.isAuthenticated.pipe(take(1));
  }
}
