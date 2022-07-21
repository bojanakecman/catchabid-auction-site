export class ContactForm {
  id: number;
  auctionPostId: number;
  firstName: string;
  lastName: string;
  email: string;
  phoneNr: string;
  country: string;
  city: string;
  street: string;
  houseNr: number;
  remark: string;

  constructor(auctionPostId: number, firstName: string, lastName: string, email: string,
              phoneNr: string, country: string, city: string, street: string, houseNr: number, remark: string) {
    this.auctionPostId = auctionPostId;
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;
    this.phoneNr = phoneNr;
    this.country = country;
    this.city = city;
    this.street = street;
    this.houseNr = houseNr;
    this.remark = remark;
  }
}

