/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package db.validation;

import db.helpers.AppUserHelper;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 *
 * @author KoRiSnIk
 */
public class UsernameValidator implements ConstraintValidator<Username, String> {
    private static final AppUserHelper HELPER = new AppUserHelper();
    
    @Override
    public void initialize(Username constraintAnnotation) {
        
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if(value == null)
            return false;

        return HELPER.getAppuser(value) == null;
    }
}
