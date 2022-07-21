package at.ac.ase.util.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Minimal price is greater than the bid!")
public class InvalidBidException extends RuntimeException{
    public InvalidBidException(){};
}
