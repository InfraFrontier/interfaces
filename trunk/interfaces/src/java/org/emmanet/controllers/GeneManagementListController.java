/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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

    @Override
    protected Object formBackingObject(HttpServletRequest request) {
        List<GenesDAO> genesList = new ArrayList();
        if (options == null) {
            initialize();
        }
        request.setAttribute("options", options);
        String action = request.getParameter("action");
        if (action == null) {
            logger.debug("formBackingObject: action is null.");
        } else {
            logger.debug("formBackingObject: action: " + action);
            if (action.compareToIgnoreCase("applyFilter") == 0) {
                return genesList;
            }
        }

        return genesList;
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
                Filter filter = new Filter();
                filter.setGeneId(request.getParameter("filterGeneId"));
                filter.setChromosome(request.getParameter("filterChromosome"));
                filter.setGeneName(request.getParameter("filterGeneName"));
                filter.setMgiReference(request.getParameter("filterMgiReference"));
                filter.setGeneSymbol(request.getParameter("filterGeneSymbol"));
                filteredGenesDAOList = genesManager.getFilteredGenesList(filter);
                request.setAttribute("filteredGenesDAOList", filteredGenesDAOList);
                request.setAttribute("resultsCount", filteredGenesDAOList.size());
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


}
