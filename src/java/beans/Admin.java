/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import db.Appuser;
import db.helpers.AppUserHelper;
import java.io.Serializable;
import java.util.Collection;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author KoRiSnIk
 */
@Named
@SessionScoped
public class Admin implements Serializable {

    private Collection<Appuser> lastLoggedInUsers;
    private Collection<Appuser> unverifiedUsers;

    private static AppUserHelper userHelper = new AppUserHelper();

    private String message;

    public void approveAppuser(Appuser user) {
        message = userHelper.approveUser(user);
        FacesContext context = FacesContext.getCurrentInstance();
        if (!message.isEmpty()) {
            context.addMessage("message", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Evo meeee", message));
        } else {
            context.addMessage("message", new FacesMessage(FacesMessage.SEVERITY_INFO, "User approval", "User " + user.getUsername() + " has been approved."));
        }
        loadUsers();
    }

    public void loadUsers() {
        unverifiedUsers = userHelper.getApprovedUsers(false);
        lastLoggedInUsers = userHelper.getLastLoggedInUsers(10);
    }

    public Admin() {
        unverifiedUsers = userHelper.getApprovedUsers(false);
        lastLoggedInUsers = userHelper.getLastLoggedInUsers(10);
    }

    public Collection<Appuser> getLastLoggedInUsers() {

        return lastLoggedInUsers;
    }

    public void setLastLoggedInUsers(Collection<Appuser> lastLoggedInUsers) {
        this.lastLoggedInUsers = lastLoggedInUsers;
    }

    public Collection<Appuser> getUnverifiedUsers() {
        return unverifiedUsers;
    }

    public void setUnverifiedUsers(Collection<Appuser> unverifiedUsers) {
        this.unverifiedUsers = unverifiedUsers;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
