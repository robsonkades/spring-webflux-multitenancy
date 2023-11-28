package app.lumini.api.handler;

import app.lumini.api.exception.PropertyBindingException;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

public abstract class AbstractValidationHandler<T, U extends Validator> {
    private final Class<T> validationClass;

    private final U validator;

    protected AbstractValidationHandler(Class<T> clazz, U validator) {
        this.validationClass = clazz;
        this.validator = validator;
    }

    abstract protected Mono<ServerResponse> processBody(T body, final ServerRequest originalRequest);


    public final Mono<ServerResponse> execute(final ServerRequest request) {
        return request.bodyToMono(this.validationClass)
                .flatMap(body -> {
                    Errors errors = new BeanPropertyBindingResult(body, this.validationClass.getName());
                    this.validator.validate(body, errors);
                    if (errors.hasErrors()) {
                        return onValidationErrors(errors, body, request);
                    }
                    return processBody(body, request);
                });
    }

    protected Mono<ServerResponse> onValidationErrors(Errors errors, T invalidBody, final ServerRequest request) {
        return Mono.error(new PropertyBindingException("Required parameter(s) missing or invalid.", errors));
    }
}

