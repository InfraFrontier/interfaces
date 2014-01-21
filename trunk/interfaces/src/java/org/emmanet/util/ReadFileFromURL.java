/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.emmanet.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author philwilkinson
 */
public class ReadFileFromURL {

    private URL url;
    final static String TMPFILES = Configuration.get("TMPFILES");

    public File ReadFileURL(URL fileurl, String filename) {

        String fileName = filename;
        File file = new File(TMPFILES + fileName);
        try {
            byte[] buffer = new byte[8 * 1024];
            // get URL content
            url = new URL(fileurl.toString());
            URLConnection conn = url.openConnection();

            InputStream input = conn.getInputStream();
            if (!file.exists()) {
                file.createNewFile();
            }
            OutputStream os = new FileOutputStream(file.getAbsoluteFile());
            int bytesRead;
            while ((bytesRead = input.read(buffer)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            os.close();
            input.close();
        } catch (MalformedURLException e) {
            Logger.getLogger(Encrypter.class.getName()).log(Level.INFO, null, e);
        } catch (IOException e) {
            Logger.getLogger(Encrypter.class.getName()).log(Level.INFO, null, e);
        }
        return file;
    }
}
