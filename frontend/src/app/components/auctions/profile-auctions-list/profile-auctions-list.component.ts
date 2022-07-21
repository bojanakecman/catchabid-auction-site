import { Component, OnInit, Input } from '@angular/core';
import { AuctionsService } from 'src/app/services/auction.service';
import { User } from 'src/app/models/user';
import { AuctionPost } from 'src/app/models/auctionpost';
import { NgbModalRef, NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { PaymentsComponent } from '../../payments/payments.component';
import { PaymentsService } from 'src/app/services/payments.service';
import { Subscription } from 'rxjs';
import { AuctionDetailsComponent } from '../../auction-details/auction-details.component';
import {BidsService} from "../../../services/bids.service";
import {ContactFormComponent} from "../contact-form/contact-form.component";
import {ToastrService} from "ngx-toastr";

@Component({
  selector: 'app-profile-auctions-list',
  templateUrl: './profile-auctions-list.component.html',
  styleUrls: ['./profile-auctions-list.component.css']
})
export class ProfileAuctionsListComponent implements OnInit {

  @Input() user: User;

  wonAuctions: AuctionPost[];

  @Input()
  typeOfAuctions:string;

  path: string;

  modalRef: NgbModalRef;

  paymentModalClosedSub: Subscription;

  auctionDetailsModalClosedSub: Subscription;

  contactFormModalClosedSub: Subscription;


  constructor(
    private auctionService: AuctionsService,
    private bidsService: BidsService,
    private modalService: NgbModal,
    private paymentService: PaymentsService,
    private toast: ToastrService
  ) { }

  ngOnInit(): void {
    console.log('ok');
    if (this.typeOfAuctions=='WINS') {
      this.getWonAuctions();
      this.path="wins";
    }
    if (this.typeOfAuctions=='BIDS'){
    this.getMyBids();
    this.path="bids";

    }
    if (this.typeOfAuctions=='AUCTIONS'){
      this.getMyAuctions();
      this.path="auctions";
    }
    if (this.typeOfAuctions=='SUBSCRIPTIONS'){
      this.getSubscriptions();
      this.path="subscriptions";
    }

  }

  private getWonAuctions() {
    this.auctionService.getWonAuctions().subscribe(
      (res) => {
        this.wonAuctions = res
      },
      (err) => console.log(err)
    )
  }

  private getMyAuctions() {
    this.auctionService.getMyAuctions().subscribe(
      (res) => {
        this.wonAuctions = res
        console.log(this.wonAuctions)
      },
      (err) => console.log(err)
    )
  }
  private getSubscriptions() {
    this.auctionService.getMySubscriptions().subscribe(
      (res) => {
        this.wonAuctions = res
      },
      (err) => console.log(err)
    )
  }
  private getMyBids() {
    this.bidsService.getMyBids().subscribe(
      (res) => {
        this.wonAuctions = res
      },
      (err) => console.log(err)
    )
  }


  openContactFormModal(auction: AuctionPost, check?: boolean): void {
    this.modalRef = this.modalService.open(ContactFormComponent);
    this.modalRef.componentInstance.auction = auction;
    if (check) {
      this.modalRef.componentInstance.check = true;
    }


    this.contactFormModalClosedSub = this.auctionService.contactFormModalClosed.subscribe(
      () => this.onContactFormClosed()
    );
  }

  onContactFormClosed(): void {
    this.modalRef.close();
    this.contactFormModalClosedSub.unsubscribe();
    this.getWonAuctions();
  }

  openPaymentModal(auction: AuctionPost): void {
    this.modalRef = this.modalService.open(PaymentsComponent);
    this.modalRef.componentInstance.auction = auction;

    this.paymentModalClosedSub = this.paymentService.paymentModalClosed.subscribe(
      () => this.onPaymentModalClosed()
    );
  }

  onPaymentModalClosed(): void {
    this.modalRef.close();
    this.paymentModalClosedSub.unsubscribe();
    this.getWonAuctions();
  }

  openAuctionDetails(auction: AuctionPost, type: string): void{
    const modal = this.modalService.open(AuctionDetailsComponent,  { size: 'lg', backdrop: 'static' });
    modal.componentInstance.auction = auction;
    modal.componentInstance.path = this.path;
    this.auctionDetailsModalClosedSub = this.paymentService.auctionDetailModalClosed.subscribe(
      () => this.onAuctionDetailsModalClosed()
    )
  }

  onAuctionDetailsModalClosed(): void {
    this.modalRef.close();
    this.auctionDetailsModalClosedSub.unsubscribe();
  }


  sendConfirmation(auction: AuctionPost): void {
    this.auctionService.sendConfirmation(auction).subscribe(contactForm => {
      this.toast.success('Contact form successfully submitted');
    }, error => {
      this.toast.error(error.error.message);
    });
  }

  convertUTCDatetimeToLocal(date){
    const dateUTC = new Date(date);
    const dateLocal = new Date(Date.UTC(dateUTC.getFullYear(),dateUTC.getMonth(), dateUTC.getDate() ,
    dateUTC.getHours(), dateUTC.getMinutes(), dateUTC.getSeconds(), dateUTC.getMilliseconds()));
    return dateLocal;
  }
}
