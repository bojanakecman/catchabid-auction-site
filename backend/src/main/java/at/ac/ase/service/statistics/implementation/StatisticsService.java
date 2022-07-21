package at.ac.ase.service.statistics.implementation;

import at.ac.ase.controllers.AuctionController;
import at.ac.ase.entities.*;
import at.ac.ase.service.auction.implementation.AuctionService;
import at.ac.ase.service.bid.implementation.BidService;
import at.ac.ase.service.statistics.IStatisticsService;
import at.ac.ase.service.user.implementation.AuctionHouseService;
import at.ac.ase.service.user.implementation.RegularUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class StatisticsService implements IStatisticsService {

    private static final Logger logger = LoggerFactory.getLogger(AuctionController.class);
    @Autowired
    RegularUserService regularUserService;

    @Autowired
    AuctionHouseService auctionHouseService;

    @Autowired
    BidService bidService;

    @Autowired
    AuctionService auctionService;


    @Override
    public Map<String, Integer> getBidsStatistics(User user) {
        User retrievedUser = getUser(user);
        if (retrievedUser == null) {
            return new HashMap<>();
        }
        Map<String, Integer> statistics = new HashMap<>();
        Set<Bid> bids = retrievedUser.getBids();

        bids.forEach(x -> addStatistics(statistics, x));
        logger.info("Bids statistics: "+ statistics);

        return statistics;
    }

    private void addStatistics(Map<String, Integer> statistics, Bid bid) {

        Integer count = statistics.get(bid.getAuction().getCategory().name());
        if (count == null) {
            statistics.put(bid.getAuction().getCategory().name(), Integer.valueOf(1));
        } else {
            statistics.put(bid.getAuction().getCategory().name(), Integer.valueOf(count.intValue() + 1));
        }
    }

    @Override
    public Map<String, Integer> getWinsStatistics(User user) {
        User retrievedUser = getUser(user);
        if (retrievedUser == null) {
            return new HashMap<>();
        }

        Map<String, Integer> statistics = new HashMap<>();
        Set<Bid> bids = retrievedUser.getBids();

        Stream<Bid> wins = bids.stream().filter(x -> x.getId() == x.getAuction().getHighestBid().getId() && x.getAuction().getEndTime().isBefore(LocalDateTime.now(ZoneOffset.UTC)));

        wins.forEach(x -> addStatistics(statistics, x));

        logger.info("Wins statistics: "+ statistics);

        return statistics;
    }

    @Override
    public Map<String, Double> getBidsWinsRatio(User user) {
        User retrievedUser = regularUserService.getUserByEmail(user.getEmail());
        if (retrievedUser == null) {
            return new HashMap<>();
        }
        Map<String, Double> statistics = new HashMap<>();
        Set<Bid> bids = retrievedUser.getBids();
        if (bids.isEmpty()){
            return new HashMap<>();
        }
        double bidsCount=bids.stream().collect(Collectors.groupingBy(Bid::getAuction)).keySet().size();
        Stream<Bid> wins = bids.stream().filter(x -> x.getId().equals(x.getAuction().getHighestBid().getId()) && x.getAuction().getEndTime().isBefore(LocalDateTime.now(ZoneOffset.UTC)));
        double winsCount = wins.toArray().length;
        statistics.put("wins", ((winsCount*100)/bidsCount));
        double lossCount = bids.stream().collect(Collectors.groupingBy(Bid::getAuction)).keySet().size() - winsCount;
        statistics.put("bids", ((lossCount*100)/bidsCount));
        logger.info("Wins ratio: "+ statistics);

        return statistics;
    }

    @Override
    public Map<String, Integer> getPopularityOfOwnAuctions(User user) {
        User retrievedUser = getUser(user);
        if (retrievedUser == null) {
            return new HashMap<>();
        }
        Set<AuctionPost> myPosts = retrievedUser.getOwnedAuctions();
        Map<Category, List<AuctionPost>> auctionsByCategory = myPosts.stream().collect(Collectors.groupingBy(AuctionPost::getCategory));
        Map<String, Integer> auctionsPopularityByCategory = new HashMap<>();
        for (Category k : auctionsByCategory.keySet()) {
            List<AuctionPost> posts = auctionsByCategory.get(k);
            int count= posts.stream().mapToInt(x -> bidService.getAuctionBids(x).size()).sum();
            auctionsPopularityByCategory.put(k.name(),count/posts.size());
        }
        logger.info("Auction popularity: "+ auctionsPopularityByCategory);

        return auctionsPopularityByCategory;
    }

    @Override
    public Map<String, Double> getSuccessOfMyAuctions(User user) {
        User retrievedUser = getUser(user);
        if (retrievedUser == null) {
            return new HashMap<>();
        }
        Set<AuctionPost> myPosts = retrievedUser.getOwnedAuctions();
        Map<Category, List<AuctionPost>> auctionsByCategory = myPosts.stream().collect(Collectors.groupingBy(AuctionPost::getCategory));
        Map<String, Double> auctionsByCategoryCount = new HashMap<>();
        for (Category k : auctionsByCategory.keySet()) {
            List<AuctionPost> posts = auctionsByCategory.get(k);
            if (posts!=null && !posts.isEmpty() ){
                double price= 0.0;
                for(AuctionPost post : posts){
                    if (post.getHighestBid()!=null){
                        price+=post.getHighestBid().getOffer()-post.getMinPrice();
                    }
                }
                auctionsByCategoryCount.put(k.name(),price/posts.size());
            }


        }
        logger.info("Auction succcess: "+ auctionsByCategoryCount);

        return auctionsByCategoryCount;
    }

    private User getUser(User user) {
        AuctionHouse house = auctionHouseService.getAuctionHouseByEmail(user.getEmail());
        if (house != null) {
            return house;
        } else {
            RegularUser regularUser = regularUserService.getUserByEmail(user.getEmail());
            return regularUser;
        }

    }
}
