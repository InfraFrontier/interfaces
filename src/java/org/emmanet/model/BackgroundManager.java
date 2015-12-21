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
import java.util.List;
import org.emmanet.util.HibernateUtil;
import org.hibernate.HibernateException;
import org.hibernate.Session;

public class BackgroundManager {

    public List getBackgrounds() {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        List bg;
        try {
            bg = session.createSQLQuery(
                    "SELECT id_bg,name "
                    + "FROM backgrounds "
                    + "WHERE name != '' "
                    + "ORDER BY name").list();
            session.getTransaction().commit();
            System.out.println("size of bg list is::: " + bg.size());
        } catch (HibernateException e) {
            session.getTransaction().rollback();
            throw e;
        }
        return bg;
    }
    
        public List getCuratedBackgrounds() {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        List bg;
        try {
            bg = session.createSQLQuery(
                    "SELECT id_bg,name "
                    + "FROM backgrounds "
                    + "WHERE name != '' "
                    + " AND curated='Y'"
                    + "ORDER BY name").list();
            session.getTransaction().commit();
            System.out.println("size of bg list is::: " + bg.size());
        } catch (HibernateException e) {
            session.getTransaction().rollback();
            throw e;
        }
        return bg;
    }

    public List getBGDAOByName(String name) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        // StrainsDAO
        List bgd = null;
        try {
            bgd = session.createQuery(
                    "FROM BackgroundDAO WHERE name LIKE '" + name + "%'")/*.setString(0, name)*/.list();
            session.getTransaction().commit();
        } catch (HibernateException e) {
            session.getTransaction().rollback();
            throw e;
        }
        System.out.println("query size is::" + bgd.size());
        return bgd;
    }
    
        public List getBGSpecies() {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        List bgs = null;
        try {
            bgs = session.createQuery(
                    "SELECT DISTINCT species FROM BackgroundDAO "
                    + "WHERE species IS NOT NULL "
                    + "AND species NOT IN ('','null')").list();
            session.getTransaction().commit();
        } catch (HibernateException e) {
            session.getTransaction().rollback();
            throw e;
        }
        return bgs;
    }

    public BackgroundDAO getBgById(int bgid) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        BackgroundDAO bd = null;
        try {
            bd = (BackgroundDAO) session.get(BackgroundDAO.class,
                    bgid);
            session.getTransaction().commit();
        } catch (HibernateException e) {
            session.getTransaction().rollback();
            throw e;
        }
        return bd;
    }

    public List getCVProjects() {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        List cvproj = null;
        try {
            cvproj = session.createSQLQuery(
                    "SELECT * FROM cv_projects").list();
            session.getTransaction().commit();
        } catch (HibernateException e) {
            session.getTransaction().rollback();
            throw e;
        }
        return cvproj;
    }

    public List getCVSources() {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        List cvsources = null;
        try {
            /*    cvsources = session.createSQLQuery(
            "SELECT * FROM cv_sources").list();*/
            //retrieves funding sources only relating to strains and not requests
            //returned in prefered order
            cvsources = session.createSQLQuery(
                    "SELECT * FROM cv_sources " +
                    "WHERE code NOT LIKE 'sTA%' " +
                    "ORDER BY FIELD(code, 'I3-p1','I3-p2','I3-p3','DONE','UNKN','CNR','Phenomin','MRC','BASH','MGP','BMBF',"
                    + "'IMG-I','IMG-B','MEYS','MUGEN','s1WP4','s2WP4','s3WP4','1JRA1'," +
                    "'2JRA1','3JRA1','4JRA1','1JRA2','2JRA2','3JRA2','4JRA2','Del1','Del2'," +
                    "'Lex1','Lex2','sTA01','sTA02','sTA03','sTA04','sTA05','sTA06','sTA07','sTA08','sTA09','sTA10','sTA11','sTA12',"
                    + "'sTA13','sTA14','sTA15')").list();
            session.getTransaction().commit();
        } catch (HibernateException e) {
            session.getTransaction().rollback();
            throw e;
        }
        return cvsources;
    }

    public void save(BackgroundDAO bDAO) {

        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();

        try {
            if(bDAO.getId_bg()==0) {
                
                session.save(bDAO);
            }else {
            session.saveOrUpdate(bDAO);
            }
            session.getTransaction().commit();

        } catch (HibernateException e) {
            session.getTransaction().rollback();
            throw e;
        }
    }
    
        public void delete(BackgroundDAO bDAO) {

        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();

        try {
             session.delete(bDAO);
            session.getTransaction().commit();
            }
         catch (HibernateException e) {
            session.getTransaction().rollback();
            throw e;
        }
    }
}
