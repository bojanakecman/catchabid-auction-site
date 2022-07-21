import { Component, Input, OnInit } from '@angular/core';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ToastrService } from 'ngx-toastr';
import { AuctionPost } from 'src/app/models/auctionpost';
import { RatingData } from 'src/app/models/ratingdata';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'app-rating',
  templateUrl: './rating.component.html',
  styleUrls: ['./rating.component.scss']
})
export class RatingComponent implements OnInit {

  @Input() 
  public auctionPost: AuctionPost;
  public currentRate: number;

  constructor(private modalService: NgbModal,
    private toast: ToastrService,
    private userService: UserService
    ) { }

  ngOnInit(): void {

  }

  setRating(){
    if (this.currentRate) {
      const ratingData = new RatingData(this.auctionPost, this.currentRate);
      this.userService.setRating(ratingData).subscribe(x =>
        {
          this.onModalClose();
          this.toast.success("User successfully rated");
        },
        error => {
          this.toast.error(error.error.message);
        });
    } else {
      this.toast.warning("Empty rating can not be submitted");
    }
  }
  
  onModalClose(){
    this,this.modalService.dismissAll();
  }

}
