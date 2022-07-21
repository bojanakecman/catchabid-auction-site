package at.ac.ase.util;

import at.ac.ase.entities.AuctionPost;
import at.ac.ase.entities.TwitterPostType;
import at.ac.ase.service.twitter.ITwitterService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;

public class AuctionTweetJob implements Job {

    @Override
    public void execute(JobExecutionContext jobExecutionContext) {

        AuctionPost auctionPost = (AuctionPost)jobExecutionContext.getJobDetail().getJobDataMap().get("auctionPost");
        TwitterPostType twitterPostType = (TwitterPostType)jobExecutionContext.getJobDetail().getJobDataMap().get("twitterPostType");
        ITwitterService twitterService = (ITwitterService)jobExecutionContext.getJobDetail().getJobDataMap().get("twitterService");

        twitterService.tweetAuction(auctionPost, twitterPostType);
    }
}
