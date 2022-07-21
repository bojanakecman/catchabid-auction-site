package at.ac.ase.service.twitter.implementation.request;

public class TweetPostRequest extends TwitterPostRequest {

    private final String postTweetApiUrl = "https://api.twitter.com/1.1/statuses/update.json";

    public TweetPostRequest(String tweetContent, String mediaId, AuthenticationConfig config) {
        super(config);
        httpParameters.put("status", tweetContent);
        httpParameters.put("media_ids", mediaId);
    }

    @Override
    protected String getServiceUrl() {
        return String.format("%s?%s", postTweetApiUrl, createParameterQueryString(httpParameters));
    }
}
