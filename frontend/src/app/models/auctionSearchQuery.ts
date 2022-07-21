export class AuctionSearchQuery {
  categories: string[] = [];
  searchKeys: string[] = [];
  countries: string[] = [];
  status: string[] = [];
  auctionsStartFrom: Date;
  auctionsStartUntil: Date;
  auctionsEndFrom : Date;
  auctionsEndUntil: Date;
  pageNumber: number;
  pageSize: number;
  sortBy: string;
  sortOrder: string;
  userEmail: string;
  useUserPreferences:boolean;

  equals (otherQuery: AuctionSearchQuery) {
    if (otherQuery == null) return false;
    let s0 = otherQuery.categories.sort().join() + otherQuery.searchKeys.sort().join() + otherQuery.countries.sort().join();
    let s1 = this.categories.sort().join() + this.searchKeys.sort().join() + this.countries.sort().join();

    // console.log("s0: " + s0);
    // console.log("s1: " + s1);

    return s0 == s1;
  }
}
