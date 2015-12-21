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
