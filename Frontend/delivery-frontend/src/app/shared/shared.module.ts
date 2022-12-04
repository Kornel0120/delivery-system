import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ShowAuthedDirective } from './show-authed.directive';
import { RouterModule } from '@angular/router';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { ListErrorsComponent } from './list-errors';




@NgModule({
  declarations: [
    ShowAuthedDirective,
    ListErrorsComponent
  ],
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    HttpClientModule,
    RouterModule
  ],
  exports: [
    CommonModule,
    ShowAuthedDirective,
    FormsModule,
    ListErrorsComponent,
    ReactiveFormsModule,
    HttpClientModule,
    RouterModule
  ]
})
export class SharedModule { }
