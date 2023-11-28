package app.lumini.api.exception;

import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.stereotype.Component;
import org.springframework.validation.FieldError;
import org.springframework.web.reactive.function.server.ServerRequest;

import java.util.List;
import java.util.Map;

@Component
public class CustomAttributes extends DefaultErrorAttributes {

    @Override
    public Map<String, Object> getErrorAttributes(ServerRequest request, ErrorAttributeOptions options) {
        Map<String, Object> errorAttributes = super.getErrorAttributes(request, options);
        Throwable throwable = getError(request);
        if (throwable instanceof PropertyBindingException bindingException) {
            List<Field> errors = bindingException
                    .getErrors()
                    .getAllErrors()
                    .stream()
                    .map(error -> Field.from(((FieldError) error).getField(), error.getCode(), error.getDefaultMessage()))
                    .toList();
            errorAttributes.put("errors", errors);
        }
        return errorAttributes;
    }
}
