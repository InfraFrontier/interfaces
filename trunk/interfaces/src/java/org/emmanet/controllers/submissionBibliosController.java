/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.emmanet.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.emmanet.model.BibliosManager;
import org.emmanet.model.SubmissionBibliosDAO;
import org.emmanet.util.Encrypter;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

/**
 *
 * @author phil
 */
public class submissionBibliosController implements Controller {

    public static final String MAP_KEY = "returnedOut";
    private Map returnedOut = new HashMap();
    private List subBiblios;
    private BibliosManager bm = new BibliosManager();
    private SubmissionBibliosDAO sbd = new SubmissionBibliosDAO();
    private Encrypter enc = new Encrypter();

    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        subBiblios = bm.submissionBiblios(Integer.parseInt(enc.decrypt(request.getParameter("Id_sub"))));

        int id_biblio =0 ;//Integer.parseInt(request.getParameter("id_biblio"));
        int sub_id_sub = Integer.parseInt(enc.decrypt(request.getParameter("Id_sub")));
        String title = request.getParameter("title");
        String author1 = request.getParameter("author1");
        String author2 = request.getParameter("author2");
        String year = request.getParameter("year");
        String journal = request.getParameter("journal");
        String username = request.getParameter("username");
        String volume = request.getParameter("volume");
        String pages = request.getParameter("pages");
        String pubmed_id = request.getParameter("pubmed_id");
        int biblio_number = 0;//Integer.parseInt(request.getParameter("biblio_number"));
        String last_change = request.getParameter("last_change");
        String notes = request.getParameter("notes");
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
            sbd.setMgi_original(mgi_original);
            sbd.setBiblio_number(biblio_number);
            sbd.setPubmed_id(pubmed_id);
            sbd.setUsername(username);
            
            bm.save(sbd);
            subBiblios = bm.submissionBiblios(sub_id_sub);

        } else if (request.getParameter("action").equals("edit")) {
            sbd=new SubmissionBibliosDAO();
            sbd = bm.getSubBiblioBySubBiblioID(id_biblio);
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
