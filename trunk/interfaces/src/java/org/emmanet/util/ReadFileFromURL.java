/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.emmanet.util;

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
    private String encodedurl="";
    public File ReadFileURL(URL fileurl, String filename) {

        String fileName = filename;
        File file = new File(TMPFILES + fileName);
        try {
            byte[] buffer = new byte[8 * 1024];
            // get URL content
            url = new URL(fileurl.toString());
            URLConnection conn = url.openConnection();
//System.out.println("content type is::- " + conn.);
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
