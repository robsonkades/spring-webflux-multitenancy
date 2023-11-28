package app.lumini.api.handler;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

public class CreateTrailRequestValidator implements Validator {

    private static final int MINIMUM_CODE_LENGTH = 6;
    @Override
    public boolean supports(Class<?> clazz) {
        return CreateTrailRequest.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "field.required", new Object[] {MINIMUM_CODE_LENGTH}, "The name must be at least [" + MINIMUM_CODE_LENGTH + "] characters in length.");
        CreateTrailRequest request = (CreateTrailRequest) target;
        String name = request.getName();
        if (name != null && name
                .trim()
                .length() < MINIMUM_CODE_LENGTH) {
            errors.rejectValue("name", "field.min.length", new Object[]{MINIMUM_CODE_LENGTH}, "The name must be at least [" + MINIMUM_CODE_LENGTH + "] characters in length.");
        }
    }
}
