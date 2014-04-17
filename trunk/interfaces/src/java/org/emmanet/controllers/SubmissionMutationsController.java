/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.emmanet.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.emmanet.model.MutationsManager;
import org.emmanet.model.SubmissionMutationsDAO;
import org.emmanet.util.Encrypter;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

/**
 *
 * @author phil
 */
public class SubmissionMutationsController implements Controller {

    private SubmissionMutationsDAO smd;
    private List mutdaos;
    private MutationsManager mm = new MutationsManager();
    public static final String MAP_KEY = "returnedOut";
    private Map returnedOut = new HashMap();
    Encrypter enc = new Encrypter();

    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        //System.out.println("Encrypted parameter::-");
        //System.out.println("id sub::" + request.getParameter("Id_sub"));
        //System.out.println("decrypted:" + enc.decrypt(request.getParameter("Id_sub")));
mutdaos = new ArrayList();

        int ID = 0;
        /*  String id_sub;
         id_sub = "0";
         if (request.getParameter("Id_sub") != null || !request.getParameter("Id_sub").isEmpty()) {
         System.out.println("id sub not null or empty");
         id_sub = enc.decrypt(request.getParameter("Id_sub"));
            
         }
         if (id_sub.equals("0")) {
         //new DAO
         System.out.println("id sub equals 0 :: " + id_sub);
         ID=0;
         } else {
         ID = Integer.parseInt(id_sub);
         //mutdaos = mm.getSubMutationBySubID(/*Integer.parseInt(enc.decrypt(request.getParameter("Id_sub")))*//*ID);
         }*/


        //new dao if id_sub=0
        String alleleMGI = request.getParameter("mutation_allele_mgi_symbol");
        String chrom = request.getParameter("mutation_chrom");
        String chromName = request.getParameter("mutation_chrom_anomaly_name");
        String chromDesc = request.getParameter("mutation_chrom_anomaly_descr");
        String domPattern = request.getParameter("mutation_dominance_pattern");
        String esCell = request.getParameter("mutation_es_cell_line");
        String lineNumber = request.getParameter("mutation_founder_line_number");

        String geneMGI = request.getParameter("mutation_gene_mgi_symbol");
        String mut = request.getParameter("mutation_mutagen");
        String bg = request.getParameter("mutation_original_backg");
        String bgText = request.getParameter("mutation_original_backg_text");
        String plasmid = request.getParameter("mutation_plasmid");
        String promoter = request.getParameter("mutation_promoter");
        String type = request.getParameter("mutation_type");
        String subtype = "";
        if (type != null) {
            if (type.equals("CH")) {
                subtype = request.getParameter("mutation_subtypeCH");
                //System.out.println(type + " CH type so mut sub is " + request.getParameter("mutation_subtypeCH"));
            } else if (type.equals("IN")) {
                subtype = request.getParameter("mutation_subtypeIN");
                //System.out.println(type + " IN type so mut sub is " + request.getParameter("mutation_subtypeIN"));
            } else if (type.equals("TM")) {
                subtype = request.getParameter("mutation_subtypeTM");
                //System.out.println(type + " TM type so mut sub is " + request.getParameter("mutation_subtypeTM"));
            }
        }
        String transMGI = request.getParameter("mutation_transgene_mgi_symbol");

        String IDFromSession = request.getParameter("IDFromSession");

        if (request.getParameter("action").equals("add")) {
            smd = new SubmissionMutationsDAO();

            //sessencID

            String encryptedID = request.getParameter("Id_sub");
            if (encryptedID.isEmpty()) {
                encryptedID = IDFromSession;//request.getParameter("IDFromSession");
            }
            System.out.println("idfromsession==" + IDFromSession);//request.getParameter("IDFromSession"));
            System.out.println("## id==" + encryptedID);
            // encryptedID = java.net.URLEncoder.encode(encryptedID, "UTF-8");
            encryptedID = java.net.URLDecoder.decode(encryptedID, "UTF-8");
            encryptedID = enc.decrypt(encryptedID);
            int decryptedID = Integer.parseInt(encryptedID);
            System.out.println("DECRYPTED++==" + decryptedID);
            smd.setId_sub(decryptedID);
            /*  if (request.getParameter("Id_sub") != null || !request.getParameter("Id_sub").isEmpty()) {
             smd.setId_sub(Integer.parseInt(enc.decrypt(request.getParameter("Id_sub"))));
             } else {
             smd.setId_sub(Integer.parseInt(enc.decrypt(request.getParameter("IDFromSession"))));
             }*/

            smd.setMutation_allele_mgi_symbol(alleleMGI);
            smd.setMutation_chrom(chrom);
            smd.setMutation_chrom_anomaly_name(chromName);
            smd.setMutation_chrom_anomaly_descr(chromDesc);
            smd.setMutation_dominance_pattern(domPattern);
            smd.setMutation_es_cell_line(esCell);
            smd.setMutation_founder_line_number(lineNumber);

            smd.setMutation_gene_mgi_symbol(geneMGI);
            smd.setMutation_mutagen(mut);
            smd.setMutation_original_backg(bg);
            smd.setMutation_original_backg_text(bgText);
            smd.setMutation_plasmid(plasmid);
            smd.setMutation_promoter(promoter);
            smd.setMutation_subtype(subtype);
                    //System.out.println("MUTATION SUBTYPE == ++ " + subtype);
            smd.setMutation_transgene_mgi_symbol(transMGI);
            smd.setMutation_type(type);
            //mutdaos.add(smd);
            //System.out.println("DEBUG INTEGER ISSUE LINE 72::" + smd.getId_sub());
            //int id_sub
            ID = smd.getId_sub();
            //System.out.println("DEBUG LINE 134::" + type);
            if(!type.isEmpty()){
            mm.save(smd);
            mutdaos = mm.getSubMutationBySubID(ID);
            }
            

        } else if (request.getParameter("action").equals("edit")) {
            //edit records
            //get record from id
            //mutdaos = mm.getSubMutationBySubID(Integer.parseInt(enc.decrypt(request.getParameter("Id_sub"))));
            System.out.print("id_Mut==" + Integer.parseInt(request.getParameter("Id_mut")));



            System.out.println("EDITING, ");
            if (request.getParameter("action2").equals("editRecord")) {
                SubmissionMutationsDAO smd = mm.getSubMutationBySubMutID(Integer.parseInt(request.getParameter("Id_mut")));
                returnedOut.put("SubMutDAO", smd);
                smd.setMutation_allele_mgi_symbol(alleleMGI);
                smd.setMutation_chrom(chrom);
                smd.setMutation_chrom_anomaly_name(chromName);
                smd.setMutation_chrom_anomaly_descr(chromDesc);
                smd.setMutation_dominance_pattern(domPattern);
                smd.setMutation_es_cell_line(esCell);
                smd.setMutation_founder_line_number(lineNumber);

                smd.setMutation_gene_mgi_symbol(geneMGI);
                smd.setMutation_mutagen(mut);
                smd.setMutation_original_backg(bg);
                smd.setMutation_original_backg_text(bgText);
                smd.setMutation_plasmid(plasmid);
                smd.setMutation_promoter(promoter);
                smd.setMutation_subtype(subtype);
                smd.setMutation_transgene_mgi_symbol(transMGI);
                smd.setMutation_type(type);

                System.out.println("CHECK FOR  CURRENT DAO AND THAT IT IS CORRECT: " + smd.getMutation_type() + " == " + smd.getMutation_original_backg());
                //save
                mm.save(smd);
                mutdaos = mm.getSubMutationBySubID(Integer.parseInt(enc.decrypt(request.getParameter("Id_sub"))));
            }
            //load record in fields

        } else if (request.getParameter("action").equals("get")) {
            if (request.getParameter("Id_sub").isEmpty()) {
                //ID = Integer.parseInt(enc.decrypt(request.getParameter("sessencID")));
                ID = Integer.parseInt(enc.decrypt(request.getParameter("IDFromSession")));
            } else {
                ID = Integer.parseInt(enc.decrypt(request.getParameter("Id_sub")));
            }

            System.out.println("ID||| " + ID);
            mutdaos = mm.getSubMutationBySubID(ID/*smd.getId_sub()*/);

        } else if (request.getParameter("action").equals("delete")) {
            System.out.println("DELETING " + request.getParameter("Id_mut"));
            mm.delete(Integer.parseInt(request.getParameter("Id_mut")));
            mutdaos = mm.getSubMutationBySubID(Integer.parseInt(enc.decrypt(request.getParameter("Id_sub"))));
        }
        //return new ModelAndView("ajaxMutations.emma");
        System.out.println("mutdaos size==" + mutdaos.size());
        int count = mutdaos.size();
        if (smd != null) {
            smd.setMutationCount(mutdaos.size());
        }

        returnedOut.put("count", count);
        returnedOut.put("mutdaos", mutdaos);
        return new ModelAndView("/publicSubmission/ajaxMutations", MAP_KEY, returnedOut);
    }
}
