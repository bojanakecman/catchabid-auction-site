import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { BidsComponent } from './bids.component';
import { HttpClientModule } from '@angular/common/http';
import { AppRoutingModule } from 'src/app/app-routing.module';
import { BrowserModule } from '@angular/platform-browser';
import { ReactiveFormsModule } from '@angular/forms';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { PaymentsModule } from '../payments/payments.module';



@NgModule({
  declarations: [
    BidsComponent
  ],
  imports: [
    CommonModule,
    HttpClientModule,
    AppRoutingModule,
    BrowserModule,
    ReactiveFormsModule,
    NgbModule,
    PaymentsModule
  ]
})
export class BidsModule { }
