import {Component} from '@angular/core';
import {TutorialComponent} from "./components/tutorial/tutorial.component";
import {NgbModal} from "@ng-bootstrap/ng-bootstrap";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  title = 'Angular';

  constructor(
              private modalService: NgbModal) {
    if (localStorage.getItem('isJustRegistered') !== null) {
      localStorage.removeItem('isJustRegistered');
      this.modalService.open(TutorialComponent, { size: 'lg', backdrop: 'static' });
    }
  }
}
