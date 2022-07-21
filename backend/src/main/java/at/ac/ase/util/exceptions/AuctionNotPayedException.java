package at.ac.ase.util.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class AuctionNotPayedException extends RuntimeException {

    public AuctionNotPayedException() {
        super("Provided auction has not been paid.");
    }
}
