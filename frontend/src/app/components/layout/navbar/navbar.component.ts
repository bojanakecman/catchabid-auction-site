import { Component, OnInit } from '@angular/core';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { SigninComponent } from '../../signin/signin.component';
import { RegisterComponent } from '../../register/register.component';
import { Subscription } from 'rxjs';
import { AuctionFormComponent } from '../../auctions/auction-form/auction-form.component';
import { Router } from '@angular/router';
import {AuctionsService} from '../../../services/auction.service';
import {AuctionsSearchService} from "../../../services/auctions-search.service";
import { NotificationsService } from 'src/app/services/notifications.service';
import * as Stomp from 'stompjs';
import { Notification } from 'src/app/models/notification';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.scss']
})
export class NavbarComponent implements OnInit {

  public focus;
  public activeModal;

  closeResult = '';

  auctionClosedSub: Subscription;

  modalRef: NgbModalRef;

  userName: any;

  searchInputText: string;

  webSocket: WebSocket;
  client: Stomp.Client;

  notifications: Notification[] = [];

  email: string;

  notficationsOpened: boolean;

  constructor(
    private modalService: NgbModal,
    private auctionsService: AuctionsService,
    private router: Router,
    private auctionsSearchService: AuctionsSearchService,
    private notificationsService: NotificationsService,
  ) { }

  ngOnInit(): void {
    this.email = localStorage.getItem('email');
    this.notficationsOpened = false;
    this.loadNotifications();
    this.webSocket = new WebSocket('ws://localhost:8080/notification');
    this.client = Stomp.over(this.webSocket);
    this.openWebSocket();
  }

  openWebSocket() {
    if (this.email) {
      this.client.connect({
      }, () => {
        this.client.subscribe('/topic/notification/' + this.email,
        (notRes: any) => {
          console.log(notRes)
          const notJSON = JSON.parse(notRes.body)
          const newNotification: Notification = {
            id: notJSON.id,
            info: notJSON.info,
            seen: notJSON.seen,
            date: notJSON.date
          }
          this.notifications.push(newNotification);
          this.notifications.sort((a, b) => (a.id < b.id) ? 1 : -1);
        },
        error => console.log(error));
      });
    } else {
      console.log('Unable to subscribe to notifications');
    }
  }

  openLoginModal(): void {
    this.modalService.open(SigninComponent);
  }

  openRegisterModal(): void {
    this.modalService.open(RegisterComponent);
  }


  createAuction(): void {
    this.modalRef = this.modalService.open(AuctionFormComponent, { size: 'lg', backdrop: 'static' });
    this.auctionClosedSub = this.auctionsService.auctionFormModalClosed.subscribe(
      () => this.onAuctionFormClose()
    );
  }

  onAuctionFormClose(): void {
    this.modalRef.close();
    this.auctionClosedSub.unsubscribe();
  }

  logout() {
    localStorage.removeItem('token');
    localStorage.removeItem('userName');
    localStorage.removeItem('email');
    this.userName = null;
    this.router.navigate(['']);
  }

  toUserSettings(){
    this.router.navigate(['/profile']);
  }

  isUserLoggedIn(){
    if(localStorage.getItem('token') !== null){
      this.userName = localStorage.getItem('userName');
      return true;
    }else{
      return false;
    }
  }

  navigateHome(){
    this.router.navigate(['']);
  }

  processTextInput($event) {
    console.log("processing search query text");
    const searchKeys = this.searchInputText.toString().split(" ");
    this.auctionsSearchService.updateSearchKeys(searchKeys);

    // prevent default behaviour and do not "submit" form
    return false;
  }

  clearTestInput() {
    this.searchInputText = "";
    this.auctionsSearchService.updateSearchKeys([]);
  }

  private loadNotifications() {
    this.notificationsService.getNotifications().subscribe(
      (res) => {
        if (res) {
          res.forEach(n => this.notifications.push(n));
          this.notifications.sort((a, b) => (a.id < b.id) ? 1 : -1);
        }
      },
      (err) => console.log(err)
    )
  }

  checkIfSomeUnseen(): boolean {
    if (this.notifications) {
      return this.notifications.some(n => !n.seen);
    } else {
      return false;
    }
  }

  onNotificationsToggle() {
    this.notficationsOpened = !this.notficationsOpened;
    if (!this.notficationsOpened) {
      console.log(this.notifications);
      this.notifications.forEach((n, index) => {
        if (!n.seen) {
          n.seen = true;

          this.notificationsService.updateNotificationsSeen(n).subscribe(
            () => {
              console.log('Notification with info ', n.info, ' successfully updated');
            },
            () => {
              console.log('Error by updating notifications');
            });
        }
      });
    }
  }

}
