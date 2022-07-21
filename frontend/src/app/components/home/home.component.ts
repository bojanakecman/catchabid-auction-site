import { Component, OnInit } from '@angular/core';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import {RegisterComponent} from "../register/register.component";

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {

  constructor(
    private modalService: NgbModal,
  ) { }

  activeAuctionsCount = -1;
  upcomingAuctionsCount = -1;
  searchFormVisibilityOnMobileDevice:boolean = false;

  ngOnInit(): void {
  }

  toggleSeachFormVisibility() {
    this.searchFormVisibilityOnMobileDevice = !this.searchFormVisibilityOnMobileDevice;
  }

  openRegisterModal(): void {
    this.modalService.open(RegisterComponent);
  }

  isUserLoggedIn(){
    if(localStorage.getItem('token') !== null){
      return true;
    }else{
      return false;
    }
  }

  setAuctionsCount(count, type) {
    console.log("setAuctionsCount " + " to " + count);
    if(type == 'upcoming') {
      this.upcomingAuctionsCount = count;
    }
    else {
      this.activeAuctionsCount = count;
    }
  }
}
