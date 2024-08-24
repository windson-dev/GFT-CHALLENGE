package com.challenge.gft.annotations;

import com.challenge.gft.annotations.impl.DocumentValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = DocumentValidator.class)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ValidDocument {

    String message() default "Invalid document format or document does not exist";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}