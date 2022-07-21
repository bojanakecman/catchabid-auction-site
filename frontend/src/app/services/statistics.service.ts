import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";

const endpoint = 'http://localhost:8080';

@Injectable({
  providedIn: 'root'
})
export class StatisticsService {
  constructor(private http: HttpClient) {
  }

  getBidStatistics(): Observable<Map<string,number>> {
    return this.http.get<Map<string,number>>(endpoint + '/bidStatistics');
  }

  getWinStatistics(): Observable<Map<string,number>> {
    return this.http.get<Map<string,number>>(endpoint + '/winStatistics');
  }

  getBidWinRatio(): Observable<Map<string,number>> {
    return this.http.get<Map<string,number>>(endpoint + '/winBidRatio');
  }

  getAuctionPopularity(): Observable<Map<string,number>> {
    return this.http.get<Map<string,number>>(endpoint + '/myAuctionsPopularity');
  }

  getSuccessOfAuctions(): Observable<Map<string,number>> {
    return this.http.get<Map<string,number>>(endpoint + '/myAuctionsSuccess');
  }
}
