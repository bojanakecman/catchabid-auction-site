package at.ac.ase.util.exceptions;

import org.apache.coyote.http2.Http2Protocol;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT, reason = "Token could not be generated!")
public class TokenGenerationException extends RuntimeException {
    public TokenGenerationException(){}
}
