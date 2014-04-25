/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.emmanet.controllers;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.emmanet.jobs.WebRequests;
import org.emmanet.model.StrainsManager;
import org.emmanet.model.SubmissionsDAO;
import org.emmanet.model.SubmissionsManager;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 *
 * @author phil
 */
public class SubmissionFormValidator implements
        Validator {
    // REGEX PATTERNS

    private WebRequests wr = new WebRequests();
    private static final String EMAIL_PATTERN
            = /* "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)"
             + "|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";*/ "^((([a-z]|\\d|[!#\\$%&'\\*\\+\\-\\/=\\?\\^_`{\\|}~]|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])+"
            + "(\\.([a-z]|\\d|[!#\\$%&'\\*\\+\\-\\/=\\?\\^_`{\\|}~]|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])+"
            + ")*)|((\\x22)((((\\x20|\\x09)*(\\x0d\\x0a))?(\\x20|\\x09)+)?(([\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x7f]|\\x21|[\\x23"
            + "-\\x5b]|[\\x5d-\\x7e]|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])|(\\\\([\\x01-\\x09\\x0b\\x0c\\x0d-\\"
            + "x7f]|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF]))))*(((\\x20|\\x09)*(\\x0d\\x0a))?(\\x20|\\x09)+)?"
            + "(\\x22)))@((([a-z]|\\d|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])|(([a-z]|\\d|[\\u00A0-\\uD7FF"
            + "\\uF900-\\uFDCF\\uFDF0-\\uFFEF])([a-z]|\\d|-|\\.|_|~|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])"
            + "*([a-z]|\\d|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])))\\.)+(([a-z]|[\\u00A0-\\uD7FF\\uF900-"
            + "\\uFDCF\\uFDF0-\\uFFEF])|(([a-z]|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])([a-z]|\\d|-|\\.|_|~|["
            + "\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])*([a-z]|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-"
            + "\\uFFEF])))\\.?$";
    private static final String FAXTEL_PATTERN = "^\\+\\d+[\\d\\-\\(\\)\\s]+(x\\d+)?$";
    private static final String DIGITSONLY_PATTERN = "^(0|[1-9][0-9]*)$";
private SubmissionsManager subm = new SubmissionsManager();
    public boolean supports(Class type) {
        return type.equals(SubmissionsDAO.class);
    }

    public void validate(Object o, Errors errors) {
        // StrainsDAO sd = (StrainsDAO) o;
        SubmissionsDAO sd = (SubmissionsDAO) o;
        if (sd == null) {
            return;
        } else {
            validateSubmissionForm0(sd, errors);
            validateSubmissionForm1(sd, errors, "");

        }
    }

    public void validateSubmissionForm(SubmissionsDAO sd, Errors errors) {
        if (!sd.isTermsAgreed()) {
            errors.rejectValue("termsAgreed", "termsAgreed",
                    "You must agree to the terms and conditions before proceeding");
        }
    }

    public void validateSubmissionForm0(SubmissionsDAO sd, Errors errors) {
        System.out.println("STEP0 EMAIL IS " + sd.getSubmitter_email());
        if (!patternMatch(EMAIL_PATTERN, sd.getSubmitter_email())) {
            errors.rejectValue("submitter_email", "incorrect.email",
                    "The submitted Email address field appears to be incorrect.");
        }
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "submitter_email",
                "required.email", " A correct Email address is a required field.");
    }

    public void validateSubmissionFormx(SubmissionsDAO sd, Errors errors) {

        if (!patternMatch(EMAIL_PATTERN, sd.getSubmitter_email())) {
            errors.rejectValue("submitter_email", "incorrect.email",
                    "The submitted Email address field appears to be incorrect");
        }

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "submitter_email",
                "required.email", "Email is a required field.");
    }

    public void validateSubmissionForm1(SubmissionsDAO sd, Errors errors, String fieldSet) {

        String fax = "";
        String tel = "";
        String ilar = "";
        String firstname = "";
        String lastname = "";
        String institute = "";
        String address1 = "";
        String address2 = "";
        String dept = "";
        String city = "";
        String county = "";
        String postcode = "";

        String field = "";

        if (fieldSet.equals("submitter")) {
            fax = sd.getSubmitter_fax();
            tel = sd.getSubmitter_tel();
            firstname = sd.getSubmitter_firstname();
            lastname = sd.getSubmitter_lastname();
            institute = sd.getSubmitter_inst();
            address1 = sd.getSubmitter_addr_1();
            address2 = sd.getSubmitter_addr_2();
            dept = sd.getSubmitter_dept();
            county = sd.getSubmitter_county();
            city = sd.getSubmitter_city();
            postcode = sd.getSubmitter_postcode();
        } else if (fieldSet.equals("producer")) {
            fax = sd.getProducer_fax();
            tel = sd.getProducer_tel();
            firstname = sd.getProducer_firstname();
            lastname = sd.getProducer_lastname();
            institute = sd.getProducer_inst();
            address1 = sd.getProducer_addr_1();
            address2 = sd.getProducer_addr_2();
            dept = sd.getProducer_dept();
            city = sd.getProducer_city();
            county = sd.getProducer_county();
            postcode = sd.getProducer_postcode();
            ilar = sd.getProducer_ilar();
            ilar = ilar.trim();
        } else if (fieldSet.equals("shipper")) {
            fax = sd.getShipper_fax();
            tel = sd.getShipper_tel();
            firstname = sd.getShipper_firstname();
            lastname = sd.getShipper_lastname();
            institute = sd.getShipper_inst();
            address1 = sd.getShipper_addr_1();
            address2 = sd.getShipper_addr_2();
            dept = sd.getShipper_dept();
            city = sd.getShipper_city();
            county = sd.getShipper_county();
            postcode = sd.getShipper_postcode();
        }

        if (!patternMatch(EMAIL_PATTERN, sd.getSubmitter_email())) {
            errors.rejectValue(fieldSet + "_email", "incorrect.email",
                    "The submitted Email address field appears to be incorrect.");
        }
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, fieldSet + "_email",
                "required.email", " A correct Email address is a required field.");

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, fieldSet + "_firstname",
                "required.firstname", "The " + fieldSet + " firstname is a required field");
        field = firstname;
        if (field.length() > wr.fieldMaxLength(fieldSet + "_firstname", "submissions")) {
            errors.rejectValue(fieldSet + "_firstname", "", "The field input appears to be too long for the database. Maximum length of input is " 
                    + wr.fieldMaxLength(fieldSet + "_firstname", "submissions") + " characters.");
        }

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, fieldSet + "_lastname", "required.lastname",
                "The " + fieldSet + " family name is a required field");
        field = lastname;
        if (field.length() > wr.fieldMaxLength(fieldSet + "_lastname", "submissions")) {
            errors.rejectValue(fieldSet + "_lastname", "" + fieldSet + "_lastname", "The field input appears to be too long for the database. Maximum length of input is " 
                    + wr.fieldMaxLength(fieldSet + "_lastname", "submissions") + " characters.");
        }
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, fieldSet + "_tel", "required.tel",
                "The telephone number is a required field");

        field = tel;
        if (field.length() > wr.fieldMaxLength(fieldSet + "_tel", "submissions")) {
            errors.rejectValue(fieldSet + "_tel", "", "The field input appears to be too long for the database. Maximum length of input is " 
                    + wr.fieldMaxLength(fieldSet + "_tel", "submissions") + " characters.");
        }

        if (ilar.length() > 5) {
            errors.rejectValue("producer_ilar", "incorrect.producer_ilar",
                    "Your submitted ILAR code exceeds the number of characters permitted (greater than 5 characters).");
        }

     /*   if (tel != null && !patternMatch(FAXTEL_PATTERN, tel)) {
           // errors.rejectValue(fieldSet + "_tel", "incorrect.tel",
            //    "Please enter a valid phone number (it must begin with <b>+</b> followed by the country code).");
        }

        if (!patternMatch(FAXTEL_PATTERN, fax)) {
            //  errors.rejectValue(fieldSet + "_fax", "incorrect.fax",
            //    "Please enter a valid fax number (it must begin with <b>+</b> followed by the country code).");
        }
        * */
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, fieldSet + "_fax", "required.fax",
                "The fax number is a required field");
        field = fax;
        if (field.length() > wr.fieldMaxLength(fieldSet + "_fax", "submissions")) {
            errors.rejectValue(fieldSet + "_fax", "", "The field input appears to be too long for the database. Maximum length of input is " 
                    + wr.fieldMaxLength(fieldSet + "_fax", "submissions") + " characters.");
        }

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, fieldSet + "_inst", "required.inst",
                "The institution is a required field");
        field = institute;
        if (field.length() > wr.fieldMaxLength(fieldSet + "_inst", "submissions")) {
            errors.rejectValue(fieldSet + "_inst", "", "The field input appears to be too long for the database. Maximum length of input is " 
                    + wr.fieldMaxLength(fieldSet + "_inst", "submissions") + " characters.");
        }
        field = dept;
        if (field.length() > wr.fieldMaxLength(fieldSet + "_dept", "submissions")) {
            errors.rejectValue(fieldSet + "_dept", "", "The field input appears to be too long for the database. Maximum length of input is " 
                    + wr.fieldMaxLength(fieldSet + "_dept", "submissions") + " characters.");
        }

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, fieldSet + "_addr_1", "required.submitter_addr_1",
                "The address line 1 field is required");

        field = address1;
        if (field.length() > wr.fieldMaxLength(fieldSet + "_addr_1", "submissions")) {
            errors.rejectValue(fieldSet + "_addr_1", "", "The field input appears to be too long for the database. Maximum length of input is " 
                    + wr.fieldMaxLength(fieldSet + "_addr_1", "submissions") + " characters.");
        }
        field = address2;
        if (field.length() > wr.fieldMaxLength(fieldSet + "_addr_2", "submissions")) {
            errors.rejectValue(fieldSet + "_addr_2", "", "The field input appears to be too long for the database. Maximum length of input is " 
                    + wr.fieldMaxLength(fieldSet + "_addr_2", "submissions") + " characters.");
        }

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, fieldSet + "_city", "required.city",
                "The city is a required field");
        field = city;
        if (field.length() > wr.fieldMaxLength(fieldSet + "_city", "submissions")) {
            errors.rejectValue(fieldSet + "_city", "", "The field input appears to be too long for the database. Maximum length of input is " 
                    + wr.fieldMaxLength(fieldSet + "_city", "submissions") + " characters.");
        }

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, fieldSet + "_country", "required.country",
                "Please select a Country from the list");
        field = county;
        if (field.length() > wr.fieldMaxLength(fieldSet + "_county", "submissions")) {
            errors.rejectValue(fieldSet + "_county", "", "The field input appears to be too long for the database. Maximum length of input is " 
                    + wr.fieldMaxLength(fieldSet + "_county", "submissions") + " characters.");
        }
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, fieldSet + "_postcode", "required.postcode",
                "Postal code/Zip code is a required field");

        field = postcode;
        if (field.length() > wr.fieldMaxLength(fieldSet + "_postcode", "submissions")) {
            errors.rejectValue(fieldSet + "_postcode", "", "The field input appears to be too long for the database. Maximum length of input is " 
                    + wr.fieldMaxLength(fieldSet + "_postcode", "submissions") + " characters.");
        }
        List err = new ArrayList();
        err = errors.getAllErrors();
        for (Iterator i = err.listIterator(); i.hasNext();) {
            Object o = i.next().toString();
            System.out.println("FINAL ERRORS OBJECT = " + o);
        }
    }

    public void validateSubmissionForm4(SubmissionsDAO sd, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "strain_name",
                "required.strain_name", "The Strain name is a required field");
        StrainsManager sm = new StrainsManager();
        BigInteger i;
        i = sm.getStrainsWithName(sd.getStrain_name());
        System.out.println("validator strain name list size is :: " + i);
        if (i.intValue() >= 1) {
            errors.rejectValue("strain_name", "strain_name",
                    "A strain with this name already exists.");
        }

        if (sd.getStrain_name().length() > wr.fieldMaxLength("strain_name", "submissions")) {
            System.out.println("STRAIN NAME IS TOO LONG");
            errors.rejectValue("strain_name", "", "The field input appears to be too long for the database. Maximum length of input is " 
                    + wr.fieldMaxLength("strain_name", "submissions") + " characters.");
        }

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "genetic_descr", "required.genetic_descr",
                "The genetic description is a required field");

        /*  if (sd.getGenetic_descr().length() > wr.fieldMaxLength("genetic_descr", "submissions")) {
         errors.rejectValue("genetic_descr", "", "The field input appears to be too long for the database. Maximum length of input is " + wr.fieldMaxLength("genetic_descr", "submissions") + " characters.");
         }*/
        if (sd.getCurrent_backg() == 0) {
            errors.rejectValue("current_backg", "current_backg",
                    "A current genetic backround must be selected");
        }

        //can be empty but if not then must be numeric
        if (sd.getBackcrosses() != null && !sd.getBackcrosses().isEmpty()) {
            if (!patternMatch(DIGITSONLY_PATTERN, sd.getBackcrosses())) {
                errors.rejectValue("backcrosses", "incorrect.backcrosses",
                        "Please enter a valid number (no leading 0).");
            }
        }

        //can be empty but if not then must be numeric
        if (sd.getSibmatings() != null && !sd.getSibmatings().isEmpty()) {
            if (!patternMatch(DIGITSONLY_PATTERN, sd.getSibmatings())) {
                errors.rejectValue("sibmatings", "incorrect.sibmatings",
                        "Please enter a valid number (no leading 0).");
            }
        }
        /*    if (sd.getBreeding_history().length() > wr.fieldMaxLength("breeding_history", "submissions")) {
         errors.rejectValue("breeding_history", "", "The field input appears to be too long for the database. Maximum length of input is " + wr.fieldMaxLength("breeding_history", "submissions") + " characters.");
         */
        
        //Check for at least one mutation in mutation_strains table EMMA-644
        BigInteger bi=subm.getSubMutationsCountBySUBID(Integer.parseInt(sd.getId_sub()));
        if(bi.intValue() < 1) {
        errors.rejectValue("mutation_type", "incorrect.mutation_type",
                        "Please ensure that you add at least one mutation, making sure you add it to the list by using the 'Record this mutation' button.");
    }
        
        //Check for at least one sub-type for main types CH/TM and IN EMMA-644
        
        if (sd.getMutation_type() != null && sd.getMutation_type().equals("CH")) {
            if (sd.getMutation_subtypeCH() == null || sd.getMutation_subtypeCH().isEmpty()) {
                errors.rejectValue("mutation_subtypeCH", "incorrect.mutation_subtypeCH",
                        "Please ensure that you select a value for mutation subtype.");
            }
        } else if (sd.getMutation_type() != null && sd.getMutation_type().equals("TM")) {
            if (sd.getMutation_subtypeTM() == null || sd.getMutation_subtypeTM().isEmpty()) {
                errors.rejectValue("mutation_subtypeTM", "incorrect.mutation_subtypeTM",
                        "Please ensure that you select a value for mutation subtype.");
            } 
        } else if (sd.getMutation_type() != null && sd.getMutation_type().equals("IN")) {
                if (sd.getMutation_subtypeIN() == null || sd.getMutation_subtypeIN().isEmpty()) {
                    errors.rejectValue("mutation_subtypeIN", "incorrect.mutation_subtypeIN",
                            "Please ensure that you select a value for mutation subtype.");
                }
        }
  
        List errorList = errors.getAllErrors();
        for (Iterator it = errorList.listIterator(); it.hasNext();) {
            Object o = it.next();
            System.out.println("Error is:: " + o);
        }

    } //end of step 4

    public void validateSubmissionForm5(SubmissionsDAO sd, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "homozygous_phenotypic_descr",
                "required.homozygous_phenotypic_descr", "The Phenotypic description of homozygous mice is a required field");
        /*    if (sd.getHomozygous_phenotypic_descr().length() > wr.fieldMaxLength("homozygous_phenotypic_descr", "submissions")) {
         errors.rejectValue("homozygous_phenotypic_descr", "", "The field input appears to be too long for the database. Maximum length of input is " + wr.fieldMaxLength("homozygous_phenotypic_descr", "submissions") + " characters.");
         }*/
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "heterozygous_phenotypic_descr", "required.heterozygous_phenotypic_descr",
                "The Phenotypic description of heterozygous mice is a required field");
        /* if (sd.getHeterozygous_phenotypic_descr().length() > wr.fieldMaxLength("heterozygous_phenotypic_descr", "submissions")) {
         errors.rejectValue("heterozygous_phenotypic_descr", "", "The field input appears to be too long for the database. Maximum length of input is " + wr.fieldMaxLength("heterozygous_phenotypic_descr", "submissions") + " characters.");
         }*/
    }

    public void validateSubmissionForm6(SubmissionsDAO sd, Errors errors) {
        ///BIBLIOS
        //IF ANSWER TO QUESTION 'HAS THIS MUTANT STRAIN BEEN PUBLISHED IS YES THEN CHECK FOR SHORT DESCRIPTION, JOURNAL,YEAR,PAGES'
        //turned off at release to avoid errors needs further work nothing added instead of yes
        if (sd.getPublished() != null && sd.getPublished().equals("nothing")) {
            //OK Now check for required fields
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "journal",
                    "required.year", "The journal is a required field");
            System.out.println("The value of pubmed id at validation is -- " + sd.getPubmed_id());
            if (sd.getPubmed_id() != null || !sd.getPubmed_id().isEmpty()) {
                ValidationUtils.rejectIfEmptyOrWhitespace(errors, "year", "required.year",
                        "The year is a required field");
                ValidationUtils.rejectIfEmptyOrWhitespace(errors, "pages", "required.pages",
                        "The page reference is a required field");
            }
        }
    }

    public void validateSubmissionForm7(SubmissionsDAO sd, Errors errors) {
        if (sd.getGenotyping().length() > wr.fieldMaxLength("genotyping", "submissions")) {
            errors.rejectValue("genotyping", "", "The field input appears to be too long for the database. Maximum length of input is " 
                    + wr.fieldMaxLength("genotyping", "submissions") + " characters.");
        }
        if (sd.getPhenotyping().length() > wr.fieldMaxLength("phenotyping", "submissions")) {
            errors.rejectValue("phenotyping", "", "The field input appears to be too long for the database. Maximum length of input is " 
                    + wr.fieldMaxLength("phenotyping", "submissions") + " characters.");
        }
        if (sd.getOthertyping().length() > wr.fieldMaxLength("othertyping", "submissions")) {
            errors.rejectValue("othertyping", "", "The field input appears to be too long for the database. Maximum length of input is " 
                    + wr.fieldMaxLength("othertyping", "submissions") + " characters.");
        }
    }

    public void validateSubmissionForm8(SubmissionsDAO sd, Errors errors) {
        //BREEDING
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "homozygous_viable",
                "required.homozygous_viable", "Are homozygous mice viable is a required field");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "breeding_performance",
                "required.breeding_performance", "Breeding performance is a required field");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "homozygous_fertile", "required.homozygous_fertile",
                "Are homozygous mice fertile is a required field");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "heterozygous_fertile", "required.heterozygous_fertile",
                "Are heterozygous mice fertile is a required field");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "homozygous_matings_required", "required.heterozygous_matings_required",
                "Are homozygous matings required is a required field");
        if (sd.getHomozygous_matings_required() != null && sd.getHomozygous_matings_required().equals("yes")) {
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "homozygous_matings_required_text", "required.heterozygous_matings_required_text",
                    "An explanation is required for the homozygous matings required response.");
            if (sd.getHomozygous_matings_required_text().length() > wr.fieldMaxLength("homozygous_matings_required_text", "submissions")) {
                errors.rejectValue("homozygous_matings_required_text", "", "The field input appears to be too long for the database. Maximum length of input is " 
                        + wr.fieldMaxLength("homozygous_matings_required_text", "submissions") + " characters.");
            }
        }

        if (sd.getWeaning_age() != null && !sd.getWeaning_age().isEmpty()) {
            if (!patternMatch(DIGITSONLY_PATTERN, sd.getWeaning_age())) {
                errors.rejectValue("weaning_age", "incorrect.weaning_age",
                        "Please enter a valid number for weaning age (no leading 0).");
            }
        }

        if (sd.getHusbandry_requirements().length() > wr.fieldMaxLength("husbandry_requirements", "submissions")) {
            errors.rejectValue("husbandry_requirements", "", "The field input appears to be too long for the database. Maximum length of input is " 
                    + wr.fieldMaxLength("husbandry_requirements", "submissions") + " characters.");
        }
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "immunocompromised", "required.immunocompromised",
                "Are mice immunocompromised is a required field");

        if (sd.getSanitary_status().length() > wr.fieldMaxLength("sanitary_status", "submissions")) {
            errors.rejectValue("sanitary_status", "", "The field input appears to be too long for the database. Maximum length of input is " 
                    + wr.fieldMaxLength("sanitary_status", "submissions") + " characters.");
        }
        if (sd.getWelfare().length() > wr.fieldMaxLength("welfare", "submissions")) {
            errors.rejectValue("welfare", "", "The field input appe,ars to be too long for the database. Maximum length of input is " 
                    + wr.fieldMaxLength("welfare", "submissions") + " characters.");
        }
        if (sd.getRemedial_actions().length() > wr.fieldMaxLength("remedial_actions", "submissions")) {
            errors.rejectValue("remedial_actions", "", "The field input appears to be too long for the database. Maximum length of input is " 
                    + wr.fieldMaxLength("remedial_actions", "submissions") + " characters.");
        }
    }

    public void validateSubmissionForm9(SubmissionsDAO sd, Errors errors) {
//        RESEARCH VALUE
        String humanCondition = "";
        humanCondition = sd.getHuman_condition();
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "human_condition",
                "required.human_condition", "Does this strain model a human condition or disease is a required field");

        if (humanCondition != null && humanCondition.equals("yes")) {
            System.out.println("human condition needs validation");
            if (sd.getHuman_condition_more().isEmpty()) {
                System.out.println("human condition more==" + sd.getHuman_condition_more());
                ValidationUtils.rejectIfEmptyOrWhitespace(errors, "human_condition_text", "required.human_condition_text",
                        "Either an entry in the OMIM identifiers field or description of the human disease or condition field is required.");
            }
            if (sd.getHuman_condition_more().length() > wr.fieldMaxLength("human_condition_more", "submissions")) {
                errors.rejectValue("human_condition_more", "", "The field input appears to be too long for the database. Maximum length of input is " 
                        + wr.fieldMaxLength("human_condition_more", "submissions") + " characters.");
            }
            if (sd.getHuman_condition_text().isEmpty()) {
                System.out.println("human condition text==" + sd.getHuman_condition_text());
                ValidationUtils.rejectIfEmptyOrWhitespace(errors, "human_condition_more", "required.human_condition_more",
                        "Either an entry in the description of the human disease or condition field or the OMIM identifiers field is required.");
            }
            /*   if (sd.getHuman_condition_text().length() > wr.fieldMaxLength("human_condition_text", "submissions")) {
             errors.rejectValue("human_condition_text", "", "The field input appears to be too long for the database. Maximum length of input is " + wr.fieldMaxLength("human_condition_text", "submissions") + " characters.");
             }*/
        }
        if (sd.getResearch_areas_other_text().length() > wr.fieldMaxLength("research_areas_other_text", "submissions")) {
            errors.rejectValue("research_areas_other_text", "", "The field input appears to be too long for the database. Maximum length of input is " 
                    + wr.fieldMaxLength("research_areas_other_text", "submissions") + " characters.");
        }
    }

    public void validateSubmissionForm10(SubmissionsDAO sd, Errors errors) {

        //ADDITIONAL INFORMATION SECTION
        String deposElsewhere = "";
        deposElsewhere = sd.getDeposited_elsewhere();
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "past_requests",
                "required.past_requests", "Please select the number of requests you have received for this strain in the last 6 months.");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "deposited_elsewhere", "deposited_elsewhere",
                "A response to this question is required");
        if (sd.getDeposited_elsewhere() != null && sd.getDeposited_elsewhere().equals("yes")) {
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "deposited_elsewhere_text", "deposited_elsewhere_text",
                    "An explanation for your response is required");
            /*    if (sd.getDeposited_elsewhere_text().length() > wr.fieldMaxLength("deposited_elsewhere_text", "submissions")) {
             errors.rejectValue("deposited_elsewhere_text", "", "The field input appears to be too long for the database. Maximum length of input is " + wr.fieldMaxLength("deposited_elsewhere_text", "submissions") + " characters.");
             }*/
        }

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "similar_strains", "similar_strains",
                "A response to this question is required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "ip_rights", "ip_rights",
                "A response to this question is required");
        if (sd.getIp_rights() != null && sd.getIp_rights().equals("yes")) {
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "ip_rights_text", "ip_rights_text",
                    "An explanation for your response is required");
            /*   if (sd.getIp_rights_text().length() > wr.fieldMaxLength("ip_rights_text", "submissions")) {
             errors.rejectValue("ip_rights_text", "", "The field input appears to be too long for the database. Maximum length of input is " + wr.fieldMaxLength("ip_rights_text", "submissions") + " characters.");
             }*/
        }
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "exclusive_owner", "exclusive_owner",
                "A response to this question is required");
        if (sd.getExclusive_owner() != null && sd.getExclusive_owner().equals("no")) {
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "exclusive_owner_text", "exclusive_owner_text",
                    "Names of additional owners with affiliation and e-mail addresses are required.");
            /*  if (sd.getExclusive_owner_text().length() > wr.fieldMaxLength("exclusive_owner_text", "submissions")) {
             errors.rejectValue("exclusive_owner_text", "", "The field input appears to be too long for the database. Maximum length of input is " + wr.fieldMaxLength("exclusive_owner_text", "submissions") + " characters.");
             }*/
        }
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "owner_permission", "owner_permission",
                "A response to this question is required");
        if (sd.getOwner_permission() != null && sd.getOwner_permission().equals("no")) {
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "owner_permission_text", "owner_permission_text",
                    "An explanation for your response is required.");
            /*  if (sd.getOwner_permission_text().length() > wr.fieldMaxLength("owner_permission_text", "submissions")) {
             errors.rejectValue("owner_permission_text", "", "The field input appears to be too long for the database. Maximum length of input is " + wr.fieldMaxLength("owner_permission_text", "submissions") + " characters.");
             }*/
        }
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "delayed_release", "delayed_release",
                "A response to this question is required");
        if (sd.getDelayed_release() != null && sd.getDelayed_release().equals("yes")) {
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "delayed_release_text", "delayed_release_text",
                    "An explanation for the delayed release is required.");
            /*   if (sd.getDelayed_release_text().length() > wr.fieldMaxLength("delayed_release_text", "submissions")) {
             errors.rejectValue("delayed_release_text", "", "The field input appears to be too long for the database. Maximum length of input is " + wr.fieldMaxLength("delayed_release_text", "submissions") + " characters.");
             }*/
        }

        //dates of mouse availability
        Date today = new Date();
        SimpleDateFormat DATE_FORMAT_YEAR = new SimpleDateFormat("yyyy");
        SimpleDateFormat DATE_FORMAT_MONTH = new SimpleDateFormat("MM");
        int year = Integer.parseInt(DATE_FORMAT_YEAR.format(today));
        int month = Integer.parseInt(DATE_FORMAT_MONTH.format(today));
        //System.out.println("combined system date " + month + " " + year);

        if (sd.getMice_avail_year() != null && !sd.getMice_avail_year().isEmpty()) {
            int availYear = Integer.parseInt(sd.getMice_avail_year());
            //System.out.println("user supplied year " + availYear);
            //possible fail on date but check month first
            if (availYear == year) {
                //check month
                if (sd.getMice_avail_month() != null && !sd.getMice_avail_month().isEmpty()) {
                    int availMonth = Integer.parseInt(sd.getMice_avail_month());
                    //System.out.println("user supplied month " + availMonth);
                    if (availMonth <= month) {
                        //failed
                        errors.rejectValue("mice_avail_month", "incorrect.mice_avail_month",
                                "Please check the availability date.");
                    }
                }
            }
        }
    }

    public void validateSubmissionForm11(SubmissionsDAO sd, Errors errors) {
        /* ValidationUtils.rejectIfEmptyOrWhitespace(errors, "peopleDAO.firstname",
         "required.userName", "The Scientists firstname is a required field");
         ValidationUtils.rejectIfEmptyOrWhitespace(errors, "peopleDAO.surname", "required.familyname",
         "The Scientists family name is a required field");*/
    }

    public boolean patternMatch(String patternValue, String input) {
        Pattern pattern = Pattern.compile(patternValue);
        Matcher matcher = pattern.matcher(input);
        boolean matchFound = matcher.matches();
        System.out.println(input + " validator reports " + matchFound);
        return matchFound;
    }

}
