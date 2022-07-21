package at.ac.ase.util.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN, reason = "User with this email is already registered!")
public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException(){}
}
