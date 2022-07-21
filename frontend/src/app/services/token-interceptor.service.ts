import { Injectable } from '@angular/core';
import { HttpEvent, HttpInterceptor, HttpHandler, HttpRequest } from '@angular/common/http';
import {Observable} from 'rxjs';



@Injectable()
export class TokenInterceptor implements HttpInterceptor {

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {

    const authReq = req.clone({
      headers: req.headers.set('Content-Type', 'application/json')
        .set('Authorization', `Bearer ${localStorage.getItem('token')}`)
    });

    console.log('Intercepted HTTP call', authReq);

    return next.handle(authReq);
  }
}
