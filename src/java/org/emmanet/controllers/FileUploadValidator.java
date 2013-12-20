/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.emmanet.controllers;

import org.apache.log4j.Logger;
import org.emmanet.model.FileUpload;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author phil
 */
public class FileUploadValidator implements Validator {
    protected static Logger logger = Logger.getLogger(FileUploadValidator.class);

    @Override
    public boolean supports(Class type) {
        return FileUpload.class.isAssignableFrom(type);
    }

    @Override
    public void validate(Object o, Errors errors) {

        FileUpload fileUpload = (FileUpload) o;

        MultipartFile file = fileUpload.getFile();
        long size = file.getSize();
        String contentType = file.getContentType();
        String originalFilename = file.getOriginalFilename();
        String name = file.getName();
        logger.debug("Filename:     " + originalFilename);
        logger.debug("Content Type: " + contentType);
        logger.debug("Size:         " + size);
        
        if (size == 0) {
            errors.reject("Message", "File '" + name + "' is empty. Please choose another file.");
        } else if (size > 2097152) {
            //around 1MB = 1258291
            //around 2MB = 2097152
            errors.reject("Message", "File '" + originalFilename + "' (" + size + " bytes), exceeds the maximum size of 2097152 bytes.");
        }
        
        if ( ! contentType.endsWith("pdf")) {
            // Bug EMMA-550: Firefox 25 has a bug: it reports pdf mime types as x-unknown/x-unknown.
            // https://bugzilla.mozilla.org/show_bug.cgi?id=891334 (NOTE: it claims to be fixed for windows but as of 2013-12-20, it's broken for mac).
            if ( ! contentType.equals("x-unknown/x-unknown")) {
                errors.reject("Message", "File '" + originalFilename + "': expected pdf contents but your browser says the content type is '" + contentType + "'.");
            }
        }
    }
}
