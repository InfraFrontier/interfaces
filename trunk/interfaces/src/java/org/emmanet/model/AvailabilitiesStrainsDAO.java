/*
 * AvailabilitiesStrainsDAO.java
 *
 * Created on 07 January 2008, 14:49
 *
 */

package org.emmanet.model;

import java.io.Serializable;

/**
 *
 * @author phil
 */
public class AvailabilitiesStrainsDAO implements Serializable{
    private int str_id_str;
    private int avail_id;
    private CVAvailabilitiesDAO cvavailDAO;
    //added 06042009
    private StrainsDAO strainsDAO;

    public int getStr_id_str() {
        return str_id_str;
    }

    public void setStr_id_str(int str_id_str) {
        this.str_id_str = str_id_str;
    }

    public int getAvail_id() {
        return avail_id;
    }

    public void setAvail_id(int avail_id) {
        this.avail_id = avail_id;
    }

    public CVAvailabilitiesDAO getCvavailDAO() {
        return cvavailDAO;
    }

    public void setCvavailDAO(CVAvailabilitiesDAO cvavailDAO) {
        this.cvavailDAO = cvavailDAO;
    }

    public StrainsDAO getStrainsDAO() {
        return strainsDAO;
    }

    public void setStrainsDAO(StrainsDAO strainsDAO) {
        this.strainsDAO = strainsDAO;
    }
    
}