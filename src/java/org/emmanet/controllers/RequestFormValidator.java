/*
 * RequestFormValidator.java
 *
 * Created on 09 January 2008, 16:24
 *
 * Validates required fields in register interest form.
 *
 * On validation error message alerts user in message area at top of page.
 * On success redirects user back to strain list with success message displayed.
 *
 * v1.0 released 04-02-2008
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

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.emmanet.jobs.WebRequests;
import org.emmanet.model.WebRequestsDAO;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

/**
 *
 * @author Phil Wilkinson
 */
public class RequestFormValidator implements
        org.springframework.validation.Validator {

    private static final String EMAIL_PATTERN = "^([a-zA-Z0-9\\'_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)"
            + "|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
    private WebRequests wr = new WebRequests();
    private WebRequests wrDupCheck = new WebRequests();
    private String table;
    private String[] euCountriesList = {"Austria", "Belgium", "Bulgaria", "Cyprus", "Czech Republic", "Denmark", "Estonia", "Finland", "France", "Germany", "Greece", "Hungary", "Ireland", "Italy", "Latvia", "Lithuania", "Luxembourg", "Malta", "Netherlands", "Poland", "Portugal", "Romania", "Slovakia", "Slovenia", "Spain", "Sweden", "United Kingdom"};
    private String[] assocCountriesList = {"Albania", "Croatia", "Iceland", "Israel", "Liechtenstein", "Macedonia", "Montenegro", "Norway", "Serbia", "Switzerland", "Turkey"};

    @Override
    public boolean supports(Class aClass) {

        return aClass.equals(WebRequestsDAO.class);
    }

    @Override
    public void validate(Object object, Errors errors) {
        WebRequestsDAO webReq = (WebRequestsDAO) object;
        if (webReq == null) {
            return;
        }

        // System.out.println("VALIDATOR LAB ID " + webReq.getLab_id_labo());
        // System.out.println("TERMS READ -    " + webReq.getApplication_type()/*.getTerms_read()*/);
        if (webReq.getApplication_type() == null || webReq.getApplication_type().isEmpty()) {

            if (webReq.getRegister_interest().equals("1")) {
                //do nowt escape validation
            } else if (!webReq.getTerms_read().equals("on")) {
                errors.reject("Message", "Please read the conditions and check the box to confirm acceptance");
            }
        } else {
            if (webReq.getApplication_type().equals("ta_or_request") || webReq.getApplication_type().equals("ta_only")) {
                System.out.println("webReq.getProject_description() value is " + webReq.getProject_description());
                if (webReq.getProject_description().equals("")) {
                    errors.reject("Message", "Please add a project description. Applications for Transnational Access must be accompanied by a project description to be considered.");
                }
            }
            if (webReq.getApplication_type().equals("request_only")) {
                if (webReq.getTerms_read() == null || !webReq.getTerms_read().equals("on")) {
                    errors.reject("Message", "Please read the conditions and check the box to confirm acceptance");
                }
            }
        }

        if (webReq.getSci_e_mail() != null || webReq.getSci_e_mail().trim().length() > 1 || patternMatch(EMAIL_PATTERN, webReq.getSci_e_mail())) {

            List duplicatedReqs = wrDupCheck.webReqDupCheck(webReq.getStrain_id(), webReq.getSci_e_mail(), webReq.getRegister_interest());
//have increased number of requests allowed to 2, this allows submitter more than one request for same strain/email
            //was previously 1 to avoid duplicates but now there is a need for more
            if (duplicatedReqs.size() >= 2) {
                outerDuplicatesLoop:
                for (Iterator it = duplicatedReqs.listIterator(); it.hasNext();) {
                    Object[] o = (Object[]) it.next();
                    System.out.println("duplicated requests value " + o[1].toString());
                    if (!o[1].toString().equals("1")) {
                        //we have a request already for this strain and email
                        errors.reject("Message", "A request for this strain is already registered to the scientists e-mail address " + webReq.getSci_e_mail() + " therefore this request cannot be submitted at this time."
                                + "<br/>Please contact the EMMA Project Office at <a href='mailto:emma@infrafrontier.eu'>emma@infrafrontier.eu</a> for more information if needed.");
                        break outerDuplicatesLoop;
                    } else {
                        System.out.println("There is a ROI");
                        errors.reject("Message", "A registration of interest has already been submitted for this strain with this scientists e-mail address " + webReq.getSci_e_mail() + " therefore this request cannot be submitted at this time."
                                + "<br/>Please contact the EMMA Project Office at <a href='mailto:emma@infrafrontier.eu'>emma@infrafrontier.eu</a> for more information if needed.");
                        break outerDuplicatesLoop;
                    }
                }
            }
        }

        if (webReq.getSci_firstname() == null || webReq.getSci_firstname().trim().length() < 1) {
            errors.reject("Message", "The Scientists firstname field appears to have no value");
        } else if (webReq.getSci_firstname().length() >= 1 && webReq.getSci_firstname().length() > wr.fieldMaxLength("sci_firstname", table)) {
            errors.reject("Message", "The Scientists firstname field input appears to be too long for the database");
        }
        if (webReq.getSci_surname() == null || webReq.getSci_surname().trim().length() < 1) {
            errors.reject("Message", "The Scientists surname field appears to have no value");
        } else if (webReq.getSci_surname().length() >= 1 && webReq.getSci_surname().length() > wr.fieldMaxLength("sci_surname", table)) {
            errors.reject("Message", "The Scientists surname field input appears to be too long for the database");
        }
        if (webReq.getSci_e_mail() == null || webReq.getSci_e_mail().trim().length() < 1 || !patternMatch(EMAIL_PATTERN, webReq.getSci_e_mail().trim())) {
            errors.reject("Message", "The Scientists Email address field appears to have no value or is incorrect");
        } else if (webReq.getSci_e_mail().length() >= 1 && webReq.getSci_e_mail().length() > wr.fieldMaxLength("sci_e_mail", table)) {
            errors.reject("Message", "The Scientists Email address field input appears to be too long for the database");
        }
         if (webReq.getSci_e_mail().contains("-.") || webReq.getSci_e_mail().contains(".-")) {
            errors.reject("Message", "The Scientists Email address field appears to be incorrect");
        } 
        
        if (webReq.getSci_phone() == null || webReq.getSci_phone().trim().length() < 1) {
            errors.reject("Message", "The Scientists phone field appears to have no value or is incorrect");
        } else if (webReq.getSci_phone().length() >= 1 && webReq.getSci_phone().length() > wr.fieldMaxLength("sci_phone", table)) {
            errors.reject("Message", "The Scientists phone number field input appears to be too long for the database");
        }
        if (webReq.getCon_institution() == null || webReq.getCon_institution().trim().length() < 1) {
            errors.reject("Message", "The institution field appears to have no value");
        } else if (webReq.getCon_institution().length() >= 1 && webReq.getCon_institution().length() > wr.fieldMaxLength("con_institution", table)) {
            errors.reject("Message", "The institution field input appears to be too long for the database");
        }
        if (webReq.getCon_addr_1() == null || webReq.getCon_addr_1().trim().length() < 1) {
            errors.reject("Message", "The address field appears to have no value");
        } else if (webReq.getCon_addr_1().length() >= 1 && webReq.getCon_addr_1().length() > wr.fieldMaxLength("con_addr_1", table)) {
            errors.reject("Message", "The address field input appears to be too long for the database");
        }

        if (webReq.getCon_addr_2().length() >= 1 && webReq.getCon_addr_2().length() > wr.fieldMaxLength("con_addr_2", table)) {
            errors.reject("Message", "The address field input appears to be too long for the database");
        }

        if (webReq.getCon_town() == null || webReq.getCon_town().trim().length() < 1) {
            errors.reject("Message", "The town field appears to have no value");
        } else if (webReq.getCon_town().length() >= 1 && webReq.getCon_town().length() > wr.fieldMaxLength("con_town", table)) {
            errors.reject("Message", "The town field input appears to be too long for the database");
        }
        if (webReq.getCon_postcode() == null || webReq.getCon_postcode().trim().length() < 1) {
            errors.reject("Message", "The postcode field appears to have no value");
        } else if (webReq.getCon_postcode().length() >= 1 && webReq.getCon_postcode().length() > wr.fieldMaxLength("con_postcode", table)) {
            errors.reject("Message", "The postcode field input appears to be too long for the database");
        }
        if (webReq.getCon_country() == null || webReq.getCon_country().trim().length() < 1) {
            errors.reject("Message", "The country field appears to have no value");
        }
        if (webReq.getCon_e_mail() == null || webReq.getCon_e_mail().trim().length() < 1 || !patternMatch(EMAIL_PATTERN, webReq.getCon_e_mail().trim())) {
            errors.reject("Message", "The Contacts Email address field appears to have no value or is incorrect");
        }  else if (webReq.getCon_e_mail().length() >= 1 && webReq.getCon_e_mail().length() > wr.fieldMaxLength("con_e_mail", table)) {
            errors.reject("Message", "The Contacts Email address field input appears to be too long for the database");
        }
        
        if (webReq.getCon_e_mail().contains("-.") || webReq.getCon_e_mail().contains(".-")) {
            errors.reject("Message", "The Contacts Email address field appears to be incorrect");
        }
        if (webReq.getCon_phone() == null || webReq.getCon_phone().trim().length() < 1) {
            errors.reject("Message", "The Shipping Contacts phone field appears to have no value or is incorrect");
        } else if (webReq.getCon_phone().length() >= 1 && webReq.getCon_phone().length() > wr.fieldMaxLength("con_phone", table)) {
            errors.reject("Message", "The Contacts phone field input appears to be too long for the database");
        }
        if (webReq.getReq_material() == null) {
            errors.reject("Message", "Please select the material you wish to order");
        }
        /*
         * Billing or PO reference validation
         */

        if (!webReq.getRegister_interest().equals("1")) {

            //   if (webReq.getPO_ref() == null || webReq.getPO_ref().trim().length() < 1) {
            // Now need to check billing compulsory fields
            if (Arrays.asList(euCountriesList).contains(webReq.getCon_country()) || Arrays.asList(assocCountriesList).contains(webReq.getCon_country())) {
                //make sure vat is filled in
                if (webReq.getBil_vat() == null || webReq.getBil_vat().trim().length() < 1) {

                    errors.reject("Message", "The VAT number field appears to have no value. "
                            + "Please ensure all the required billing fields are filled in.");
                } else if (webReq.getBil_vat().length() >= 1 && webReq.getBil_vat().length() > wr.fieldMaxLength("bil_vat", table)) {
                    errors.reject("Message", "The VAT number field input appears to be too long for the database");
                }
            }
            if (webReq.getPO_ref() != null) {
                if (webReq.getPO_ref().length() >= 1 && webReq.getPO_ref().length() > wr.fieldMaxLength("PO_ref", table)) {
                    errors.reject("Message", "The purchase order reference field input appears to be too long for the database");
                }
            }

            if (webReq.getBil_firstname() == null || webReq.getBil_firstname().trim().length() < 1) {
                errors.reject("Message", "The billing contacts firstname field appears to have no value. "
                        + "Please ensure all the required billing contact fields are filled in.");
            } else if (webReq.getBil_firstname().length() >= 1 && webReq.getBil_firstname().length() > wr.fieldMaxLength("bil_firstname", table)) {
                errors.reject("Message", "The billing contacts firstname field input appears to be too long for the database");
            }
            if (webReq.getBil_surname() == null || webReq.getBil_surname().trim().length() < 1) {
                errors.reject("Message", "The billing contacts surname field appears to have no value. "
                        + "Please ensure all the required billing contact fields are filled in.");
            } else if (webReq.getBil_surname().length() >= 1 && webReq.getBil_surname().length() > wr.fieldMaxLength("bil_surname", table)) {
                errors.reject("Message", "The billing contacts surname field input appears to be too long for the database");
            }
            if (webReq.getBil_e_mail() == null || webReq.getBil_e_mail().trim().length() < 1 || !patternMatch(EMAIL_PATTERN, webReq.getBil_e_mail().trim())) {
                errors.reject("Message", "The billing contacts Email address field appears to have no value or is incorrect "
                        + "Please ensure all the required billing contact fields are filled in.");
            } else if (webReq.getBil_e_mail().length() >= 1 && webReq.getBil_e_mail().length() > wr.fieldMaxLength("bil_e_mail", table)) {
                errors.reject("Message", "The billing contacts Email address field input appears to be too long for the database");
            } 
            
             if (webReq.getBil_e_mail().contains("-.") || webReq.getBil_e_mail().contains(".-")) {
            errors.reject("Message", "The billing contacts Email address field appears to be incorrect");
            }
             
            if (webReq.getBil_phone() == null || webReq.getBil_phone().trim().length() < 1) {
                errors.reject("Message", "The billing contacts phone field appears to have no value. "
                        + "Please ensure all the required billing contact fields are filled in.");
            } else if (webReq.getBil_phone().length() >= 1 && webReq.getBil_phone().length() > wr.fieldMaxLength("bil_phone", table)) {
                errors.reject("Message", "The billing contacts phone field input appears to be too long for the database");
            }
            if (webReq.getBil_institution() == null || webReq.getBil_institution().trim().length() < 1) {
                errors.reject("Message", "The billing contacts institution field appears to have no value. "
                        + "Please ensure all the required billing contact fields are filled in.");
            } else if (webReq.getBil_institution().length() >= 1 && webReq.getBil_institution().length() > wr.fieldMaxLength("bil_institution", table)) {
                errors.reject("Message", "The billing contacts institution field input appears to be too long for the database");
            }
            if (webReq.getBil_addr_1() == null || webReq.getBil_addr_1().trim().length() < 1) {
                errors.reject("Message", "The billing contacts address field appears to have no value. "
                        + "Please ensure all the required billing contact fields are filled in.");
            } else if (webReq.getBil_addr_1().length() >= 1 && webReq.getBil_addr_1().length() > wr.fieldMaxLength("bil_addr_1", table)) {
                errors.reject("Message", "The billing contacts address field input appears to be too long for the database");
            }

            if (webReq.getBil_addr_2().length() >= 1 && webReq.getBil_addr_2().length() > wr.fieldMaxLength("bil_addr_2", table)) {
                errors.reject("Message", "The billing contacts address field input appears to be too long for the database");
            }

            if (webReq.getBil_town() == null || webReq.getBil_town().trim().length() < 1) {
                errors.reject("Message", "The billing contacts town field appears to have no value. "
                        + "Please ensure all the required billing contact fields are filled in.");
            } else if (webReq.getBil_town().length() >= 1 && webReq.getBil_town().length() > wr.fieldMaxLength("bil_town", table)) {
                errors.reject("Message", "The billing contacts town field input appears to be too long for the database");
            }
            if (webReq.getBil_postcode() == null || webReq.getBil_postcode().trim().length() < 1) {
                errors.reject("Message", "The billing contacts postcode field appears to have no value. "
                        + "Please ensure all the required billing contact fields are filled in.");
            } else if (webReq.getBil_postcode().length() >= 1 && webReq.getBil_postcode().length() > wr.fieldMaxLength("bil_postcode", table)) {
                errors.reject("Message", "The billing contacts postcode field input appears to be too long for the database");
            }
            if (webReq.getBil_country() == null || webReq.getBil_country().trim().length() < 1) {
                errors.reject("Message", "The billing contacts country field appears to have no value. "
                        + "Please ensure all the required billing contact fields are filled in.");
            }
            // }
        }

        if (webReq.getStrain_id() == null || webReq.getStrain_id().trim().length() < 1) {
            //the strain_id field is blank only way this can happen is if an internal insert is occurring and user hasn't selected the id from dropdown list
            errors.reject("Message", "The strain id field appears to have no value. "
                    + "Please ensure you select an ID from the highlighted autocomplete droplist, when inserting an internal request.");
        }

       // if (webReq.getStrain_name().contains("Wtsi")/*.getProjectID().equals("5")*/) {
            if (webReq.getImpc_phenotype_data_exists() != null && webReq.getImpc_phenotype_data_exists().equals("yes")) {
            System.out.println("STRAIN NAME CONTAINS Wtsi " + webReq.getWtsi_mouse_portal());
            if (webReq.getEurophenome() == null && webReq.getWtsi_mouse_portal() == null) {
                errors.reject("Message", "Please select the option that generated your interest or select no, if none apply?");
            }
            if (webReq.getWtsi_mouse_portal() != null && webReq.getEurophenome() != null) {
                if (webReq.getEurophenome().equals("yes") && webReq.getWtsi_mouse_portal().equals("yes")) {
                    //errors.reject("Message", "Please select no to one option that didn't generate your interest in the strain you have selected?"); REMOVED- requested by Sabine
                }
            }
            if (webReq.getWtsi_mouse_portal() != null && webReq.getWtsi_mouse_portal().equals("no")) {
                //europhenome must be checked then
                if (webReq.getEurophenome() == null) {
                    errors.reject("Message", "Was your interest in this strain generated from the International Mouse "
                            + "Phenotyping Consortium website?");
                }
            }

                if (webReq.getEurophenome() != null && webReq.getEurophenome().equals("no")) {
                    //pheno must be checked then
                    if (webReq.getWtsi_mouse_portal() == null) {
                        errors.reject("Message", "Was your interest in this strain generated from the WTSI Mouse Portal?");
                    }
                }
         

            if (webReq.getWtsi_mouse_portal() != null && webReq.getWtsi_mouse_portal().equals("yes")) {
                //europhenome must = no
                if (webReq.getEurophenome() == null) {
                    errors.reject("Message", "Please select 'No' to the question related to whether your interest in the strain was "
                            + "generated from the International Mouse Phenotyping Consortium website.");
                }
            }

            if (webReq.getEurophenome() != null && webReq.getEurophenome().equals("yes")) {
                //wtsi must = no
                if (webReq.getWtsi_mouse_portal() == null) {
                    errors.reject("Message", "Please select 'No' to the question related to whether your interest in the strain was "
                            + "generated from the WTSI Mouse Portal.");
                }
            }
        }
        //    }
        if (webReq.getProjectID() != null) {
            if (webReq.getProjectID().equals("300") || webReq.getProjectID().equals("400")) {//NOT USED ATM
                if (webReq.getEurophenome() == null) {
                    //errors.reject("Message", "Please select whether your interest in this strain was generated from the Europhenome website or not");
                }
            }
        }
    }

    public boolean patternMatch(String patternValue, String input) {
        Pattern pattern = Pattern.compile(patternValue);
        Matcher matcher = pattern.matcher(input);
        boolean matchFound = matcher.matches();

        //System.out.println(input);
        System.out.println(input + " validator reports " + matchFound);
        return matchFound;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }
}
