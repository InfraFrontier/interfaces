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
