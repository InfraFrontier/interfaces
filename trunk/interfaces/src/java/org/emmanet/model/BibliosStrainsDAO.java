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
public class BibliosStrainsDAO {

    private int bib_id_biblio;
    private int str_id_str;    // RELATIONSHIP MAPPING
    private BibliosDAO bibliosDAO;
    private StrainsDAO strainsDAO;

    public int getBib_id_biblio() {
        return bib_id_biblio;
    }

    public void setBib_id_biblio(int bib_id_biblio) {
        this.bib_id_biblio = bib_id_biblio;
    }

    public int getStr_id_str() {
        return str_id_str;
    }

    public void setStr_id_str(int str_id_str) {
        this.str_id_str = str_id_str;
    }

    public BibliosDAO getBibliosDAO() {
        return bibliosDAO;
    }

    public void setBibliosDAO(BibliosDAO bibliosDAO) {
        this.bibliosDAO = bibliosDAO;
    }

    public StrainsDAO getStrainsDAO() {
        return strainsDAO;
    }

    public void setStrainsDAO(StrainsDAO strainsDAO) {
        this.strainsDAO = strainsDAO;
    }
}
