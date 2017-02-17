/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import java.io.*;
import java.util.*;
import javax.enterprise.context.ApplicationScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;
import javax.inject.Named;
import org.primefaces.model.*;

/**
 *
 * @author KoRiSnIk
 */
@Named
@ApplicationScoped
public class GalleryService {
    public static final Map<String,String> mimeLookupTable = new HashMap<>();
    static{
        mimeLookupTable.put("jpg", "image/jpeg");
        mimeLookupTable.put("jpeg", "image/jpeg");
        mimeLookupTable.put("png", "image/png");
        mimeLookupTable.put("gif", "image/gif");
    }

    public StreamedContent getImage() throws IOException {
        FacesContext context = FacesContext.getCurrentInstance();

        if (context.getCurrentPhaseId() == PhaseId.RENDER_RESPONSE) {
            // So, we're rendering the view. Return a stub StreamedContent so that it will generate right URL.
            return new DefaultStreamedContent();
        }
        else {
            // So, browser is requesting the image. Return a real StreamedContent with the image bytes.
            String filename = context.getExternalContext().getRequestParameterMap().get("filename");
            return new DefaultStreamedContent(new FileInputStream(new File(filename)), mimeLookupTable.get(filename.substring(filename.lastIndexOf('.')+1)));
        }
    }
    
    public StreamedContent getVideo() throws IOException {
        FacesContext context = FacesContext.getCurrentInstance();

        if (context.getCurrentPhaseId() == PhaseId.RENDER_RESPONSE) {
            // So, we're rendering the view. Return a stub StreamedContent so that it will generate right URL.
            return new DefaultStreamedContent();
        }
        else {
            // So, browser is requesting the image. Return a real StreamedContent with the image bytes.
            String filename = context.getExternalContext().getRequestParameterMap().get("filename");
            return new DefaultStreamedContent(new FileInputStream(new File(filename)), "application/x-troff-msvideo");
        }
    }
}
