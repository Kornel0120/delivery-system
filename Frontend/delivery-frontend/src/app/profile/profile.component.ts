import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { concatMap, tap } from 'rxjs';
import { AppUser, Profile, ProfileService, UserService } from '../core';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit {

  constructor(
    private router: Router,
    private route: ActivatedRoute,
    private userService: UserService,
    private profileServie: ProfileService
  ) { }

  profile: Profile | undefined;
  currentUser: AppUser | undefined;
  isUser: boolean | undefined;

  ngOnInit() {
    console.log("this.route.data: ", this.route.data)
    this.route.data.pipe(
      concatMap((data) => {
        console.log("data[profile]: ", data['profile']);
        this.profile = data['profile'];
    
        return this.userService.currentUser.pipe(tap(
          (userData: AppUser) => {
            this.currentUser = userData;
            this.isUser = (this.currentUser?.email === this.profile?.email);
          }
        ));
      })
    ).subscribe();
  }

  public getProfile() {
    console.log("this.route.snapshot.data: ", this.route.snapshot.data);
    this.profileServie.get(this.route.snapshot.data['email']).subscribe((response:Profile) => {
      this.profile = response;
    });
    return this.userService.currentUser.pipe(tap(
      (userData: AppUser) => {
        this.currentUser = userData;
        this.isUser = (this.currentUser?.email === this.profile?.email);
      }
    ));
  }

  logout() {
    this.userService.purgeAuth();
    this.router.navigateByUrl('/');
  }
}
