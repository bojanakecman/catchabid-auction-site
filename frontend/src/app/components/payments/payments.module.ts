import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { PaymentsComponent } from './payments.component';

import { NgxStripeModule } from 'ngx-stripe';

@NgModule({
  declarations: [
    PaymentsComponent
  ],
  imports: [
    CommonModule,
    NgxStripeModule.forRoot('pk_test_51I3UX3GarWZmyaWtEZaYjrmGokQ6OlpdqdAdgAH7Lwss9VD5PXttH5sPf5ZKZB35wSuPJYzc2FfCb1tnVJoxwHjK003DrbiqdB'),
  ],
  exports: [
    PaymentsComponent
  ]
})
export class PaymentsModule { }
