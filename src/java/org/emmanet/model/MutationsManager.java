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
import java.math.BigInteger;
import java.util.List;
import org.apache.log4j.Logger;
import org.emmanet.jobs.EmmaBiblioJOB;
import org.emmanet.util.HibernateUtil;
import org.emmanet.util.Utils;
import org.hibernate.HibernateException;
import org.hibernate.Session;

public class MutationsManager {
    
    protected static Logger logger = Logger.getLogger(EmmaBiblioJOB.class);

    public MutationsDAO getMutByID(int id) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        MutationsDAO md = null;
        try {

            md = (MutationsDAO) session.get(MutationsDAO.class,
                    id);
            session.getTransaction().commit();
        } catch (HibernateException e) {
            session.getTransaction().rollback();
            throw e;
        }

        return md;
    }

    public List getMutationIDsByStrain(int strID) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        List mutIds = null;
        try {
            mutIds = session.createQuery(
                    "FROM MutationsStrainsDAO WHERE str_id_str=?").setParameter(0, strID).list();
            session.getTransaction().commit();
        } catch (HibernateException e) {
            session.getTransaction().rollback();
            throw e;
        }
        return mutIds;
    }
    
    public List MutationInExistence(int bgId, String main, String Sub, String dom) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        List muts = null;
        try {
            muts = session.createQuery(
                    "FROM MutationsStrainsDAO WHERE bg_id_bg=? AND main_type=? AND sub_type=? AND dominance=?")
                    .setParameter(0, bgId)
                    .setParameter(1, main)
                    .setParameter(2, dom).list();

            session.getTransaction().commit();
        } catch (HibernateException e) {
            session.getTransaction().rollback();
            throw e;
        }
        return muts;
    }

    /**
     * List the EMMA mutations by main type.
     * @param maintype the main mutation type
     * @return a list of mutations of this type.
     * @see MutationTypes
     */
    public List<MutationsDAO> getMutationByMainType(String maintype) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        List muts = null;
        try {
            muts = session.createQuery(
                    "FROM MutationsDAO WHERE main_type=?")
                    .setParameter(0, maintype).list();

            session.getTransaction().commit();
        } catch (HibernateException e) {
            session.getTransaction().rollback();
            throw e;
        }
        return muts;
    }
    
    public BigInteger getBackgroundIDCount(int bg_id) {

        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        BigInteger bgCount = null;
        try {
            bgCount = (BigInteger) session.createSQLQuery("SELECT count(bg_id_bg) FROM mutations where bg_id_bg=?").setParameter(0, bg_id).uniqueResult();
        } catch (HibernateException e) {
            session.getTransaction().rollback();
            throw e;
        }
        return bgCount;
    }

    public void save(MutationsDAO mDAO) {

        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();

        try {
            session.saveOrUpdate(mDAO);
            session.getTransaction().commit();

        } catch (HibernateException e) {
            session.getTransaction().rollback();
            throw e;
        }
    }

    //Submission mutations 
    // Saves and deletes submission form mutations prior to them reaching the mutations table
    // allows users to modify their mutation submissions before final submission form controller stores data relating to the new strain.
    public List getSubMutationBySubID(int subID) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        List subMutations = null;
        try {
            //.setMaxResults(10)
            subMutations = session.createQuery(
                    "FROM SubmissionMutationsDAO WHERE id_sub=? ORDER BY id DESC").setMaxResults(10).setParameter(0, subID).list();
            session.getTransaction().commit();
        } catch (HibernateException e) {
            session.getTransaction().rollback();
            throw e;
        }
        return subMutations;
    }

    public SubmissionMutationsDAO getSubMutationBySubMutID(int subMutID) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        SubmissionMutationsDAO smd = null;
        try {

            smd = (SubmissionMutationsDAO) session.get(SubmissionMutationsDAO.class,
                    subMutID);
            session.getTransaction().commit();
        } catch (HibernateException e) {
            session.getTransaction().rollback();
            throw e;
        }

        return smd;
    }

    public void save(SubmissionMutationsDAO smDAO) {
System.out.println("at database update/insert for id " + smDAO.getId_sub());
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();

        try {
            session.saveOrUpdate(smDAO);
            session.getTransaction().commit();

        } catch (HibernateException e) {
            session.getTransaction().rollback();
            throw e;
        }
    }
    
        public void save(MutationsStrainsDAO msDAO) {

        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();

        try {
            session.save(msDAO);
            session.getTransaction().commit();

        } catch (HibernateException e) {
            session.getTransaction().rollback();
            throw e;
        }
    }
        
                public void saveSQL(int mutID, int strainID,MutationsStrainsDAO msd) {

        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();

        try {
            //session.save(msDAO); 
            session.createSQLQuery("INSERT INTO mutations_strains (str_id_str,mut_id) VALUES (?,?)").setParameter(0, strainID).setParameter(1, mutID);//strainID,mutID
           // session.save(msd);
            session.getTransaction().commit();
System.out.println("SAVED MUTATION STRAIN");
        } catch (HibernateException e) {
            session.getTransaction().rollback();
            throw e;
        }
    }
        
          public void save(AllelesDAO aDAO) {

        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();

        try {
            session.saveOrUpdate(aDAO);
            session.getTransaction().commit();

        } catch (HibernateException e) {
            session.getTransaction().rollback();
            throw e;
        }
    }
          
          
                  public void save(GenesDAO gDAO) {

        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();

        try {
            // Centimorgan is defined as String in the DAO and integer in the database.
            // Empty centimorgan string values throw an exception, so re-map any
            // non-integer values to null and log them.
            String centimorganDisplay = (gDAO.getCentimorgan() == null ? "<null>" : gDAO.getCentimorgan());
            Integer centimorgan = Utils.tryParseInt(gDAO.getCentimorgan());
            if (centimorgan == null) {
                logger.warn("id_gene: " + gDAO.getId_gene() + " (" + gDAO.getName() + "): centimorgan value '" + centimorganDisplay + ". centimorgan remapped to null.");
                gDAO.setCentimorgan(null);
            }
            session.saveOrUpdate(gDAO);
            session.getTransaction().commit();

        } catch (HibernateException e) {
            session.getTransaction().rollback();
            throw e;
        }
    }
          

    public void delete(int mutID) {
        SubmissionMutationsDAO smDAO = this.getSubMutationBySubMutID(mutID);
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();

        try {
            session.delete(smDAO);
            session.getTransaction().commit();

        } catch (HibernateException e) {
            session.getTransaction().rollback();
            throw e;
        }
    }
}
