import {Component, Input, OnInit} from '@angular/core';

@Component({
  selector: 'app-auction-countdown',
  templateUrl: './auction-countdown.component.html',
  styleUrls: ['./auction-countdown.component.scss']
})
export class AuctionCountdownComponent implements OnInit {

  @Input() countdownType: string;
  @Input() endDate: string;
  counterEnded: boolean = false;

  constructor() { }

  ngOnInit(): void { }

  timeDiffInSeconds() {
    const now = new Date();

    const endDateDateUTC = new Date(this.endDate);
    const endDateDateLocal = new Date(Date.UTC(endDateDateUTC.getFullYear(),endDateDateUTC.getMonth(), endDateDateUTC.getDate() ,
      endDateDateUTC.getHours(), endDateDateUTC.getMinutes(), endDateDateUTC.getSeconds(), endDateDateUTC.getMilliseconds()));

    return (endDateDateLocal.getTime() - now.getTime()) / 1000;
  }

  timeDiffInDays() {
    return this.timeDiffInSeconds() / (3600 * 24);
  }

  countdownInDaysString() {
    const daysUntilEndDate = this.timeDiffInDays();
    if (daysUntilEndDate >= 2) {
      return Math.floor(daysUntilEndDate).toString().concat(' days');
    }
    return '1 day';
  }

  counterFinishedCallback(event) {
    if(event.action == "done") {
      this.counterEnded = true;
    }
  }

  countdownConfig() {
    const diffSeconds = this.timeDiffInSeconds();
    let cformat = "m'm'";

    if((diffSeconds / 60) < 10) { // < 10m
      cformat = "m'm' s's'"
    }
    else if((diffSeconds / (60 * 60)) > 1) { // > 1h
      cformat = "H'h' m'm'";
    }
      return  {
      leftTime: this.timeDiffInSeconds(),
      format: cformat
    };
  }
}
