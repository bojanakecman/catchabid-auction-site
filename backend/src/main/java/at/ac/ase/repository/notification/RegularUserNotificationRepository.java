package at.ac.ase.repository.notification;

import at.ac.ase.entities.RegularUserNotification;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface RegularUserNotificationRepository extends NotificationRepository {

    /**
     * Get a list of {@link RegularUserNotification}s
     * for the given {@link at.ac.ase.entities.User}'s id
     *
     * @param userId Id of the {@link at.ac.ase.entities.User}
     * @return List of found {@link RegularUserNotification}s
     */
    List<RegularUserNotification> findAllByReceiverId(Long userId);

}
