import { Component, OnInit, ViewChild, Input } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from "@angular/forms";

import { StripeService, StripeCardComponent } from 'ngx-stripe';
import {
  StripeCardElementOptions,
  StripeElementsOptions
} from '@stripe/stripe-js';
import { AuctionPost } from 'src/app/models/auctionpost';
import { PaymentsService } from 'src/app/services/payments.service';
import { LoadingSpinnerService } from 'src/app/services/loading-spinner.service';
import { PaymentIntent } from 'src/app/models/paymentIntent';
import { switchMap } from 'rxjs/operators';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-payments',
  templateUrl: './payments.component.html',
  styleUrls: ['./payments.component.css']
})
export class PaymentsComponent implements OnInit {

  @ViewChild(StripeCardComponent) card: StripeCardComponent;

  @Input() auction: AuctionPost;

  processingPayment = false;

  cardOptions: StripeCardElementOptions = {
    style: {
      base: {
        iconColor: '#666EE8',
        color: '#31325F',
        fontWeight: '300',
        fontFamily: '"Helvetica Neue", Helvetica, sans-serif',
        fontSize: '18px',
        '::placeholder': {
          color: '#CFD7E0'
        }
      }
    }
  };

  elementsOptions: StripeElementsOptions = {
    locale: 'en'
  };

  stripeTest: FormGroup;

  paymentIntent: PaymentIntent;

  userName: string;

  constructor(
    private fb: FormBuilder,
    private stripeService: StripeService,
    private paymentService: PaymentsService,
    private toastrService: ToastrService) {}

  ngOnInit(): void {
    this.stripeTest = this.fb.group({
      name: ['', [Validators.required]]
    });
    this.userName = localStorage.getItem('userName');
    if (this.userName == null) {
      this.userName = "user";
    }
  }

  processPayment(): void {
    this.processingPayment = true;
    this.paymentService.createPaymentIntent(this.auction.id)
      .pipe(
        switchMap(
          (pi) => this.stripeService.confirmCardPayment(pi.clientSecret, {
            payment_method: {
              card: this.card.element,
              billing_details: {
                name: this.userName
              },
            },
          })
        )
      )
      .pipe(
        switchMap(
          (ps) => this.paymentService.storePayment(ps.paymentIntent.id, this.auction.id)
        )
      )
      .subscribe(
        (result) => {
          console.log(result);
          this.toastrService.success('Auction successfully paid.');
          this.onModalClose();
        },
        (error) => {
          console.log(error);
          this.toastrService.error('Error processing payment');
        },
      ).add(() => this.processingPayment = false);
  }

  onModalClose(): void {
    this.paymentService.paymentModalClosed.next();
  }

}
