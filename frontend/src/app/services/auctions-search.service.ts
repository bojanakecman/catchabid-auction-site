import {Injectable} from "@angular/core";
import {BehaviorSubject} from "rxjs";
import {AuctionSearchQuery} from "../models/auctionSearchQuery";


@Injectable({
  providedIn: 'root'
})
export class AuctionsSearchService {

  private auctionsQuerySource = new BehaviorSubject<AuctionSearchQuery>(new AuctionSearchQuery());
  auctionsQuery = this.auctionsQuerySource.asObservable();

  updateSearchKeys(newSearchKeys:string[]) {
    const newQuery = this.cloneCurrentQuery();
    newQuery.searchKeys = Array.from(newSearchKeys);
    this.auctionsQuerySource.next(newQuery);
  }

  updateSelectedCategories(newCategories:string[]) {
    const newQuery = this.cloneCurrentQuery();
    newQuery.categories = Array.from(newCategories);
    this.auctionsQuerySource.next(newQuery);
  }

  updateSelectedCountries(newCountries:string[]) {
    const newQuery = this.cloneCurrentQuery();
    newQuery.countries = Array.from(newCountries);
    this.auctionsQuerySource.next(newQuery);
  }

  selectCategory(category:string) {
    let categories:string[] = Array.from(this.auctionsQuerySource.getValue().categories);
    if (!categories.includes(category)) {
      categories.push(category);
    }
    this.updateSelectedCategories(categories);
  }

  deselectCategory(category:string) {
    let categories:string[] = Array.from(this.auctionsQuerySource.getValue().categories);
    const index = categories.indexOf(category);
    if (index > -1) {
      categories.splice(index, 1);
    }
    this.updateSelectedCategories(categories);
  }

  selectCountry(country:string) {
    let countries:string[] = Array.from(this.auctionsQuerySource.getValue().countries);
    if (!countries.includes(country)) {
      countries.push(country);
    }
    this.updateSelectedCountries(countries);
  }

  deselectCountry(country:string) {
    let countries:string[] = Array.from(this.auctionsQuerySource.getValue().countries);
    const index = countries.indexOf(country);
    if (index > -1) {
      countries.splice(index, 1);
    }
    this.updateSelectedCountries(countries);
  }

  private cloneCurrentQuery() {
    const query = this.auctionsQuerySource.getValue();
    const newQuery = new AuctionSearchQuery();
    newQuery.categories = Array.from(query.categories);
    newQuery.countries  = Array.from(query.countries);
    newQuery.searchKeys = Array.from(query.searchKeys);
    return newQuery;
  }
}
