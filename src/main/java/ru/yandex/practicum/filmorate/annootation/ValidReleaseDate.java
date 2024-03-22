package ru.yandex.practicum.filmorate.annootation;

import javax.validation.Constraint;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = ReleaseDateConstraintValidator.class)
public @interface ValidReleaseDate {
    String message() default "Некорректная дата релиза";

    Class<?>[] groups() default {};

    Class<?>[] payload() default {};
}