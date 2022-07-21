import {CUSTOM_ELEMENTS_SCHEMA, NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {AuctionsComponent} from './auctions.component';
import {HttpClientModule} from '@angular/common/http';
import {AppRoutingModule} from "../../app-routing.module";
import {BrowserModule} from "@angular/platform-browser";
import {AuctionCountdownComponent} from "./auction-countdown/auction-countdown.component";
import {CountdownModule} from "ngx-countdown";
import {AuctionsListComponent} from "./auctions-list/auctions-list.component";
import {AuctionFormComponent} from "./auction-form/auction-form.component";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {NgbModule} from "@ng-bootstrap/ng-bootstrap";
import { AuctionsSearchFormComponent } from './auctions-search-form/auctions-search-form.component';
import { ProfileAuctionsListComponent } from './profile-auctions-list/profile-auctions-list.component';
import {AuctionCancellationConfirmationComponent} from "../auction-details/auction-cancellation-confirmation/auction-cancellation-confirmation.component";

@NgModule({
  declarations: [
    AuctionsListComponent,
    AuctionsComponent,
    AuctionCountdownComponent,
    AuctionFormComponent,
    AuctionsSearchFormComponent,
    AuctionCancellationConfirmationComponent,
    ProfileAuctionsListComponent
  ],
  imports: [
    CommonModule,
    HttpClientModule,
    AppRoutingModule,
    BrowserModule,
    CountdownModule,
    ReactiveFormsModule,
    NgbModule,
    FormsModule
  ],
  providers:[],
  exports:[
    AuctionsListComponent,
    AuctionCountdownComponent,
    AuctionsSearchFormComponent,
    ProfileAuctionsListComponent
  ],
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
})
export class AuctionsModule {
}
