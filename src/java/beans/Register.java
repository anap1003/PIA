/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import db.Appuser;
import db.helpers.AppUserHelper;
import db.validation.Password;
import db.validation.Username;
import java.io.Serializable;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author KoRiSnIk
 */
@Named
@RequestScoped
public class Register implements Serializable {
    private String name;
    private String surname;
    
    @Username
    private String username;
    @Password
    private String password;
    private String passrepeat;
    
    private String phoneNum;
    private String email;
    
    @Inject
    private Navigate navigate;
    @Inject
    private Admin admin;
    
    private String message = "";
    
    private static AppUserHelper userHelper = new AppUserHelper();
    
    public String register() {
        FacesContext c = FacesContext.getCurrentInstance();
        Appuser user = new Appuser(name, surname, username, password, phoneNum, email);
        if (userHelper.addAppuser(user)) {
            navigate.setPage("index");
            navigate.setHeader("eader");
            navigate.setLeftSidebar("calendar");
            
            message = "You have succesfully signed up, please wait for an admin to approve your registration.";
            c.addMessage("notificationbubble", new FacesMessage("Successful registration", message));
            admin.loadUsers();
            return "/index";
        } else {
            message = "The username and/or email provided is already in use.";
            c.addMessage("notificationbubble", new FacesMessage("Error", message));
            return "";
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassrepeat() {
        return passrepeat;
    }

    public void setPassrepeat(String passrepeat) {
        this.passrepeat = passrepeat;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
/*
    public Navigate getNavigate() {
        return navigate;
    }

    public void setNavigate(Navigate navigate) {
        this.navigate = navigate;
    }*/

    public static AppUserHelper getUserHelper() {
        return userHelper;
    }

    public static void setUserHelper(AppUserHelper userHelper) {
        Register.userHelper = userHelper;
    }
     
}
