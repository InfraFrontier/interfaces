/*
 * To change this template, choose Tools | Templates
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
    RToolsManager rm= new RToolsManager();

    @Override
    protected Object formBackingObject(HttpServletRequest request) {

        if (request.getParameter("action") != null) {
            int id = Integer.parseInt(request.getParameter("EditStrain"));

            if (request.getParameter("action").equals("add")) {

                //  CVRtoolsDAO rtools = sm.getRToolsDAO();

                rtDAO = new RToolsDAO();
                //rtDAO = (RToolsDAO) sm.getRtoolsByID(id);
                returnedOut.put("RToolsDAO", rtDAO);
                // returnedOut.put("rtools", rtools);

                //return returnedOut;
                return rtDAO;
            }

            if (request.getParameter("action").equals("edit")) {

                List rtools = sm.getRTools();
                List rtoolsList = new ArrayList();
                rtoolsList = rm.getRToolsList(id);
                System.out.println(" RTOOLSLIST SIZE IS :: " + rtoolsList.size());
               // rtDAO = (RToolsDAO) sm.getRtoolsByID(id);
                returnedOut.put("RToolsDAO", rtoolsList/*rtDAO*/);
                returnedOut.put("rtools", rtools);
                //cvrtoolsDAO

                //return rtDAO;
                return returnedOut;
            }
        }

        return rtDAO;
    }

    // SAVE
    @Override
    protected ModelAndView onSubmit(
            HttpServletRequest request,
            HttpServletResponse response,
            Object command,
            BindException errors) 
    throws ServletException, Exception{
        rtDAO = (RToolsDAO) command;

        //If an addition
        if (request.getParameter("action").equals("add")) {
            int id = Integer.parseInt(request.getParameter("EditStrain"));

            rtDAO.setStr_id_str(id);

        }


        sm.save(rtDAO);
        System.out.println("saved");
        request.getSession().setAttribute(
                "message",
                getMessageSourceAccessor().getMessage("Message",
                "Your update submitted successfully"));

        //   return new ModelAndView(getSuccessView());
        ModelAndView mav = showForm(request, response, errors);
        return mav;
    }
}
