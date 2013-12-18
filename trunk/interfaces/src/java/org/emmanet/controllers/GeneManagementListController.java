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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
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
public class GeneManagementListController extends SimpleFormController {
    private List<GenesDAO> filteredGenesDAOList = new ArrayList();
    private Map<String, List<String>> options = null;
    private GenesDAO genesDAO;
    private final GenesManager genesManager = new GenesManager();
    private final Filter filter = new Filter();
    private boolean initialLoad;
    
    @Override
    protected Object formBackingObject(HttpServletRequest request) {
        if (options == null) {
            initialize();
        }
        request.setAttribute("options", options);
        String action = request.getParameter("action");
        if (action == null) {
            logger.debug("formBackingObject: action is null.");
            if (isInitialLoad()) {
                filteredGenesDAOList.clear();
            }
        } else {
            logger.debug("formBackingObject: action: " + action);
            if (action.compareToIgnoreCase("applyFilter") == 0) {
                ;   // Nothing to do.
            } else if (action.compareToIgnoreCase("initialize") == 0) {
                filteredGenesDAOList.clear();
            } else if (action.compareToIgnoreCase("deleteGene") == 0) {
                int id = Integer.parseInt(request.getParameter("id"));
                genesManager.delete(genesManager.getGene(id));
                filteredGenesDAOList = genesManager.getFilteredGenesList(filter);
                request.setAttribute("filteredGenesDAOList", filteredGenesDAOList);
                request.setAttribute("resultsCount", filteredGenesDAOList.size());
            }
        }

        return filteredGenesDAOList;
    }
    
    @Override
    protected ModelAndView onSubmit(
            HttpServletRequest request,
            HttpServletResponse response,
            Object command,
            BindException errors)
            throws ServletException, Exception {
        
        String action = request.getParameter("action");
        if (action == null) {
            logger.debug("onSubmit: action is null.");
        } else {
            logger.debug("onSubmit: action = " + action);
            if (action.compareToIgnoreCase("applyFilter") == 0) {
                filter.setGeneId(request.getParameter("filterGeneId"));
                filter.setChromosome(request.getParameter("filterChromosome"));
                filter.setGeneName(request.getParameter("filterGeneName"));
                filter.setMgiReference(request.getParameter("filterMgiReference"));
                filter.setGeneSymbol(request.getParameter("filterGeneSymbol"));
                filteredGenesDAOList = genesManager.getFilteredGenesList(filter);
                request.setAttribute("filteredGenesDAOList", filteredGenesDAOList);
                request.setAttribute("resultsCount", filteredGenesDAOList.size());
            } else if (action.compareToIgnoreCase("deleteGene") == 0) {
                genesManager.delete(genesDAO);
            }
        }

        return new ModelAndView(getSuccessView(), "command", filteredGenesDAOList);
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

    public GenesDAO getGenesDAO() {
        return genesDAO;
    }

    public boolean isInitialLoad() {
        return initialLoad;
    }

    public void setInitialLoad(boolean initialLoad) {
        this.initialLoad = initialLoad;
    }


}
