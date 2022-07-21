package at.ac.ase.service.twitter.implementation;

import at.ac.ase.entities.AuctionPost;
import at.ac.ase.entities.TwitterPostType;
import at.ac.ase.service.twitter.ITwitterService;
import at.ac.ase.service.twitter.implementation.request.MediaPostRequest;
import at.ac.ase.service.twitter.implementation.request.TweetPostRequest;
import at.ac.ase.service.twitter.implementation.request.TwitterPostRequest;
import at.ac.ase.util.exceptions.TwitterServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class TwitterService implements ITwitterService {

    private static final Logger logger = LoggerFactory.getLogger(TwitterService.class);

    @Value("${application.twitter.api.key}")
    private String APIKey;
    @Value("${application.twitter.api.key_secret}")
    private String APIKeySecret;
    @Value("${application.twitter.api.token}")
    private String APIAccessToken;
    @Value("${application.twitter.api.token_secret}")
    private String APIAccessTokenSecret;

    @Override
    public void tweetAuction(AuctionPost auction, TwitterPostType tweetType) {

        MediaPostRequest mediaPostRequest = new MediaPostRequest(auction.getImage(), getAuthenticationConfig());
        String mediaId = null;
        try {
            int responseCode = mediaPostRequest.execute();
            mediaId = mediaPostRequest.getMediaId();

            if (responseCode != HttpStatus.OK.value()) {
                logger.error(String.format("Failed to access twitter API while uploading auction image. auctionId: %s StatusCode: %s",
                        auction.getId(), tweetType, responseCode));
                return;
            }
        } catch (TwitterServiceException e) {
            logger.error(String.format("Failed to access twitter API while uploading auction image. auctionId: %s Error: %s",
                    auction.getId(), e.getMessage()));
            return;
        }


        TweetPostRequest tweet = new TweetPostRequest(createTweetContent(auction, tweetType), mediaId, getAuthenticationConfig());
        try {
            int responseCode = tweet.execute();

            if (responseCode != HttpStatus.OK.value()) {
                logger.error(String.format("Failed to access twitter API while posting tweet(auctionId: %s, type: %s). StatusCode: %s",
                        auction.getId(), tweetType, responseCode));
                return;
            }
        } catch (TwitterServiceException e) {
            logger.error(String.format("Failed to access twitter API while posting tweet(auctionId: %s, type: %s). Error: %s",
                    auction.getId(), tweetType, e.getMessage()));
        }
    }

    private String createTweetContent(AuctionPost auction, TwitterPostType tweetType) {

        switch (tweetType) {
            case AUCTION_START:
                return String.format("Auction '%s' starts now! Dont miss is it!", auction.getName());
            case AUCTION_END:
                return String.format("Auction '%s' ended!! Congratulation to the winner who won with a sum of € %s",
                        auction.getName(), auction.getHighestBid().getOffer());
            case AUCTION_UPCOMING:
                return String.format("Auction '%s' starts in one hour! Be prepared!", auction.getName());
            case AUCTION_CLOSE_TO_END:
                return String.format("Auction '%s' ends in one hour!", auction.getName());
        }
        return null;
    }

    private TwitterPostRequest.AuthenticationConfig getAuthenticationConfig() {
        return new TwitterPostRequest.AuthenticationConfig(APIKey, APIKeySecret, APIAccessToken, APIAccessTokenSecret);
    }

}
