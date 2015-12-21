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

import org.emmanet.model.WebRequestsDAO;
import org.springframework.validation.Errors;

/**
 *
 * @author Phil Wilkinson
 */
public class RequestUpdateValidator implements
        org.springframework.validation.Validator {

    public boolean supports(Class aClass) {

        return aClass.equals(WebRequestsDAO.class);
    }

    public void validate(Object object, Errors errors) {
        WebRequestsDAO webReq = (WebRequestsDAO) object;
        System.out.println();
        System.out.println(webReq.getReq_status() + "~~~~~~" + webReq.getDate_processed());
        System.out.println();
        
        System.out.println();
        System.out.println(webReq.getReq_status() + "~~~~~~" + webReq.getReq_material_state());
        System.out.println();

        if (webReq.getReq_status().equals("SHIP") && webReq.getDate_processed().equals(null)) {/*;.isEmpty()) {*/
            errors.reject("Message", "Please ensure that the processed date field is completed");
        }


        if (webReq.getReq_status().equals("SHIP") && webReq.getReq_material_state().equals(null)){/*.isEmpty()) {*/
            errors.reject("Message", "Please ensure that the material used to fulfill the request field is completed");
        }
   /*     if (webReq.getRegister_interest().equals("1")) {
            if (webReq.getReq_status().equals("CANC")) {
                if (webReq.getCancelation_reason() == null || webReq.getCancelation_reason().trim().length() < 1) {
                    errors.reject("Message", "Please ensure a reason for ROI cancellation is selected.");
                }

            }
        }*/
    }
}
