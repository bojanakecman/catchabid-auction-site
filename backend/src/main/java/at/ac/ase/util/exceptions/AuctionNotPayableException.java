package at.ac.ase.util.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class AuctionNotPayableException extends RuntimeException {

    public AuctionNotPayableException() {
        super("Provided auction has not been finished or has no bid.");
    }
}
