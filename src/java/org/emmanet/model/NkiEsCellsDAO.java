/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
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
 * @author philwilkinson
 */
public class NkiEsCellsDAO {

    private String clone_id;
    private String genotype_strain;
    private String strain_background;
    private String coat_color;
    private String es_cell_clone_number;
    private String col1a1_locus;
    private String mgi_allele_symbol;
    private String mgi_allele_id;
    private String strain_name;
    private String targeting_qc;
    private String embryo_stage;
    private String chimera_embryo_number;
    private String germline_transmission;
    private String pmid;
    private String principal_scientist;
    private String lab_id_labo;
    private String mta_file;

    /**
     * @return the clone_id
     */
    public String getClone_id() {
        return clone_id;
    }

    /**
     * @param clone_id the clone_id to set
     */
    public void setClone_id(String clone_id) {
        this.clone_id = clone_id;
    }

    /**
     * @return the genotype_strain
     */
    public String getGenotype_strain() {
        return genotype_strain;
    }

    /**
     * @param genotype_strain the genotype_strain to set
     */
    public void setGenotype_strain(String genotype_strain) {
        this.genotype_strain = genotype_strain;
    }

    /**
     * @return the strain_background
     */
    public String getStrain_background() {
        return strain_background;
    }

    /**
     * @param strain_background the strain_background to set
     */
    public void setStrain_background(String strain_background) {
        this.strain_background = strain_background;
    }

    /**
     * @return the coat_colour
     */
    public String getCoat_color() {
        return coat_color;
    }

    /**
     * @param coat_color the coat_colour to set
     */
    public void setCoat_color(String coat_color) {
        this.coat_color = coat_color;
    }

    /**
     * @return the es_cell_clone_number
     */
    public String getEs_cell_clone_number() {
        return es_cell_clone_number;
    }

    /**
     * @param es_cell_clone_number the es_cell_clone_number to set
     */
    public void setEs_cell_clone_number(String es_cell_clone_number) {
        this.es_cell_clone_number = es_cell_clone_number;
    }

    /**
     * @return the col1a1_locus
     */
    public String getCol1a1_locus() {
        return col1a1_locus;
    }

    /**
     * @param col1a1_locus the col1a1_locus to set
     */
    public void setCol1a1_locus(String col1a1_locus) {
        this.col1a1_locus = col1a1_locus;
    }

    /**
     * @return the mgi_allele_symbol
     */
    public String getMgi_allele_symbol() {
        return mgi_allele_symbol;
    }

    /**
     * @param mgi_allele_symbol the mgi_allele_symbol to set
     */
    public void setMgi_allele_symbol(String mgi_allele_symbol) {
        this.mgi_allele_symbol = mgi_allele_symbol;
    }

    /**
     * @return the mgi_allele_id
     */
    public String getMgi_allele_id() {
        return mgi_allele_id;
    }

    /**
     * @param mgi_allele_id the mgi_allele_id to set
     */
    public void setMgi_allele_id(String mgi_allele_id) {
        this.mgi_allele_id = mgi_allele_id;
    }

    /**
     * @return the strain_name
     */
    public String getStrain_name() {
        return strain_name;
    }

    /**
     * @param strain_name the strain_name to set
     */
    public void setStrain_name(String strain_name) {
        this.strain_name = strain_name;
    }

    /**
     * @return the targeting_qc
     */
    public String getTargeting_qc() {
        return targeting_qc;
    }

    /**
     * @param targeting_qc the targeting_qc to set
     */
    public void setTargeting_qc(String targeting_qc) {
        this.targeting_qc = targeting_qc;
    }

    /**
     * @return the embryo_stage
     */
    public String getEmbryo_stage() {
        return embryo_stage;
    }

    /**
     * @param embryo_stage the embryo_stage to set
     */
    public void setEmbryo_stage(String embryo_stage) {
        this.embryo_stage = embryo_stage;
    }

    /**
     * @return the chimera_embryo_number
     */
    public String getChimera_embryo_number() {
        return chimera_embryo_number;
    }

    /**
     * @param chimera_embryo_number the chimera_embryo_number to set
     */
    public void setChimera_embryo_number(String chimera_embryo_number) {
        this.chimera_embryo_number = chimera_embryo_number;
    }

    /**
     * @return the germline_transmission
     */
    public String getGermline_transmission() {
        return germline_transmission;
    }

    /**
     * @param germline_transmission the germline_transmission to set
     */
    public void setGermline_transmission(String germline_transmission) {
        this.germline_transmission = germline_transmission;
    }

    /**
     * @return the pmid
     */
    public String getPmid() {
        return pmid;
    }

    /**
     * @param pmid the pmid to set
     */
    public void setPmid(String pmid) {
        this.pmid = pmid;
    }

    /**
     * @return the principal_scientist
     */
    public String getPrincipal_scientist() {
        return principal_scientist;
    }

    /**
     * @param principal_scientist the principal_scientist to set
     */
    public void setPrincipal_scientist(String principal_scientist) {
        this.principal_scientist = principal_scientist;
    }

    /**
     * @return the mta_file
     */
    public String getMta_file() {
        return mta_file;
    }

    /**
     * @param mta_file the mta_file to set
     */
    public void setMta_file(String mta_file) {
        this.mta_file = mta_file;
    }

    /**
     * @return the lab_id_labo
     */
    public String getLab_id_labo() {
        return lab_id_labo;
    }

    /**
     * @param lab_id_labo the lab_id_labo to set
     */
    public void setLab_id_labo(String lab_id_labo) {
        this.lab_id_labo = lab_id_labo;
    }

}
