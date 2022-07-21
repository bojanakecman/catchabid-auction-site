package at.ac.ase.util.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT, reason = "You can not give rating to yourself")
public class UserAndCreatorAreTheSameException extends RuntimeException{
    public UserAndCreatorAreTheSameException(){};
}
