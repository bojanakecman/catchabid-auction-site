package at.ac.ase.util.exceptions;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;
import java.util.Set;

public class ErrorResponse {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    private Date timestamp;

    private int status;

    private String error;

    private String message;

    private Set<String> validationMessages;

    public ErrorResponse(Date timestamp, int status, String error, String message) {
        this.timestamp = timestamp;
        this.status = status;
        this.error = error;
        this.message = message;
    }

}
