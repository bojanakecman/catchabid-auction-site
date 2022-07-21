package at.ac.ase.service;

import at.ac.ase.basetest.BaseIntegrationTest;
import at.ac.ase.entities.AuctionHouse;
import at.ac.ase.entities.Category;
import at.ac.ase.entities.RegularUser;
import at.ac.ase.service.auction.IAuctionService;
import at.ac.ase.service.bid.IBidService;
import at.ac.ase.service.statistics.IStatisticsService;
import at.ac.ase.service.user.IAuctionHouseService;
import at.ac.ase.service.user.IRegularUserService;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;
import java.util.Optional;

public class StatisticsServiceTest extends BaseIntegrationTest {

    @Autowired
    IAuctionService auctionService;
    @Autowired
    IAuctionHouseService auctionHouseService;
    @Autowired
    IRegularUserService regularUserService;
    @Autowired
    IStatisticsService statisticsService;
    @Autowired
    IBidService bidService;

    @After
    public void cleanup() {
        cleanDatabase();
    }

    @Test
    public void testBidStatistics(){
        insertTestData("F32_tc1.sql");
        Optional<RegularUser> user=regularUserService.getUserById(1l);
        if (!user.isPresent()){
            Assert.fail("user not found, but should exist");
        }
        Map<String,Integer> statistics = statisticsService.getBidsStatistics(user.get());
        Assertions.assertNull(statistics.get(Category.ANTIQUES.name()));
        Assertions.assertNull(statistics.get(Category.TRAVEL.name()));
        Assertions.assertEquals(2,statistics.get(Category.JEWELRY.name()));
        Assertions.assertEquals(2,statistics.get(Category.ELECTRONICS.name()));
        Assertions.assertNull(statistics.get(Category.ART.name()));
        Assertions.assertEquals(2,statistics.get(Category.CARS.name()));
        Assertions.assertNull(statistics.get(Category.FURNITURE.name()));
        Assertions.assertNull(statistics.get(Category.MUSIC.name()));
        Assertions.assertNull(statistics.get(Category.SPORT.name()));
        Assertions.assertNull(statistics.get(Category.OTHER.name()));
    }
    @Test
    public void testBidStatisticsNoBids(){
        insertTestData("F32_tc1.sql");
        Optional<RegularUser> user=regularUserService.getUserById(5l);
        if (!user.isPresent()){
            Assert.fail("user not found, but should exist");
        }
        Map<String,Integer> statistics = statisticsService.getBidsStatistics(user.get());
        Assert.assertEquals(0,statistics.size());

    }

    @Test
    public void testWinsStatistics(){
        insertTestData("F32_tc1.sql");
        Optional<RegularUser> user=regularUserService.getUserById(4l);
        if (!user.isPresent()){
            Assert.fail("user not found, but should exist");
        }
        Map<String,Integer> statistics = statisticsService.getWinsStatistics(user.get());

        Assertions.assertEquals(2,statistics.size());
        Assertions.assertEquals(1,statistics.get(Category.JEWELRY.name()));
        Assertions.assertEquals(1,statistics.get(Category.ELECTRONICS.name()));
    }
    @Test
    public void testWinsStatisticsNoWins(){
        insertTestData("F32_tc1.sql");
        Optional<RegularUser> user=regularUserService.getUserById(6l);
        if (!user.isPresent()){
            Assert.fail("user not found, but should exist");
        }
        Map<String,Integer> statistics = statisticsService.getWinsStatistics(user.get());

        Assertions.assertTrue(statistics.isEmpty());
    }

    @Test
    public void testBidWinRatio(){
        insertTestData("F32_tc1.sql");
        Optional<RegularUser> user=regularUserService.getUserById(4l);
        if (!user.isPresent()){
            Assert.fail("user not found, but should exist");
        }
        Map<String,Double> statistics = statisticsService.getBidsWinsRatio(user.get());

        Assertions.assertEquals(2.0,statistics.size());
        Assertions.assertEquals(66.66666666666667,statistics.get("wins"));
        Assertions.assertEquals(33.333333333333336,statistics.get("bids").doubleValue());

    }
    @Test
    public void testBidWinRatioNoBids(){
        insertTestData("F32_tc1.sql");
        Optional<RegularUser> user=regularUserService.getUserById(5l);
        if (!user.isPresent()){
            Assert.fail("user not found, but should exist");
        }
        Map<String,Integer> statistics = statisticsService.getWinsStatistics(user.get());
        Assert.assertTrue(statistics.isEmpty());

    }

    @Test
    public void testBidWinRatioNoWins(){
        insertTestData("F32_tc1.sql");
        Optional<RegularUser> user=regularUserService.getUserById(6l);
        if (!user.isPresent()){
            Assert.fail("user not found, but should exist");
        }
        Map<String,Double> statistics = statisticsService.getBidsWinsRatio(user.get());
        Assert.assertFalse(statistics.isEmpty());
        Assert.assertEquals(100.0,statistics.get("bids").doubleValue(),0);

    }

    @Test
    public void testPopularityOfAuctions(){
        insertTestData("F32_tc1.sql");

        Optional<AuctionHouse> user=auctionHouseService.getAuctionHouseById(3l);
        if (!user.isPresent()){
            Assert.fail("user not found, but should exist");
        }
        Map<String,Integer> statistics = statisticsService.getPopularityOfOwnAuctions(user.get());
        Assert.assertFalse(statistics.isEmpty());
        Assert.assertEquals(2,statistics.size());
        Assert.assertEquals(0,statistics.get("CARS").intValue());
        Assert.assertEquals(5,statistics.get("JEWELRY").intValue());

    }

    @Test
    public void testPopularityOfAuctionsNoBids(){
        insertTestData("F32_tc1.sql");

        Optional<AuctionHouse> user=auctionHouseService.getAuctionHouseById(3l);
        if (!user.isPresent()){
            Assert.fail("user not found, but should exist");
        }
        Map<String,Integer> statistics = statisticsService.getPopularityOfOwnAuctions(user.get());
        System.out.println(statistics);
        Assert.assertFalse(statistics.isEmpty());

    }
    @Test
    public void testPopularityOfAuctionsNoAuctions() {
        insertTestData("F32_tc1.sql");
        Optional<AuctionHouse> user=auctionHouseService.getAuctionHouseById(7l);
        if (!user.isPresent()){
            Assert.fail("user not found, but should exist");
        }
        Map<String,Integer> statistics = statisticsService.getPopularityOfOwnAuctions(user.get());
        Assert.assertTrue(statistics.isEmpty());
    }

    @Test
    public void testSuccessOfAuctions(){
        insertTestData("F32_tc1.sql");
        Optional<AuctionHouse> user=auctionHouseService.getAuctionHouseById(2l);
        if (!user.isPresent()){
            Assert.fail("user not found, but should exist");
        }
        Map<String,Double> statistics = statisticsService.getSuccessOfMyAuctions(user.get());
        Assert.assertFalse(statistics.isEmpty());
        Assert.assertEquals(1,statistics.size());
        double cars=statistics.get("CARS").doubleValue();
        Assert.assertEquals(21.00,cars,0);

    }
    @Test
    public void testSuccessOfAuctionsNoAuctions() {
        insertTestData("F32_tc1.sql");
        Optional<AuctionHouse> user=auctionHouseService.getAuctionHouseById(7l);
        if (!user.isPresent()){
            Assert.fail("user not found, but should exist");
        }
        Map<String,Double> statistics = statisticsService.getSuccessOfMyAuctions(user.get());
        Assert.assertTrue(statistics.isEmpty());
    }

}
