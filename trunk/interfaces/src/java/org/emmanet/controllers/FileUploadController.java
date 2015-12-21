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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.emmanet.model.FileUpload;
import org.emmanet.util.Configuration;
import org.emmanet.util.DirFileList;
import org.emmanet.util.Encrypter;
import org.springframework.validation.BindException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;

/**
 *
 * @author phil
 */
public class FileUploadController extends SimpleFormController {

    FileUpload file = new FileUpload();
    Encrypter encrypter = new Encrypter();
    HttpServletRequest request;
    final static String SUBFORMUPLOAD = Configuration.get("SUBFORMUPLOAD");
    // String toDecrypt="";

    public FileUploadController() {
        setCommandClass(FileUpload.class);
        setCommandName("fileUploadForm");//TODO MAY NEED CHANGING
    }

    @Override
    protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
        System.out.println("##ONSUBMIT CALLED##");
        System.out.println("##SUBMIT BUTTON " + request.getParameter("upload"));
        String isSubmitted = request.getParameter("upload");
        // file.setFile(null);
        if (isSubmitted != null) {


            file = (FileUpload) command;
            MultipartFile multipartFile = file.getFile();
            String decryptedID = encrypter.decrypt(request.getParameter("submissionID"));
            System.out.println(request.getParameter("submissionID") + " UNENCRYPTED AND DECRYPTED ID ==" + decryptedID);

            String fileName = "";
            if (multipartFile != null) {
                fileName = multipartFile.getOriginalFilename();

                System.out.println("FileName of Uploaded file==" + fileName + " || Size = " + multipartFile.getSize() + " || Type == " + multipartFile.getContentType());
                //read file into byte array method call
                byte[] bytes = getBytesFromFile(multipartFile);
                fileName = decryptedID + "_" + request.getParameter("submissionFileType").toString() + "_" + fileName;
                //write bytes to file
                File outFile = new File(fileName);
                writeBytes2File(bytes, outFile);
                request.getSession().setAttribute(
                        "message",
                        getMessageSourceAccessor().getMessage("Message", " Your file " + multipartFile.getOriginalFilename() + " uploaded successfully"));

            }
        }
//return new ModelAndView("publicSubmission/fileUploadForm","fileName",fileName);
//return new ModelAndView(getSuccessView());
        // return new ModelAndView("fileUploadForm.emma");
        return new ModelAndView(getSuccessView(), "fileName", file);
    }

    // Returns the contents of the file in a byte array.
    public static byte[] getBytesFromFile(MultipartFile file) throws IOException {
        System.out.println("Start of getBytesFromFile reached / " + file.getSize());
        InputStream is = file.getInputStream();

        // Get the size of the file
        long length = file.getSize();

        if (length > Integer.MAX_VALUE) {
            // File is too large 
        }

        // Create the byte array to hold the data
        byte[] bytes = new byte[(int) length];

        // Read in the bytes
        int offset = 0;
        int numRead = 0;
        while (offset < bytes.length
                && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
            offset += numRead;
        }

        // Ensure all the bytes have been read in
        if (offset < bytes.length) {
            throw new IOException("Could not completely read file " + file.getName());
        }

        // Close the input stream and return bytes
        is.close();
        System.out.println("End of getBytesFromFile reached / " + bytes.length);
        return bytes;
    }

    public void writeBytes2File(byte[] bytes, File submissionFile) {
        
        String path = SUBFORMUPLOAD;
        System.out.println("writeBytes2File reached / " + bytes.length + " / path=" + path + "File::" + submissionFile);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(path + submissionFile);
            fos.write(bytes);
            fos.flush();
        } catch (IOException ex) {
            Logger.getLogger(FileUploadController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                fos.close();
            } catch (IOException ex) {
                Logger.getLogger(FileUploadController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
