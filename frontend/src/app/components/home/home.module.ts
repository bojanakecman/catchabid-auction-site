import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {HomeComponent} from './home.component';
import {AuctionsModule} from "../auctions/auctions.module";


@NgModule({
  imports: [
    CommonModule,
    AuctionsModule
  ],
  declarations: [HomeComponent]
})
export class HomeModule { }
