import { HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { map, Observable } from 'rxjs';
import { Profile } from '../model';
import { ApiService } from './api.service';

const httpOptions = {
  headers: new HttpHeaders({ 'Content-Type': 'application/json'})
};

@Injectable()
export class ProfileService {

  constructor(private apiService: ApiService) { }

  get(email: string): Observable<Profile> {
    console.log("Profile service!")
    return this.apiService.get('/api/appUser/profile/:' + email)
      .pipe(map((data: Profile) => data));
  }
}
