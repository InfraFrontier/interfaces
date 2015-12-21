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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.emmanet.model.Syn_StrainsDAO;
import org.emmanet.model.Syn_StrainsManager;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;

/**
 *
 * @author phil
 */
public class SynonymUpdateInterfaceController extends SimpleFormController {

    private Syn_StrainsDAO ssd = new Syn_StrainsDAO();
    private Syn_StrainsManager ssm = new Syn_StrainsManager();

    @Override
    protected Object formBackingObject(HttpServletRequest request) {
if(request.getParameter("id_syn") != null){
        int syn_id = Integer.parseInt(request.getParameter("id_syn"));
      if (syn_id > 0) {
             ssd = ssm.getSynStrainsByID(syn_id);
        }
}

        return ssd;

    }

    @Override
    protected ModelAndView onSubmit(
            HttpServletRequest request,
            HttpServletResponse response,
            Object command,
            BindException errors)
            throws ServletException, Exception {
        
ssd=new Syn_StrainsDAO();
ssd = (Syn_StrainsDAO) command;
DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
Date date = new Date();
ssd.setLast_change(dateFormat.format(date));

System.out.println("dao before save = " + ssd.getName() + "-" );
ssm.save(ssd);
 request.getSession().setAttribute(
                "message",
                getMessageSourceAccessor().getMessage("Message",
                "Your update submitted successfully"));

         return new ModelAndView(getSuccessView());
 
    }
}
