package com.xtravels.utils;
/*
we can optionally create multiple field matching validators using the @FieldMatch.List annotation.

@FieldMatch.List({
       @FieldMatch(first = "password", second = "confirmPassword", message = "The password fields must match"),
      @FieldMatch(first = "email", second = "confirmEmail", message = "The email fields must match")
})

// on model
@FieldMatch(first = "password", second = "confirmPassword", message = "The password fields must match")

**/
import org.springframework.beans.BeanWrapperImpl;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class FieldMatchValidator  implements ConstraintValidator<FieldMatch, Object> {

    private String firstFieldName;
    private String secondFieldName;
    private String message;

    @Override
    public void initialize(final FieldMatch constraintAnnotation) {
        firstFieldName = constraintAnnotation.first();
        secondFieldName = constraintAnnotation.second();
        message = constraintAnnotation.message();
    }

    @Override
    public boolean isValid(final Object value, final ConstraintValidatorContext context) {
        boolean valid = true;
        try
        {
            final Object firstObj = new BeanWrapperImpl(value)
                    .getPropertyValue(firstFieldName);
            final Object secondObj = new BeanWrapperImpl(value)
                    .getPropertyValue(secondFieldName);

            valid =  firstObj == null && secondObj == null || firstObj != null && firstObj.equals(secondObj);
        }
        catch (final Exception ignore)
        {
            // ignore
        }

        if (!valid){
            context.buildConstraintViolationWithTemplate(message)
                    .addPropertyNode(firstFieldName)
                    .addConstraintViolation()
                    .disableDefaultConstraintViolation();
        }

        return valid;
    }
}