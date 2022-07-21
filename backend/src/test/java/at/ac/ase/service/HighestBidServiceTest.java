package at.ac.ase.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import at.ac.ase.basetest.BaseIntegrationTest;
import at.ac.ase.entities.AuctionHouse;
import at.ac.ase.entities.AuctionPost;
import at.ac.ase.entities.Bid;
import at.ac.ase.entities.Category;
import at.ac.ase.entities.RegularUser;
import at.ac.ase.entities.Status;
import at.ac.ase.redis.model.HighestBid;
import at.ac.ase.redis.service.IHighestBidService;
import at.ac.ase.service.auction.IAuctionService;
import at.ac.ase.service.bid.IBidService;
import at.ac.ase.service.user.IAuctionHouseService;
import at.ac.ase.service.user.IRegularUserService;
import at.ac.ase.util.exceptions.AuctionExpiredException;
import java.time.LocalDateTime;
import org.junit.After;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

public class HighestBidServiceTest extends BaseIntegrationTest {

    @Autowired
    private IBidService bidService;

    @Autowired
    private IAuctionService auctionService;

    @Autowired
    private IRegularUserService regularUserService;

    @Autowired
    private IAuctionHouseService auctionHouseService;

    @Autowired
    private IHighestBidService highestBidService;


    @After
    public void cleanup() {
        cleanDatabase();
    }

    @Test
    public void testServiceNotNull() {
        assertNotNull(regularUserService);
        assertNotNull(auctionHouseService);
        assertNotNull(auctionService);
        assertNotNull(bidService);
        assertNotNull(highestBidService);

    }


    @Test
    @Transactional
    public void testUpdateHighestBidWithNewHigherBid() {
        insertTestData("users.sql");

        RegularUser user = regularUserService.getUserByEmail("test@test.com");
        assertNotNull(user);

        AuctionHouse auctionHouse = auctionHouseService.getAuctionHouseByEmail("test1@test.com");
        assertNotNull(auctionHouse);

        AuctionPost auctionPost = new AuctionPost();
        auctionPost.setStatus(Status.ACTIVE);
        auctionPost.setStartTime(LocalDateTime.now().minusHours(5));
        auctionPost.setEndTime(LocalDateTime.now().plusHours(2));
        auctionPost.setCategory(Category.CARS);
        auctionPost.setMinPrice(500.0);
        auctionPost.setCreator(auctionHouse);
        auctionPost.setName("Expired auction");
        auctionPost = auctionService.saveAuction(auctionPost);

        assertNotNull(auctionPost.getId());

        Bid bid1 = new Bid();
        bid1.setUser(user);
        bid1.setAuction(auctionPost);
        bid1.setDateTime(LocalDateTime.now());
        bid1.setOffer(600.0);
        bidService.placeBid(bid1);

        assertNotNull(bid1.getId());

        Bid bid2 = new Bid();
        bid2.setUser(user);
        bid2.setAuction(auctionPost);
        bid2.setDateTime(LocalDateTime.now());
        bid2.setOffer(700.0);
        bidService.placeBid(bid2);

        assertNotNull(bid2.getId());

        HighestBid highestBid = highestBidService.getHighestBid(auctionPost);
        // assertEquals(700, highestBid.getOffer(), 0);

    }




}
