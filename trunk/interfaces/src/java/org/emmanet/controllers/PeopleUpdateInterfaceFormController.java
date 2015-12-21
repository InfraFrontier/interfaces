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

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.emmanet.model.PeopleDAO;
import org.emmanet.model.PeopleManager;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;
import org.springframework.validation.BindException;

/**
 *
 * @author phil
 */
public class PeopleUpdateInterfaceFormController extends SimpleFormController {

    private PeopleManager pm = new PeopleManager();
    private PeopleDAO pDAO;

    @Override
    protected Object formBackingObject(HttpServletRequest request) {

        if (request.getParameter("contactID") != null) {
            pDAO = (PeopleDAO) pm.getPerson(request.getParameter("contactID"));
            return pDAO;

        }
        return pDAO;

    }
    // SAVE
    @Override
    protected ModelAndView onSubmit(
            HttpServletRequest request,
            HttpServletResponse response,
            Object command,
            BindException errors)
            throws ServletException, Exception {
        pDAO = (PeopleDAO) command;

        pm.save(pDAO);
        System.out.println("saved");
        request.getSession().setAttribute(
                "message",
                getMessageSourceAccessor().getMessage("Message",
                "Your update submitted successfully"));

        //  return new ModelAndView(getSuccessView());
        ModelAndView mav = showForm(request, response, errors);
        return mav;
    }
}
