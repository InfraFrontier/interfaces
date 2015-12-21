/*
 * WebRequestsDAO.java
 *
 * Created on 06 December 2007, 10:44
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

import java.util.List;
import java.util.Set;

/**
 *
 * @author phil
 */
public class WebRequestsDAO {

    private String id_req;
    
    private String encryptedId_req;
    
    private int str_id_str;
    private String lab_id_labo;
    private String timestamp;
    private String roi_date;
    private String mta_arrived_date;
    private String all_paperwork_date;
    private String userstamp;
    private String strain_id;
    private String strain_name;
    private String common_name_s;
    private String live_animals;
    private String frozen_emb;
    private String frozen_spe;
    private String sci_title;
    private String sci_firstname;
    private String sci_surname;
    private String sci_e_mail;
    private String sci_phone;
    private String sci_fax;
    private String con_title;
    private String con_firstname;
    private String con_surname;
    private String con_e_mail;
    private String con_phone;
    private String con_fax;
    private String con_institution;
    private String con_dept;
    private String con_addr_1;
    private String con_addr_2;
    private String con_province;
    private String con_town;
    private String con_postcode;
    private String con_country;
    private String con_ccode;
    private String req_material;
    private String req_material_state;
    private String shipped;
    private String notes;
    private String req_status;
    private String cancelation_reason;
    private String date_processed;
    private String register_interest;
    private String terms_read;/*NOW SUPERSEDED BY APPLICATION_TYPE*/
    private String ftimestamp;
    /*  TA FIELDS   */
    private String project_description;
    private String application_type;
    private String eligible_country;
    private String ta_panel_sub_date;
    private String ta_panel_decision_date;
    private String ta_panel_decision;
     /*  TA FIELDS   */
    //private String europhenome;
   // private String wtsi_mouse_portal;
    private String projectID;
    /*  NEW BILLING DETAILS IMPLEMENTED IN DATABASE  */
    
    private String bil_title;
    private String bil_firstname;
    private String bil_surname;
    private String bil_e_mail;
    private String bil_phone;
    private String bil_fax;
    private String bil_institution;
    private String bil_dept;
    private String bil_addr_1;
    private String bil_addr_2;
    private String bil_province;
    private String bil_town;
    private String bil_postcode;
    private String bil_country;
    private String bil_vat;
    
    private String PO_ref;
    private String eucomm_funding;
    private String user;
    private String impc_phenotype_data_exists;//used to set questions for Europhenome/WTSI mouse portal interest on web form, obtained from strains.impc_phenotype_data_exists


    /**
     * Set a default value for these 2 following fields
     * reproducing what are the default values in the schema.
     */
    private String europhenome = "no";
    private String wtsi_mouse_portal = "no";
    
    private List cvDAO;
    private List availabilities;
    private LabsDAO distributionCentre;
    private Set sourcesrequestsDAO;
    
    private Set projectsstrainsDAO;
    
    private String frozenCost;
    private String liveCost;
    private String liveShelfCost;
    private String frozenDelivery;
    private String liveDelivery;
    private String liveShelfDelivery;
    
    private String import_permission_date;
    private String payment_date;
    
    public String getId_req() {
        return id_req;
    }

    public void setId_req(String id_req) {
        this.id_req = id_req;
    }

    public int getStr_id_str() {
        return str_id_str;
    }

    public void setStr_id_str(int str_id_str) {
        this.str_id_str = str_id_str;
    }

    public String getLab_id_labo() {
        return lab_id_labo;
    }

    public void setLab_id_labo(String lab_id_labo) {
        this.lab_id_labo = lab_id_labo;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getUserstamp() {
        return userstamp;
    }

    public void setUserstamp(String userstamp) {
        this.userstamp = userstamp;
    }

    public String getStrain_id() {
        return strain_id;
    }

    public void setStrain_id(String strain_id) {
        this.strain_id = strain_id;
    }

    public String getStrain_name() {
        return strain_name;
    }

    public void setStrain_name(String strain_name) {
        this.strain_name = strain_name;
    }

    public String getCommon_name_s() {
        return common_name_s;
    }

    public void setCommon_name_s(String common_name_s) {
        this.common_name_s = common_name_s;
    }

    public String getLive_animals() {
        return live_animals;
    }

    public void setLive_animals(String live_animals) {
        this.live_animals = live_animals;
    }

    public String getFrozen_emb() {
        return frozen_emb;
    }

    public void setFrozen_emb(String frozen_emb) {
        this.frozen_emb = frozen_emb;
    }

    public String getFrozen_spe() {
        return frozen_spe;
    }

    public void setFrozen_spe(String frozen_spe) {
        this.frozen_spe = frozen_spe;
    }

    public String getSci_title() {
        return sci_title;
    }

    public void setSci_title(String sci_title) {
        this.sci_title = sci_title;
    }

    public String getSci_firstname() {
        return sci_firstname;
    }

    public void setSci_firstname(String sci_firstname) {
        this.sci_firstname = sci_firstname;
    }

    public String getSci_surname() {
        return sci_surname;
    }

    public void setSci_surname(String sci_surname) {
        this.sci_surname = sci_surname;
    }

    public String getSci_e_mail() {
        return sci_e_mail;
    }

    public void setSci_e_mail(String sci_e_mail) {
        this.sci_e_mail = sci_e_mail;
    }

    public String getSci_phone() {
        return sci_phone;
    }

    public void setSci_phone(String sci_phone) {
        this.sci_phone = sci_phone;
    }

    public String getSci_fax() {
        return sci_fax;
    }

    public void setSci_fax(String sci_fax) {
        this.sci_fax = sci_fax;
    }

    public String getCon_title() {
        return con_title;
    }

    public void setCon_title(String con_title) {
        this.con_title = con_title;
    }

    public String getCon_firstname() {
        return con_firstname;
    }

    public void setCon_firstname(String con_firstname) {
        this.con_firstname = con_firstname;
    }

    public String getCon_surname() {
        return con_surname;
    }

    public void setCon_surname(String con_surname) {
        this.con_surname = con_surname;
    }

    public String getCon_e_mail() {
        return con_e_mail;
    }

    public void setCon_e_mail(String con_e_mail) {
        this.con_e_mail = con_e_mail;
    }

    public String getCon_phone() {
        return con_phone;
    }

    public void setCon_phone(String con_phone) {
        this.con_phone = con_phone;
    }

    public String getCon_fax() {
        return con_fax;
    }

    public void setCon_fax(String con_fax) {
        this.con_fax = con_fax;
    }

    public String getCon_institution() {
        return con_institution;
    }

    public void setCon_institution(String con_institution) {
        this.con_institution = con_institution;
    }

    public String getCon_dept() {
        return con_dept;
    }

    public void setCon_dept(String con_dept) {
        this.con_dept = con_dept;
    }

    public String getCon_addr_1() {
        return con_addr_1;
    }

    public void setCon_addr_1(String con_addr_1) {
        this.con_addr_1 = con_addr_1;
    }

    public String getCon_addr_2() {
        return con_addr_2;
    }

    public void setCon_addr_2(String con_addr_2) {
        this.con_addr_2 = con_addr_2;
    }

    public String getCon_province() {
        return con_province;
    }

    public void setCon_province(String con_province) {
        this.con_province = con_province;
    }

    public String getCon_town() {
        return con_town;
    }

    public void setCon_town(String con_town) {
        this.con_town = con_town;
    }

    public String getCon_postcode() {
        return con_postcode;
    }

    public void setCon_postcode(String con_postcode) {
        this.con_postcode = con_postcode;
    }

    public String getCon_country() {
        return con_country;
    }

    public void setCon_country(String con_country) {
        this.con_country = con_country;
    }

    public String getCon_ccode() {
        return con_ccode;
    }

    public void setCon_ccode(String con_ccode) {
        this.con_ccode = con_ccode;
    }

    public String getReq_material() {
        return req_material;
    }

    public void setReq_material(String req_material) {
        this.req_material = req_material;
    }

    public String getReq_material_state() {
        return req_material_state;
    }

    public void setReq_material_state(String req_material_state) {
        this.req_material_state = req_material_state;
    }

    public String getShipped() {
        return shipped;
    }

    public void setShipped(String shipped) {
        this.shipped = shipped;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getReq_status() {
        return req_status;
    }

    public void setReq_status(String req_status) {
        this.req_status = req_status;
    }

    public String getDate_processed() {
        return date_processed;
    }

    public void setDate_processed(String date_processed) {
        this.date_processed = date_processed;
    }

    public String getRegister_interest() {
        return register_interest;
    }

    public void setRegister_interest(String register_interest) {
        this.register_interest = register_interest;
    }

    public String getTerms_read() {
        return terms_read;
    }

    public void setTerms_read(String terms_read) {
        this.terms_read = terms_read;
    }

    public String getFtimestamp() {
        return ftimestamp;
    }

    public void setFtimestamp(String ftimestamp) {
        this.ftimestamp = ftimestamp;
    }

    public String getApplication_type() {
        return application_type;
    }

    public void setApplication_type(String application_type) {
        this.application_type = application_type;
    }

    public String getEligible_country() {
        return eligible_country;
    }

    public void setEligible_country(String eligible_country) {
        this.eligible_country = eligible_country;
    }

    public String getTa_panel_sub_date() {
        return ta_panel_sub_date;
    }

    public void setTa_panel_sub_date(String ta_panel_sub_date) {
        this.ta_panel_sub_date = ta_panel_sub_date;
    }

    public String getTa_panel_decision_date() {
        return ta_panel_decision_date;
    }

    public void setTa_panel_decision_date(String ta_panel_decision_date) {
        this.ta_panel_decision_date = ta_panel_decision_date;
    }

    public String getTa_panel_decision() {
        return ta_panel_decision;
    }

    public void setTa_panel_decision(String ta_panel_decision) {
        this.ta_panel_decision = ta_panel_decision;
    }

    public String getProject_description() {
        return project_description;
    }

    public void setProject_description(String project_description) {
        this.project_description = project_description;
    }

    public List getCvDAO() {
        return cvDAO;
    }

    public void setCvDAO(List cvDAO) {
        this.cvDAO = cvDAO;
    }

    public String getBil_title() {
        return bil_title;
    }

    public void setBil_title(String bil_title) {
        this.bil_title = bil_title;
    }

    public String getBil_firstname() {
        return bil_firstname;
    }

    public void setBil_firstname(String bil_firstname) {
        this.bil_firstname = bil_firstname;
    }

    public String getBil_surname() {
        return bil_surname;
    }

    public void setBil_surname(String bil_surname) {
        this.bil_surname = bil_surname;
    }

    public String getBil_e_mail() {
        return bil_e_mail;
    }

    public void setBil_e_mail(String bil_e_mail) {
        this.bil_e_mail = bil_e_mail;
    }

    public String getBil_phone() {
        return bil_phone;
    }

    public void setBil_phone(String bil_phone) {
        this.bil_phone = bil_phone;
    }

    public String getBil_fax() {
        return bil_fax;
    }

    public void setBil_fax(String bil_fax) {
        this.bil_fax = bil_fax;
    }

    public String getBil_institution() {
        return bil_institution;
    }

    public void setBil_institution(String bil_institution) {
        this.bil_institution = bil_institution;
    }

    public String getBil_dept() {
        return bil_dept;
    }

    public void setBil_dept(String bil_dept) {
        this.bil_dept = bil_dept;
    }

    public String getBil_addr_1() {
        return bil_addr_1;
    }

    public void setBil_addr_1(String bil_addr_1) {
        this.bil_addr_1 = bil_addr_1;
    }

    public String getBil_addr_2() {
        return bil_addr_2;
    }

    public void setBil_addr_2(String bil_addr_2) {
        this.bil_addr_2 = bil_addr_2;
    }

    public String getBil_province() {
        return bil_province;
    }

    public void setBil_province(String bil_province) {
        this.bil_province = bil_province;
    }

    public String getBil_town() {
        return bil_town;
    }

    public void setBil_town(String bil_town) {
        this.bil_town = bil_town;
    }

    public String getBil_postcode() {
        return bil_postcode;
    }

    public void setBil_postcode(String bil_postcode) {
        this.bil_postcode = bil_postcode;
    }

    public String getBil_country() {
        return bil_country;
    }

    public void setBil_country(String bil_country) {
        this.bil_country = bil_country;
    }

    public String getPO_ref() {
        return PO_ref;
    }

    public void setPO_ref(String PO_ref) {
        this.PO_ref = PO_ref;
    }

    public Set getSourcesrequestsDAO() {
        return sourcesrequestsDAO;
    }

    public void setSourcesrequestsDAO(Set sourcesrequestsDAO) {
        this.sourcesrequestsDAO = sourcesrequestsDAO;
    }

    public Set getProjectsstrainsDAO() {
        return projectsstrainsDAO;
    }

    public void setProjectsstrainsDAO(Set projectsstrainsDAO) {
        this.projectsstrainsDAO = projectsstrainsDAO;
    }

    public String getEucomm_funding() {
        return eucomm_funding;
    }

    public void setEucomm_funding(String eucomm_funding) {
        this.eucomm_funding = eucomm_funding;
    }

    public String getProjectID() {
        return projectID;
    }

    public void setProjectID(String projectID) {
        this.projectID = projectID;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getBil_vat() {
        return bil_vat;
    }

    public void setBil_vat(String bil_vat) {
        this.bil_vat = bil_vat;
    }

    public String getRoi_date() {
        return roi_date;
    }

    public void setRoi_date(String roi_date) {
        this.roi_date = roi_date;
    }

    public String getCancelation_reason() {
        return cancelation_reason;
    }

    public void setCancelation_reason(String cancelation_reason) {
        this.cancelation_reason = cancelation_reason;
    }

    /**
     * @return the mta_arrived_date
     */
    public String getMta_arrived_date() {
        return mta_arrived_date;
    }

    /**
     * @param mta_arrived_date the mta_arrived_date to set
     */
    public void setMta_arrived_date(String mta_arrived_date) {
        this.mta_arrived_date = mta_arrived_date;
    }

    /**
     * @return the all_paperwork_date
     */
    public String getAll_paperwork_date() {
        return all_paperwork_date;
    }

    /**
     * @param all_paperwork_date the all_paperwork_date to set
     */
    public void setAll_paperwork_date(String all_paperwork_date) {
        this.all_paperwork_date = all_paperwork_date;
    }

    /**
	 * @return the europhenome
     */
	public String getEurophenome() {
		return europhenome;
    }

    /**
	 * @param europhenome the europhenome to set
     */
	public void setEurophenome(String europhenome) {
		this.europhenome = europhenome;
    }

	/**
	 * @return the wtsi_mouse_portal
	 */
	public String getWtsi_mouse_portal() {
		return wtsi_mouse_portal;
	}

	/**
	 * @param wtsi_mouse_portal the wtsi_mouse_portal to set
	 */
	public void setWtsi_mouse_portal(String wtsi_mouse_portal) {
		this.wtsi_mouse_portal = wtsi_mouse_portal;
}

    /**
     * @return the encryptedId_req
     */
    public String getEncryptedId_req() {
        return encryptedId_req;
    }

    /**
     * @param encryptedId_req the encryptedId_req to set
     */
    public void setEncryptedId_req(String encryptedId_req) {
        this.encryptedId_req = encryptedId_req;
    }

    /**
     * @return the availabilities
     */
    public List getAvailabilities() {
        return availabilities;
    }

    /**
     * @param availabilities the availabilities to set
     */
    public void setAvailabilities(List availabilities) {
        this.availabilities = availabilities;
    }

    /**
     * @return the distributionCentre
     */
    public LabsDAO getDistributionCentre() {
        return distributionCentre;
    }

    /**
     * @param distributionCentre the distributionCentre to set
     */
    public void setDistributionCentre(LabsDAO distributionCentre) {
        this.distributionCentre = distributionCentre;
    }

    /**
     * @return the frozenCost
     */
    public String getFrozenCost() {
        return frozenCost;
    }

    /**
     * @param frozenCost the frozenCost to set
     */
    public void setFrozenCost(String frozenCost) {
        this.frozenCost = frozenCost;
    }

    /**
     * @return the liveCost
     */
    public String getLiveCost() {
        return liveCost;
    }

    /**
     * @param liveCost the liveCost to set
     */
    public void setLiveCost(String liveCost) {
        this.liveCost = liveCost;
    }

    /**
     * @return the frozenDelivery
     */
    public String getFrozenDelivery() {
        return frozenDelivery;
    }

    /**
     * @param frozenDelivery the frozenDelivery to set
     */
    public void setFrozenDelivery(String frozenDelivery) {
        this.frozenDelivery = frozenDelivery;
    }

    /**
     * @return the liveDelivery
     */
    public String getLiveDelivery() {
        return liveDelivery;
    }

    /**
     * @param liveDelivery the liveDelivery to set
     */
    public void setLiveDelivery(String liveDelivery) {
        this.liveDelivery = liveDelivery;
    }

    /**
     * @return the liveShelfCost
     */
    public String getLiveShelfCost() {
        return liveShelfCost;
    }

    /**
     * @param liveShelfCost the liveShelfCost to set
     */
    public void setLiveShelfCost(String liveShelfCost) {
        this.liveShelfCost = liveShelfCost;
    }

    /**
     * @return the liveShelfDelivery
     */
    public String getLiveShelfDelivery() {
        return liveShelfDelivery;
    }

    /**
     * @param liveShelfDelivery the liveShelfDelivery to set
     */
    public void setLiveShelfDelivery(String liveShelfDelivery) {
        this.liveShelfDelivery = liveShelfDelivery;
    }

    /**
     * @return the impc_phenotype_data_exists
     */
    public String getImpc_phenotype_data_exists() {
        return impc_phenotype_data_exists;
    }

    /**
     * @param impc_phenotype_data_exists the impc_phenotype_data_exists to set
     */
    public void setImpc_phenotype_data_exists(String impc_phenotype_data_exists) {
        this.impc_phenotype_data_exists = impc_phenotype_data_exists;
    }

    /**
     * @return the import_permission_date
     */
    public String getImport_permission_date() {
        return import_permission_date;
    }

    /**
     * @param import_permission_date the import_permission_date to set
     */
    public void setImport_permission_date(String import_permission_date) {
        this.import_permission_date = import_permission_date;
    }

    /**
     * @return the payment_date
     */
    public String getPayment_date() {
        return payment_date;
    }

    /**
     * @param payment_date the payment_date to set
     */
    public void setPayment_date(String payment_date) {
        this.payment_date = payment_date;
    }
    
    
}
