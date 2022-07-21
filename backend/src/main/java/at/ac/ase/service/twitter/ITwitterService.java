package at.ac.ase.service.twitter;

import at.ac.ase.entities.AuctionPost;
import at.ac.ase.entities.TwitterPostType;

public interface ITwitterService {
    void tweetAuction(AuctionPost auction, TwitterPostType tweetType);
}
