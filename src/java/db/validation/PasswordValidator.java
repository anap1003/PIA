/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package db.validation;

import javax.validation.*;

/**
 *
 * @author KoRiSnIk
 */
public class PasswordValidator implements ConstraintValidator<Password, String> {

    @Override
    public void initialize(Password constraintAnnotation) {
        
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        int upperCaseLettersCount = 0, lowerCaseLettersCount = 0, numbersCount = 0, specialsCharsCount = 0;
        if (value.length() < 6 || value.length() > 12) {
            return false;
        }
        if (!Character.isAlphabetic(value.charAt(0))) {
            return false;
        }
        for (int i = 0; i < value.length(); i++) {
            if (Character.isLetter(value.charAt(i)) && Character.isUpperCase(value.charAt(i))) {
                upperCaseLettersCount++;
            } else if (Character.isLetter(value.charAt(i)) && Character.isLowerCase(value.charAt(i))) {
                lowerCaseLettersCount++;
            } else if (Character.isDigit(value.charAt(i))) {
                numbersCount++;
            } else {
                specialsCharsCount++;
            }
            if (i > 1 && value.charAt(i) == value.charAt(i-1) && value.charAt(i) == value.charAt(i-2)) {
                return false;
            }
        }
        return upperCaseLettersCount > 0 && lowerCaseLettersCount > 2 && numbersCount > 0 && specialsCharsCount > 0;
    }
    
}
