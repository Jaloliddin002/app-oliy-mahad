package project.anotation.role_constraint;

import org.springframework.context.annotation.Role;
import project.model.enums.ERole;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.reflect.Array;
import java.util.Arrays;

public class RoleValidator implements ConstraintValidator<Role, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return Arrays.stream(ERole.values()).anyMatch(u -> u.name().equals(value));
    }
}
