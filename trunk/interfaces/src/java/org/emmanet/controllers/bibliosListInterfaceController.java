/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.emmanet.controllers;

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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.emmanet.model.BibliosDAO;
import org.emmanet.model.BibliosManager;
import org.emmanet.model.BibliosStrainsDAO;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;

/**
 *
 * @author phil
 */
public class bibliosListInterfaceController extends SimpleFormController {

    public static final String MAP_KEY = "returnedOut";
    private Map returnedOut = new HashMap();
    private String listView;
    private BibliosStrainsDAO bsDAO;
    private BibliosDAO bDAO;
    private BibliosManager bm = new BibliosManager();
    List bibliosStrains = null;

    @Override
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession(true);

        if (request.getParameter("action") != null) {
            int id = Integer.parseInt(request.getParameter("EditStrain"));
            String action = request.getParameter("action");
            bibliosStrains = bm.bibliosStrains(id);
            if (action.equals("edit")) {
                //More than one biblio ref so list for edit
                session.setAttribute("view", "show");
                for (int it = 0; it < bibliosStrains.size(); it++) {
                    bsDAO = (BibliosStrainsDAO) bibliosStrains.get(it);
                    int bibID = bsDAO.getBib_id_biblio();
                    List bibsDAO = bm.biblios(bibID);
                    returnedOut.put("BibliosDAO" + it, bibsDAO);
                }
            }
            return new ModelAndView(getListView(), MAP_KEY, returnedOut);
        }
        return null;
    }

    public String getListView() {
        return listView;
    }

    public void setListView(String listView) {
        this.listView = listView;
    }
}

