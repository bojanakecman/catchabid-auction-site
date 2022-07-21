package at.ac.ase.service.notification.implementation;

import at.ac.ase.entities.Notification;
import at.ac.ase.entities.RegularUser;
import at.ac.ase.entities.RegularUserNotification;
import at.ac.ase.entities.User;
import at.ac.ase.repository.notification.AuctionHouseNotificationRepository;
import at.ac.ase.repository.notification.RegularUserNotificationRepository;
import at.ac.ase.service.notification.INotificationService;
import at.ac.ase.util.exceptions.ObjectNotFoundException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotificationService implements INotificationService {

    @Autowired
    private RegularUserNotificationRepository regularUserNotificationRepository;

    @Autowired
    private AuctionHouseNotificationRepository auctionHouseNotificationRepository;

    @Override
    public List<? extends Notification> getNotificationsForUser(User user) {
        if (user instanceof RegularUser) {
            return regularUserNotificationRepository.findAllByReceiverId(user.getId());
        } else {
            return auctionHouseNotificationRepository.findAllByReceiverId(user.getId());
        }
    }


    @Override
    public <T extends Notification> T saveNotification(T notification) {
        if (notification instanceof RegularUserNotification) {
            notification = regularUserNotificationRepository.save(notification);
        } else {
            notification = auctionHouseNotificationRepository.save(notification);
        }
        return notification;
    }

    @Override
    public void updateNotificationSeen(Notification notification) {
        notification.setSeen(true);
        saveNotification(notification);
    }

    @Override
    public Notification getNotificationById(Long id) {
        return regularUserNotificationRepository.findById(id).orElseThrow(
            ObjectNotFoundException::new);
    }
}
