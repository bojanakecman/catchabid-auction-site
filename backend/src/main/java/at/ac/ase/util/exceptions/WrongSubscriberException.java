package at.ac.ase.util.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Cannot subscribe to your own auction!")
public class WrongSubscriberException extends RuntimeException{
    public WrongSubscriberException(){}
}
