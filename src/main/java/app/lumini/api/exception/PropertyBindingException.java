package app.lumini.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.validation.Errors;
import org.springframework.web.server.ResponseStatusException;

public class PropertyBindingException extends ResponseStatusException {

    private final Errors errors;

    public PropertyBindingException(String reason, Errors errors) {
        super(HttpStatus.BAD_REQUEST, reason);
        this.errors = errors;
    }

    public Errors getErrors() {
        return errors;
    }
}
