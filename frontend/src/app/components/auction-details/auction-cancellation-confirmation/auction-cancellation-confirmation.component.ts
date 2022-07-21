import {Component, OnInit} from '@angular/core';
import {NgbActiveModal} from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'app-auction-cancellation-confirmation',
  templateUrl: './auction-cancellation-confirmation.component.html',
  styleUrls: ['./auction-cancellation-confirmation.component.css']
})
export class AuctionCancellationConfirmationComponent implements OnInit {

  constructor(public activeModal: NgbActiveModal) { }

  ngOnInit(): void {
  }

  confirmCancellation(confirmed: boolean): void {
    this.activeModal.close(confirmed);
  }

}
