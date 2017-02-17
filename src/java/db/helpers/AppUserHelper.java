/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package db.helpers;

import com.corejsf.hibernate.HibernateUtil;
import org.hibernate.*;
import db.Appuser;
import java.util.*;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author KoRiSnIk
 */
public class AppUserHelper {

    private Session session;

    public AppUserHelper() {
    }
    
    public Appuser getUnregisteredUser() {
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        org.hibernate.Transaction tx = session.beginTransaction();
        Appuser appuser = null;
        try {
            Criteria c = session.createCriteria(Appuser.class);
            c.add(Restrictions.eq("username", "unregistered"));
            appuser = (Appuser) c.uniqueResult();
            tx.commit();
        } catch (HibernateException ex) {
            tx.rollback();
        }
        if (appuser == null) {
            appuser = createUnregisteredUser();
        }
        return appuser;
    }
    
    private Appuser createUnregisteredUser() {
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        org.hibernate.Transaction tx = session.beginTransaction();
        Appuser appuser = new Appuser("unregistered", "unregistered", "unregistered", "unregistered", "unregistered", "unregistered");
        try {
            session.save(appuser);
            tx.commit();
        } catch (HibernateException ex) {
            tx.rollback();
        }
        return appuser;
    }

    public String approveUser(Appuser user) {
        String message = "";
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        org.hibernate.Transaction tx = session.beginTransaction();
        try {
            user.setIsApproved(true);
            session.update(user);
            tx.commit();
        } catch (Exception ex) {
            tx.rollback();
            message = ex.getLocalizedMessage();
            ex.printStackTrace();
        }
        return message;
    }

    public Appuser getAppuser(String username) {
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        Appuser appuser = null;
        org.hibernate.Transaction tx = session.beginTransaction();
        try {
            Query q = session.createQuery("from Appuser as appuser where appuser.username = '" + username + "'");
            List l = q.list();
            appuser = (Appuser) q.uniqueResult();
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        }
        return appuser;
    }

    public Appuser getAppuser(String username, String password) {
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        Appuser u = null;
        org.hibernate.Transaction tx = session.beginTransaction();
        try {
            Query q = session.createQuery("from Appuser as appuser where appuser.username = '" + username + "' AND appuser.password = '" + password + "'");
            u = (Appuser) q.uniqueResult();
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        }
        return u;
    }

    public boolean addAppuser(Appuser user) {
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        boolean ret = false;
        org.hibernate.Transaction tx = session.beginTransaction();
        try {
            Query q = session.createQuery("from Appuser as appuser where appuser.username = '" + user.getUsername() + "'");
            if (q.uniqueResult() == null) {
                session.saveOrUpdate(user);
                ret = true;
            }
            tx.commit();
        } catch (Exception e) {
            ret = false;
            tx.rollback();
        }
        //session.close();
        return ret;
    }

    public List<Appuser> getApprovedUsers(boolean isApproved) {
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        List<Appuser> users = Collections.EMPTY_LIST;
        org.hibernate.Transaction tx = session.beginTransaction();
        try {
            Criteria c = session.createCriteria(Appuser.class);
            c.add(Restrictions.eq("isApproved", isApproved));
            users = c.list();
            tx.commit();
        } catch (Exception ex) {
            tx.rollback();
            ex.printStackTrace();
        }
        return users;
    }

    public List<Appuser> getLastLoggedInUsers(int count) {
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        List<Appuser> users = Collections.EMPTY_LIST;
        org.hibernate.Transaction tx = session.beginTransaction();
        try {
            Criteria c = session.createCriteria(Appuser.class);
            c.add(Restrictions.eq("isApproved", true));
            c.add(Restrictions.eq("isAdmin", false));
            c.addOrder(Order.desc("lastLogin"));
            users = c.list();
            tx.commit();
        } catch (Exception ex) {
            tx.rollback();
            ex.printStackTrace();
        }
        return users;
    }

    public List<Appuser> getAppusersWithType(boolean isAdmin) {
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        List<Appuser> users = Collections.EMPTY_LIST;
        org.hibernate.Transaction tx = session.beginTransaction();
        try {
            Criteria c = session.createCriteria(Appuser.class);
            c.add(Restrictions.eq("isAdmin", isAdmin)); //MENJAJ

            users = c.list();
            tx.commit();
        } catch (Exception ex) {
            tx.rollback();
            ex.printStackTrace();
        }
        return users;
    }

    public void deleteAppuser(Appuser u) {
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        org.hibernate.Transaction tx = session.beginTransaction();
        try {
            session.delete(u);
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        }
    }

    public void updateAppuserTimeStamp(Appuser user) {
        user.setLastLogin(new Date());
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        org.hibernate.Transaction tx = session.beginTransaction();
        try {
            session.update(user);
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        }

    }

    public List<Appuser> getRecent() {
        List<Appuser> users = Collections.EMPTY_LIST;
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        org.hibernate.Transaction tx = session.beginTransaction();
        try {
            Criteria c = session.createCriteria(Appuser.class);
            c.addOrder(Order.desc("lastLogin")); //MENJAJ
            c.setMaxResults(10);
            users = c.list();
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        }
        return users;
    }
}
