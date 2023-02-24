package project.anotation.phone_num_constraint;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PhoneNumberValidator implements ConstraintValidator<PhoneNumber, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value != null &&
                value.length() == 13 &&
                value.substring(1).matches("\\d+") &&
                value.indexOf("+998") == 0;
    }
}
