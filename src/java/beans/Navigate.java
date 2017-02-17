/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import db.Festival;
import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.FacesComponent;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author KoRiSnIk
 */
@Named
@SessionScoped
public class Navigate implements Serializable {

    private String page = "index";
    private String header = "header";
    private String leftSidebar = "calendar";
    
    private Festival festivalDetail;

    @Inject
    private Login login;
    @Inject
    private Admin admin;
    
    
    public void test() {
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage("message", new FacesMessage(FacesMessage.SEVERITY_INFO, "Evo meeee", "Ovo je veoma bitna poruka."));
        //return "";
    }
    
    public String goToFestivalDetails(Festival festival) {
        festivalDetail = festival;
        return "/userPages/festivalDetails";
    }
    
    public void registerLogin() {
        admin.loadUsers();
    }
    
    public String goToImportFestival() {
        return "/adminPages/importFestival";
    }
    
    public String goToAddFestival() {
        return "/adminPages/addFestival";
    }

    public String goToHome() {
        if (login.isNotLoggedIn()) {
            page = "index";
            header = "header";
            leftSidebar = "calendar";
            return "/index";
        } else if (login.getLoggedInUser().isIsAdmin()) {
            page = "adminPage";
            header = "adminHeader";
            leftSidebar = "empty";
            return "/adminPages/admin";
        } else {
            page = "userPage";
            header = "userHeader";
            leftSidebar = "calendar";
            
        }
        return "/userPages/user";
    }

    public String goToRegister() {
        page = "register";
        header = "registerHeader";
        leftSidebar = "empty";
        return "/register";
    }

    public String goToUsersInformation() {
        page = "usersInfo";
        header = "adminHeader";
        leftSidebar = "empty";
        admin.loadUsers();
        return "/adminPages/usersInfo";
    }

    public Navigate() {
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getLeftSidebar() {
        return leftSidebar;
    }

    public void setLeftSidebar(String leftSidebar) {
        this.leftSidebar = leftSidebar;
    }

    public Login getLogin() {
        return login;
    }

    public void setLogin(Login login) {
        this.login = login;
    }

    public Festival getFestivalDetail() {
        return festivalDetail;
    }

    public void setFestivalDetail(Festival festivalDetail) {
        this.festivalDetail = festivalDetail;
    }
    
}
