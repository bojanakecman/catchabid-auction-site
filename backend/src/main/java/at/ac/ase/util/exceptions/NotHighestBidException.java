package at.ac.ase.util.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class NotHighestBidException extends RuntimeException {

    public NotHighestBidException(String message) {
        super("Your offer is already exceeded. The current offer is: " + message + "€");
    }
}
