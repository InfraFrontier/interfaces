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

import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.emmanet.model.StrainsManager;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.security.userdetails.UserDetails;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;

/**
 *
 * @author phil
 */
public class poListController extends SimpleFormController {

    private ModelAndView includeView;
    private Map returnedOut = new HashMap();
    public static final String MAP_KEY = "returnedOut";
    private StrainsManager sm = new StrainsManager();

    @Override
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Object obj = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        
        Object o = ((UserDetails) obj).getUsername();
        String userName = o.toString();
        returnedOut.put(
                "UserName", userName);
        
        List results = sm.getLivePOByCentre(userName);
        returnedOut.put("searchDescription", "all strains under evaluation");
        returnedOut.put("results", results);
        returnedOut.put("searchSize", results.size());
        
        return new ModelAndView(getSuccessView(), MAP_KEY, returnedOut);
    }

    public ModelAndView getIncludeView() {
        return includeView;
    }

    public void setIncludeView(ModelAndView includeView) {
        this.includeView = includeView;
    }
/*
    public String getPathToMTA() {
        return pathToMTA;
    }

    public void setPathToMTA(String pathToMTA) {
        this.pathToMTA = pathToMTA;
    }
 */ 
}
