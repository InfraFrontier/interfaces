/*
 * LabsDAO.java
 *
 * Created on 21 February 2008, 10:53
 *
 * To change this template, choose Tools | Template Manager
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
public class PeopleDAO {
    private String id_per;
    private String title;
    private String surname;
    private String firstname;
    private String phone;
    private String fax;
    private String email;
    private String username;
    private String last_change;
    private String lab_id_labo;
    private String id_ilar;
    //relationship mapping
    private LabsDAO labsDAO;
    private IlarDAO ilarDAO;
    
    
    public String getId_per() {
        return id_per;
    }

    public void setId_per(String id_per) {
        this.id_per = id_per;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getLast_change() {
        return last_change;
    }

    public void setLast_change(String last_change) {
        this.last_change = last_change;
    }

    public String getLab_id_labo() {
        return lab_id_labo;
    }

    public void setLab_id_labo(String lab_id_labo) {
        this.lab_id_labo = lab_id_labo;
    }

    public LabsDAO getLabsDAO() {
        return labsDAO;
    }

    public void setLabsDAO(LabsDAO labsDAO) {
        this.labsDAO = labsDAO;
    }

    /**
     * @return the id_ilar
     */
    public String getId_ilar() {
        return id_ilar;
    }

    /**
     * @param id_ilar the id_ilar to set
     */
    public void setId_ilar(String id_ilar) {
        this.id_ilar = id_ilar;
    }

    /**
     * @return the ilarDAO
     */
    public IlarDAO getIlarDAO() {
        return ilarDAO;
    }

    /**
     * @param ilarDAO the ilarDAO to set
     */
    public void setIlarDAO(IlarDAO ilarDAO) {
        this.ilarDAO = ilarDAO;
    }
}
