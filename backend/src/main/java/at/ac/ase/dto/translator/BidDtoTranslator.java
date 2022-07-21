package at.ac.ase.dto.translator;

import at.ac.ase.dto.BidDTO;
import at.ac.ase.entities.AuctionPost;
import at.ac.ase.entities.Bid;
import at.ac.ase.service.auction.IAuctionService;
import at.ac.ase.util.exceptions.ObjectNotFoundException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BidDtoTranslator {

    @Autowired
    private IAuctionService auctionService;

    public Bid toBid(BidDTO bidDTO) {
        Bid bid = new Bid();
        bid.setOffer(bidDTO.getOffer());
        bid.setDateTime(LocalDateTime.now(ZoneOffset.UTC));

        AuctionPost auction = auctionService.getAuctionPost(bidDTO.getAuctionId())
            .orElseThrow(ObjectNotFoundException::new);
        bid.setAuction(auction);

        return bid;
    }

    public BidDTO toBidDTO(Bid bid) {
        if (bid == null) {
            return null;
        }
        BidDTO bidDTO = new BidDTO();
        bidDTO.setAuctionId(bid.getAuction() != null ? bid.getAuction().getId() : null);
        bidDTO.setOffer(bid.getOffer());

        return bidDTO;
    }

}
