package edu.grsu.tracker.utils.validation;

import edu.grsu.tracker.dto.rq.auth.SignupRq;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, Object> {

    @Override
    public void initialize(PasswordMatches constraintAnnotation) {
    }

    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext context) {
        SignupRq user = (SignupRq) obj;
        return user.getPassword().equals(user.getMatchingPassword());
    }
}