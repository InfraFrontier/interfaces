/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.emmanet.model;

/**
 *
 * @author phil
 */
public class IlarDAO {
    private int id;
    private String labcode;
    private String status;
    private String investigator;
    private String organisation;

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the labcode
     */
    public String getLabcode() {
        return labcode;
    }

    /**
     * @param labcode the labcode to set
     */
    public void setLabcode(String labcode) {
        this.labcode = labcode;
    }

    /**
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @return the investigator
     */
    public String getInvestigator() {
        return investigator;
    }

    /**
     * @param investigator the investigator to set
     */
    public void setInvestigator(String investigator) {
        this.investigator = investigator;
    }

    /**
     * @return the organisation
     */
    public String getOrganisation() {
        return organisation;
    }

    /**
     * @param organisation the organisation to set
     */
    public void setOrganisation(String organisation) {
        this.organisation = organisation;
    }
    
}
