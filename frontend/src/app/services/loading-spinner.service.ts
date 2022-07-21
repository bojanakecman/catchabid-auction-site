import {Injectable} from "@angular/core";
import {NgxSpinnerService} from "ngx-spinner";


/**
 * Global Loading Spinner
 * if "show" is called multiple times from different components,
 * also "hide" has to be called multiple times to really hide the spinner
 */
@Injectable({
  providedIn: 'root'
})
export class LoadingSpinnerService {

  constructor(private spinner: NgxSpinnerService) {}
  private counter = 0;


  show() {
    console.log("SHOW: " + this.counter);
    if (this.counter == 0) {
      this.spinner.show();
    }
    this.counter++;
  }

  hide() {
    console.log("HIDE: " + this.counter);
    this.counter--;
    if (this.counter == 0) {
      this.spinner.hide();
    }
  }


}
