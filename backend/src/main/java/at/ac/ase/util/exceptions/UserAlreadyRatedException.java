package at.ac.ase.util.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT, reason = "User already rated")
public class UserAlreadyRatedException extends RuntimeException {
    public UserAlreadyRatedException(){}
}
