/*
 * This class implements a spring <code>Validator</code> for emma.
 */
package org.emmanet.healthcheck;

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

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.apache.log4j.Logger;
import org.emmanet.model.BibliosDAO;
import org.emmanet.util.Utils;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.PropertyValue;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.validation.Validator;

/**
 * This class implements a spring <code>Validator</code> for emma.
 * @author mrelac
 */
public class EmmaBiblioValidator implements Validator {
    private ApplicationContext ac;
    private String[] allowedFields;
    private boolean hasErrors;
    private Field[] fields;
    private ReloadableResourceBundleMessageSource messages;
    
    protected Logger logger = Logger.getLogger(EmmaBiblioValidator.class);
    
    public EmmaBiblioValidator() {
        setAllowedFields();                                                     // Uses reflection, so can be slow. Call only once.
        hasErrors = false;
        ac = new ClassPathXmlApplicationContext("/jobApplicationContext.xml");  // Get job application context.
        messages = (ReloadableResourceBundleMessageSource)ac.getBean("messageSource");    // Get message resource file.
    }
    
    public boolean hasErrors() {
        return hasErrors;
    }

    /**
     * This is the main entry point for <code>BibliosDAO</code> validation. Given
     * a list of <code>BibliosDAO</code> which may be null or empty, this method
     * performs field validation, logging any errors. Errors are returned in a
     * <code>Map</code> whose key is the biblio_id primary key and whose value
     * is the corresponding <code>BibliosDAO</code> instance. If there are no
     * errors, an empty map is returned.
     * 
     * @param biblioList The list of values to be validated
     * @return  a <code>Map</code> whose key is the biblio_id primary key and
     * whose value is the corresponding <code>BibliosDAO</code> instance
     */
    public Map<Integer, BibliosDAO> validate(List<BibliosDAO> biblioList) {
        Map<Integer, BibliosDAO> validationErrorMap = new HashMap<>();
        BindingResult bindingResult = null;

        if (( biblioList != null) && ( ! biblioList.isEmpty())) {
            Object[] params = new Object[] { biblioList.size() };
            String logMsg = messages.getMessage("Biblios.BiblioValidationCount", params, Locale.UK);
            logger.info(logMsg);
            
            for (BibliosDAO biblio : biblioList) {
                bindingResult = validate(biblio);
                hasErrors = bindingResult.hasErrors();
                if (bindingResult.hasErrors()) {
                    validationErrorMap.put(biblio.getId_biblio(), biblio);
                }
                
                // Log any errors.
                if (bindingResult.hasErrors()) {
                    List<ObjectError> errors = bindingResult.getAllErrors();
                    for (ObjectError error : errors) {
                        logger.error(error.getDefaultMessage());
                    }
                }
            }
        }
        
        return validationErrorMap;
    }
    
    /**
     * NOT INTENDED TO BE DIRECTLY CALLED BY CLIENTS.
     * 
     * This is the <code>BibliosDAO</code> validation implementation. <code>
     * object</code> must be of type <code>BibliosDAO</code>.
     * @param object the <code>BibliosDAO</code> object to validate
     * @param errors The returned errors
     */
    @Override
    public void validate(Object object, Errors errors) {
        MutablePropertyValues mutablePropertyValues = (MutablePropertyValues)object;


        // As of 10-Sept-2013, all biblio parameters are optional.
        
        // Check that each text field doesn't exceed maximum length defined in database.
        
        // Check that 'year', if supplied, is a number.
        PropertyValue pvYear = mutablePropertyValues.getPropertyValue("year");
        String sPk = mutablePropertyValues.getPropertyValue("id_biblio").getValue().toString();
        if ((pvYear.getValue() != null) && ( ! pvYear.getValue().toString().trim().isEmpty())) {
            Integer year = Utils.tryParseInt(pvYear.getValue());
            if (year == null) {
                Object[] parmArray = new Object[] { sPk, pvYear.getValue().toString() };
                String defaultMessage = messages.getMessage("Biblios.InvalidYear", parmArray, Locale.UK);
                errors.reject("Biblios.InvalidYear", defaultMessage);     // Can't get message parameter replacement to work. Do it by hand using default message.
            }
        }
            
        // Check that 'pubmed_id', if supplied, is a number and is > 0.
        PropertyValue pvPubmed_id = mutablePropertyValues.getPropertyValue("pubmed_id");
        if ((pvPubmed_id.getValue() != null) && ( ! pvPubmed_id.getValue().toString().trim().isEmpty())) {
            Integer pubmed_id = Utils.tryParseInt(pvPubmed_id.getValue());
            if (pubmed_id == null) {
                Object[] parmArray = new Object[] { sPk, pvPubmed_id.getValue().toString() };
                String defaultMessage = messages.getMessage("Biblios.InvalidPubmed_id", parmArray, Locale.UK);
                errors.reject("Biblios.InvalidPubmed_id", defaultMessage);     // Can't get message parameter replacement to work. Do it by hand using default message.
            }
        }
    }

    /**
     * NOT INTENDED TO BE DIRECTLY CALLED BY CLIENTS.
     * 
     * This validator supports only the <code>BibliosDAO</code> class.
     * @param clazz The class of the object being validated
     * @return true if <code>clazz</code> is of type <code>BibliosDAO</code>;
     * false otherwise
     */
    @Override
    public boolean supports(Class clazz) {
        return BibliosDAO.class.equals(clazz);
    }


    // PRIVATE METHODS
    
    
    /**
     * Allowed fields are defined by the biblio job dao. Because this method
     * uses reflection, which can be slow, it should be called as infrequently
     * as possible; e.g. from the constructor.
     */
    private void setAllowedFields() {
        fields = BibliosDAO.class.getDeclaredFields();
        
        allowedFields = new String[fields.length];
        for (int i = 0; i < fields.length; i++)
            allowedFields[i] = fields[i].getName();
    }

    private BindingResult validate(/*DataBinder dataBinder,*/ BibliosDAO biblio) {
        List<PropertyValue> biblioPropertyList = new ArrayList<>();
        biblioPropertyList.add(new PropertyValue("id_biblio", biblio.getId_biblio()));
        biblioPropertyList.add(new PropertyValue("title", biblio.getTitle()));
        biblioPropertyList.add(new PropertyValue("author1", biblio.getAuthor1()));
        biblioPropertyList.add(new PropertyValue("author2", biblio.getAuthor2()));
        biblioPropertyList.add(new PropertyValue("year", biblio.getYear()));
        biblioPropertyList.add(new PropertyValue("journal", biblio.getJournal()));
        biblioPropertyList.add(new PropertyValue("volume", biblio.getVolume()));
        biblioPropertyList.add(new PropertyValue("pubmed_id", biblio.getPubmed_id()));
        biblioPropertyList.add(new PropertyValue("updated", biblio.getUpdated()));
        MutablePropertyValues mpv = new MutablePropertyValues(biblioPropertyList);
        
        DataBinder dataBinder = new DataBinder(biblio);
        dataBinder.setAllowedFields(allowedFields);
        dataBinder.bind(mpv);
        BindingResult bindingResult = dataBinder.getBindingResult();
            
        validate(mpv, bindingResult);
        return bindingResult;
    }

}