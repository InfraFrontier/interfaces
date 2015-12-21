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
public class RToolsDAO implements Serializable {

    private int str_id_str;
    private int rtls_id;
    private StrainsDAO strainsDAO;
    private CVRtoolsDAO cvrtoolsDAO;
   

    public int getStr_id_str() {
        return str_id_str;
    }

    public void setStr_id_str(int str_id_str) {
        this.str_id_str = str_id_str;
    }

    public int getRtls_id() {
        return rtls_id;
    }

    public void setRtls_id(int rtls_id) { 
        this.rtls_id = rtls_id;
    }

    public StrainsDAO getStrainsDAO() {
      return strainsDAO;
    }

    public void setStrainsDAO(StrainsDAO strainsDAO) {
       this.strainsDAO = strainsDAO;
    }

    public CVRtoolsDAO getCvrtoolsDAO() {
      return  cvrtoolsDAO;
    }

    public void setCvrtoolsDAO(CVRtoolsDAO cvrtoolsDAO) {
      this.cvrtoolsDAO = cvrtoolsDAO;
    }


}
