import { Component, OnInit } from '@angular/core';
import {AuctionsService} from "../../../services/auction.service";
import {AuctionsSearchService} from "../../../services/auctions-search.service";

@Component({
  selector: 'app-auctions-search-form',
  templateUrl: './auctions-search-form.component.html',
  styleUrls: ['./auctions-search-form.component.scss']
})
export class AuctionsSearchFormComponent implements OnInit {

  constructor(
    private auctionsService: AuctionsService,
    private auctionsSearchService: AuctionsSearchService,
  ) { }

  categories: any = [];
  countries: any = [];
  searchInputText;

  ngOnInit(): void {
    this.auctionsService.getCategories().subscribe(categories => this.categories = categories);
    this.auctionsService.getCountriesWhereAuctionsExist().subscribe(countries => this.countries = countries);
  }

  capitalizeFirstLetter (str:string) {
    str = str.toLowerCase();
    return str.charAt(0).toUpperCase() + str.slice(1);
  }

  toggleCategoryCheckbox($event, category) {
    console.log($event.currentTarget.checked);
    console.log(JSON.stringify(category));

    // checked
    if ($event.currentTarget.checked) {
      this.auctionsSearchService.selectCategory(category);
    }
    // unchecked
    else {
      this.auctionsSearchService.deselectCategory(category)
    }
  }

  toggleCountryCheckbox($event, country) {
    console.log($event.currentTarget.checked);
    console.log(JSON.stringify(country));

    // checked
    if ($event.currentTarget.checked) {
      this.auctionsSearchService.selectCountry(country);
    }
    // unchecked
    else {
      this.auctionsSearchService.deselectCountry(country)
    }
  }

  processTextInput($event) {

    console.log("processing search query text");

    const searchKeys = this.searchInputText.toString().split(" ");
    this.auctionsSearchService.updateSearchKeys(searchKeys);

    // prevent default behaviour and do not "submit" form
    return false;
  }


}
