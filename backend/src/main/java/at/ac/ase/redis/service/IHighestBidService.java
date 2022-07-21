package at.ac.ase.redis.service;

import at.ac.ase.entities.AuctionPost;
import at.ac.ase.entities.Bid;
import at.ac.ase.redis.model.HighestBid;

public interface IHighestBidService {

    /**
     * Updates the highest bid in cache.
     *
     * @param bid {@link Bid} to be updated in cache
     */
    void updateHighestBid(Bid bid);

    /**
     * Get the highest bid for the given auction
     *
     * @param auctionPost {@link AuctionPost} which highest bid
     * should be returned
     *
     * @return {@link HighestBid} for the given {@link AuctionPost} if found,
     * otherwise null
     */
    HighestBid getHighestBid(AuctionPost auctionPost);

}
