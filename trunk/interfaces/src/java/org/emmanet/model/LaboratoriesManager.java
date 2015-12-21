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
 * 
 * 
 */
import java.util.Iterator;
import java.util.List;
import org.emmanet.util.HibernateUtil;
import org.hibernate.HibernateException;
import org.hibernate.Session;

public class LaboratoriesManager {

    public String getLabIDByCode(String labCode) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        String labID;
        try {
            System.out.println("");
            System.out.println("CODE IS :: " + labCode);
            System.out.println("");
            labID = (String) session.createQuery(
                    "SELECT id_labo FROM LabsDAO WHERE code = ?").setParameter(0, labCode).uniqueResult();
            session.getTransaction().commit();
        } catch (HibernateException e) {
            session.getTransaction().rollback();
            throw e;
        }
        return labID;
    }

    public List getArchivesByCode() {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        List archive;
        try {
            archive = session.createQuery(
                    "FROM LabsDAO WHERE authority IS NOT NULL" + " ORDER BY code").list();
            session.getTransaction().commit();
        } catch (HibernateException e) {
            session.getTransaction().rollback();
            throw e;
        }

        return archive;
    }

    public LabsDAO getLabByStrainID(String id) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        LabsDAO lsDAO = null;
        try {
            lsDAO = (LabsDAO) session.get(LabsDAO.class,
                    id);
            session.getTransaction().commit();
        } catch (HibernateException e) {
            session.getTransaction().rollback();
            throw e;
        }
        return lsDAO;
    }
    
        public LabsDAO getLabCheck(String queryParam, String field) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        LabsDAO ld = new LabsDAO();
        List labs;
        try {
            labs = session.createQuery(
                    "FROM LabsDAO WHERE " + field + "=?").setParameter(0, queryParam).list();//poss to bring back more than 1 but we will only use the first
            session.getTransaction().commit();
            
        } catch (HibernateException e) {
            session.getTransaction().rollback();
            throw e;
        }
        
        for(Iterator it = labs.listIterator();it.hasNext();){
            ld=(LabsDAO) it.next();
            break;
        }
        return ld;
    }
        
    public void save(LaboratoriesStrainsDAO lsDAO) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        try {
          //  System.out.println("AT UPDATE POINT THE VALUE OF lsDAO lab_id_labo IS... " + lsDAO.getLab_id_labo());
          //  System.out.println("AT UPDATE POINT THE VALUE OF lsDAO STR_ID_STR IS... " + lsDAO.getStr_id_str());
            //session.saveOrUpdate(lsDAO);
            session.createSQLQuery("insert into laboratories_strains (lab_id_labo, str_id_str) values ( " + lsDAO.getLab_id_labo() + "," + lsDAO.getStr_id_str() + ")");
            session.getTransaction().commit();
        } catch (HibernateException e) {
            session.getTransaction().rollback();
            throw e;
        }
    }
    
        public void save(LabsDAO labsDAO) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        try {
           session.saveOrUpdate(labsDAO);
            session.getTransaction().commit();
        } catch (HibernateException e) {
            session.getTransaction().rollback();
            throw e;
        }
    }
}
