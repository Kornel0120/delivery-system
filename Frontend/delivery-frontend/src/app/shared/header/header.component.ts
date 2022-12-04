import { Component, OnInit } from '@angular/core';
import { AppUser, UserService } from 'src/app/core';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {
  constructor(
    private userService: UserService
  ) {}

  currentUser: AppUser | undefined;
  isAdmin: boolean | undefined;

  ngOnInit(): void {

    this.userService.currentUser.subscribe(
      (userData) => {
        this.currentUser = userData;
        if(this.currentUser.roles != undefined) {
          this.isAdmin = this.currentUser?.roles.includes('ROLE_ADMIN');
        }
      }
    )
  }
}
