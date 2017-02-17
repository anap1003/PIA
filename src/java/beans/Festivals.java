/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import db.*;
import db.helpers.FestivalHelper;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.FileAttribute;
import java.util.*;
import java.util.regex.Pattern;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author KoRiSnIk
 */
@Named
@ViewScoped
public class Festivals implements Serializable {
    private static FestivalHelper festivalHelper =  new FestivalHelper();
    private int step;
    
    private Festival festival;
    private Performance performance;
    private Date dateStart, dateEnd;
    private Date timeStart, timeEnd;
    
    private String videoUrl = "";
    
    private List<Performance> performances;
    private List<Gallery> pictures;
    private List<Gallery> video;

    @PostConstruct
    public void init() {
        festival = new Festival();
        performance = new Performance();
        performances = new ArrayList<>();
        pictures = new ArrayList<>();
        video = new ArrayList<>();
    }
    
    public void saveFestival() {
        festivalHelper.addFestival(festival, performances, pictures, video);
    }
    
    public String discardFestival() {
        //dodati notificationbubble
        return "/adminPages/admin";
    }
    
    public void addPerformance() {
        boolean errorFound = false;
        String message = "Uspesno dodat nastup!";
        FacesContext context = FacesContext.getCurrentInstance();
        if (performance.getArtist()== null || "".equals(performance.getArtist())) {
            message = "Morate da unesete izvodjaca!";
            errorFound = true;
        } else {
            if (performance.getDateStart()== null) {
                message = "Morate da unesete datum pocetka!";
                errorFound = true;
            } else {
                if (performance.getDateEnd()== null) {
                    message = "Morate da unesete datum zavrsetka!";
                    errorFound = true;
                } else {
                    if (performance.getTimeStart()== null) {
                        message = "Morate da unesete vreme pocetka!";
                        errorFound = true;
                    } else {
                        if (performance.getTimeEnd()== null) {
                            message = "Morate da unesete vreme zavrsetka!";
                            errorFound = true;
                        }
                    }
                }
            }
        }
        if (errorFound) {
            performance.setDateStart(null);
            performance.setDateEnd(null);
            performance.setTimeStart(null);
            performance.setTimeEnd(null);
            context.addMessage("message", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Neuspesno dodavanje nastupa!", message));
        } else {
            performances.add(performance);
            performance = new Performance();
            context.addMessage("message", new FacesMessage(FacesMessage.SEVERITY_INFO, "Sve je u redu!", message));
        }
    }
    
    public void uploadImage(FileUploadEvent event) {
        FacesContext context = FacesContext.getCurrentInstance();
        UploadedFile uploadedFile = event.getFile();
        
        String path = "d:\\piauploads\\" + festival.getName();
        Path tempfile;
        try (InputStream istream = uploadedFile.getInputstream()) {
            Path folder;
            folder = Paths.get(path);
            if (!folder.toFile().exists()) {
                folder.toFile().mkdir();
            }

            String filename = uploadedFile.getFileName();
            String[] split = filename.split("\\.");
            String extension = "." + split[split.length - 1];

            filename = filename.subSequence(0, filename.lastIndexOf('.')).toString();
            tempfile = Files.createTempFile(folder, filename, extension, new FileAttribute<?>[]{});

            Files.copy(istream, tempfile, StandardCopyOption.REPLACE_EXISTING);

            Gallery gallery = new Gallery();
            gallery.setFestival(festival);
            gallery.setIsApproved(true);
            gallery.setPath(tempfile.toAbsolutePath().toString());
            gallery.setType(1);
            pictures.add(gallery);
            context.addMessage("messages", new FacesMessage(FacesMessage.SEVERITY_INFO, "Upload successful!", uploadedFile.getFileName()));
            
        } catch (IOException ex) {
            context.addMessage("messages", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Upload unsuccessful!", uploadedFile.getFileName()));
        }
    }
    
    public void uploadVideo() {
        FacesContext context = FacesContext.getCurrentInstance();
        String videoUrlFormatted = "https://www.youtube.com/v/";
        String videoUrlLowerCase = videoUrl.toLowerCase();
        Pattern regVariant1 = Pattern.compile("(http(s)?://)?(www\\.)?youtube.com/watch\\?v\\=[a-zA-Z0-9\\_\\-\\=]+");
        Pattern regVariant2 = Pattern.compile("(http(s)?://)?(www\\.)?youtu.be/[a-zA-Z0-9]+");
        
        boolean matchFound = false;
        
        if( regVariant1.matcher(videoUrlLowerCase).matches() ){
            videoUrlFormatted = videoUrlFormatted.concat(videoUrl.substring(videoUrl.lastIndexOf('=')+1, videoUrl.length()));
            matchFound = true;
        }
        
        if( !matchFound && regVariant2.matcher(videoUrlLowerCase).matches() ){
            videoUrlFormatted = videoUrlFormatted.concat(videoUrl.substring(videoUrl.lastIndexOf('/')+1, videoUrl.length()));
            matchFound = true;
        }
        
        if(matchFound){
            Gallery gallery = new Gallery();
            gallery.setFestival(festival);
            gallery.setIsApproved(true);
            gallery.setPath(videoUrlFormatted);
            gallery.setType(2);
            video.add(gallery);
            context.addMessage("notificationbubble", new FacesMessage(FacesMessage.SEVERITY_INFO, "You have successfully added a video!", videoUrl));
        } else {
            context.addMessage("notificationbubble", new FacesMessage(FacesMessage.SEVERITY_INFO, "Neispravan format url-a!", videoUrl));
        }
    } 
    
    public Date performanceMinEndDate() {
        return performance.getDateStart() != null ? performance.getDateStart() : festival.getDateStart();
    }
    
    public void saveInfoAndContinue() {
        step++;
    }
    
    public Festival getFestival() {
        return festival;
    }

    public void setFestival(Festival festival) {
        this.festival = festival;
    }

    public Performance getPerformance() {
        return performance;
    }

    public void setPerformance(Performance performance) {
        this.performance = performance;
    }

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }

    public Date getDateStart() {
        return dateStart;
    }

    public void setDateStart(Date dateStart) {
        this.dateStart = dateStart;
    }

    public Date getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(Date dateEnd) {
        this.dateEnd = dateEnd;
    }

    public Date getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(Date timeStart) {
        this.timeStart = timeStart;
    }

    public Date getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(Date timeEnd) {
        this.timeEnd = timeEnd;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public List<Performance> getPerformances() {
        return performances;
    }

    public void setPerformances(List<Performance> performances) {
        this.performances = performances;
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
