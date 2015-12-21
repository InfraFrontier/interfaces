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

import java.util.Iterator;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.emmanet.model.GenesDAO;
import org.emmanet.model.GenesManager;
import org.emmanet.model.Syn_GenesDAO;
import org.emmanet.util.Utils;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;
import org.springframework.web.servlet.view.RedirectView;

/**
 *
 * @author mrelac
 */
public class GeneManagementDetailController extends SimpleFormController implements Validator {
    private final GenesManager genesManager = new GenesManager();
    
    public GeneManagementDetailController() {
		setCommandClass(GenesDAO.class);
		setCommandName("gene");
    }
    
    @Override
    protected Object formBackingObject(HttpServletRequest request) {
        GenesDAO gene = null;
        String action = request.getParameter("action");
        if (action == null) {
            logger.debug("formBackingObject: action is null.");
        } else {
            logger.debug("formBackingObject: action = " + action);
            
            Integer id = Utils.tryParseInt(request.getParameter("id"));
            if (id != null) {
                gene = genesManager.getGene(id.intValue());
            }
        }
        return (gene == null ? new GenesDAO() : gene);
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
      protected ModelAndView onSubmit(
            HttpServletRequest request,
            HttpServletResponse response,
            Object command,
            BindException errors)
            throws Exception {
        
        GenesDAO gene = (GenesDAO)command;
//        ModelAndView modelAndView = new ModelAndView(getSuccessView(), "gene", gene);
        ModelAndView modelAndView = new ModelAndView(new RedirectView("geneManagementDetail.emma?id=" + gene.getId_gene() + "&action=editGene"), "gene", gene);
        String action = request.getParameter("action");
        if (action == null) {
            logger.debug("processFormSubmission: action is null.");
        } else {
            logger.debug("processFormSubmission: action = " + action);
            if (action.compareToIgnoreCase("newSynonym") == 0) {
                genesManager.addSynonym(gene);
                modelAndView = new ModelAndView(new RedirectView("geneManagementDetail.emma?id=" + gene.getId_gene() + "&action=editGene"), "gene", gene);
            } else if (action.compareToIgnoreCase("deleteSynonym") == 0) {
                int id_syn = Utils.tryParseInt(request.getParameter("id_syn"));
                genesManager.deleteSynonym(gene, id_syn);
            } else if (action.compareToIgnoreCase("save") == 0) {
                genesManager.save(gene);
                modelAndView = new ModelAndView(new RedirectView("geneManagementList.emma"));
            }
        }
        
        return modelAndView;
    }

    
    /**
     * Required for Validator implementation.
     * @param clazz caller's class
     * @return true if caller's class is supported; false otherwise.
     */
    @Override
    public boolean supports(Class clazz) {
            //just validate the Customer instances
            return GenesDAO.class.isAssignableFrom(clazz);
    }

    /**
     * Required for Validator implementation.
     * @param target target object to be validated
     * @param errors errors object
     */
    @Override
    public void validate(Object target, Errors errors) {
        GenesDAO gene = (GenesDAO)target;
        
        // Centimorgan, if supplied, must be an integer.
        if ((gene.getCentimorgan() != null) && ( ! gene.getCentimorgan().isEmpty())) {
            Integer centimorgan = Utils.tryParseInt(gene.getCentimorgan());
            if (centimorgan == null) {
                errors.rejectValue("centimorgan", null, "Please enter an integer.");
            }
        }
        if ((gene.getName() != null) && (gene.getName().trim().length() == 0)) {
            errors.rejectValue("name", null, "Please provide a name for the gene.");
        }
        
        Utils.validateMaxFieldLengths(gene, "genes", errors);                   // Validate 'gene' max String lengths
        
        // Validate that Syn_GenesDAO String data doesn't exceed maximum varchar lengths.
        if (gene.getSynonyms() != null) {                                       // Validate each 'synGenes' instance's max String lengths
            Set<Syn_GenesDAO> geneSynonyms = gene.getSynonyms();
            Iterator<Syn_GenesDAO> synGenesIterator = geneSynonyms.iterator();
            int i = 0;
            while (synGenesIterator.hasNext()) {
                Syn_GenesDAO geneSynonym = (Syn_GenesDAO)synGenesIterator.next();
                errors.pushNestedPath("synonyms[" + Integer.toString(i) + "]");
                Utils.validateMaxFieldLengths(geneSynonym, "syn_genes", errors);
                errors.popNestedPath();
                i++;
            }
        }
    }
    
    // PRIVATE METHODS
    
    
    // GETTERS AND SETTERS
    

}

