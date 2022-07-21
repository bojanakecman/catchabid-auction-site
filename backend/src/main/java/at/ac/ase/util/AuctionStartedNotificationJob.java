package at.ac.ase.util;

import at.ac.ase.config.WebSocketConfig;
import at.ac.ase.controllers.NotificationWebSocketController;
import at.ac.ase.entities.*;
import at.ac.ase.service.auction.implementation.AuctionService;
import at.ac.ase.service.notification.implementation.NotificationService;
import at.ac.ase.util.exceptions.EmailNotSentException;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

public class AuctionStartedNotificationJob implements Job {

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        AuctionPost auctionPost = (AuctionPost)jobExecutionContext.getJobDetail().getJobDataMap().get("auctionPost");
        JavaMailSender emailSender = (JavaMailSender)jobExecutionContext.getJobDetail().getJobDataMap().get("emailSender");
        NotificationService notificationService = (NotificationService) jobExecutionContext.getJobDetail().getJobDataMap().get("notificationService");
        AuctionService auctionService = (AuctionService) jobExecutionContext.getJobDetail().getJobDataMap().get("auctionService");
        NotificationWebSocketController webSocketController = (NotificationWebSocketController)jobExecutionContext.getJobDetail().getJobDataMap().get("webSocketController");

        String notificationMessage = "Auction you subscribed for \"" + auctionPost.getName() + "\" just started!";

        for(User user: auctionPost.getSubscriptions()) {
            // create and store notification for each subscribed user
            RegularUserNotification notification = new RegularUserNotification();
            notification.setReceiver((RegularUser) user);
            notification.setInfo(notificationMessage);
            notification.setSeen(false);
            notificationService.saveNotification(notification);
            webSocketController.sendNotification(user, notification);

            // send an email to all subscribed user
            try {
                SimpleMailMessage message = new SimpleMailMessage();
                message.setFrom("noreply.catchabid@gmail.com");
                message.setTo(user.getEmail());
                message.setSubject("Auction: " + auctionPost.getName());
                message.setText(notificationMessage);
                emailSender.send(message);
            } catch (MailException e) {
                throw new EmailNotSentException();
            }

            // update auction post status to ACTIVE
            auctionPost.setStatus(Status.ACTIVE);
            auctionService.saveAuction(auctionPost);

            auctionService.scheduleNotificationJob(auctionPost);
        }
    }
}
