/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.emmanet.controllers;

import java.text.SimpleDateFormat;
import java.util.Date;
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
    private GenesDAO genesDAO = new GenesDAO();
    
    @Override
    protected Object formBackingObject(HttpServletRequest request) {
        String action = request.getParameter("action");
        if (action == null) {
            logger.debug("formBackingObject: action is null.");
        } else {
            logger.debug("formBackingObject: action = " + action);
            Integer id = Utils.tryParseInt(request.getParameter("id"));
            if (action.compareToIgnoreCase("editGene") ==  0) {
                genesDAO = GenesManager.getGene(id.intValue());
            } else if (action.compareToIgnoreCase("newGene") ==  0) {
                genesDAO = new GenesDAO();
            }
        }
        
        return genesDAO;
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
            
            if (action.compareToIgnoreCase("save") == 0) {
                genesDAO = (GenesDAO)command;
                genesDAO.setUsername("EMMA");
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String now = sdf.format(new Date());
                genesDAO.setLast_change(now);                                   // Set last_changed timestamp.
                // Centimorgan is defined as an int in the database. If it is not
                // already null, validate that it is a number. If it is not a
                // number, set it to null; otherwise, the insert/update will fail.
                if (genesDAO.getCentimorgan() != null) {
                    Integer centimorgan = Utils.tryParseInt(genesDAO.getCentimorgan().toString());
                    genesDAO.setCentimorgan((centimorgan == null ? null : centimorgan.toString()));
                }
                
                new GenesManager().save(genesDAO);
            }  
        }
        
        genesDAO = (GenesDAO)command;

        return new ModelAndView(getSuccessView(), "command", genesDAO);
    }

    // PRIVATE METHODS
    
    
    // GETTERS AND SETTERS
    
    
    public GenesDAO getGenesDAO() {
        return genesDAO;
    }

    public void setGenesDAO(GenesDAO genesDAO) {
        this.genesDAO = genesDAO;
    }


}

