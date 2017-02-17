/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import db.*;
import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

/**
 *
 * @author KoRiSnIk
 */
@Named
@SessionScoped
public class Edit implements Serializable {
    private Festival festival;
    
    public String editFestival(Festival festival) {
        this.festival = festival;
        return "/adminPages/editFestival";
    }
}
