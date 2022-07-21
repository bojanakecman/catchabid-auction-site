import { Injectable } from '@angular/core';
import { Subject, Observable, throwError } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { Bid } from '../models/bid.model';
import { catchError } from 'rxjs/operators';
import {AuctionPost} from "../models/auctionpost";


@Injectable({
  providedIn: 'root'
})
export class BidsService {

  bidModalClosed: Subject<void> = new Subject<void>();

  private api = 'api/bids';

  constructor(
    private http: HttpClient
  ) {
  }

  saveBid(bid: Bid): Observable<Bid> {
    return this.http.post<Bid>(this.api, bid)
    .pipe(catchError(error => {
      return throwError(error);
    }));
  }

  getMyBids():Observable<AuctionPost[]> {
    return this.http.get<AuctionPost[]>(this.api+'/myBids');
  }
}
