package at.ac.ase.redis.service.implementation;

import at.ac.ase.controllers.NotificationWebSocketController;
import at.ac.ase.entities.AuctionPost;
import at.ac.ase.entities.Bid;
import at.ac.ase.entities.RegularUser;
import at.ac.ase.entities.RegularUserNotification;
import at.ac.ase.redis.model.HighestBid;
import at.ac.ase.redis.repository.RedisHighestBidRepository;
import at.ac.ase.redis.service.IHighestBidService;
import at.ac.ase.repository.user.UserRepository;
import at.ac.ase.service.notification.implementation.NotificationService;
import at.ac.ase.util.exceptions.NotHighestBidException;
import at.ac.ase.util.exceptions.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class HighestBidService implements IHighestBidService {

    @Autowired
    private RedisHighestBidRepository highestBidRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private NotificationWebSocketController webSocketController;

    @Override
    public void updateHighestBid(Bid bid) {
        HighestBid currentHighestBid = getHighestBid(bid.getAuction());
        if (currentHighestBid != null && currentHighestBid.getOffer() >= bid.getOffer()) {
                throw new NotHighestBidException(currentHighestBid.getOffer().toString());
        }
        HighestBid newHighestBid = new HighestBid();
        newHighestBid.setOffer(bid.getOffer());
        newHighestBid.setUserId(bid.getUser().getId());

        highestBidRepository.saveHighestBid(bid.getAuction().getId(), newHighestBid);

        if(currentHighestBid != null) {
            if(!currentHighestBid.getUserId().equals(newHighestBid.getUserId())) {
                Optional<RegularUser> user = userRepository.findById(currentHighestBid.getUserId());
                if (!user.isPresent()) {
                    throw new UserNotFoundException();
                }
                RegularUserNotification notification = new RegularUserNotification();
                notification.setReceiver(user.get());
                notification.setInfo("You're bid is not the highest anymore. Current highest bid is: " + newHighestBid.getOffer() + "€");
                notification.setSeen(false);
                notificationService.saveNotification(notification);
                webSocketController.sendNotification(notification.getReceiver(), notification);
            }
        }
    }

    @Override
    public HighestBid getHighestBid(AuctionPost auctionPost) {
        return highestBidRepository.getHighestBid(auctionPost.getId());
    }
}