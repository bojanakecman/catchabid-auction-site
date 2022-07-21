import {Injectable} from '@angular/core';
import {HttpClient, HttpParameterCodec, HttpParams} from '@angular/common/http';
import {Observable, Subject} from "rxjs";
import 'rxjs/add/operator/map';
import {AuctionPost} from '../models/auctionpost';
import {AuctionSearchQuery} from "../models/auctionSearchQuery";
import 'rxjs/add/operator/map'
import {ContactForm} from "../models/contact-form";

const api = 'api/auctions';

@Injectable({
  providedIn: 'root'
})
export class AuctionsService {
  auctionFormModalClosed: Subject<void> = new Subject<void>();
  auctionDetailModalClosed: Subject<void> = new Subject<void>();
  contactFormModalClosed: Subject<void> = new Subject<void>();

  private REST_API_SERVER: string = "http://localhost:8080/";

  constructor(private http: HttpClient) {
  }

  public findAuctions(query: AuctionSearchQuery) {

    let params = new HttpParams()
      .set("pageNumber", query.pageNumber.toString())
      .set("pageSize", query.pageSize.toString())
      .set("categories", query.categories.join(","))
      .set("searchKeys", query.searchKeys.join(","))
      .set("countries", query.countries.join(","))
      .set("status", query.status.join(","))
      .set("sortBy", query.sortBy)
      .set("sortOrder", query.sortOrder)
      .set("useUserPreferences", String(query.useUserPreferences))
      .set("userEmail",query.userEmail);

    if(query.auctionsStartUntil) {
      params = params.append("auctionsStartUntil", query.auctionsStartUntil.toISOString());
    }
    if(query.auctionsStartFrom) {
      params = params.append("auctionsStartFrom", (query.auctionsStartFrom.toISOString()));
    }
    if(query.auctionsEndFrom) {
      params = params.append("auctionsEndFrom", (query.auctionsEndFrom.toISOString()));
    }
    if(query.auctionsEndUntil) {
      params = params.append("auctionsEndUntil", (query.auctionsEndUntil.toISOString()));
    }
    return this.http.get<Array<AuctionPost>>(this.REST_API_SERVER + api, {params});
  }

  getRecentPosts(pageNumber: number, pageSize: number): Observable<Array<AuctionPost>> {
    const params = new HttpParams()
      .set("pageNumber", pageNumber.toString())
      .set("auctionsPerPage", pageSize.toString())
      .set("userEmail", localStorage.getItem("email"));

    return this.http.get<Array<AuctionPost>>(this.REST_API_SERVER + api + "/recent", {params});
  }

  public getUpcomingRequests(pageNumber: number, pageSize: number): Observable<Array<AuctionPost>> {
    const params = new HttpParams()
      .set("pageNumber", pageNumber.toString())
      .set("pageSize", pageSize.toString())
      .set("userEmail", localStorage.getItem("email"));

    const request: string = this.REST_API_SERVER + api + "/upcoming";

    return this.http.get<Array<AuctionPost>>(request, {params});
  }


  saveAuction(auctionPost: AuctionPost): Observable<AuctionPost> {
    return this.http.post<AuctionPost>(this.REST_API_SERVER + api + "/save", auctionPost);
  }

  getCategories(): Observable<Object> {
    return this.http.get(this.REST_API_SERVER + api + "/getCategories");
  }

  getCountriesWhereAuctionsExist() : Observable<Object> {
    return this.http.get(this.REST_API_SERVER + api + "/countriesWhereAuctionsExist");
  }

  postContactForm(contactForm: ContactForm): Observable<ContactForm> {
    return this.http.post<ContactForm>(this.REST_API_SERVER + api + '/postContactForm', contactForm);
  }

  getContactForm(auctionPost: AuctionPost): Observable<ContactForm> {
    const params = new HttpParams()
      .set('auctionPostId', auctionPost.id.toString());
    return this.http.get<ContactForm>(this.REST_API_SERVER + api + '/getContactForm', {params});
  }

  subscribeToAuction(auctionPost: AuctionPost): Observable<AuctionPost> {
    return this.http.post<AuctionPost>(this.REST_API_SERVER + api + '/subscribe', auctionPost);
  }

  unsubsribeFromAuction(auctionPost: AuctionPost): Observable<AuctionPost>{
    return this.http.post<AuctionPost>(this.REST_API_SERVER + api + '/unsubscribe', auctionPost)
  }

  getSubscriptionByUserID() {
    return this.http.get(this.REST_API_SERVER + api + '/getSubscriptionForUser')
  }

  getWonAuctions(): Observable<AuctionPost[]> {
    return this.http.get<AuctionPost[]>(this.REST_API_SERVER + api + "/won");
  }

  getMyAuctions():Observable<AuctionPost[]>{
    return this.http.get<AuctionPost[]>(this.REST_API_SERVER+api+'/myAuctions')
  }

  getMySubscriptions():Observable<AuctionPost[]>{
    return this.http.get<AuctionPost[]>(this.REST_API_SERVER+api+'/mySubscriptions')
  }

  cancelAuction(auction: AuctionPost): Observable<AuctionPost> {
    return this.http.post<AuctionPost>(this.REST_API_SERVER + api + '/cancel', auction.id);
  }

  sendConfirmation(auctionPost: AuctionPost): Observable<AuctionPost>{
    return this.http.post<AuctionPost>(this.REST_API_SERVER + api + '/sendConfirmation', auctionPost);
  }
}
