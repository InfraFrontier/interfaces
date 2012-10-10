/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.emmanet.controllers;

import org.emmanet.model.FileUpload;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 *
 * @author phil
 */
public class FileUploadValidator implements Validator {

    public boolean supports(Class type) {
        //throw new UnsupportedOperationException("Not supported yet.");
        return FileUpload.class.isAssignableFrom(type);
    }

    public void validate(Object o, Errors errors) {

        FileUpload file = (FileUpload) o;

        if (file.getFile().getSize() == 0) {
            errors.reject("Message","No file selected for upload");
        }
        
        if (file.getFile().getSize() > 1258291) {
            //around 1MB
            errors.reject("Message","The file you are trying to upload is too large.");
        }
        
        if (!file.getFile().getContentType().endsWith("pdf")) {
            errors.reject("Message","The file type must be a PDF");
        }
    }
}
