import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { User } from '../models/user';
import { AuctionHouse } from '../models/auctionhouse';

@Injectable()
export class PasswordManagementService {
    constructor(private http: HttpClient) { }

    endpoint = 'http://localhost:8080';

    requestPasswordReset(email: String) {
        return this.http.post(this.endpoint + '/requestPasswordReset', email);
    }

    sendResetPasswordToken(email: String, token: Number) {
        return this.http.post(this.endpoint + '/sendResetPasswordToken', {email: email, token: token});
    }

    resetPassword(email: String, password:String){
        return this.http.post(this.endpoint + '/resetPassword', {email: email, password: password});
    }

}