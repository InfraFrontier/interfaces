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
public class Strains_OmimManager {
    /**
     * Get the <code>Strains_OmimDAO</code> by its primary key
     * @param id_strains_omim the primary key
     * @return the <code>Strains_OmimDAO</code> by its primary key
     */
    public Strains_OmimDAO getStrains_OmimByID(int id_strains_omim) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        Strains_OmimDAO strainsOmimDAO = null;
        try {
            strainsOmimDAO = (Strains_OmimDAO) session.get(OmimDAO.class, id_strains_omim);
            session.getTransaction().commit();
        } catch (HibernateException e) {
            session.getTransaction().rollback();
            throw e;
        }
        
        return strainsOmimDAO;
    }
    
    /**
     * Return a list of <code>Strains_OmimDAO</code> matching the given strain id
     * @param id_strains the strain id against which to search
     * @return a list of <code>Strains_OmimDAO</code> matching the given strain id.
     *         If no entries are found, an empty list is returned.
     */
    public List<Strains_OmimDAO> findById_Strains(int id_strains) {
        List<Strains_OmimDAO> strainsOmimDAOList;
        
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        
        try {
            strainsOmimDAOList = (List<Strains_OmimDAO>)session.createQuery("FROM Strains_OmimDAO WHERE id_strains = ?").setParameter(0, id_strains).list();
            session.getTransaction().commit();
        } catch (HibernateException e) {
            session.getTransaction().rollback();
            throw e;
        }
        
        return strainsOmimDAOList;
    }
    
    /**
     * Save the <code>Strains_OmimDAO</code>.
     * @param strainsOmimDAO the object to be saved
     */
    public void save(Strains_OmimDAO strainsOmimDAO) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();

        try {
            session.saveOrUpdate(strainsOmimDAO);
            session.getTransaction().commit();

        } catch (HibernateException e) {
            session.getTransaction().rollback();
            throw e;
        }
    }
}
