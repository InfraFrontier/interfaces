/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.emmanet.model;

import java.util.List;
import org.emmanet.util.HibernateUtil;
import org.hibernate.HibernateException;
import org.hibernate.Session;

import java.sql.*;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.naming.NamingException;
import javax.sql.DataSource;

/**
 *
 * @author phil
 */
public class RToolsManager {

    private static String DRIVER = "com.mysql.jdbc.Driver";

private static String CONNECTIONSTRING = "jdbc:mysql://mysql-TEST-emmastr:4167/emmastr?user=admin&password=qGUZ1dx4";
private static String CONNECTIONSTRINGLOCAL = "jdbc:mysql://localhost/emmastr?user=root&password=18061963";

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

    public List getRToolsList(int id) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        List rtd = null;
        RToolsDAO rt = new RToolsDAO();
        try {
            rtd = session.createSQLQuery("SELECT rtls_id FROM rtools_strains WHERE str_id_str=?").setParameter(0, id).list();
            // rtd = session.createQuery("FROM RToolsDAO WHERE str_id_str=?").setParameter(0, id).list();

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
            Class.forName(DRIVER);
            // connect = ds.getConnection();//ds.getConnection(); //getConnection("java:comp/env/jdbc/sourceEmma");// 
            connect = DriverManager.getConnection(CONNECTIONSTRING);
            /*  connect=DriverManager.getConnection("jdbc:mysql://mysql-emmastr:4167/emmastr?"
             + "user=admin&password=qGUZ1dx4");*/
            if(!connect.isValid(2)){
                connect = DriverManager.getConnection(CONNECTIONSTRINGLOCAL);
            }
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
            connect = DriverManager.getConnection(CONNECTIONSTRING);
             if(!connect.isValid(2)){
                connect = DriverManager.getConnection(CONNECTIONSTRINGLOCAL);
            }
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
            connect = DriverManager.getConnection(CONNECTIONSTRING);
             if(!connect.isValid(2)){
                connect = DriverManager.getConnection(CONNECTIONSTRINGLOCAL);
            }
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
