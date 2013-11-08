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
public class SubmissionsManager {

    public SubmissionsDAO getSubByID(int id) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        SubmissionsDAO sd = null;
        try {
            sd = (SubmissionsDAO) session.get(SubmissionsDAO.class,
                    "" + id);
            session.getTransaction().commit();
        } catch (HibernateException e) {
            session.getTransaction().rollback();
            throw e;
        }

        return sd;
    }
    
      public List getSubMutationsBySUBID(int id) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        List smd = null;
        try {
             smd = session.createQuery(
                    "FROM SubmissionMutationsDAO WHERE id_sub=?").setParameter(0, id).list();
            session.getTransaction().commit();
        } catch (HibernateException e) {
            session.getTransaction().rollback();
            throw e;
        }

        return smd;
    }

    public List getSubBibliosBySUBID(int id) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        List sbd = null;
        try {
           sbd = session.createQuery(
                    "FROM SubmissionBibliosDAO WHERE sub_id_sub=?").setParameter(0, id).list();
            session.getTransaction().commit();
        } catch (HibernateException e) {
            session.getTransaction().rollback();
            throw e;
        }

        return sbd;
    }

    public List getSubsByEmail(String email) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        List prevSubmission = null;
        try {
            prevSubmission = session.createQuery(
                    "FROM SubmissionsDAO WHERE submitter_email=? ORDER BY timestamp DESC").setParameter(0, email).setMaxResults(1).list();
            session.getTransaction().commit();
        } catch (HibernateException e) {
            session.getTransaction().rollback();
            throw e;
        }
        return prevSubmission;
    }        
        
    public List getesCellLines(String q) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        List esCellLines = null;
        System.out.println("Query string from manager == " + q);
        try {
            esCellLines = session.createSQLQuery(
                    "SELECT name FROM es_cell_lines WHERE name like '" + q + "%'").list();
            session.getTransaction().commit();
          System.out.println("ES response " + esCellLines.size());  
        } catch (HibernateException e) {
            session.getTransaction().rollback();
            throw e;
        }
        return esCellLines;
    }
            
        
        public void save(CategoriesStrainsDAO csDAO) {
        

        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();

        try {
            session.saveOrUpdate(csDAO);
            session.getTransaction().commit();

        } catch (HibernateException e) {
            session.getTransaction().rollback();
            throw e;
        }
    }
        
    public void saveSQL(int catID, int strainID) {

        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();

        try {
            System.out.println("ABOUT TO SAVE CATEGORIES");
            session.createSQLQuery("INSERT INTO categories_strains (cat_id_cat,str_id_str) VALUES (?,?)").setParameter(1, strainID).setParameter(0, catID);//
            System.out.println("SAVED CATEGORIES" + strainID);
            session.getTransaction().commit();

        } catch (HibernateException e) {
            session.getTransaction().rollback();
            throw e;
        }
    }

    public void save(SubmissionsDAO sDAO) {
        System.out.print("SUBMISSION DAO IS::" + sDAO.getId_sub());

        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();

        try {
            session.saveOrUpdate(sDAO);
            session.getTransaction().commit();

        } catch (HibernateException e) {
            session.getTransaction().rollback();
            throw e;
        }
    }

    public void save(SubmissionBibliosDAO sbDAO) {

        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();

        try {
            System.out.println("AT SUBMISSION BIBLIOS SAVE OR UPDATE");
            session.saveOrUpdate(sbDAO);
            session.getTransaction().commit();

        } catch (HibernateException e) {
            session.getTransaction().rollback();
            throw e;
        }
    }
}
