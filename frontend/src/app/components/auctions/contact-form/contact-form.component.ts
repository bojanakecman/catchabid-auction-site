  import {Component, Input, OnInit} from '@angular/core';
  import {UserService} from '../../../services/user.service';
  import {JsonObject} from '@angular/compiler-cli/ngcc/src/packages/entry_point';
  import {AuctionsService} from '../../../services/auction.service';
  import {ContactForm} from '../../../models/contact-form';
  import {ToastrService} from "ngx-toastr";
  import {NgbModal} from "@ng-bootstrap/ng-bootstrap";

  @Component({
  selector: 'app-contact-form',
  templateUrl: './contact-form.component.html',
  styleUrls: ['./contact-form.component.css']
})
export class ContactFormComponent implements OnInit {

  @Input() public auction;
  @Input() public check;
  user: JsonObject;
  model: any = {};
  buttonDisable = false;

    constructor(private userService: UserService,
                private auctionService: AuctionsService,
                private toast: ToastrService,
                private modalService: NgbModal) { }

  ngOnInit(): void {
    console.log(this.auction);
    const userData = JSON.parse(atob(localStorage.getItem('token').split('.')[1]));
    console.log(userData.sub);

    if (this.check) {
      this.auctionService.getContactForm(this.auction).subscribe(
        contactForm => {
          this.model = {
            firstName: contactForm.firstName,
            lastName: contactForm.lastName,
            email: contactForm.email,
            // @ts-ignore
            country: contactForm.country,
            // @ts-ignore
            city: contactForm.city,
            // @ts-ignore
            street: contactForm.street,
            // @ts-ignore
            houseNr: contactForm.houseNr,
            phoneNumbe: contactForm.phoneNr,
            remark: contactForm.remark
          };
        },
        error => this.toast.error(error.error.message)
      );
    } else {
      this.userService.findByEmail(userData.sub).subscribe(
        user => {
          this.user = JSON.parse(JSON.stringify(user));
          this.model = {
            firstName: this.user.firstName,
            lastName: this.user.lastName,
            email: this.user.email,
            // @ts-ignore
            country: this.user.address.country,
            // @ts-ignore
            city: this.user.address.city,
            // @ts-ignore
            street: this.user.address.street,
            // @ts-ignore
            houseNr: this.user.address.houseNr,
            phoneNumbe: this.user.phoneNr
          };
        },
        error => this.toast.error(error.error.message)
    );
    }
  }

  submitContactForm(): void {
    this.buttonDisable = true;
    const contactForm = new ContactForm(this.auction.id, this.model.firstName,
      this.model.lastName, this.model.email, this.model.phoneNumbe, this.model.country, this.model.city,
      this.model.street, this.model.houseNr, this.model.remark);
    this.auctionService.postContactForm(contactForm).subscribe(contactForm => {
      this.modalService.dismissAll();
      this.toast.success('Contact form successfully submitted');
    }, error => {
      this.buttonDisable = false;
      this.toast.error(error.error.message);
    });
  }
}
