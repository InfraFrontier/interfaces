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
public class BackgroundDAO {
    
private int id_bg;
private String name;
private String symbol;
private String inbred;
private String notes;
private String username;
private String species;
private String last_change;
private String curated;

    public int getId_bg() {
        return id_bg;
    }

    public void setId_bg(int id_bg) {
        this.id_bg = id_bg;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getInbred() {
        return inbred;
    }

    public void setInbred(String inbred) {
        this.inbred = inbred;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    /**
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return the species
     */
    public String getSpecies() {
        return species;
    }

    /**
     * @param species the species to set
     */
    public void setSpecies(String species) {
        this.species = species;
    }

    /**
     * @return the last_change
     */
    public String getLast_change() {
        return last_change;
    }

    /**
     * @param last_change the last_change to set
     */
    public void setLast_change(String last_change) {
        this.last_change = last_change;
    }

    /**
     * @return the curated
     */
    public String getCurated() {
        return curated;
    }

    /**
     * @param curated the curated to set
     */
    public void setCurated(String curated) {
        this.curated = curated;
    }


    
}
