/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.emmanet.controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.net.URLDecoder;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.emmanet.util.Configuration;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

/**
 *
 * @author phil
 */
public class PdfDownloadController extends AbstractController {

    final static String SUBFORMUPLOAD = Configuration.get("SUBFORMUPLOAD");

    @Override
    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
        // @RequestMapping(value = "/download" , method = RequestMethod.GET) public void doDownload(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ServletContext context = request.getSession().getServletContext();
        String filename = request.getParameter("filename");
        filename = URLDecoder.decode(filename, "UTF-8");
        
        System.out.println("DECODED FILENAME == " + filename);
        
        String filePath = SUBFORMUPLOAD;
        String fullPath = filePath + filename;
        File downloadFile = new File(fullPath);
        FileInputStream inputStream = new FileInputStream(downloadFile);
        // get MIME type of the file
        String mimeType = context.getMimeType(fullPath);
        if (mimeType == null) {
            // set to binary type if MIME mapping not found
            mimeType = "application/pdf";
        }
        System.out.println("MIME type: " + mimeType);
        response.setContentType("application/pdf");

        // get output stream of the response
        OutputStream outputStream = response.getOutputStream();

        byte[] buffer = new byte[1024];
        int bytesRead = -1;
        // write bytes read from the input stream into the output stream
        try {
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
            if (outputStream != null) {
                outputStream.close();
            }
        }
        return null;
    }
}