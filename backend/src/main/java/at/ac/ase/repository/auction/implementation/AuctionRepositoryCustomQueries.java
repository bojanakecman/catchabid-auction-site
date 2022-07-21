package at.ac.ase.repository.auction.implementation;

import at.ac.ase.entities.AuctionPost;
import at.ac.ase.repository.auction.AuctionPostQuery;

import java.util.List;

public interface AuctionRepositoryCustomQueries {
    List<AuctionPost> query(AuctionPostQuery auctionQuery);
}
