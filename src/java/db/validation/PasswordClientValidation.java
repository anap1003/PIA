/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package db.validation;

import java.util.*;
import javax.validation.metadata.ConstraintDescriptor;
import org.primefaces.validate.bean.ClientValidationConstraint;

/**
 *
 * @author KoRiSnIk
 */
public class PasswordClientValidation implements ClientValidationConstraint {
    public static final String MESSAGE_METADATA = "data-password-chars";
    
    @Override
    public Map<String, Object> getMetadata(ConstraintDescriptor cd) {
        Map<String,Object> metadata = new HashMap<>();
        Map attrs = cd.getAttributes();
        Object message = attrs.get("message");    
        if(message != null) {
            metadata.put(MESSAGE_METADATA, message);
        }
        return metadata;
    }

    @Override
    public String getValidatorId() {
        return Password.class.getSimpleName();
    }
    
}
