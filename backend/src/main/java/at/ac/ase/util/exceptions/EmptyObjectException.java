package at.ac.ase.util.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Received Object is empty")
public class EmptyObjectException extends RuntimeException {
    public EmptyObjectException() {
    }
}
