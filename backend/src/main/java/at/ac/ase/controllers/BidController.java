package at.ac.ase.controllers;

import at.ac.ase.dto.BidDTO;
import at.ac.ase.entities.Bid;
import at.ac.ase.entities.User;
import at.ac.ase.service.bid.IBidService;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bids")
public class BidController {

    private static final Logger logger = LoggerFactory.getLogger(BidController.class);

    @Autowired
    private IBidService bidService;

    @PostMapping
    public ResponseEntity<Object> placeBid(
        @RequestBody @Valid BidDTO bidDTO,
        @CurrentSecurityContext(expression = "authentication.principal") User user) {
        Bid bid = bidService.toBid(bidDTO, user);
        return ResponseEntity.ok(
            bidService.toBidDTO(
                bidService.placeBid(bid)));
    }

    @GetMapping(value = "/myBids")
    public ResponseEntity<Object> myBids(
            @CurrentSecurityContext(expression = "authentication.principal") User user) throws Exception {
        try {
            return ResponseEntity.ok(bidService.getMyBids(user));
        }catch(Exception e ){
            logger.error("Could not retrieve bids",e);
            throw new Exception("Could not retrieve bids");
        }
    }
}
