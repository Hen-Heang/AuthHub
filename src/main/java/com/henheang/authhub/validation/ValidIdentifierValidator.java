package com.henheang.authhub.validation;

import com.henheang.authhub.payload.SignUpRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ValidIdentifierValidator implements ConstraintValidator<ValidIdentifier, SignUpRequest> {

    @Override
    public void initialize(ValidIdentifier constraintAnnotation) {
        // No initialization needed
    }

    @Override
    public boolean isValid(SignUpRequest signUpRequest, ConstraintValidatorContext context) {
        if (signUpRequest == null) {
            return false;
        }

        boolean hasEmail = signUpRequest.getEmail() != null && !signUpRequest.getEmail().trim().isEmpty();
        boolean hasPhone = signUpRequest.getPhoneNumber() != null && !signUpRequest.getPhoneNumber().trim().isEmpty();

        // At least one must be provided
        if (!hasEmail && !hasPhone) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Either email or phone number must be provided")
                    .addConstraintViolation();
            return false;
        }

        // If phone number is provided, validate its format
        if (hasPhone && !com.henheang.authhub.utils.PhoneNumberUtil.isValidPhoneNumber(signUpRequest.getPhoneNumber())) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Phone number format is invalid")
                    .addPropertyNode("phoneNumber")
                    .addConstraintViolation();
            return false;
        }

        return true;
    }
}