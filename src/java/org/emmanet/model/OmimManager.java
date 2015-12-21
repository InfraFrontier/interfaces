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

import java.util.List;
import org.emmanet.util.HibernateUtil;
import org.hibernate.HibernateException;
import org.hibernate.Session;

/**
 *
 * @author mrelac
 */
public class OmimManager {
    
    /**
     * Get the <code>OmimDAO</code> by its primary key
     * @param id_omim the primary key
     * @return the <code>OmimDAO</code> by its primary key
     */
    public OmimDAO getOmimByID(int id_omim) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        OmimDAO omimDAO = null;
        try {
            omimDAO = (OmimDAO) session.get(OmimDAO.class, id_omim);
            session.getTransaction().commit();
        } catch (HibernateException e) {
            session.getTransaction().rollback();
            throw e;
        }
        return omimDAO;
    }
    
    /**
     * Returns a list of all omims.
     * 
     * @return a list of all omims
     */
    public List<OmimDAO> getAll() {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        List<OmimDAO> omimList = null;
        try {
            omimList = session.createQuery(
                    "FROM OmimDAO").list();
            session.getTransaction().commit();
        } catch (HibernateException e) {
            session.getTransaction().rollback();
            throw e;
        }
        return omimList;
    }
    
    /**
     * Return the given omim information if it exists.
     * @param omim the omim against which to search
     * @return the <code>OmimDAO</code> matching the given omim, if found; null
     * otherwise
     */
    public static OmimDAO findByOmim(String omim) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        OmimDAO omimDAO;
        try {
            omimDAO = (OmimDAO)session.createQuery("FROM OmimDAO WHERE omim = ?").setParameter(0, omim).uniqueResult();
            session.getTransaction().commit();
        } catch (HibernateException e) {
            session.getTransaction().rollback();
            throw e;
        }
        
        return omimDAO;
    }
    
    /**
     * Save the <code>OmimDAO</code>.
     * @param omimDAO the object to be saved
     */
    public void save(OmimDAO omimDAO) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();

        try {
            session.saveOrUpdate(omimDAO);
            session.getTransaction().commit();

        } catch (HibernateException e) {
            session.getTransaction().rollback();
            throw e;
        }
    }
}
