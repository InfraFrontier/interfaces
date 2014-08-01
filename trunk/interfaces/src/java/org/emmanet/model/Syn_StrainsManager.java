/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.emmanet.model;

import java.util.Set;
import org.emmanet.util.HibernateUtil;
import org.hibernate.HibernateException;
import org.hibernate.Session;

/**
 *
 * @author phil
 */
public class Syn_StrainsManager {

    public Syn_StrainsDAO getSynStrainsByID(int id) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        Syn_StrainsDAO ssd = null;
        try {
            ssd = (Syn_StrainsDAO) session.get(Syn_StrainsDAO.class,
                    id);
            session.getTransaction().commit();
        } catch (HibernateException e) {
            session.getTransaction().rollback();
            throw e;
        }
        return ssd;
    }
    
     public Set getSetSynStrainsByID(int id) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        Set ssd = null;
        try {
            ssd = (Set) session.get(Syn_StrainsDAO.class,
                    id);
            session.getTransaction().commit();
        } catch (HibernateException e) {
            session.getTransaction().rollback();
            throw e;
        }
        return ssd;
    }

    public void save(Syn_StrainsDAO ssDAO) {

        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
System.out.println("DAO value is " + ssDAO.getName().toString());
        try {
            session.saveOrUpdate(ssDAO);
            session.getTransaction().commit();

        } catch (HibernateException e) {
            session.getTransaction().rollback();
            throw e;
        }
    }
}
