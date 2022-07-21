import {Component, OnInit, ViewChild} from '@angular/core';
import {AbstractControl, FormControl, FormGroup, ValidatorFn, Validators} from '@angular/forms';
import {ToastrService} from 'ngx-toastr';
import {NgbDate, NgbModule, NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {Observable, Subject} from "rxjs";
import {AuctionsService} from '../../../services/auction.service';
import { AuctionPost } from '../../../models/auctionpost';

@Component({
  selector: 'app-auction-form',
  templateUrl: './auction-form.component.html',
  styleUrls: ['./auction-form.component.scss']
})
export class AuctionFormComponent implements OnInit {

  image = 'Choose file';
  imgPreview: any;
  imageFile: string;
  nameForm = new FormControl('', [Validators.required]);
  categoryForm = new FormControl('', [Validators.required]);
  startDateForm = new FormControl('', [Validators.required]);
  startTimeForm = new FormControl('', [Validators.required]);
  endDateForm = new FormControl('', [Validators.required]);
  endTimeForm = new FormControl('', [Validators.required, Validators.min(0)]);
  countryForm = new FormControl();
  cityForm = new FormControl();
  addressForm = new FormControl();
  houseNrForm = new FormControl('', [Validators.pattern('^[0-9]*$'),
    Validators.min(0)]);
  priceForm = new FormControl('', [Validators.required,  Validators.pattern('^[0-9]*$'),
    Validators.min(0)]);
  descriptionForm = new FormControl();
  imageForm = new FormControl('', [Validators.required]);
  form = new FormGroup({
    name: this.nameForm,
    category: this.categoryForm,
    startDate: this.startDateForm,
    startTime: this.startTimeForm,
    endDate: this.endDateForm,
    endTime: this.endTimeForm,
    country: this.countryForm,
    city: this.cityForm,
    address: this.addressForm,
    houseNr: this.houseNrForm,
    minPrice: this.priceForm,
    description: this.descriptionForm,
    image: this.imageForm
  });
  categories: any;
  currentDate = new Date();
  minStartDate = {
    year: this.currentDate.getFullYear(),
    month: this.currentDate.getMonth() + 1,
    day: this.currentDate.getDate()
  };
  minEndDate = this.minStartDate;
  buttonDisable = false;

  constructor(private auctionsService: AuctionsService, private toast: ToastrService, private modalService: NgbModal) { }

  ngOnInit(): void {
    this.imgPreview = 'assets/img/placeholder-image-300x225.png';
    this.auctionsService.getCategories().subscribe(x => this.categories = x);
  }

  inputImage($event: Event): void {
    const file = ($event.target as HTMLInputElement).files[0];
    this.getBase64(file);
    this.image = file.name;
  }

  onModalClose(): void {
    this.auctionsService.auctionFormModalClosed.next();
  }

  getBase64(file: File): void {
    const reader = new FileReader();
    reader.readAsDataURL(file);
    reader.onload = (file64: any) => {
      this.imageFile = file64.target.result.split(',')[1];
      this.imgPreview = 'data:image/png;base64,' + this.imageFile;
    };
  }

  dateFormToValue(formDateValue: any, formTimeValue?: any): Date {
    if (formTimeValue) {
      return new Date(formDateValue.year, formDateValue.month - 1,
        formDateValue.day, formTimeValue.hour, formTimeValue.minute);
    } else {
      return new Date(formDateValue.year, formDateValue.month - 1,
        formDateValue.day);
    }
  }

  saveAuction(): void{
    if (!this.form.invalid) {
      const startDate = this.dateFormToValue(this.startDateForm.value, this.startTimeForm.value);

      const endDate = this.dateFormToValue(this.endDateForm.value, this.endTimeForm.value);
      const now = new Date();
      now.setMinutes(now.getMinutes() + 15);
      if (startDate.getTime() < now.getTime()) {
        this.toast.error('Auction must start at least 15 minutes from now');
      } else if ((new Date(startDate.getTime() + (15 * 60 * 1000))).getTime() > endDate.getTime()) {
        this.toast.error('Auction must last at least 15 minutes');
      } else if (startDate.getTime() < (new Date(endDate.getTime() - (24 * 60 * 60 * 1000))).getTime()) {
        this.toast.error('Auction can last for maximum 24 hours');
      } else {
        this.buttonDisable = true;
        const userData = JSON.parse(atob(localStorage.getItem('token').split('.')[1]));

        const auctionPost = new AuctionPost(null, this.nameForm.value, this.categoryForm.value.toUpperCase(),
          new Date(startDate), new Date(endDate), this.countryForm.value, this.cityForm.value, this.addressForm.value,
          this.houseNrForm.value, this.priceForm.value, this.descriptionForm.value, this.imageFile);

        this.auctionsService.saveAuction(auctionPost).subscribe(post => {
          this.toast.success('Auction successfully saved');
          this.onModalClose();
          window.location.reload();
        }, error => {
          this.buttonDisable = false;
          this.toast.error('Failed to save the auction');
        });
      }
    } else {
      Object.keys(this.form.controls).forEach(key => {
        if (this.form.get(key).status === 'INVALID') {
          this.toast.error('Format of ' + key + ' is invalid');
        }
      });
    }
  }

  setMaxEndDate(): void {
    this.minEndDate = this.startDateForm.value;
  }
}
