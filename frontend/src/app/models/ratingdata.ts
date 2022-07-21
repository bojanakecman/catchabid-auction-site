import { AuctionPost } from "./auctionpost";

export class RatingData {
    auctionPost: AuctionPost;
    ratingValue: number;

    constructor(auctionPost: AuctionPost, ratingValue: number){
        this.auctionPost = auctionPost;
        this.ratingValue = ratingValue;
    }
}