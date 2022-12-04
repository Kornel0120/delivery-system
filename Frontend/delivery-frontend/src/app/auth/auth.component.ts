import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Errors } from '../core/model/Errors.model';
import { UserService } from '../core/services/user.service';

@Component({
  selector: 'app-auth',
  templateUrl: './auth.component.html',
  styleUrls: ['./auth.component.css']
})
export class AuthComponent implements OnInit {
  authType: String = '';
  title: String = '';
  errors: Errors = {errors: {}};
  isSubmitting = false;
  authForm: FormGroup;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private userService: UserService,
    private fb: FormBuilder
  ) {
    this.authForm = this.fb.group({
      'email': ['', Validators.required],
      'password': ['', Validators.required]
    });
  }

  ngOnInit(): void {
    this.route.url.subscribe({
      next: data => {
        this.authType = data[data.length - 1].path;
        this.title = (this.authType === 'login') ? 'Sign in' : 'Sign up';
        if(this.authType === 'register') {
          this.authForm.addControl('email', new FormControl());
        }
      }
    });
  }

  submitForm() {
    this.isSubmitting = true;
    this.errors = {errors: {}};
    const credentials = this.authForm.value;
    this.userService.attemptAuth(this.authType, credentials).subscribe({
      next: data => {this.router.navigateByUrl('/shipments')},
      error: err => {
        this.errors = err;
        this.isSubmitting = false;
      }
    });
  }
}
