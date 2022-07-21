import { Component, OnInit, Input, ChangeDetectorRef } from '@angular/core';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { AuctionPost } from '../../models/auctionpost';
import { AuctionsService } from '../../services/auction.service';
import { BidsComponent } from '../bids/bids.component';
import { BidsService } from 'src/app/services/bids.service';
import { Subscription } from 'rxjs';
import { UserService } from 'src/app/services/user.service';
import { RatingComponent } from '../rating/rating/rating.component';
import {AuctionCancellationConfirmationComponent} from "./auction-cancellation-confirmation/auction-cancellation-confirmation.component";

@Component({
  selector: 'app-auction-details',
  templateUrl: './auction-details.component.html',
  styleUrls: ['./auction-details.component.scss']
})
export class AuctionDetailsComponent implements OnInit {

  @Input()
  public auction: AuctionPost;

  @Input()
  public path: string;

  modalRef: NgbModalRef;

  bidModalClosedSub: Subscription;

  rating: number;

  alreadyRated: boolean;

  constructor(
    private modalService : NgbModal,
    private auctionService: AuctionsService,
    private bidsService: BidsService,
    private userService: UserService,
    private cdRef: ChangeDetectorRef) {
     }

  ngOnInit(): void {
    this.calculateDateDiff();
    this.userService.calculateRating(this.auction.creatorEmail).subscribe(x => this.rating = x);
    this.userService.checkIfUserAlreadyRated(this.auction).subscribe(data => {
      this.alreadyRated = false;
    }, error => {
      this.alreadyRated = true;
    });
  }



  onModalClose(): void {
    this.modalService.dismissAll();
  }

  openBidModal(auction: AuctionPost): void {
    this.modalRef = this.modalService.open(BidsComponent);
    this.modalRef.componentInstance.auction = auction;

    this.bidModalClosedSub = this.bidsService.bidModalClosed.subscribe(
      () => this.onBidModalClose()
    );
  }

  openRatingModal(auction: AuctionPost): void {
    this.modalRef = this.modalService.open(RatingComponent);
    this.modalRef.componentInstance.auctionPost = auction;
  }

  onBidModalClose(): void {
    this.modalRef.close();
    this.bidModalClosedSub.unsubscribe();
  }

  isAuctionEnded(): boolean {
    const endDate = new Date(this.convertUTCDatetimeToLocal(this.auction.endTime));
    return endDate.getTime() < new Date().getTime();
  }

  calculateDateDiff() : boolean{
    const now = new Date();
    console.log(now);
    const date = new Date(this.convertUTCDatetimeToLocal(this.auction.endTime));
    console.log(date);
    const diffTime = Math.abs(now.getTime() - date.getTime());
    const diffDays = Math.round(diffTime / (1000 * 60 * 60 * 24));
    if (diffDays < 30) {
      return true;
    } else {
      return false;
    }
  }

  cancelAuction(): void {
    this.modalRef = this.modalService.open(AuctionCancellationConfirmationComponent);

    this.modalRef.result.then((result) => {
        if (result) {
          console.log("cancel auction");
          this.auctionService.cancelAuction(this.auction).subscribe(contactForm => {
            this.auction.status = 'CANCELLED';
            console.log("auction cancelled successfully");
          }, error => {
            console.log("auction cancellation error")
          });
        }
      }
    );
  }

  isOwnAuction(): boolean {
    return this.auction.creatorId.toString() == localStorage.getItem('userId');
  }

  isActive(): boolean {
    return this.auction.status == 'ACTIVE';
  }

  isUpcoming(): boolean {
    return this.auction.status == 'UPCOMING';
  }

  isRatingSet(){
    if(!isNaN(this.rating)){
      return true;
    }else{
      return false;
    }
  }

  convertUTCDatetimeToLocal(date){
    const dateUTC = new Date(date);
    const dateLocal = new Date(Date.UTC(dateUTC.getFullYear(),dateUTC.getMonth(), dateUTC.getDate() ,
    dateUTC.getHours(), dateUTC.getMinutes(), dateUTC.getSeconds(), dateUTC.getMilliseconds()));
    return dateLocal;
  }
}
