import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { BehaviorSubject, distinctUntilChanged, map, Observable, ReplaySubject } from 'rxjs';
import { AppUser } from '../model/AppUser.model';
import { ApiService } from './api.service';
import { TokenStorageService } from './token-storage.service';

@Injectable()
export class UserService {
  private currentUserSubject = new BehaviorSubject<AppUser>({} as AppUser);
  public currentUser = this.currentUserSubject.asObservable().pipe(distinctUntilChanged());

  private isAuthenticatedSubject = new ReplaySubject<boolean>(1);
  public isAuthenticated = this.isAuthenticatedSubject.asObservable();

  public isAdmin = false;

  constructor(
    private apiService: ApiService,
    private http: HttpClient,
    private tokenStorageService: TokenStorageService
  ) { }

    populate() {
      console.log("populate", this.tokenStorageService.getToken());
      if(this.tokenStorageService.getToken()) {
        this.apiService.get('/api/auth/user').subscribe({
            next: data => {this.setAuth(data)},
            error: err => {this.purgeAuth()}
          });
      } else {
        this.purgeAuth();
      }
    }

  setAuth(appUser: AppUser) {
    this.tokenStorageService.saveToken(appUser.token);
    this.tokenStorageService.saveRefreshToken(appUser.refreshToken);
    this.currentUserSubject.next(appUser);
    this.isAuthenticatedSubject.next(true);
    this.isAdmin = appUser.roles.includes('ROLE_ADMIN');
  }

  purgeAuth() {
    if(this.tokenStorageService.getToken()) {
      this.apiService.post('/api/auth/signout').subscribe();
    }

    this.tokenStorageService.destroyToken();
    this.currentUserSubject.next({} as AppUser);
    this.isAuthenticatedSubject.next(false);
  }

  attemptAuth(type: String, credentials: AppUser): Observable<AppUser> {
    const route = (type === 'login') ? '/signin' : '';
    return this.apiService.post('/api/auth' + route, credentials)
    .pipe(map(
      data => {
        this.setAuth(data);
        return data;
      }
    ));
  }

  refreshToken(token: string) {
    return this.apiService.post('/api/auth/refreshtoken', {refreshToken: token})
  }

  getCurrentUser(): AppUser {
    return this.currentUserSubject.getValue();
  }

  update(appUser: AppUser): Observable<AppUser> {
    return this.apiService.put('/api/appUser/modify', {
      id: appUser.userId,
      email: appUser.email,
      password: appUser.password,
     })
    .pipe(map(data => {
      this.currentUserSubject.next(data);
      return data;
    }));
  }
}
