/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package db.validation;

import java.lang.annotation.*;
import javax.validation.*;
import org.primefaces.validate.bean.ClientConstraint;

/**
 *
 * @author KoRiSnIk
 */
@Documented
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PasswordValidator.class)
@ClientConstraint(resolvedBy = PasswordClientValidation.class)
public @interface Password {
    String message() default "The password must contain between 6 and 12 characters, it must begin with an upper case letter, and it must contain at least 1 uppercase and 3 lowercase letters, 1 number and 1 special case character.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
