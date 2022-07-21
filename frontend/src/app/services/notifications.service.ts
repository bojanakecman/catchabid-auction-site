import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Notification } from '../models/notification';

@Injectable({
  providedIn: 'root'
})
export class NotificationsService {

  private api = 'api/notifications';

  constructor(
    private http: HttpClient
  ) {
  }

  getNotifications():Observable<Notification[]> {
    return this.http.get<Notification[]>(this.api);
  }

  updateNotificationsSeen(notification: Notification): Observable<Object> {
    return this.http.put(this.api + '/' + notification.id, null);
  }
}
