/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import com.csvreader.CsvReader;
import db.Gallery;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
import db.Festival;
import db.Performance;
import db.helpers.FestivalHelper;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.FileAttribute;
import java.text.ParseException;
import java.util.*;
import java.util.regex.Pattern;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import org.primefaces.json.JSONArray;
import org.primefaces.json.JSONObject;

/**
 *
 * @author KoRiSnIk
 */
@Named
@ViewScoped
public class Import implements Serializable {
    private static FestivalHelper festivalHelper =  new FestivalHelper();
    
    private Festival festival;
    private List<Performance> performances;
    
    private int step = 0;
    private boolean uploaded = false;
    private List<Gallery> pictures = new ArrayList<>();
    private List<Gallery> video = new ArrayList<>();
    private String videoUrl ="";
    private String btnLabel="Next";
    

    private String message = "";

    public Import() {
    }
    
    public void next() {
        step++;
    }
    
    public void saveFestival() {
        festivalHelper.addFestival(festival, performances, pictures, video);
    }
    
    public String discardFestival() {
        //dodati notificationbubble
        return "/adminPages/admin";
    }

    public void uploadCSV(FileUploadEvent event) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        UploadedFile uploadedFile = event.getFile();
        try {
            CsvReader csvReader = new CsvReader(uploadedFile.getInputstream(), Charset.defaultCharset());
            festival = new Festival();
            festival.setDetails("");

            csvReader.readHeaders();
            csvReader.readRecord();
            festival.setName(csvReader.get("Festival"));
            festival.setLocation(csvReader.get("Place"));
            String[] date = csvReader.get("StartDate").split("/");
            festival.setDateStart(new Date(Integer.parseInt(date[2]) - 1900, Integer.parseInt(date[1]), Integer.parseInt(date[0])));
            date = csvReader.get("EndDate").split("/");
            festival.setDateEnd(new Date(Integer.parseInt(date[2]) - 1900, Integer.parseInt(date[1]), Integer.parseInt(date[0])));

            csvReader.skipLine();

            csvReader.readHeaders();
            csvReader.readRecord();
            festival.setDayPrice(Integer.parseInt(csvReader.get("Price")));
            csvReader.readRecord();
            festival.setPacketPrice(Integer.parseInt(csvReader.get("Price")));

            csvReader.skipLine();

            csvReader.readHeaders();
            csvReader.readRecord();
            festival.setMaxTicketsPerUser(Integer.parseInt(csvReader.get("MaxPerUser")));
            festival.setCapacityPerDay(Integer.parseInt(csvReader.get("MaxPerDay")));

            csvReader.skipLine();

            performances = new ArrayList<>();
            csvReader.readHeaders();
            while (true) {
                csvReader.readRecord();
                if (csvReader.getColumnCount() < 5) {
                    break;
                }
                Performance performance = new Performance();
                performance.setArtist(csvReader.get("Performer"));
                date = csvReader.get("StartDate").split("/");
                performance.setDateStart(new Date(Integer.parseInt(date[2]) - 1900, Integer.parseInt(date[1]), Integer.parseInt(date[0])));
                date = csvReader.get("EndDate").split("/");
                performance.setDateEnd(new Date(Integer.parseInt(date[2]) - 1900, Integer.parseInt(date[1]), Integer.parseInt(date[0])));
                String[] timeFormat = csvReader.get("StartTime").split(" ");
                String[] time = timeFormat[0].split(":");
                int hour = Integer.parseInt(time[0]);
                int minute = Integer.parseInt(time[1]);
                int sec = Integer.parseInt(time[2]);
                if ("PM".equals(timeFormat[1]) && !"12".equals(time[0])) {
                    hour += 12;
                }
                Date dateT = new Date();
                dateT.setHours(hour); dateT.setMinutes(minute); dateT.setSeconds(sec);
                performance.setTimeStart(dateT);
                timeFormat = csvReader.get("EndTime").split(" ");
                time = timeFormat[0].split(":");
                hour = Integer.parseInt(time[0]);
                minute = Integer.parseInt(time[1]);
                sec = Integer.parseInt(time[2]);
                if ("PM".equals(timeFormat[1]) && !"12".equals(time[0])) {
                    hour += 12;
                }
                dateT = new Date();
                dateT.setHours(hour); dateT.setMinutes(minute); dateT.setSeconds(sec);
                performance.setTimeEnd(dateT);
                performance.setFestival(festival);
                
                performances.add(performance);
            }
            
            festival.setPerformances(new HashSet<>(performances));
            
            while (csvReader.readRecord()) {
                switch (csvReader.get(0)) {
                    case "Facebook":
                        festival.setFacebook(csvReader.get(1));
                        break;
                    case "Twitter":
                        festival.setTwitter(csvReader.get(1));
                        break;
                    case "Instagram":
                        festival.setInstagram(csvReader.get(1));
                        break;
                    case "YouTube":
                        festival.setYoutube(csvReader.get(1));
                        break;
                }
            }
            
            csvReader.close();
            
            //provera da li imaju sva polja
            
            //festivalHelper.addFestival(festival);
            uploaded = true;
            FacesContext.getCurrentInstance().addMessage("messages", new FacesMessage("Successfully imported festival!"));
        } catch (IOException ex) {
            FacesContext.getCurrentInstance().addMessage("messages", new FacesMessage("Error during importation of festival"));
            Logger.getLogger(Import.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
    public void uploadJSON(FileUploadEvent event) {
        festival = new Festival();
        performances = new ArrayList<>();
        try {
            byte[] filecontent = event.getFile().getContents();
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            String contentAsString = new String(filecontent);
            JSONObject json = new JSONObject(contentAsString.trim());
            JSONObject festivalJSON = json.getJSONObject("Festival");
            if (festivalJSON != null) {
                festival.setName(festivalJSON.getString("Name"));
                festival.setLocation(festivalJSON.getString("Place"));
                String dateString = festivalJSON.getString("StartDate");
                festival.setDateStart(formatter.parse(dateString));
                dateString = festivalJSON.getString("EndDate");
                festival.setDateEnd(formatter.parse(dateString));
                if (festival.getDateStart().after(festival.getDateEnd())) {
                    FacesContext.getCurrentInstance().addMessage("messages", new FacesMessage("End date must be after the start date!"));
                    return;
                }
               
                JSONArray tickets = festivalJSON.getJSONArray("Tickets");
               
                festival.setDayPrice(tickets.getInt(0));
                festival.setPacketPrice(tickets.getInt(1));
               
                festival.setDetails("");
               
                festival.setMaxTicketsPerUser(festivalJSON.getInt("MaxPerUser"));
                festival.setCapacityPerDay(festivalJSON.getInt("MaxPerDay"));
               
                JSONArray performancesJSON = festivalJSON.getJSONArray("PerformersList");
                for (int i = 0; i < performancesJSON.length(); i++) {
                    Performance performance = new Performance();
                    JSONObject performanceObj = performancesJSON.getJSONObject(i);
                    performance.setFestival(festival);
                    performance.setArtist(performanceObj.getString("Performer"));
                    performance.setDateStart(formatter.parse(performanceObj.getString("StartDate")));
                    performance.setDateEnd(formatter.parse(performanceObj.getString("EndDate")));
                   
                    SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm:ss aaa");
                    performance.setTimeStart(timeFormat.parse(performanceObj.getString("StartTime")));
                    performance.setTimeEnd(timeFormat.parse(performanceObj.getString("EndTime")));
                    System.out.println("Nastup: " + performance.getArtist());
                    performances.add(performance);
                }
               
                JSONArray socialArray = festivalJSON.getJSONArray("SocialNetworks");
                for (int i = 0; i < socialArray.length(); i++) {
                    JSONObject socialObject = socialArray.getJSONObject(i);
                    String socialName = socialObject.getString("Name");
                    String socialLink = socialObject.getString("Link");
                   
                    if (socialName.equalsIgnoreCase("Facebook")) {
                        festival.setFacebook(socialLink);
                    }
                    if (socialName.equalsIgnoreCase("Twitter")) {
                        festival.setTwitter(socialLink);
                    }
                    if (socialName.equalsIgnoreCase("Instagram")) {
                        festival.setInstagram(socialLink);
                    }
                    if (socialName.equalsIgnoreCase("YouTube")) {
                        festival.setYoutube(socialLink);
                    }
                }
               
                //izbaciti posle
                //festivalHelper.addFestival(festival, performances, new LinkedList<>());
                uploaded = true;
                FacesContext.getCurrentInstance().addMessage("messages", new FacesMessage("Imported festival!"));
            }
        } catch (ParseException ex) {
            FacesContext.getCurrentInstance().addMessage("messages", new FacesMessage("Error during importation of festival"));
            Logger.getLogger(Import.class.getName()).log(Level.SEVERE, null, ex);
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
    
    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }

    public boolean isUploaded() {
        return uploaded;
    }

    public void setUploaded(boolean uploaded) {
        this.uploaded = uploaded;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Festival getFestival() {
        return festival;
    }

    public void setFestival(Festival festival) {
        this.festival = festival;
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

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getBtnLabel() {
        return btnLabel;
    }

    public void setBtnLabel(String btnLabel) {
        this.btnLabel = btnLabel;
    }

    public List<Performance> getPerformances() {
        return performances;
    }

    public void setPerformances(List<Performance> performances) {
        this.performances = performances;
    }
    
}
