import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { catchError, Observable, throwError } from 'rxjs';
import { environment } from 'src/environments/environment';
import { TokenStorageService } from './token-storage.service';

const httpOptions = {
  headers: new HttpHeaders({ 'Content-Type': 'application/json'})
};

@Injectable()
export class ApiService {

  constructor(
    private http: HttpClient,
    private tokenStorageService: TokenStorageService
    ) { }

    private formatErrors(error: any) {
      return throwError(() => error);
    }

    get(path: string, params: HttpParams = new HttpParams()): Observable<any> {
      return this.http.get(`${environment.base_url}${path}`, { params })
      .pipe(catchError(this.formatErrors));
    }

    getBy(path: string, body: Object = {}): Observable<any> {
      return this.http.get(`${environment.base_url}${path}`, body)
      .pipe(catchError(this.formatErrors));
    }

    put(path: string, body: Object = {}): Observable<any> {
      return this.http.put(`${environment.base_url}${path}`,
      body)
      .pipe(catchError(this.formatErrors));
    }

    post(path: string, body: Object = {}): Observable<any> {
      return this.http.post(`${environment.base_url}${path}`,
      body)
      .pipe(catchError(this.formatErrors));

    }

    delete(path: string): Observable<any> {
      return this.http.delete(`${environment.base_url}${path}`)
      .pipe(catchError(this.formatErrors));
    }
}
