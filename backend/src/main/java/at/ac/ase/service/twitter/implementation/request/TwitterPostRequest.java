package at.ac.ase.service.twitter.implementation.request;

import at.ac.ase.util.exceptions.TwitterServiceException;
import oauth.signpost.basic.DefaultOAuthConsumer;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import oauth.signpost.http.HttpParameters;
import oauth.signpost.signature.AuthorizationHeaderSigningStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class TwitterPostRequest {

    private static final Logger logger = LoggerFactory.getLogger(TwitterPostRequest.class);

    protected abstract String getServiceUrl();
    protected void processOutputStream(OutputStream outputStream) throws IOException {}
    protected void configureRequestConnection(HttpURLConnection request) {}

    protected HttpParameters httpParameters = new HttpParameters();
    private String responseContent;
    private AuthenticationConfig authenticationConfig;

    public TwitterPostRequest(AuthenticationConfig authenticationConfig) {
        this.authenticationConfig = authenticationConfig;
    }

    public String getResponseContent() {
        return responseContent;
    }

    public AuthenticationConfig getAuthenticationConfig() {
        return authenticationConfig;
    }

    public int execute() throws TwitterServiceException {

        HttpURLConnection request;

        try {
            URL url = new URL(getServiceUrl());

            request = createConnetion(url);

            configureRequestConnection(request);

            getOAuthConsumer().sign(request);

            processOutputStream(request.getOutputStream());

            request.connect();

            logHeader(request);

            String response = request.getResponseMessage();

            System.out.println("RESPONSE:");
            System.out.println(response);
            System.out.println("RESP-CODE: " + request.getResponseCode());

            this.responseContent = new BufferedReader(
                    new InputStreamReader(request.getInputStream(), StandardCharsets.UTF_8))
                    .lines()
                    .collect(Collectors.joining("\n"));

            request.disconnect();

            return request.getResponseCode();

        } catch (OAuthExpectationFailedException | OAuthMessageSignerException | IOException | OAuthCommunicationException e) {
            throw new TwitterServiceException(e.getMessage());
        }
    }

    private DefaultOAuthConsumer getOAuthConsumer() {
        DefaultOAuthConsumer consumer = new DefaultOAuthConsumer(authenticationConfig.key, authenticationConfig.keySecret);
        consumer.setTokenWithSecret(authenticationConfig.accessToken, authenticationConfig.accessTokenSecret);
        consumer.setSigningStrategy(new AuthorizationHeaderSigningStrategy());
        return consumer;
    }

    private void logHeader(HttpURLConnection httpCon) {
        logger.info("HEADER: ");
        Map<String, List<String>> hdrs = httpCon.getHeaderFields();
        Set<String> hdrKeys = hdrs.keySet();

        for (String k : hdrKeys)
            logger.info("Key: " + k + "  Value: " + hdrs.get(k));
    }

    private HttpURLConnection createConnetion(URL url) throws TwitterServiceException {
        try {
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setConnectTimeout(3000);
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);

            return httpURLConnection;
        } catch (IOException e) {
            throw new TwitterServiceException(e.getMessage());
        }
    }

    protected String createParameterQueryString(HttpParameters parameters) {
        return parameters.entrySet()
                .stream()
                .map(param -> String.format("%s=%s",
                        URLEncoder.encode(param.getKey(), StandardCharsets.UTF_8),
                        URLEncoder.encode(param.getValue().first())))
                .collect(Collectors.joining("&"));
    }

    public static class AuthenticationConfig{
        String key;
        String keySecret;
        String accessToken;
        String accessTokenSecret;

        public AuthenticationConfig(String key, String keySecret, String accessToken, String accessTokenSecret) {
            this.key = key;
            this.keySecret = keySecret;
            this.accessToken = accessToken;
            this.accessTokenSecret = accessTokenSecret;
        }
    }
}
