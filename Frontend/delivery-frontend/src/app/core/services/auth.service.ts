import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';

const AUTH_API =  environment.base_url + '/api/auth/';

const httpOptions = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json'})
};

@Injectable()
export class AuthService {
  constructor(private http: HttpClient) { }

  login(email: string, password: string): Observable<any> {
      return this.http.post(AUTH_API + 'signin', {
          email,
          password
        }, httpOptions);
  }

  register(email: string, password: string): Observable<any> {
      return this.http.post(AUTH_API + 'signup', {
          email,
          password
      }, httpOptions);
  }

  logout(): Observable<any> {
      return this.http.post(AUTH_API + 'signout', { }, httpOptions);
    }

  refreshToken(token: string) {
      return this.http.post(AUTH_API + 'refreshtoken', {
        refreshToken: token
      }, httpOptions);
  }
}
