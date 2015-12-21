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
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.emmanet.model.ArchiveDAO;
import org.emmanet.model.ArchiveManager;
import org.emmanet.model.LaboratoriesManager;
//import org.emmanet.model.LaboratoriesStrainsDAO;
import org.emmanet.model.StrainsDAO;
import org.emmanet.model.StrainsManager;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;

/**
 *
 * @author phil
 */
public class poUpdateInterfaceFormController extends SimpleFormController {

    private String requestsSuccessView;
    private ArchiveManager am = new ArchiveManager();
    private StrainsManager sm = new StrainsManager();
    private int strainID;
    private Map returnedOut = new HashMap();
    private List strainAccess;
    private Object command;
    //private ArchiveDAO xDAO = (ArchiveDAO) command;
    private StrainsDAO sDAO = (StrainsDAO) command;

    @Override
    protected Object formBackingObject(HttpServletRequest request) {
        if (request.getParameter("poEdit") != null) {
            strainID = Integer.parseInt(request.getParameter("poEdit"));
            int archID = sm.getArchID(strainID);
            ArchiveDAO ad = am.getReqByID(archID);//am.getReqByArchID(strainID);

            sDAO = sm.getStrainByID(strainID);
            return sDAO;//am.getReqByID(strainID);
        }
        return am;
    }

    @Override
    protected ModelAndView onSubmit(
            HttpServletRequest request,
            HttpServletResponse response,
            Object command,
            BindException errors)
            throws ServletException, Exception {
        StrainsDAO sDAO = (StrainsDAO) command;
        // LaboratoriesStrainsDAO lsDAO = new LaboratoriesStrainsDAO();
        LaboratoriesManager lm = new LaboratoriesManager();
        //am.save(aDAO);

        if (sDAO.getArchiveDAO().getLab_id_labo().length() == 0 && sDAO.getStr_status().equals("RJCTD")) {
            sDAO.getArchiveDAO().setLab_id_labo("3");
        }

//clean up dates previously buried in manager but stripped out to resolve stale object issue
        if (sDAO.getArchiveDAO().getEvaluated() != null) {
            if (sDAO.getArchiveDAO().getEvaluated().length() == 0) {
                sDAO.getArchiveDAO().setEvaluated(null);
            }
        }

        if (sDAO.getGp_release() != null) {
            if (sDAO.getGp_release().length() == 0) {
                sDAO.setGp_release(null);
            }
        }

        sm.saveArchive(sDAO);
        System.out.println("saved");
        request.getSession().setAttribute(
                "message",
                getMessageSourceAccessor().getMessage("Message",
                "Your update submitted successfully"));

        //System.out.println("ARCHIVE ID from sDAO " + sDAO.getArchiveDAO().getLab_id_labo());
        //System.out.println("STRAIN ID FROM sDAO " + sDAO.getId_str());

        ModelAndView mav = showForm(request, response, errors);
        return mav;
    }

    public String getRequestsSuccessView() {
        return requestsSuccessView;
    }

    public void setRequestsSuccessView(String requestsSuccessView) {
        this.requestsSuccessView = requestsSuccessView;
    }
}
