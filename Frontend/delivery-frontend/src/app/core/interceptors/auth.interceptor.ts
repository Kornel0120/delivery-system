import { Injectable } from '@angular/core';
import {
  HttpRequest,
  HttpHandler,
  HttpEvent,
  HttpInterceptor,
  HTTP_INTERCEPTORS,
  HttpErrorResponse
} from '@angular/common/http';
import { BehaviorSubject, catchError, filter, Observable, switchMap, take, throwError } from 'rxjs';
import { TokenStorageService } from '../services/token-storage.service';
import { UserService } from '../services/user.service';
import { Router } from '@angular/router';

const TOKEN_HEADER_KEY = 'Authorization';
const REFRESHTOKEN_HEADER_KEY = 'REFRESH_TOKEN';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {
  private isRefreshing = false;
  private refreshTokenSubject: BehaviorSubject<any> = new BehaviorSubject<any>(null);

  constructor(private tokenStorageService: TokenStorageService, private userService: UserService, private router: Router) {}

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    const headersConfig = {
      'Content-Type': 'application/json',
      'Authorization': 'application/json',
    };

    const token = this.tokenStorageService.getToken();
    if (token) {
      headersConfig[TOKEN_HEADER_KEY] = `Bearer ${token}`;
    }

    const request = req.clone({ setHeaders: headersConfig }); 
    return next.handle(request).pipe(catchError(error => {
      if (error instanceof HttpErrorResponse) {
        if(error.error instanceof ErrorEvent) {
        } else {
          switch (error. status) {
            case 401:
              return this.handle401Error(req, next);
              break;
            case 403:
              this.handle403Error();
              break;
          }
        }
      }
      return throwError(() => error);
    }));
  }

  private handle401Error(request: HttpRequest<any>, next: HttpHandler) {
    if(!this.isRefreshing) {
      this.isRefreshing = true;
      this.refreshTokenSubject.next(null);

      const refreshToken = this.tokenStorageService.getRefreshToken();
      if(refreshToken) {
        return this.userService.refreshToken(refreshToken).pipe(
          switchMap((token: any) => {
            this.isRefreshing = false;

            this.tokenStorageService.saveToken(token.token);
            this.refreshTokenSubject.next(token.token);

            return next.handle(this.addTokenHeader(request, token.accessToken));
          }));
      } else {
        this.userService.purgeAuth();
        this.router.navigateByUrl("/login");
      }
    }

    return this.refreshTokenSubject.pipe(
      filter(token => token !== null),
      take(1),
      switchMap((token) => next.handle(this.addTokenHeader(request,token)))
    );
  }

  private handle403Error() {
    this.router.navigateByUrl("/");
  }

  private addTokenHeader(request: HttpRequest<any>, token: string) {
    return request.clone({ headers: request.headers.set(TOKEN_HEADER_KEY, 'Bearer ' + token) });
  }
}

export const authInterceptorProviders = [
  { provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true}
];
