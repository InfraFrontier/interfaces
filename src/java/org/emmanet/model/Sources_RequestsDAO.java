/*

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
public class Sources_RequestsDAO {

    private int req_id_req;
    private int sour_id;
    //Relationship mapping
    private WebRequestsDAO webrequestsDAO;
    private CVSourcesDAO cvsourcesDAO;

    public int getReq_id_req() {
        return req_id_req;
    }

    public void setReq_id_req(int req_id_req) {
        this.req_id_req = req_id_req;
    }

    public int getSour_id() {
        return sour_id;
    }

    public void setSour_id(int sour_id) {
        this.sour_id = sour_id;
    }

    public CVSourcesDAO getCvsourcesDAO() {
        return cvsourcesDAO;
    }

    public void setCvsourcesDAO(CVSourcesDAO cvsourcesDAO) {
        this.cvsourcesDAO = cvsourcesDAO;
    }

    public WebRequestsDAO getWebrequestsDAO() {
        return webrequestsDAO;
    }

    public void setWebrequestsDAO(WebRequestsDAO webrequestsDAO) {
        this.webrequestsDAO = webrequestsDAO;
    }
}
