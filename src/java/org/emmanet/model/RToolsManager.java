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

import java.sql.*;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import org.emmanet.util.HibernateUtil;
import org.hibernate.HibernateException;
import org.hibernate.Session;

/**
 *
 * @author phil
 */
public class RToolsManager {

    private static String DRIVER = "com.mysql.jdbc.Driver";

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
    
        public RToolsDAO getRToolsByIDandstrainid(int id,int str_id_str) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        RToolsDAO rtd = null;
        try {
            rtd = (RToolsDAO) session.createQuery("FROM RToolsDAO WHERE rtls_id=? AND str_id_str=?").setParameter(0, id).setParameter(1, str_id_str).uniqueResult();
            session.getTransaction().commit();
        } catch (HibernateException e) {
            session.getTransaction().rollback();
            throw e;
        }
        
        System.out.println("RTDAO is " + rtd.getRtls_id() + " - " + rtd.getStr_id_str());
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
    
        public void update(RToolsDAO rtDAO) {

        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();

        try {
            session.update(rtDAO);
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
    
        public void updateSQL(int strainID,int newRtl_id,int rtlsID) {

        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();

        try {
            
            session.createSQLQuery("UPDATE rtools_strains (str_id_str,rtls_id) VALUES (?,?) " + 
                    "WHERE str_id_str=? AND rtls_id=?")
                    .setParameter(0,strainID)
                    .setParameter(1,newRtl_id)
                    .setParameter(2,strainID)
                    .setParameter(3,rtlsID);
          // 
            session.getTransaction().commit();
 //session.flush();
            System.out.println("UPDATED RTOOLS" + strainID);
        } catch (HibernateException e) {
            session.getTransaction().rollback();
            throw e;
        }
    }

    public void XsaveSQL(int rtlsID, int strainID) {

        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();

        try {
            String sql = "INSERT INTO rtools_strains (str_id_str,rtls_id) VALUES (" + strainID + "," + rtlsID + " )";
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

    public void saveUsingJDBCSQL(int rtlsID, int strainID) throws SQLException, NamingException, Exception {
        Connection connect = null;
        PreparedStatement preparedStatement = null;
        DataSource ds = null;


        try {
            //  Context ctx = new InitialContext();
            //ds = (DataSource)ctx.lookup("connection.datasource");
            InitialContext ctxt = new InitialContext();
            ds = (DataSource) ctxt.lookup("java:comp/env/jdbc/sourceEmma");//java:/comp/env/ds/TestDS
            connect = ds.getConnection();
            Class.forName(DRIVER);
            // connect = DriverManager.getConnection(CONNECTIONSTRING);
            preparedStatement = connect.prepareStatement("INSERT INTO rtools_strains (str_id_str,rtls_id) VALUES (?,?)");

            preparedStatement.setInt(1, strainID);
            preparedStatement.setInt(2, rtlsID);
            System.out.println(preparedStatement.toString());
            preparedStatement.executeUpdate();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(RToolsManager.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (connect != null) {
                connect.close();
            }
        }
    }

    public void saveCategoriesUsingJDBCSQL(int catID, int strainID) throws SQLException, NamingException, Exception {
        Connection connect = null;
        PreparedStatement preparedStatement = null;
        DataSource ds = null;
        try {
            Class.forName(DRIVER);
            InitialContext ctxt = new InitialContext();
            ds = (DataSource) ctxt.lookup("java:comp/env/jdbc/sourceEmma");//java:/comp/env/ds/TestDS
            connect = ds.getConnection();
            // connect = DriverManager.getConnection(CONNECTIONSTRING);
            preparedStatement = connect.prepareStatement("INSERT INTO categories_strains (cat_id_cat,str_id_str) VALUES (?,?)");
            preparedStatement.setInt(2, strainID);
            preparedStatement.setInt(1, catID);
            System.out.println(preparedStatement.toString());
            preparedStatement.executeUpdate();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(RToolsManager.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (connect != null) {
                connect.close();
            }
        }
    }

    public void saveMutsStrainsUsingJDBCSQL(int mutID, int strainID) throws SQLException, NamingException, Exception {
        Connection connect = null;
        PreparedStatement preparedStatement = null;
        DataSource ds = null;
        try {
            Class.forName(DRIVER);
            InitialContext ctxt = new InitialContext();
            ds = (DataSource) ctxt.lookup("java:comp/env/jdbc/sourceEmma");//java:/comp/env/ds/TestDS
            connect = ds.getConnection();
            //connect = DriverManager.getConnection(CONNECTIONSTRING);
            preparedStatement = connect.prepareStatement("INSERT INTO mutations_strains (mut_id,str_id_str) VALUES (?,?)");
            preparedStatement.setInt(2, strainID);
            preparedStatement.setInt(1, mutID);
            System.out.println(preparedStatement.toString());
            preparedStatement.executeUpdate();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(RToolsManager.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (connect != null) {
                connect.close();
            }
        }
    }
}
