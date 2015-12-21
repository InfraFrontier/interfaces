/**
 * Copyright © 2013 EMBL - European Bioinformatics Institute
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.emmanet.model.GenesDAO;
import org.emmanet.model.GenesManager;
import org.emmanet.util.Filter;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;

/**
 *
 * @author mrelac
 */
public class AlleleManagementListController extends SimpleFormController {
    private Map<String, List<String>> options = null;
    private final GenesManager genesManager = new GenesManager();
    
	public AlleleManagementListController(){
            setCommandClass(Filter.class);
            setCommandName("filter");
	}
    
    /**
     * Initialize the form backing object.
     * 
     * @param request the <code>HttpServletRequest</code> instance
     * @return a new <code>Filter</code> instance
     */
    @Override
    protected Object formBackingObject(HttpServletRequest request) {
        String action = request.getParameter("action");
        initialize();
        request.setAttribute("options", options);
        request.setAttribute("filteredAllelesDAOList", new ArrayList());
        Filter filter = new Filter(request);
        
        if (request.getParameter("action") == null) {
            logger.debug("formBackingObject: hiding divResults.");
            request.setAttribute("hidShowResultsForm", 0);                      // Hide results div on iinitial entry to form.
        } else {
            List<GenesDAO> filteredAllelesDAOList = genesManager.getFilteredGenesList(filter);
            if (action.compareToIgnoreCase("applyFilter") == 0) {
                request.setAttribute("hidShowResultsForm", 1);                  // Show results div now.
            }
            request.setAttribute("filteredAllelesDAOList", filteredAllelesDAOList);
            request.setAttribute("resultsCount", filteredAllelesDAOList.size());
        }
        
        return filter;
    }
    
    /**
     * Process the form submission (GET or POST).
     * 
     * @param request the <code>HttpServletRequest</code> instance
     * @param response the <code>HttpServletResponse</code> instance
     * @param command the form's fields (model) to be operated upon
     * @param errors the <code>BindException</code> errors instance
     * @return the <code>ModelAndView</code> instance to invoke
     * @throws Exception upon error
     */
    @Override
    protected ModelAndView processFormSubmission(
            HttpServletRequest request,
            HttpServletResponse response,
            Object command,
            BindException errors)
            throws Exception {
        
        Filter filter = (Filter)command;
        
        String action = request.getParameter("action");
        if (action == null) {
            logger.debug("processFormSubmission: action is null.");
        } else {
            logger.debug("processFormSubmission: action = " + action);
            if (action.compareToIgnoreCase("deleteGene") == 0) {
                int id = Integer.parseInt(request.getParameter("id"));
                genesManager.delete(genesManager.getGene(id));
            }
            List<GenesDAO> filteredAllelesDAOList = genesManager.getFilteredGenesList(filter);
            request.setAttribute("filteredAllelesDAOList", filteredAllelesDAOList);
            request.setAttribute("resultsCount", filteredAllelesDAOList.size());
        }

        return new ModelAndView(getSuccessView(), "filter", filter);
    }
    
    
    // PRIVATE METHODS
    
    
    private void initialize() {
        options = new HashMap();
        options.put("geneIds", genesManager.getGeneIds());
        options.put("geneNames", genesManager.getNames());
        options.put("geneSymbols", genesManager.getSymbols());
        options.put("chromosomes", genesManager.getChromosomes());
        options.put("mgiReferences", genesManager.getMGIReferences());
    }

    
    // GETTERS AND SETTERS

    
    public Map getOptions() {
        return options;
    }


}
