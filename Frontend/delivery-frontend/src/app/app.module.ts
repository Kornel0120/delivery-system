import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { AuthModule } from './auth/auth.module';
import { CoreModule } from './core';
import { FooterComponent, SharedModule } from './shared';
import { HeaderComponent } from "./shared/header/header.component";
import { NoopAnimationsModule } from '@angular/platform-browser/animations';


@NgModule({
    declarations: [
      AppComponent,
      FooterComponent,
      HeaderComponent,
    ],
    providers: [],
    bootstrap: [AppComponent],
    imports: [
        BrowserModule,
        AuthModule,
        AppRoutingModule,
        SharedModule,
        CoreModule,
        NoopAnimationsModule,

    ]
})
export class AppModule { }
