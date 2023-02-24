package project.anotation.phone_num_constraint;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {PhoneNumberValidator.class})

public @interface PhoneNumber {

    String message() default "Invalid phone number format";

    Class<?>[] groups () default {};

    Class<? extends Payload>[] payload() default {};
}
