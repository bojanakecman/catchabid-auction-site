package at.ac.ase.service.notification;

import at.ac.ase.entities.Notification;

import at.ac.ase.entities.User;
import java.util.List;

public interface INotificationService {

    List<? extends Notification> getNotificationsForUser(User user);

    <T extends Notification> T saveNotification(T notification);

    void updateNotificationSeen(Notification notification);

    Notification getNotificationById(Long id);

}
