/**
 * Copyright © 2009-2013 EMBL - European Bioinformatics Institute
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License.  
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.emmanet.model;

import java.util.List;
import java.util.Set;
import javax.persistence.Transient;

/**
 *
 * @author phil
 */
public class GenesDAO {
    
    private int id_gene;
    private String name;
    private String symbol;
    private String chromosome;
    private /*int*/ String centimorgan;
    private String cytoband;
    private String species;
    private String mgi_ref;
    private String username;
    private String last_change;
    private String promoter;
    private String founder_line_number;
    private String plasmid_construct;
    private String ensembl_ref;
    private Set<Syn_GenesDAO> synonyms;

    public int getId_gene() {
        return id_gene;
    }

    public void setId_gene(int id_gene) {
        this.id_gene = id_gene;
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

    public String getChromosome() {
        return chromosome;
    }

    public void setChromosome(String chromosome) {
        this.chromosome = chromosome;
    }

    public String getCentimorgan() {
        return centimorgan;
    }

    public void setCentimorgan(String centimorgan) {
        this.centimorgan = centimorgan;
    }

    public String getCytoband() {
        return cytoband;
    }

    public void setCytoband(String cytoband) {
        this.cytoband = cytoband;
    }

    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public String getMgi_ref() {
        return mgi_ref;
    }

    public void setMgi_ref(String mgi_ref) {
        this.mgi_ref = mgi_ref;
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

    public String getPromoter() {
        return promoter;
    }

    public void setPromoter(String promoter) {
        this.promoter = promoter;
    }

    public String getFounder_line_number() {
        return founder_line_number;
    }

    public void setFounder_line_number(String founder_line_number) {
        this.founder_line_number = founder_line_number;
    }

    public String getPlasmid_construct() {
        return plasmid_construct;
    }

    public void setPlasmid_construct(String plasmid_construct) {
        this.plasmid_construct = plasmid_construct;
    }

    public String getEnsembl_ref() {
        return ensembl_ref;
    }

    public void setEnsembl_ref(String ensembl_ref) {
        this.ensembl_ref = ensembl_ref;
    }

    public Set<Syn_GenesDAO> getSynonyms() {
        return synonyms;
    }

    public void setSynonyms(Set synonyms) {
        this.synonyms = synonyms;
    }

    @Transient
    public List<AllelesDAO> getBoundAlleles() {
        return new GenesManager().getBoundAlleles(id_gene);
    }

    @Override
    public String toString() {
        return GenesManager.toJson(this);
    }
    
}
