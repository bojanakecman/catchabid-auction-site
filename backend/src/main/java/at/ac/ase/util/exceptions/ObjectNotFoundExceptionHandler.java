package at.ac.ase.util.exceptions;

import java.time.Instant;
import java.util.Date;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ObjectNotFoundExceptionHandler {

    @ExceptionHandler(value = {ObjectNotFoundException.class})
    public ResponseEntity<?> handleObjectNotFound(ObjectNotFoundException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
            Date.from(Instant.now()),
            HttpStatus.NOT_FOUND.value(),
            "Not found",
            (String.format("Object of type %s with %s not found in the database",
            ex.getExpectedObjectClass().getSimpleName(), ex.getExpectedObjectId())));
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

}
