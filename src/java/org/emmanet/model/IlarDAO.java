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
