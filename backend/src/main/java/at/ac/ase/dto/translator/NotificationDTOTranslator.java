package at.ac.ase.dto.translator;

import at.ac.ase.dto.NotificationDTO;
import at.ac.ase.entities.Notification;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class NotificationDTOTranslator {

    @Autowired
    private AuctionDtoTranslator auctionDtoTranslator;


    public <T extends  Notification> NotificationDTO toNotificationDTO(T notification) {
        NotificationDTO notificationDTO = new NotificationDTO();
        notificationDTO.setId(notification.getId());
        notificationDTO.setDate(notification.getDate());
        notificationDTO.setInfo(notification.getInfo());
        notificationDTO.setSeen(notification.getSeen());
        return notificationDTO;
    }

    public List<NotificationDTO> toDtoList(List<? extends Notification> notifications) {
        return notifications.stream()
            .map(n -> toNotificationDTO(n))
            .collect(Collectors.toList());
    }
}
