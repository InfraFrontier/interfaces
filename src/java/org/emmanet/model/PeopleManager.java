/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.emmanet.model;

/*
 * #%L
 * InfraFrontier
 * $Id:$
 * $HeadURL:$
 * %%
 * Copyright (C) 2015 EMBL-European Bioinformatics Institute
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

/**
 *
 * @author phil
 */
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.emmanet.util.HibernateUtil;
import org.hibernate.HibernateException;
import org.hibernate.Session;

public class PeopleManager {

    public PeopleDAO getPerson(String id) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        //List personDetails = null;
        PeopleDAO personDetails;
        try {
            /* personDetails = (PeopleDAO)session.createQuery(
            "FROM PeopleDAO WHERE id_per = ?").setParameter(0, id);*/
            personDetails = (PeopleDAO) session.get(PeopleDAO.class, id);
            session.getTransaction().commit();
        } catch (HibernateException e) {
            session.getTransaction().rollback();
            throw e;
        }
        return personDetails;
    }

    public List sciMails() {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        List sciMails = null;
        try {
            sciMails = session.createSQLQuery(
                    "SELECT DISTINCT e_mail,country FROM people p,roles_people rp,laboratories l " +
                    "WHERE role_id=4 " +
                    "AND (p.id_per=rp.per_id_per AND p.lab_id_labo=l.id_labo " +
                    "AND e_mail is not null AND e_mail LIKE '%@%' AND country IS NOT NULL) " +
                    "ORDER BY e_mail").list();
            session.getTransaction().commit();
        } catch (HibernateException e) {
            session.getTransaction().rollback();
            throw e;
        }
        return sciMails;
    }
    
    public List getPeopleByEMail(String email) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        List personDetails;
        try {
            personDetails = session.createQuery("FROM PeopleDAO WHERE e_mail=? AND e_mail is not null AND e_mail LIKE '%@%'").setParameter(0, email).list();
            session.getTransaction().commit();
        } catch (HibernateException e) {
            session.getTransaction().rollback();
            throw e;
        }
        return personDetails;
    }
    
        public String ilarID(String labcode) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
       String id="";
        List iId = new LinkedList();
        
        try {
             iId = session.createQuery(
                    "FROM IlarDAO " +
                    "WHERE labcode=? AND status='active'").setParameter(0, labcode).list();
            session.getTransaction().commit();
        } catch (HibernateException e) {
            session.getTransaction().rollback();
            throw e;
        }
        for(Iterator it = iId.listIterator(); it.hasNext();){
            IlarDAO obj =  (IlarDAO)it.next();
            id="" + obj.getId();
        }
        System.out.println("PEOPLE MANAGER ILARID IS RETURNING==" + id);
        return id;
    }
    
        public void save(PeopleDAO pDAO) {

        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();

        try {
            session.saveOrUpdate(pDAO);
            session.getTransaction().commit();

        } catch (HibernateException e) {
            session.getTransaction().rollback();
            throw e;
        }
    }
}
