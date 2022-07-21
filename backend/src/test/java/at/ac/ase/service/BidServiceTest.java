package at.ac.ase.service;

import static org.junit.Assert.assertNotNull;

import at.ac.ase.basetest.BaseIntegrationTest;
import at.ac.ase.entities.AuctionHouse;
import at.ac.ase.entities.AuctionPost;
import at.ac.ase.entities.Bid;
import at.ac.ase.entities.Category;
import at.ac.ase.entities.RegularUser;
import at.ac.ase.entities.Status;
import at.ac.ase.service.auction.IAuctionService;
import at.ac.ase.service.bid.IBidService;
import at.ac.ase.service.user.IAuctionHouseService;
import at.ac.ase.service.user.IRegularUserService;
import at.ac.ase.util.exceptions.AuctionExpiredException;
import at.ac.ase.util.exceptions.InvalidBidException;
import java.time.LocalDateTime;
import org.junit.After;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

public class BidServiceTest extends BaseIntegrationTest {

    @Autowired
    private IBidService bidService;

    @Autowired
    private IAuctionService auctionService;

    @Autowired
    private IRegularUserService regularUserService;

    @Autowired
    private IAuctionHouseService auctionHouseService;


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
    }


    @Test(expected = AuctionExpiredException.class)
    @Transactional
    public void testPlaceABidOnExpiredAuctionThrowsAuctionExpiredException() {
        insertTestData("users.sql");

        RegularUser user = regularUserService.getUserByEmail("test@test.com");
        assertNotNull(user);

        AuctionHouse auctionHouse = auctionHouseService.getAuctionHouseByEmail("test1@test.com");
        assertNotNull(auctionHouse);

        AuctionPost auctionPost = new AuctionPost();
        auctionPost.setStatus(Status.ACTIVE);
        auctionPost.setStartTime(LocalDateTime.now().minusHours(5));
        auctionPost.setEndTime(LocalDateTime.now().minusHours(2));
        auctionPost.setCategory(Category.CARS);
        auctionPost.setMinPrice(100.0);
        auctionPost.setCreator(auctionHouse);
        auctionPost.setName("Expired auction");
        auctionPost = auctionService.saveAuction(auctionPost);

        assertNotNull(auctionPost.getId());

        Bid bid = new Bid();
        bid.setUser(user);
        bid.setAuction(auctionPost);
        bid.setDateTime(LocalDateTime.now());
        bid.setOffer(200.0);

        bidService.placeBid(bid);
    }

    @Test
    @Transactional
    public void testPlaceABidOnAnActiveAuction() {
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
        auctionPost.setMinPrice(100.0);
        auctionPost.setCreator(auctionHouse);
        auctionPost.setName("Active auction");
        auctionService.saveAuction(auctionPost);

        assertNotNull(auctionPost.getId());

        Bid bid = new Bid();
        bid.setUser(user);
        bid.setAuction(auctionPost);
        bid.setDateTime(LocalDateTime.now());
        bid.setOffer(200.0);

        bidService.placeBid(bid);
    }

    @Test(expected = InvalidBidException.class)
    @Transactional
    public void testPlaceABidLessThenMinimalPriceThrowsInvalidBidException() {
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
        auctionPost.setMinPrice(100.0);
        auctionPost.setCreator(auctionHouse);
        auctionPost.setName("Active auction");
        auctionService.saveAuction(auctionPost);

        assertNotNull(auctionPost.getId());

        Bid bid = new Bid();
        bid.setUser(user);
        bid.setAuction(auctionPost);
        bid.setDateTime(LocalDateTime.now());
        bid.setOffer(50.0);

        bidService.placeBid(bid);
    }


}
