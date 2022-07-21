package at.ac.ase.repository.bid;

import at.ac.ase.entities.Bid;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BidRepository extends JpaRepository<Bid, Long> {

    /**
     * Get list of {@link Bid}s for the given {@link at.ac.ase.entities.User}
     *
     * @param userId Id of the {@link at.ac.ase.entities.User}
     * @return the list of found {@link Bid}s
     */
    List<Bid> findAllByUser_Id(Long userId);
    List<Bid> findAllByAuction_id(Long auctionId);
}
