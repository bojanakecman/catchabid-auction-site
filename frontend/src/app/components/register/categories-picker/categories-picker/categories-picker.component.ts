import { Component, Input, OnInit } from '@angular/core';
import { FormArray, FormBuilder, FormControl, FormGroup } from '@angular/forms';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ToastrService } from 'ngx-toastr';
import { of } from 'rxjs';
import { SigninComponent } from 'src/app/components/signin/signin.component';
import { Address } from 'src/app/models/address';
import { User } from 'src/app/models/user';
import { AuctionsService } from 'src/app/services/auction.service';
import { RegisterService } from 'src/app/services/register.service';

@Component({
  selector: 'app-categories-picker',
  templateUrl: './categories-picker.component.html',
  styleUrls: ['./categories-picker.component.scss']
})
export class CategoriesPickerComponent implements OnInit {

  form: FormGroup;
  preferences: any;

  @Input()
  public user: User;

  @Input()
  public address: Address;

  constructor(
    private modalService: NgbModal,
    private registerService: RegisterService,
    private toast: ToastrService,
    private formBuilder: FormBuilder,
    private auctionsService: AuctionsService,
  ) { }

  addCheckboxes(){
    this.preferences.forEach(() => this.ordersFormArray.push(new FormControl(false)))
  }

  getCategories(){

  }

  ngOnInit(): void {
    this.form = this.formBuilder.group({
      categories: new FormArray([])
   });
    this.auctionsService.getCategories().subscribe(x => {
      this.preferences = x;
      this.addCheckboxes();
    });
  }

  get ordersFormArray() {
    return this.form.controls.categories as FormArray;
  }



  openLoginModal() {
    this.modalService.dismissAll();
    this.modalService.open(SigninComponent);
}

  registerUser(){
    const selectedPreferences = this.form.value.categories
    .map((checked, i) => checked ? this.preferences[i] : null)
    .filter(v => v !== null);
    this.user.preferences = selectedPreferences;
    this.registerService.registerUser(this.user)
           .subscribe(
               data => {
                   this.toast.success("User successfully registered");
                   localStorage.setItem('isJustRegistered', 'true');
                   this.openLoginModal();
               },
               error => {
                 this.toast.error(error.error.message);
               });
  }

}
