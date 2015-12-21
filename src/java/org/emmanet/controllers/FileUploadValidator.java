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
            // application/x-download condition added by philw as another Firefox content type for pdf
            if ( ! contentType.equals("x-unknown/x-unknown") && ! contentType.equals("application/x-download")) {
                errors.reject("Message", "File '" + originalFilename + "': expected pdf contents but your browser says the content type is '" + contentType + "'.");
            }
        }
    }
}
