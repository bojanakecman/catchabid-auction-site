package at.ac.ase.util.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Rating value is invalid or time period has expired. ")
public class InvalidRatingDataException extends RuntimeException {

    public InvalidRatingDataException(){}
}
