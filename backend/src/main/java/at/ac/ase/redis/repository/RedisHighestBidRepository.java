package at.ac.ase.redis.repository;

import at.ac.ase.redis.model.HighestBid;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class RedisHighestBidRepository {

    private static final String HIGHEST_BID_CACHE_KEY = "HIGHEST_BID";

    private HashOperations hashOperations;

    private RedisTemplate<Long, HighestBid> redisTemplate;

    public RedisHighestBidRepository(RedisTemplate<Long, HighestBid> redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.hashOperations = this.redisTemplate.opsForHash();
    }

    /**
     * Updates the highest bid for the given auction id in cache.
     *
     * @param auctionId Id of an {@link at.ac.ase.entities.AuctionPost}
     * @param highestBid {@link HighestBid} object containing information about
     * user and it's offer
     */
    public void saveHighestBid(Long auctionId, HighestBid highestBid) {
        hashOperations.put(HIGHEST_BID_CACHE_KEY, auctionId, highestBid);
    }

    /**
     * Method to get the highest bid for the provided auction id from the cache.
     *
     * @param auctionId Id of an {@link at.ac.ase.entities.AuctionPost}
     * @return the highest bid of the given auction
     */
    public HighestBid getHighestBid(Long auctionId) {
        return (HighestBid) hashOperations.get(HIGHEST_BID_CACHE_KEY, auctionId);
    }

    /**
     * Method to delete the highest bid entry for the given auction
     * @param auctionId Id of the {@link at.ac.ase.entities.AuctionPost},
     * which highest bid cache entry should be deleted
     */
    public void delete(Long auctionId){
        hashOperations.delete(HIGHEST_BID_CACHE_KEY, auctionId);
    }

    

}
