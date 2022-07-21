package at.ac.ase.service.bid;

import at.ac.ase.dto.AuctionPostSendDTO;
import at.ac.ase.dto.BidDTO;
import at.ac.ase.entities.AuctionPost;
import at.ac.ase.entities.Bid;
import at.ac.ase.entities.User;

import java.util.List;

public interface IBidService {

    /**
     * Method to tranform a {@link BidDTO} object to a {@link Bid}
     * @param bidDTO {@link BidDTO} object to be transformed
     *
     * @return converted {@link Bid}
     */
    Bid toBid(BidDTO bidDTO, User user);

    /**
     * Method to transform a {@link Bid} object to a {@link BidDTO}
     *
     * @param bid {@link Bid} object to be transformed
     * @return converted {@link BidDTO}
     */
    BidDTO toBidDTO(Bid bid);

    /**
     * Method to place a {@link Bid} for and auction.
     *
     * @param bid {@link Bid} object to be stored.
     */
    Bid placeBid(Bid bid);

    List<AuctionPostSendDTO> getMyBids(User user);

    List<AuctionPostSendDTO> getAuctionBids(AuctionPost post);


}
