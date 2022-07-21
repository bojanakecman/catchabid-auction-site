package at.ac.ase.util.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Provided token is not valid")
public class ResetTokenNotFound extends RuntimeException{
    public ResetTokenNotFound(){};
}
