package at.ac.ase.util;

import at.ac.ase.controllers.NotificationWebSocketController;
import at.ac.ase.entities.*;
import at.ac.ase.service.auction.implementation.AuctionService;
import at.ac.ase.service.notification.implementation.NotificationService;
import at.ac.ase.util.exceptions.EmailNotSentException;
import at.ac.ase.util.exceptions.ObjectNotFoundException;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.Optional;

public class AuctionFinishedNotificationJob implements Job {

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        AuctionPost auctionPost = (AuctionPost)jobExecutionContext.getJobDetail().getJobDataMap().get("auctionPost");
        JavaMailSender emailSender = (JavaMailSender)jobExecutionContext.getJobDetail().getJobDataMap().get("emailSender");
        NotificationService notificationService = (NotificationService) jobExecutionContext.getJobDetail().getJobDataMap().get("notificationService");
        AuctionService auctionService = (AuctionService) jobExecutionContext.getJobDetail().getJobDataMap().get("auctionService");
        NotificationWebSocketController webSocketController = (NotificationWebSocketController)jobExecutionContext.getJobDetail().getJobDataMap().get("webSocketController");

        String userNotificationMessage = "You won the auction \"" + auctionPost.getName() + "\"!";

        Optional<AuctionPost> finalAuctionPost = auctionService.getAuctionPost(auctionPost.getId());
        if(!finalAuctionPost.isPresent()){
            throw new ObjectNotFoundException();
        }

        Bid highestBid = finalAuctionPost.get().getHighestBid();

        if(highestBid != null) {
            // stores and sends notification to user who won the auction
            RegularUserNotification notification = new RegularUserNotification();
            notification.setReceiver(highestBid.getUser());
            notification.setInfo(userNotificationMessage);
            notification.setSeen(false);
            notificationService.saveNotification(notification);
            webSocketController.sendNotification(highestBid.getUser(), notification);

            // sends email notification to user who won the auction
            try {
                SimpleMailMessage message = new SimpleMailMessage();
                message.setFrom("noreply.catchabid@gmail.com");
                message.setTo(highestBid.getUser().getEmail());
                message.setSubject("Auction: " + auctionPost.getName());
                message.setText(userNotificationMessage);
                emailSender.send(message);
            } catch (MailException e) {
                throw new EmailNotSentException();
            }
        }
        String creatorNotificationMessage;

        if(highestBid != null) {
           creatorNotificationMessage = "The auction you created \"" + auctionPost.getName() + "\" finished. The bidder " + highestBid.getUser().getFirstName() + " " + highestBid.getUser().getLastName() + " offered the highest bid of " + highestBid.getOffer() + "€";
        }else {
            creatorNotificationMessage = "The auction you created \"" + auctionPost.getName() + "\" finished. There were no bids for this auction!";
        }

        // stores and sends notification to creator of the auction
        if(finalAuctionPost.get().getCreator() instanceof RegularUser) {
            RegularUserNotification notificationForCreator = new RegularUserNotification();
            notificationForCreator.setReceiver((RegularUser)finalAuctionPost.get().getCreator());
            notificationForCreator.setInfo(creatorNotificationMessage);
            notificationForCreator.setSeen(false);
            notificationService.saveNotification(notificationForCreator);
            webSocketController.sendNotification(finalAuctionPost.get().getCreator(), notificationForCreator);
        }else {
            AuctionHouseNotification notificationForCreator = new AuctionHouseNotification();
            notificationForCreator.setReceiver((AuctionHouse) finalAuctionPost.get().getCreator());
            notificationForCreator.setInfo(creatorNotificationMessage);
            notificationForCreator.setSeen(false);
            notificationService.saveNotification(notificationForCreator);
            webSocketController.sendNotification(finalAuctionPost.get().getCreator(), notificationForCreator);
        }

        // sends email notification to creator of the auction
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("noreply.catchabid@gmail.com");
            message.setTo(finalAuctionPost.get().getCreator().getEmail());
            message.setSubject("Auction: " + finalAuctionPost.get().getName());
            message.setText(creatorNotificationMessage);
            emailSender.send(message);
        } catch (MailException e) {
            throw new EmailNotSentException();
        }
    }
}
