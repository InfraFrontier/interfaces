/*
 * AvailabilitiesStrainsDAO.java
 *
 * Created on 07 January 2008, 14:49
 *
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
public class MutationsDAO {
    private int id;
    private String main_type;
    private String sub_type;
    private String dominance;
    private String tm_esline;
    private String ch_ano_name;
    private String ch_ano_desc;
    private String mu_cause;
    private int alls_id_allel;
    private String bg_id_bg;
    private String str_id_str;
    private String sex;
    private String genotype;
    private String ki_alter;
    private String username;
    private String last_change;
    private int alls_id_allel_replaced;
    private String chromosome;

    private AllelesDAO allelesDAO;
    private BackgroundDAO backgroundDAO;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMain_type() {
        return main_type;
    }

    public void setMain_type(String main_type) {
        this.main_type = main_type;
    }

    public String getSub_type() {
        return sub_type;
    }

    public void setSub_type(String sub_type) {
        this.sub_type = sub_type;
    }

    public int getAlls_id_allel() {
        return alls_id_allel;
    }

    public void setAlls_id_allel(int alls_id_allel) {
        this.alls_id_allel = alls_id_allel;
    }

    public AllelesDAO getAllelesDAO() {
        return allelesDAO;
    }

    public void setAllelesDAO(AllelesDAO allelesDAO) {
        this.allelesDAO = allelesDAO;
    }

    public BackgroundDAO getBackgroundDAO() {
        return backgroundDAO;
    }

    public void setBackgroundDAO(BackgroundDAO backgroundDAO) {
        this.backgroundDAO = backgroundDAO;
    }

    public String getDominance() {
        return dominance;
    }

    public void setDominance(String dominance) {
        this.dominance = dominance;
    }

    public String getTm_esline() {
        return tm_esline;
    }

    public void setTm_esline(String tm_esline) {
        this.tm_esline = tm_esline;
    }

    public String getCh_ano_name() {
        return ch_ano_name;
    }

    public void setCh_ano_name(String ch_ano_name) {
        this.ch_ano_name = ch_ano_name;
    }

    public String getCh_ano_desc() {
        return ch_ano_desc;
    }

    public void setCh_ano_desc(String ch_ano_desc) {
        this.ch_ano_desc = ch_ano_desc;
    }

    public String getMu_cause() {
        return mu_cause;
    }

    public void setMu_cause(String mu_cause) {
        this.mu_cause = mu_cause;
    }

    public String getBg_id_bg() {
        return bg_id_bg;
    }

    public void setBg_id_bg(String bg_id_bg) {
        this.bg_id_bg = bg_id_bg;
    }

    /**
     * @return the str_id_str
     */
    public String getStr_id_str() {
        return str_id_str;
    }

    /**
     * @param str_id_str the str_id_str to set
     */
    public void setStr_id_str(String str_id_str) {
        this.str_id_str = str_id_str;
    }

    /**
     * @return the sex
     */
    public String getSex() {
        return sex;
    }

    /**
     * @param sex the sex to set
     */
    public void setSex(String sex) {
        this.sex = sex;
    }

    /**
     * @return the genotype
     */
    public String getGenotype() {
        return genotype;
    }

    /**
     * @param genotype the genotype to set
     */
    public void setGenotype(String genotype) {
        this.genotype = genotype;
    }

    /**
     * @return the ki_alter
     */
    public String getKi_alter() {
        return ki_alter;
    }

    /**
     * @param ki_alter the ki_alter to set
     */
    public void setKi_alter(String ki_alter) {
        this.ki_alter = ki_alter;
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
     * @return the alls_id_allel_replaced
     */
    public int getAlls_id_allel_replaced() {
        return alls_id_allel_replaced;
    }

    /**
     * @param alls_id_allel_replaced the alls_id_allel_replaced to set
     */
    public void setAlls_id_allel_replaced(int alls_id_allel_replaced) {
        this.alls_id_allel_replaced = alls_id_allel_replaced;
    }

    /**
     * @return the chromosome
     */
    public String getChromosome() {
        return chromosome;
    }

    /**
     * @param chromosome the chromosome to set
     */
    public void setChromosome(String chromosome) {
        this.chromosome = chromosome;
    }

}
