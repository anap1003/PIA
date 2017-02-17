/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import db.Festival;
import db.helpers.FestivalHelper;
import java.io.Serializable;
import java.util.*;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

/**
 *
 * @author KoRiSnIk
 */
@Named
@ViewScoped
public class Populate implements Serializable {

    private static final FestivalHelper festivalHelper = new FestivalHelper();

    private List<Festival> festivals;
    private String name, location, artist;
    private Date dateFrom, dateTo;
    
    private Date day;

    private int i;

    @PostConstruct
    public void init() {
        name = null;
        location = null;
        artist = null;
        dateTo = null;
        dateFrom = null;
        refreshFestivals();
    }

    private boolean isSearching() {
        if (name != null && !("".equals(name))) {
            return true;
        }
        if (location != null && !("".equals(location))) {
            return true;
        }
        if (artist != null && !("".equals(artist))) {
            return true;
        }
        return dateFrom != null || dateTo != null;
    }

    public void refreshFestivals() {
        if (!isSearching()) {
            festivals = festivalHelper.getBestRatedFestivals(10);
        } else {
            festivals = festivalHelper.getFestivals(name, location, artist, dateFrom, dateTo);
        }
        i++;
    }
    
    public String getDaysEvents() {
        festivals = festivalHelper.getDaysEvents(day);
        return "";
    }

    public List<Festival> getFestivals() {
        return festivals;
    }

    public void setFestivals(List<Festival> festivals) {
        this.festivals = festivals;
    }

    public int getI() {
        return i;
    }

    public void setI(int i) {
        this.i = i;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Date getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(Date dateFrom) {
        this.dateFrom = dateFrom;
    }

    public Date getDateTo() {
        return dateTo;
    }

    public void setDateTo(Date dateTo) {
        this.dateTo = dateTo;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public Date getDay() {
        return day;
    }

    public void setDay(Date day) {
        this.day = day;
    }
    
}
