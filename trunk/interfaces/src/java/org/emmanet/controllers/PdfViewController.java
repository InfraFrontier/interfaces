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
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.emmanet.jobs.WebRequests;
import org.emmanet.model.StrainsDAO;
import org.emmanet.model.StrainsManager;
import org.emmanet.model.WebRequestsDAO;
import org.emmanet.util.Encrypter;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

/**
 *
 * @author phil
 */
public class PdfViewController implements Controller {

    private String PATH;
    private WebRequestsDAO wrd = new WebRequestsDAO();
    private WebRequests wr = new WebRequests();
    private StrainsDAO sd = new StrainsDAO();
    private StrainsManager sm = new StrainsManager();
    private String id = "";
    private Map map = new HashMap();
    private Encrypter encrypter = new Encrypter();

    public ModelAndView handleRequest(HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession(true);
        if (request.getParameter("id") != null) {
            id = request.getParameter("id");
        } else if (request.getParameter("encid") != null) {
            //decrypt
            id = encrypter.decrypt(request.getParameter("encid"));
        }
        if (request.getParameter("pdfView") != null) {
            if (request.getParameter("type").equals("req")) {
                map.put("WebRequestsDAO", wr.getReqByID(id));
                return new ModelAndView(new PdfView(), map);
            } else if (request.getParameter("type").equals("sub")) {
                map.put("StrainsDAO", sm.getStrainByID(Integer.parseInt(id)));
                return new ModelAndView(new PdfView(), map);
            }
        }
        return null;
    }

    /**
     * @return the PATH
     */
    public String getPATH() {
        return PATH;
    }

    /**
     * @param PATH the PATH to set
     */
    public void setPATH(String PATH) {
        this.PATH = PATH;
    }
}