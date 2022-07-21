package at.ac.ase.util.exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Auction cannot be cancelled!")
public class AuctionCancellationException extends RuntimeException{
    public AuctionCancellationException(){};
}
