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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.emmanet.model.AllelesDAO;
import org.emmanet.model.AllelesManager;
import org.emmanet.model.GenesManager;
import org.emmanet.model.MutationsDAO;
import org.emmanet.model.MutationsManager;
import org.emmanet.model.MutationsStrainsDAO;
import org.emmanet.model.StrainsDAO;
import org.emmanet.model.StrainsManager;
import org.emmanet.util.Utils;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;

/**
 *
 * @author phil
 */
public class AlleleUpdateInterfaceFormController extends SimpleFormController {

    private AllelesDAO ad = new AllelesDAO();
    private AllelesManager am = new AllelesManager();
    private MutationsDAO md = new MutationsDAO();
    private MutationsStrainsDAO msd = new MutationsStrainsDAO();
    private MutationsManager mm = new MutationsManager();
    private GenesManager gm = new GenesManager();
    private StrainsDAO sd = new StrainsDAO();
    private StrainsManager sm = new StrainsManager();
    public static final String MAP_KEY = "returnedOut";
    private Map returnedOut = new HashMap();
    private HttpSession session;
    private int strainID;

    @Override
    protected Object formBackingObject(HttpServletRequest request) {

        HttpSession session = request.getSession(true);

        List AllelesDAOList = new ArrayList();
        List MutationByStrainID = null;// = new ArrayList();
        List alleles;
        List allAlleles;
        List allGenes;
        String alleleIDs = "";
        String action = "";

        allGenes = gm.getGenes();
        session.setAttribute("allGenes", allGenes);
        session.setAttribute(
                "strainName", null);
        session.setAttribute(
                "emmaID", null);
        if (request.getParameter("action") != null) {
            action = "";
            action = request.getParameter("action");
        }

        if (request.getParameter("strainID") != null && action.isEmpty()) {
            Integer strainID_Integer = Utils.tryParseInt(request.getParameter("strainID"));
            if (strainID_Integer == null)
                return new MutationsStrainsDAO();
            strainID = strainID_Integer.intValue();
            sd = sm.getStrainByID(strainID);
            session.setAttribute(
                    "strainName", sd.getName());
            session.setAttribute(
                    "emmaID", sd.getEmma_id());

            MutationByStrainID = mm.getMutationIDsByStrain(strainID);
            System.out.println("mutations size==" + MutationByStrainID.size());
            for (Iterator it = MutationByStrainID.listIterator(); it.hasNext();) {
                msd = (MutationsStrainsDAO) it.next();
                md = (MutationsDAO) msd.getMutationsDAO();
                ad = (AllelesDAO) md.getAllelesDAO();

                alleleIDs = new StringBuffer(alleleIDs).append(md.getAlls_id_allel()).append(",").toString();

                ad.setStrainID("" + strainID);
                AllelesDAOList.add(ad);
                System.out.println("alleles list size = " + AllelesDAOList.size());
            }
            //clean alleleIDs

            if (alleleIDs.endsWith(",")) {
                alleleIDs = alleleIDs.replace(",", "");
            }
            System.out.println("ALLELE IDS == " + alleleIDs.toString());
            alleles = am.getAllelesByID(alleleIDs);
            System.out.println("ALLELE IDS SIZE == " + alleles.size());
            returnedOut.put("allelesDAO", ad);


            returnedOut.put("mutsDAO", MutationByStrainID);
            return MutationByStrainID;
        } else if (request.getParameter("action") != null) {


            if (request.getParameter("action").equals("edit")) {
                ad = new AllelesDAO();
                ad = am.getAlleleByID(Integer.parseInt(request.getParameter("alleleID")));
                return ad;
            } else if (request.getParameter("action").equals("add")) {
                //adding ALLELE 
                ad = new AllelesDAO();
                return ad;
            }

            System.out.println("AlleleName==" + ad.getName());
            return ad;
        } else {
            returnedOut.put("mutsDAO", MutationByStrainID);
            alleles = am.getAllelesByID(null);
            msd = new MutationsStrainsDAO();
            msd.setStr_id_str(strainID);
            // MutationByStrainID.add(msd);
            return msd;
        }
    }

    @Override
    protected ModelAndView onSubmit(
            HttpServletRequest request,
            HttpServletResponse response,
            Object command,
            BindException errors)
            throws ServletException, Exception {
        String specificMessage = "";
        ad = (AllelesDAO) command;
        //do stuff before save
        ad.setUsername("EMMA");

        Date dt = new Date();
        SimpleDateFormat sdf =
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentTimestamp = sdf.format(dt);
        ad.setLast_change(currentTimestamp);
        am.save(ad);
         if (request.getParameter("action").equals("add")){
             specificMessage = "new allele";
         } else  if (request.getParameter("action").equals("edit")){
             specificMessage = "allele edit";
         }
        request.getSession().setAttribute(
                "message",
                getMessageSourceAccessor().getMessage("Message",
                "Your " + specificMessage + " submitted successfully"));
        
        return new ModelAndView(getSuccessView());
    }
}
