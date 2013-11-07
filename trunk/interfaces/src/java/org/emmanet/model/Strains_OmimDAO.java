/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.emmanet.model;

/**
 *
 * @author mrelac
 */
public class Strains_OmimDAO {
    private int id_strains_omim;
    private int id_strains;
    private int id_omim;
    private OmimDAO omimDAO;
    private StrainsDAO strainsDAO;

    public int getId_strains_omim() {
        return id_strains_omim;
    }

    public void setId_strains_omim(int id_strains_omim) {
        this.id_strains_omim = id_strains_omim;
    }

    public int getId_strains() {
        return id_strains;
    }

    public void setId_strains(int id_strains) {
        this.id_strains = id_strains;
    }

    public int getId_omim() {
        return id_omim;
    }

    public void setId_omim(int id_omim) {
        this.id_omim = id_omim;
    }

    public OmimDAO getOmimDAO() {
        return omimDAO;
    }

    public void setOmimDAO(OmimDAO omimDAO) {
        this.omimDAO = omimDAO;
    }

    public StrainsDAO getStrainsDAO() {
        return strainsDAO;
    }

    public void setStrainsDAO(StrainsDAO strainsDAO) {
        this.strainsDAO = strainsDAO;
    }

}
