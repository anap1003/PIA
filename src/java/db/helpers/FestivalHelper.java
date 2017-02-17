/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package db.helpers;

import com.corejsf.hibernate.HibernateUtil;
import org.hibernate.*;
import db.*;
import java.util.*;
import org.hibernate.criterion.*;
import org.hibernate.sql.JoinType;

/**
 *
 * @author KoRiSnIk
 */
public class FestivalHelper {

    private Session session = null;

    public void addFestival(Festival festival, List<Performance> performances, List<Gallery> pictures, List<Gallery> video) {
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = session.beginTransaction();
        try {
            session.save(festival);
            performances.forEach((p) -> {
                session.save(p);
            });
            pictures.forEach((p) -> {
                session.save(p);
            });
            video.forEach((v) -> {
                session.save(v);
            });
            tx.commit();
        } catch (Exception ex) {
            tx.rollback();
        }

    }
    
    private List<Festival> truncateFestivals(Criteria c) {
        c.add(Restrictions.ge("dateEnd", new Date()));
        return c.list();
    }
    
    public List<Festival> getBestRatedFestivals(int count) {
        List<Festival> festivals = new ArrayList<>();
        List<Review> reviews = new ArrayList<>();
        Map<String, Festival> festivalsMap = new HashMap<>();
        Map<String, Double> festivalsAvgRating = new HashMap<>();
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        org.hibernate.Transaction tx = session.beginTransaction();
        try {
            Criteria c = session.createCriteria(Festival.class);
            festivals = c.list();
            
            c = session.createCriteria(Review.class);
            reviews = c.list();
            
            
            for (Festival f : festivals) {
                if (festivalsMap.containsKey(f.getName())) {
                    festivalsMap.put(f.getName(), f);
                }
            }
            tx.commit();
            
        } catch (HibernateException ex) {
            tx.rollback();
        }
        return festivals;
    }
    
    public void addFestivalView (Festival festival) {
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        org.hibernate.Transaction tx = session.beginTransaction();
        try {
            festival.setViews(festival.getViews() + 1);
            session.update(festival);
            tx.commit();
        } catch (HibernateException ex) {
            tx.rollback();
        }
    }
    
    private void saveTicket(Ticket ticket) {
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        org.hibernate.Transaction tx = session.beginTransaction();
        try {
            session.save(ticket);
            tx.commit();
        } catch (HibernateException ex) {
            tx.rollback();
        }
    }
    
    public void reserveTickets(Appuser appuser, Festival festival, int amount) {
        Ticket ticket = new Ticket(appuser, festival, amount);
        saveTicket(ticket);
    }
    
    public void sellAtDesk(Festival festival, int amount) {
        Ticket ticket = new Ticket((new AppUserHelper()).getUnregisteredUser(), festival, amount, true);
        saveTicket(ticket);
    }

    public List<Festival> getFestivals(String name, String location, String artist, Date dateFrom, Date dateTo) {
        List<Festival> festivals = new ArrayList<>();
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        org.hibernate.Transaction tx = session.beginTransaction();
        try {
            Criteria c = session.createCriteria(Festival.class);
            if (name != null && !("".equals(name))) {
                c.add(Restrictions.ilike("name", name, MatchMode.ANYWHERE));
            }
            if (location != null && !("".equals(location))) {
                c.add(Restrictions.ilike("location", location, MatchMode.ANYWHERE));
            }
            if (dateFrom != null) {
                c.add(Restrictions.ge("dateStart", dateFrom));
            }
            if (dateTo != null) {
                c.add(Restrictions.le("dateEnd", dateTo));
            }
            if (artist != null && !("".equals(artist))) {
                c.createAlias("performances", "performance");
                c.add(Restrictions.ilike("performance.artist", artist, MatchMode.ANYWHERE));
            }
            festivals = c.list();
            tx.commit();
        } catch (HibernateException ex) {
            tx.rollback();
        }
        return festivals;
    }
    
    public List<Festival> getDaysEvents(Date date) {
        List<Festival> festivals = new ArrayList<>();
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        org.hibernate.Transaction tx = session.beginTransaction();
        try {
            Criteria c = session.createCriteria(Festival.class);
            c.add(Restrictions.le("dateStart", date));
            c.add(Restrictions.ge("dateEnd", date));
            festivals = c.list();
            tx.commit();
        } catch (HibernateException ex) {
            tx.rollback();
        }
        return festivals;
    }
    
    public List<Gallery> getFestivalPictures(Festival festival) {
        List<Gallery> pictures = new ArrayList<>();
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        org.hibernate.Transaction tx = session.beginTransaction();
        try {
            Criteria c = session.createCriteria(Festival.class);
            c.createAlias("galleries", "gallery");
            c.add(Restrictions.eq("type", 1));
            pictures = c.list();
            tx.commit();
        } catch (HibernateException ex) {
            tx.rollback();
        }
        return pictures;
    }
    
    public List<Gallery> getFestivalVideo(Festival festival) {
        List<Gallery> video = new ArrayList<>();
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        org.hibernate.Transaction tx = session.beginTransaction();
        try {
            Criteria c = session.createCriteria(Festival.class);
            c.createAlias("galleries", "gallery");
            c.add(Restrictions.eq("type", 2));
            video = c.list();
            tx.commit();
        } catch (HibernateException ex) {
            tx.rollback();
        }
        return video;
    }
    
    public Festival getFestivalById(long id) {
        Festival festival = null;
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        org.hibernate.Transaction tx = session.beginTransaction();
        try {
            Criteria c = session.createCriteria(Festival.class);
            c.add(Restrictions.eq("idFestival", id));
            festival = (Festival) c.uniqueResult();
            tx.commit();
        } catch (HibernateException ex) {
            tx.rollback();
        }
        return festival;
    }
}
