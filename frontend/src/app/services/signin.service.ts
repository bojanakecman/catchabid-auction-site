import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { AuctionHouse } from '../models/auctionhouse';
import { User } from '../models/user';

const endpoint = 'http://localhost:8080';

@Injectable()
export class SigninService {
    constructor(private http: HttpClient) { }

    login(email: String, password: String) {
        return this.http.post(endpoint + '/login', {email: email, password: password});
    }
}
