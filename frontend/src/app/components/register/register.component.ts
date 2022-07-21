import { Component, OnInit } from '@angular/core';
import { Route } from '@angular/compiler/src/core';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { SigninComponent } from '../signin/signin.component';
import { RegisterService } from '../../services/register.service';
import { Address } from 'src/app/models/address';
import { ToastrService } from 'ngx-toastr';
import { CategoriesPickerComponent } from './categories-picker/categories-picker/categories-picker.component';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss'],
})
export class RegisterComponent implements OnInit  {

  model: any = {};
  address: Address = new Address();

  constructor(
      private registerService: RegisterService,
      private modalService: NgbModal,
      private toast: ToastrService) { }

  ngOnInit(): void {
    const togglePassword1 = document.querySelector('#togglePassword1');
    const toggleConfirmPassword1 = document.querySelector('#toggleConfirmPassword1');
    const password1 = document.querySelector('#password1');
    const confirmPassword1 = document.querySelector('#confirmPassword1');
    const togglePassword2 = document.querySelector('#togglePassword2');
    const toggleConfirmPassword2 = document.querySelector('#toggleConfirmPassword2');
    const password2 = document.querySelector('#password2');
    const confirmPassword2 = document.querySelector('#confirmPassword2');

    togglePassword1.addEventListener('click', function (e) {
      // toggle the type attribute
      const type = password1.getAttribute('type') === 'password' ? 'text' : 'password';
      password1.setAttribute('type', type);
      // toggle the eye slash icon
      this.classList.toggle('fa-eye-slash');
    });

    toggleConfirmPassword1.addEventListener('click', function (e) {
        // toggle the type attribute
        const type = confirmPassword1.getAttribute('type') === 'password' ? 'text' : 'password';
        confirmPassword1.setAttribute('type', type);
        // toggle the eye slash icon
        this.classList.toggle('fa-eye-slash');
    });

    togglePassword2.addEventListener('click', function (e) {
        // toggle the type attribute
        const type = password2.getAttribute('type') === 'password' ? 'text' : 'password';
        password2.setAttribute('type', type);
        // toggle the eye slash icon
        this.classList.toggle('fa-eye-slash');
      });
  
      toggleConfirmPassword2.addEventListener('click', function (e) {
          // toggle the type attribute
          const type = confirmPassword2.getAttribute('type') === 'password' ? 'text' : 'password';
          confirmPassword2.setAttribute('type', type);
          // toggle the eye slash icon
          this.classList.toggle('fa-eye-slash');
      });
  }

  onModalClose(){
    this,this.modalService.dismissAll();
  }

  openLoginModal() {
      this.modalService.dismissAll();
      this.modalService.open(SigninComponent);
  }

  openCategories() {
    let phoneNumber = +this.model.phoneNumber;
    let finalPhoneNumber = this.model.areaCode + phoneNumber;
    this.model.phoneNr = finalPhoneNumber;
    this.address.city = this.model.city;
    this.address.country = this.model.country;
    this.address.street = this.model.street;
    this.address.houseNr = this.model.houseNr;
    this.model.address = this.address;
    this.modalService.dismissAll();
    const component = this.modalService.open(CategoriesPickerComponent);
    component.componentInstance.user = this.model;
    component.componentInstance.address = this.address;
  }

  registerAuctionHouse() {
      let phoneNumber = +this.model.phoneNumber;
      let finalPhoneNumber = this.model.areaCode + phoneNumber;
      this.model.phoneNr = finalPhoneNumber;
      this.address.city = this.model.city;
      this.address.country = this.model.country;
      this.address.street = this.model.street;
      this.address.houseNr = this.model.houseNr;
      this.model.address = this.address;
      this.registerService.registerAuctionHouse(this.model)
          .subscribe(
              data => {
                  this.toast.success("User successfully registered")
                  this.openLoginModal();
              },
              error => {
                this.toast.error(error.error.message);
              });
  }
}
