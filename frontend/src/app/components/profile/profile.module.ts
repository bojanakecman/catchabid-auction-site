import {NgModule} from '@angular/core';
import {NgbdModalConfirm, ProfileComponent} from "./profile.component";
import {CommonModule} from "@angular/common";
import {BrowserModule} from "@angular/platform-browser";
import {UpdatePasswordComponent} from './update-password/update-password.component';
import { NgMultiSelectDropDownModule } from 'ng-multiselect-dropdown';
import { FormsModule } from '@angular/forms';
import {AuctionsModule} from "../auctions/auctions.module";
import {StatisticsComponent} from "./statistics/statistics.component";
import { ChartsModule } from 'ng2-charts';
import {NgbTooltipModule} from "@ng-bootstrap/ng-bootstrap";


@NgModule({
  declarations: [UpdatePasswordComponent, ProfileComponent, NgbdModalConfirm, StatisticsComponent],
  imports: [
    BrowserModule,
    CommonModule,
    NgMultiSelectDropDownModule.forRoot(),
    FormsModule,
    AuctionsModule,
    ChartsModule,
    NgbTooltipModule
  ]
})
export class ProfileModule {
}
