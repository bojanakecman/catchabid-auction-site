import {Component, HostListener, OnInit} from '@angular/core';
import {TutorialService} from "../../services/tutorial.service";
import {NgbModal} from "@ng-bootstrap/ng-bootstrap";

@Component({
  selector: 'app-tutorial',
  templateUrl: './tutorial.component.html',
  styleUrls: ['./tutorial.component.css']
})
export class TutorialComponent implements OnInit {

  count = 1;

  constructor(private modalService: NgbModal) { }

  @HostListener('document:keydown', ['$event'])
  handleKeyboardEvent(event: KeyboardEvent): void {
    if (event.key === 'ArrowRight') {
      this.onNext();
    } else if (event.key === 'ArrowLeft') {
      this.onPrevious();
    }
  }

  ngOnInit(): void {
  }

  onModalClose(): void {
    this.modalService.dismissAll();
  }

  onNext(): void {
    if (this.count < 3) {
      this.count++;
    }
  }

  onPrevious(): void {
    if (this.count > 1) {
      this.count--;
    }
  }

}
