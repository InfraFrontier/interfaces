/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.emmanet.model;

import java.util.List;
import org.emmanet.util.HibernateUtil;
import org.hibernate.HibernateException;
import org.hibernate.Session;

/**
 *
 * @author phil
 */
public class RToolsManager {
    
   
    
       public RToolsDAO getRToolsByID(int id) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        RToolsDAO rtd = null;
        try {
            rtd = (RToolsDAO) session.get(RToolsDAO.class,
                    id);
            session.getTransaction().commit();
        } catch (HibernateException e) {
            session.getTransaction().rollback();
            throw e;
        }
        return rtd;
    }
       
    public List getRToolsList(int id) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        List rtd = null;
        try {
            rtd = session.createQuery("FROM RToolsDAO WHERE str_id_str=?").setParameter(0, id).list();
            session.getTransaction().commit();
        } catch (HibernateException e) {
            session.getTransaction().rollback();
            throw e;
        }
        return rtd;
    }

    public void save(RToolsDAO rtDAO) {

        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();

        try {
            session.saveOrUpdate(rtDAO);
            session.getTransaction().commit();

        } catch (HibernateException e) {
            session.getTransaction().rollback();
            throw e;
        }
    }

        public void saveOnly(RToolsDAO rtDAO) {

        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();

        try {
            session.save(rtDAO);
            session.getTransaction().commit();

        } catch (HibernateException e) {
            session.getTransaction().rollback();
            throw e;
        }
    }
    
                   public void saveSQL(int rtlsID, int strainID) {

        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();

        try {
            String sql = "INSERT INTO rtools_strains (str_id_str,rtls_id) VALUES (" + strainID + "," + rtlsID +" )";
            System.out.println("ABOUT TO SAVE RTOOLS with query " + sql);
            session.createSQLQuery(sql);//.setParameter(0,strainID).setParameter(1,rtlsID);
            session.flush();
            session.getTransaction().commit();
           
 System.out.println("SAVED RTOOLS" + strainID);
        } catch (HibernateException e) {
            session.getTransaction().rollback();
            throw e;
        }
    }
    
}
