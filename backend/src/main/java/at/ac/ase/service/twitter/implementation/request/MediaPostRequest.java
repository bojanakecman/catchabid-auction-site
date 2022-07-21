package at.ac.ase.service.twitter.implementation.request;


import at.ac.ase.util.exceptions.TwitterServiceException;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;


import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class MediaPostRequest extends TwitterPostRequest {

    private final byte[] imageContent;
    private final String boundary;


    public MediaPostRequest(byte[] imageContent, AuthenticationConfig config) {
        super(config);
        this.imageContent = imageContent;
        this.boundary = UUID.randomUUID().toString();
    }

    @Override
    protected String getServiceUrl() {
        return  "https://upload.twitter.com/1.1/media/upload.json";
    }

    @Override
    protected void configureRequestConnection(HttpURLConnection request) {
        request.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
    }

    @Override
    protected void processOutputStream(OutputStream outputStream) throws IOException {

        String LINE = "\r\n";
        PrintWriter writer = new PrintWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8), true);

        writer.append("--" + boundary).append(LINE);
        writer.append("Content-Disposition: form-data; name=\"" + "media" + "\"; filename=\"" + "media" + "\"").append(LINE);
        writer.append("Content-Type: " + "image/png").append(LINE);
        writer.append("Content-Transfer-Encoding: binary").append(LINE);
        writer.append(LINE);
        writer.flush();

        outputStream.write(imageContent);
        outputStream.flush();
        writer.append(LINE);
        writer.flush();

        writer.append(LINE).flush();
        writer.append("--" + boundary + "--").append(LINE);
        writer.flush();
        writer.close();
    }

    public String getMediaId() throws TwitterServiceException {
        if (getResponseContent() != null) {
            try {
                JSONObject obj = (JSONObject) new JSONParser(JSONParser.MODE_JSON_SIMPLE).parse(getResponseContent());
                return String.valueOf(obj.get("media_id"));
            } catch (ParseException e) {
                throw new TwitterServiceException(e.getMessage());
            }
        }
        throw new TwitterServiceException("could not extract media_id from " + getResponseContent());
    }
}
