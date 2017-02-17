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
@Constraint(validatedBy = UsernameValidator.class)
@ClientConstraint(resolvedBy = UsernameClientValidation.class)
public @interface Username {
    String message() default "The username must be unique!";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
