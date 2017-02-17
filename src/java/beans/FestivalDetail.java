/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import db.Festival;
import db.Gallery;
import db.Ticket;
import db.helpers.FestivalHelper;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author KoRiSnIk
 */
@Named
@SessionScoped
public class FestivalDetail implements Serializable {

    private static final FestivalHelper festivalHelper = new FestivalHelper();
    private Festival festival;
    private List<Gallery> pictures;
    private List<Gallery> video;

    private int tickets;

    private long id;

    @Inject
    private Login login;

    public String showDetail(int id) {
        /*System.out.println("MAPIRANI ID JE " + id);
        if (id == 0) {
            FacesContext fc = FacesContext.getCurrentInstance();
            Map<String, String> params = fc.getExternalContext().getRequestParameterMap();
            id = Long.parseLong(params.get("id"));
        }*/
        festival = festivalHelper.getFestivalById(id);
        pictures = festival.getGalleries().stream()
                .filter((gallery) -> {
                    return gallery.getType() == 1;
                })
                .collect(Collectors.toList());
        video = festival.getGalleries().stream()
                .filter((gallery) -> {
                    return gallery.getType() == 2;
                })
                .collect(Collectors.toList());
        registerFestivalView();
        return "/userPages/festivalDetails";
    }

    private void registerFestivalView() {
        festivalHelper.addFestivalView(festival);
    }

    public void reserveTickets() {
        if (login.getLoggedInUser() != null) {
            festivalHelper.reserveTickets(login.getLoggedInUser(), festival, tickets);
        } else {
            festivalHelper.sellAtDesk(festival, tickets);
        }
    }

    public Festival getFestival() {
        return festival;
    }

    public void setFestival(Festival festival) {
        this.festival = festival;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getTickets() {
        return tickets;
    }

    public void setTickets(int tickets) {
        this.tickets = tickets;
    }

    public List<Gallery> getPictures() {
        return pictures;
    }

    public void setPictures(List<Gallery> pictures) {
        this.pictures = pictures;
    }

    public List<Gallery> getVideo() {
        return video;
    }

    public void setVideo(List<Gallery> video) {
        this.video = video;
    }

}
