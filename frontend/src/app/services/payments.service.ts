import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, Subject } from 'rxjs';
import { PaymentIntent } from '../models/paymentIntent';


@Injectable({
  providedIn: 'root'
})
export class PaymentsService {

  private api = 'api/payments';

  paymentModalClosed: Subject<void> = new Subject<void>();

  auctionDetailModalClosed: Subject<void> = new Subject<void>();

  constructor(
    private http: HttpClient
  ) {
  }

  createPaymentIntent(auctionId: number): Observable<PaymentIntent> {
    const paymentIntentReq = {
      "auctionId": auctionId
    };
    return this.http.post<PaymentIntent>(this.api, paymentIntentReq);
  }

  storePayment(paymentIntent: string, auctionId: number) {
    const paymentStoreReq = {
      "auctionId": auctionId,
      "paymentIntent": paymentIntent
    };
    return this.http.post<PaymentIntent>(this.api + '/store', paymentStoreReq);
  }
}
