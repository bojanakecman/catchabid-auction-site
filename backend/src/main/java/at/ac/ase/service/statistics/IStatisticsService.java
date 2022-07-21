package at.ac.ase.service.statistics;

import at.ac.ase.entities.User;

import java.util.Map;

public interface IStatisticsService {

    /**
     * How many bids per category did the user place
     * @param user - for which we check
     * @return map of statistics - count for each category
     */
    Map<String,Integer> getBidsStatistics(User user);

    /**
     * How many wins per category does the user have
     * @param user -for which we check
     * @return map of statistics - count for each category
     */
    Map<String,Integer> getWinsStatistics(User user);
    /**
     * For how many auctions did the user bid vs how many did he win
     * @param user - for which we check
     * @return map of counts for bids and wins
     */
    Map<String,Double> getBidsWinsRatio(User user);

    /**
     * How popular (average bids per auction) are the auctions per category
     * @param user - for which we check
     * @return map of statistics- average values per category
     */
    Map<String,Integer> getPopularityOfOwnAuctions(User user);

    /**
     * success is highest average increase in price per auction per category
     * @param user - for which we check
     * @return map of statistics- average values per category
     */
    Map<String,Double> getSuccessOfMyAuctions(User user);


}
