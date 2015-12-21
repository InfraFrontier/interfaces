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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.emmanet.model.BibliosManager;
import org.emmanet.model.SubmissionBibliosDAO;
import org.emmanet.util.Encrypter;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

/**
 *
 * @author phil
 * 
 */
public class submissionBibliosController implements Controller {

    public static final String MAP_KEY = "returnedOut";
    private Map returnedOut = new HashMap();
    private List subBiblios;
    private BibliosManager bm = new BibliosManager();
    private SubmissionBibliosDAO sbd = new SubmissionBibliosDAO();
    private Encrypter enc = new Encrypter();

    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        subBiblios = new ArrayList();
        System.out.println("from sub biblios controller ::-- " + request.getParameter("Id_sub"));
         int ID = 0;
         
   subBiblios = bm.submissionBiblios(ID/*Integer.parseInt(enc.decrypt(request.getParameter("Id_sub")))*/);

        String id_biblio =request.getParameter("id_biblio");
        int sub_id_sub = 0;//Integer.parseInt(enc.decrypt(request.getParameter("Id_sub")));
        if(!request.getParameter("Id_sub").isEmpty()){
sub_id_sub = Integer.parseInt(enc.decrypt(request.getParameter("Id_sub")));

}
        String title = request.getParameter("title");
        String author1 = request.getParameter("author1");
        String author2 = request.getParameter("author2");
        String year = request.getParameter("year");
        String journal = request.getParameter("journal");
        String username = request.getParameter("username");
        String volume = request.getParameter("volume");
        String pages = request.getParameter("pages");
        String pubmed_id = request.getParameter("pubmed_id");
        String biblio_number = request.getParameter("biblio_number");//TODO probably ditch this field we have biblio ref
        String last_change = request.getParameter("last_change");
        String notes = request.getParameter("notes");
        String notesadditional = request.getParameter("notesadditional");
        String mgi_original = request.getParameter("mgi_original");

        if (request.getParameter("action").equals("add")) {

            sbd = new SubmissionBibliosDAO();
              
            sbd.setSub_id_sub(sub_id_sub);
            sbd.setTitle(title);
            sbd.setAuthor1(author1);
            sbd.setAuthor2(author2);
            sbd.setYear(year);
            sbd.setJournal(journal);
            sbd.setVolume(volume);
            sbd.setPages(pages);
            sbd.setLast_change(last_change);
            sbd.setNotes(notes);
            sbd.setNotesadditional(notesadditional);
            sbd.setMgi_original(mgi_original);
           // sbd.setBiblio_number(Integer.parseInt(biblio_number));
            sbd.setPubmed_id(pubmed_id);
            sbd.setUsername(username);
            
            bm.save(sbd);
            subBiblios = bm.submissionBiblios(sub_id_sub);

        } else if (request.getParameter("action").equals("edit")) {
            
            
            System.out.println("EDITING, ");
            if (request.getParameter("action2").equals("editRecord")) {
                System.out.println("BIBID=" + request.getParameter("id_biblio"));
            sbd=new SubmissionBibliosDAO();
            sbd = bm.getSubBiblioBySubBiblioID(Integer.parseInt(id_biblio));
            sbd.setTitle(title);
            sbd.setAuthor1(author1);
            sbd.setAuthor2(author2);
            sbd.setYear(year);
            sbd.setJournal(journal);
            sbd.setVolume(volume);
            sbd.setPages(pages);
            sbd.setLast_change(last_change);
            sbd.setNotes(notes);
            sbd.setNotesadditional(notesadditional);
            
            //sbd.setBiblio_number(Integer.parseInt(biblio_number));
            sbd.setPubmed_id(pubmed_id);
                System.out.println("CHECK FOR  CURRENT DAO AND THAT IT IS CORRECT: " + sbd.getTitle() + " == " + sbd.getId_biblio());
           bm.save(sbd);
            }
            subBiblios = bm.submissionBiblios(sub_id_sub);
        }   else if (request.getParameter("action").equals("get")) {
            subBiblios = bm.submissionBiblios(sub_id_sub);
        }else if (request.getParameter("action").equals("delete")) {
            bm.delete(Integer.parseInt(request.getParameter("Id_bib")));
            subBiblios = bm.submissionBiblios(sub_id_sub);
        }

        System.out.println("bibliodaos size==" + subBiblios.size());
        int count = subBiblios.size();
        if(sbd != null){
            sbd.setBiblioCount(subBiblios.size());
        }
        
        returnedOut.put("count", count);
        
        returnedOut.put("subBiblios", subBiblios);
        return new ModelAndView("/publicSubmission/ajaxBiblios", MAP_KEY, returnedOut);
    }
}
