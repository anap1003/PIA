package db;
// Generated Feb 10, 2017 10:08:47 PM by Hibernate Tools 4.3.1



/**
 * Notification generated by hbm2java
 */
public class Notification  implements java.io.Serializable {


     private Long idNotification;
     private Appuser appuser;
     private String message;
     private boolean seen;

    public Notification() {
    }

    public Notification(Appuser appuser, String message, boolean seen) {
       this.appuser = appuser;
       this.message = message;
       this.seen = seen;
    }
   
    public Long getIdNotification() {
        return this.idNotification;
    }
    
    public void setIdNotification(Long idNotification) {
        this.idNotification = idNotification;
    }
    public Appuser getAppuser() {
        return this.appuser;
    }
    
    public void setAppuser(Appuser appuser) {
        this.appuser = appuser;
    }
    public String getMessage() {
        return this.message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    public boolean isSeen() {
        return this.seen;
    }
    
    public void setSeen(boolean seen) {
        this.seen = seen;
    }




}


