package at.ac.ase.util.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Error occurred: Email could not be sent")
public class EmailNotSentException extends RuntimeException{
    public EmailNotSentException(){}
}
