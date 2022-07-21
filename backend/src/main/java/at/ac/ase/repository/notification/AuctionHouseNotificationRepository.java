package at.ac.ase.repository.notification;

import at.ac.ase.entities.AuctionHouseNotification;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface AuctionHouseNotificationRepository extends NotificationRepository {

    /**
     * Get a list of {@link AuctionHouseNotification}s
     * for the given {@link at.ac.ase.entities.User}'s id
     *
     * @param userId Id of the {@link at.ac.ase.entities.User}
     * @return List of found {@link AuctionHouseNotification}s
     */
    List<AuctionHouseNotification> findAllByReceiverId(Long userId);
}
