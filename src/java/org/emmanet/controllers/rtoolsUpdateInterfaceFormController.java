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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.emmanet.model.RToolsDAO;
import org.emmanet.model.RToolsManager;
import org.emmanet.model.StrainsManager;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;

/**
 *
 * @author phil
 */
public class rtoolsUpdateInterfaceFormController extends SimpleFormController {

    private RToolsDAO rtDAO;
    private StrainsManager sm = new StrainsManager();
    public static final String MAP_KEY = "returnedOut";
    private Map returnedOut = new HashMap();
    RToolsManager rm = new RToolsManager();

    @Override
    protected Object formBackingObject(HttpServletRequest request) {

        if (request.getParameter("action") != null) {
            int id = Integer.parseInt(request.getParameter("EditStrain"));
            List rtools = null;
            rtools = rm.getRToolsList(id);


            if (request.getParameter("action").equals("add")) {
                rtDAO = new RToolsDAO();
                returnedOut.put("RToolsDAO", rtDAO);
                return rtDAO;
            } else if (request.getParameter("action").equals("edit")) {
                rtools = sm.getRToolsStrains(id);
                return rtools;
            } else if (request.getParameter("action").equals("update")) {
                //we are to update the database entry
                RToolsManager rtm = new RToolsManager();
                //Object[] o = null;
                rtDAO = (RToolsDAO) rtm.getRToolsByIDandstrainid(Integer.parseInt(request.getParameter("rtoolsID")), Integer.parseInt(request.getParameter("EditStrain")));
                //  rtDAO= (RToolsDAO) o[0];
               // rtDAO.setRtls_id(Integer.parseInt(request.getParameter("newvalue")));
                //rtm.update(rtDAO);
                //rtm.save(rtDAO);
                //rtm.updateSQL(Integer.parseInt(request.getParameter("EditStrain")),Integer.parseInt(request.getParameter("newvalue")),Integer.parseInt(request.getParameter("rtoolsID")));

               // rtools = sm.getRToolsStrains(Integer.parseInt(request.getParameter("EditStrain")));

               // returnedOut.put("RToolsDAO", rtools);
                //return returnedOut;
                //return rtools;
                return rtDAO;
            } else if (request.getParameter("action").equals("delete")) {
                
                //DELETE
                
            } else if (rtools.size() == 1) {
                //just return single Object
                return rtDAO = rm.getRToolsByID(id);
            } else {
                return rtools;
            }


        }
        return null;
    }

    // SAVE
    @Override
    protected ModelAndView onSubmit(
            HttpServletRequest request,
            HttpServletResponse response,
            Object command,
            BindException errors)
            throws ServletException, Exception {
        rtDAO = (RToolsDAO) command;

        //If an addition
        if (request.getParameter("action").equals("add")) {
            int id = Integer.parseInt(request.getParameter("EditStrain"));

            rtDAO.setStr_id_str(id);

        }


       // sm.save(rtDAO);
       // rm.save(rtDAO);
        rm.update(rtDAO);
        System.out.println("RTDAO updated");
        request.getSession().setAttribute(
                "message",
                getMessageSourceAccessor().getMessage("Message",
                "Your update submitted successfully"));

        //   return new ModelAndView(getSuccessView());
        ModelAndView mav = showForm(request, response, errors);
        return mav;
    }
}
