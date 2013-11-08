/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.emmanet.model;

import java.util.List;
import org.emmanet.util.HibernateUtil;
import org.hibernate.HibernateException;
import org.hibernate.Session;

/**
 *
 * @author mrelac
 */
public class CategoriesManager {

    /**
     * Return a list of id_cat and main_cat from Categories. The list is unfiltered
     * by the 'curated' flag [this is legacy code written before getCategoryListCurated]
     * @return a list of id_cat and main_cat from Categories. The list is unfiltered
     */
    public List getCategoryList() {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        List categoryList = null;
        try {
            categoryList = session.createQuery(
                    "SELECT id_cat,main_cat FROM CategoriesDAO ORDER BY main_cat").list();
            session.getTransaction().commit();
        } catch (HibernateException e) {
            session.getTransaction().rollback();
            throw e;
        }
        return categoryList;
    }

    /**
     * Return a list of categories
     * @param curated a String of value 'Y' (fetch only curated rows) or 'N' (fetch all rows, curated and not curated)
     * @return a list of categories
     */
    public List getCategoryListCurated(String curated) {
        if ((curated != null) && (curated.compareTo("N") != 0) && (curated.compareTo("Y") != 0))
            throw new RuntimeException("Programming error: filter value must be either 'Y' or 'N'. Value supplied: " + curated);
        
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        List categoryList = null;
        try {
            categoryList = session.createQuery(
                    "SELECT id_cat,main_cat FROM CategoriesDAO WHERE curated = ? ORDER BY main_cat").setParameter(0, curated).list();
            session.getTransaction().commit();
        } catch (HibernateException e) {
            session.getTransaction().rollback();
            throw e;
        }
        return categoryList;
    }
    
    /**
     * Try to find the <code>CategoriesDTO</code> by category name.
     * @param categoryName the category name to query
     * @return a list of matching <code>CategoriesDTO</code> records
     */
    public List<CategoriesDAO> findByCategoryName(String categoryName) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        List<CategoriesDAO> categoryList = null;
        try {
            categoryList = session.createQuery(
                    "FROM CategoriesDAO WHERE main_cat = ? ORDER BY main_cat").setParameter(0, categoryName).list();
            session.getTransaction().commit();
        } catch (HibernateException e) {
            session.getTransaction().rollback();
            throw e;
        }
        return categoryList;
    }
    
    /**
     * Save the <code>CategoriesDAO</code>.
     * @param categoriesDTO the object to be saved
     */
    public void save(CategoriesDAO categoriesDTO) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();

        try {
            session.saveOrUpdate(categoriesDTO);
            session.getTransaction().commit();

        } catch (HibernateException e) {
            session.getTransaction().rollback();
            throw e;
        }
    }
}
