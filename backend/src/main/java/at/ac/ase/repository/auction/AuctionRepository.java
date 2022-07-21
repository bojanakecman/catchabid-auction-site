package at.ac.ase.repository.auction;

import at.ac.ase.entities.AuctionPost;
import at.ac.ase.entities.Category;
import at.ac.ase.repository.auction.implementation.AuctionRepositoryCustomQueries;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AuctionRepository extends JpaRepository<AuctionPost, Long>, AuctionRepositoryCustomQueries {

    /**
     * get countries where auctions exist
     * @return
     */
    @Query(value = "SELECT distinct country FROM auction_post", nativeQuery = true)
    List<String> getAllCountriesWhereAuctionsExist();

    /**
     * Get {@link AuctionPost} that the given user has won
     *
     * @param userId Id of the user
     * @param endDate earliest end date of the {@link AuctionPost}
     * @return List of auction satisfying the given criteria
     */
    List<AuctionPost> findAllByHighestBidUserIdAndEndTimeLessThan(Long userId, LocalDateTime endDate);

    /**
     * Get all auctions created by one user
      * @param user user who created the auctions
     * @return
     */
    List<AuctionPost> findALlByCreatorId(Long user);


}
