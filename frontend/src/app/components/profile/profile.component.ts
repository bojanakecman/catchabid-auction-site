import {Component, Input, OnInit} from '@angular/core';
import {User} from "../../models/user";
import {AuctionHouse} from "../../models/auctionhouse";
import {UserService} from "../../services/user.service";
import {ToastrService} from "ngx-toastr";
import {AuctionsService} from "../../services/auction.service";

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit {
  updatedUser:User | AuctionHouse;
  isRegularUser:boolean=true;
  dropdownList:any = [];
  selectedPreferences:[]= [];
  dropdownSettings = {
    selectAllText: 'Select all',
    unSelectAllText: 'Unselect all'
  };


  constructor(
    private toast: ToastrService,
    private userService: UserService,
    private auctionsService: AuctionsService,
    private router: Router,
    private modalService: NgbModal) {
    this.ngOnInit();
  }

  ngOnInit(): void {
    let email = localStorage.getItem('email')
    console.log(email)
    this.updatedUser=new User();
    this.updatedUser.address=new Address();

    this.userService.findByEmail(email).subscribe(
      fetcheduser => {
        this.updatedUser=fetcheduser;

        if(this.updatedUser['firstName'] == undefined && this.updatedUser['lastName'] == undefined){
          this.isRegularUser=false;
          console.log("to false")
        }else {
          this.isRegularUser=true;
          this.selectedPreferences = (<User>this.updatedUser).preferences;
          console.log("to true")
        }
      },
      error => this.toast.error(error.error.message)
    )
    this.auctionsService.getCategories().subscribe(x => {

      console.log("x",x)
      this.dropdownList = x;
      console.log("dropL", this.dropdownList)
    });
  }

  save(){
    if (this.isRegularUser){
      console.log("user")
      console.log(this.updatedUser);
      console.log(this.selectedPreferences);
      (<User>this.updatedUser).preferences = this.selectedPreferences;
      this.userService.updateUser(localStorage.getItem("email"),<User>this.updatedUser) .subscribe(
        data => {
          this.toast.success("User successfully updated")
          localStorage.setItem("email",this.updatedUser.email)
          let userName = this.updatedUser['firstName'].concat(" ", this.updatedUser['lastName']);
          localStorage.setItem('userName', userName);
          this.router.navigate(['']);
        },
        error => {
          this.toast.error(error.error.message);
        });
      console.log("user")
    }else {
      this.userService.updateHouse(this.updatedUser.email, <AuctionHouse>this.updatedUser) .subscribe(
        data => {
          this.toast.success("Auction House successfully updated")
        },
        error => {
          this.toast.error(error.error.message);
        });
      console.log("house")
    }
    console.log("Updated")
  }

  confirmSave() {
    this.modalService.open(NgbdModalConfirm).result.then((userResponse) => {
      if (userResponse) {
        this.save();
      }
    });
  }


}

import {NgbActiveModal, NgbModal, NgbModalRef} from '@ng-bootstrap/ng-bootstrap';
import { Address } from 'src/app/models/address';
import { Router } from '@angular/router';

@Component({
  selector: 'ngbd-modal-confirm',
  template: `
  <div class="modal-header">
    <h4 class="modal-title" id="modal-title">Profile update</h4>
    <button type="button" class="close" aria-label="Close button" aria-describedby="modal-title" (click)="modal.dismiss('Cross click')">
      <span aria-hidden="true">&times;</span>
    </button>
  </div>
  <div class="modal-body">
    <p><strong>Are you sure you want to update your information?</strong></p>
    <p>All information associated to this user profile will be permanently changed.
    </p>
  </div>
  <div class="modal-footer">
    <button type="btn btn-primary" class="btn btn-outline-secondary" (click)="modal.dismiss(false)">Cancel</button>
    <button type="submit" ngbAutofocus class="btn btn-danger" (click)="modal.close(true)">Ok</button>
  </div>
  `
})
export class NgbdModalConfirm {
  constructor(public modal: NgbActiveModal) {}
}

