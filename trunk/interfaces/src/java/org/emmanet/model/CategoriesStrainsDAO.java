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

import java.io.Serializable;

/**
 *
 * @author phil
 */
public class CategoriesStrainsDAO implements Serializable {

    private int cat_id_cat;
    private int str_id_str;
    private CategoriesDAO categoriesDAO;
     private StrainsDAO strainsDAO;

    /**
     * @return the cat_id_cat
     */
    public int getCat_id_cat() {
        return cat_id_cat;
    }

    /**
     * @param cat_id_cat the cat_id_cat to set
     */
    public void setCat_id_cat(int cat_id_cat) {
        this.cat_id_cat = cat_id_cat;
    }

    /**
     * @return the str_id_str
     */
    public int getStr_id_str() {
        return str_id_str;
    }

    /**
     * @param str_id_str the str_id_str to set
     */
    public void setStr_id_str(int str_id_str) {
        this.str_id_str = str_id_str;
    }

    /**
     * @return the categoriesDAO
     */
    public CategoriesDAO getCategoriesDAO() {
        return categoriesDAO;
    }

    /**
     * @param categoriesDAO the categoriesDAO to set
     */
    public void setCategoriesDAO(CategoriesDAO categoriesDAO) {
        this.categoriesDAO = categoriesDAO;
    }

    /**
     * @return the strainsDAO
     */
    public StrainsDAO getStrainsDAO() {
        return strainsDAO;
    }

    /**
     * @param strainsDAO the strainsDAO to set
     */
    public void setStrainsDAO(StrainsDAO strainsDAO) {
        this.strainsDAO = strainsDAO;
    }
}
