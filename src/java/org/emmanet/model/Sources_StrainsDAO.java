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
public class Sources_StrainsDAO {
    
private int str_id_str;
private int sour_id;
private int lab_id_labo;

//Relationship mapping
private LabsDAO labsDAO;
//private StrainsDAO strainsDAO;
private CVSourcesDAO cvsourcesDAO;


    public int getStr_id_str() {
        return str_id_str;
    }

    public void setStr_id_str(int str_id_str) {
        this.str_id_str = str_id_str;
    }

    public int getSour_id() {
        return sour_id;
    }

    public void setSour_id(int sour_id) {
        this.sour_id = sour_id;
    }

    public int getLab_id_labo() {
        return lab_id_labo;
    }

    public void setLab_id_labo(int lab_id_labo) {
        this.lab_id_labo = lab_id_labo;
    }

    public LabsDAO getLabsDAO() {
        return labsDAO;
    }

    public void setLabsDAO(LabsDAO labsDAO) {
        this.labsDAO = labsDAO;
    }

    public StrainsDAO getStrainsDAO() {
      //  return strainsDAO;
        return null;
    }

    public void setStrainsDAO(StrainsDAO strainsDAO) {
      //  this.strainsDAO = strainsDAO;
    }

   /* public CVSourcesDAO getCvsourcesDAO() {
        return cvsourcesDAO;
    }

    public void setCvsourcesDAO(CVSourcesDAO cvsourcesDAO) {
        this.cvsourcesDAO = cvsourcesDAO;
    }
*/

    /**
     * @return the cvsourcesDAO
     */
    public CVSourcesDAO getCvsourcesDAO() {
        return cvsourcesDAO;
    }

    /**
     * @param cvsourcesDAO the cvsourcesDAO to set
     */
    public void setCvsourcesDAO(CVSourcesDAO cvsourcesDAO) {
        this.cvsourcesDAO = cvsourcesDAO;
    }
}
