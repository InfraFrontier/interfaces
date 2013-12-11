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
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.emmanet.model.GenesDAO;
import org.emmanet.model.GenesManager;
import org.emmanet.util.Utils;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;

/**
 *
 * @author mrelac
 */
public class GeneManagementDetailController extends SimpleFormController {
    private GenesDAO gene = new GenesDAO();
    private final GenesManager genesManager;
    
    public GeneManagementDetailController() {
        genesManager = new GenesManager();
    }
    
    @Override
    protected Object formBackingObject(HttpServletRequest request) {
        String action = request.getParameter("action");
        if (action == null) {
            logger.debug("formBackingObject: action is null.");
        } else {
            logger.debug("formBackingObject: action = " + action);
            Integer id = Utils.tryParseInt(request.getParameter("id"));
            if (action.compareToIgnoreCase("editGene") ==  0) {
                gene = genesManager.getGene(id.intValue());
            } else if (action.compareToIgnoreCase("newGene") ==  0) {
                gene = new GenesDAO();
            } else if (action.compareToIgnoreCase("deleteSynonym") == 0) {
                genesManager.deleteSynonym(gene, id);
            } 
        }
        
        return gene;
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
            
            if (action.compareToIgnoreCase("newSynonym") == 0) {
                gene = (GenesDAO)command;
                genesManager.addSynonym(gene);
                genesManager.save(gene); 
            } else if (action.compareToIgnoreCase("save") == 0) {
                gene = (GenesDAO)command;
                genesManager.save(gene);                                        // Save the GenesDAO.
                List<GenesDAO> filteredGenesList = new ArrayList();
                return new ModelAndView("/interfaces/geneManagementList", "command", filteredGenesList);
            }
        }
        
        gene = (GenesDAO)command;
        return new ModelAndView(getSuccessView(), "command", gene);
    }
    
    
    // PRIVATE METHODS
    
    
    // GETTERS AND SETTERS
    
    
    public GenesDAO getGene() {
        return gene;
    }

    public void setGene(GenesDAO gene) {
        this.gene = gene;
    }


}

