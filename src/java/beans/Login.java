/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import db.Appuser;
import db.helpers.AppUserHelper;
import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpSession;

/**
 *
 * @author KoRiSnIk
 */
@Named
@SessionScoped
public class Login implements Serializable{
    private String username;
    private String password;
    
    private String message;
    
    @Inject
    private Navigate navigate;
    
    private Appuser loggedInUser;
    private boolean notLoggedIn = true;
    
    private static AppUserHelper userHelper = new AppUserHelper();
    private static final int NUM_OF_WARNINGS_ALLOWED = 2;

    public String login() {
        String ret = "index";
        FacesContext c = FacesContext.getCurrentInstance();
        loggedInUser = userHelper.getAppuser(username, password);
        if (loggedInUser != null) {
            if (loggedInUser.getWarnings() <= NUM_OF_WARNINGS_ALLOWED) {
                if (loggedInUser.isIsApproved()) {
                    if (loggedInUser.isIsAdmin()) {
                        navigate.setPage("adminPage");
                        navigate.setHeader("adminHeader");
                        navigate.setLeftSidebar("empty");
                        ret = "adminPages/admin.xhtml";
                    } else {
                        navigate.setHeader("userHeader");
                        navigate.setPage("userPage");
                        navigate.setLeftSidebar("calendar");
                        ret = "userPages/user.xhtml";
                    }
                    message = "Welcome " + loggedInUser.getName();
                    c.addMessage("notificationbubble", new FacesMessage("Successful login", message));
                    userHelper.updateAppuserTimeStamp(loggedInUser);
                    notLoggedIn = false;
                    navigate.registerLogin();
                } else {
                    message = "Please wait for the admin to verify your registration.";
                    notLoggedIn = true;
                    c.addMessage("notificationbubble", new FacesMessage("Verification process", message));
                }
            } else {
                message = "Sorry, your account has been banned!";
                c.addMessage("notificationbubble", new FacesMessage("Permanent ban", message));
            }
        } else {
            message = "You have to register first!";
            navigate.setHeader("registerHeader");
            navigate.setPage("register");
            navigate.setLeftSidebar("empty");
            c.addMessage("notificationbubble", new FacesMessage("Registration required", message));
        }
        return ret;
    }
    
    public String logout() {
        FacesContext c = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) c.getExternalContext().getSession(false);
        session.invalidate();
        
        //brisanje polja?
        loggedInUser = null;
        notLoggedIn = true;
        password = "";
        
        navigate.setHeader("header");
        navigate.setPage("index");
        navigate.setLeftSidebar("calendar");
        return "/index";
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Navigate getNavigate() {
        return navigate;
    }

    public void setNavigate(Navigate navigate) {
        this.navigate = navigate;
    }

    public static AppUserHelper getUserHelper() {
        return userHelper;
    }

    public static void setUserHelper(AppUserHelper userHelper) {
        Login.userHelper = userHelper;
    }

    public Appuser getLoggedInUser() {
        return loggedInUser;
    }

    public void setLoggedInUser(Appuser loggedInUser) {
        this.loggedInUser = loggedInUser;
    }

    public boolean isNotLoggedIn() {
        return notLoggedIn;
    }

    public void setNotLoggedIn(boolean notLoggedIn) {
        this.notLoggedIn = notLoggedIn;
    }
}
