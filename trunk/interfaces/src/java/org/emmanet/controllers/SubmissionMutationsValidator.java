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

import org.apache.log4j.Logger;
import org.emmanet.model.SubmissionMutationsDAO;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 *
 * @author phil
 */
public class SubmissionMutationsValidator implements Validator {
 protected static Logger logger = Logger.getLogger(SubmissionMutationsValidator.class);
 
    @Override
    public boolean supports(Class clazz) {
       // throw new UnsupportedOperationException("Not supported yet.");
        return SubmissionMutationsDAO.class.equals(clazz);
    }

    @Override
    public void validate(Object obj, Errors errors) {
        SubmissionMutationsDAO smd = (SubmissionMutationsDAO) obj;
         //Check for at least one sub-type for main types CH/TM and IN EMMA-644
        System.out.println("WE ARE HERE AT VALIDATION OF " + obj.toString());
        if (smd.getMutation_type() != null && smd.getMutation_type().equals("CH")) {
              System.out.println("WE ARE HERE line 31 " + smd.getMutation_subtype());
                System.out.println("WE ARE HERE AT line 32 " + smd.getMutation_type());
            if (smd.getMutation_subtype() == null || smd.getMutation_subtype().isEmpty()) {
              //  ValidationUtils.
                errors.rejectValue("mutation_subtype", "incorrect.mutation_subtype",
                        "Please ensure that you select a value for mutation subtype in the mutation entry fields above.");
            }
        } else if (smd.getMutation_type() != null && smd.getMutation_type().equals("TM")) {
            if (smd.getMutation_subtype() == null || smd.getMutation_subtype().isEmpty()) {
                errors.rejectValue("mutation_subtype", "incorrect.mutation_subtype",
                        "Please ensure that you select a value for mutation subtype in the mutation entry fields above.");
            } 
        } else if (smd.getMutation_type() != null && smd.getMutation_type().equals("IN")) {
                if (smd.getMutation_subtype() == null || smd.getMutation_subtype().isEmpty()) {
                    errors.rejectValue("mutation_subtype", "incorrect.mutation_subtype",
                            "Please ensure that you select a value for mutation subtype  in the mutation entry fields above.");
                }
        }
       // throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
