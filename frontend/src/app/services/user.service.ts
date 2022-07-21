import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { User } from '../models/user';
import { Observable } from 'rxjs';
import { AuctionHouse } from '../models/auctionhouse';
import { RatingData } from '../models/ratingdata';
import { AuctionPost } from '../models/auctionpost';

const endpoint = 'http://localhost:8080';

@Injectable()
export class UserService {
    constructor(private http: HttpClient) { }

    findByEmail(email: string) : Observable<User|AuctionHouse>{
        let params = new HttpParams().set("email", email);
        return this.http.get<User|AuctionHouse>(endpoint + '/getUser', {params: params});
    }

    calculateRating(email: string) : any {
        let params = new HttpParams().set("email", email);
        return this.http.get(endpoint + '/calculateRating', {params: params})
    }

    setRating(ratingData: RatingData) : any {
        return this.http.post<RatingData>(endpoint + '/setRating', ratingData);
    }

  updateUser(email:string, user: User) {
    let params = new HttpParams().set("email", email);
    return this.http.post(endpoint + '/updateUser', user,{params})
  }

  updateHouse(email:string, auctionHouse: AuctionHouse) {
    let params = new HttpParams().set("email", email);
    return this.http.post(endpoint + '/updateHouse', auctionHouse,{params})
  }

  checkIfUserAlreadyRated(auction: AuctionPost){
    return this.http.post(endpoint + '/checkRated', auction);
  }
}
