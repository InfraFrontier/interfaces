/*
 * RequestFormController.java
 *
 * Created on 19 December 2007, 09:32
 *
 * Form controller to carry out actions on form to support
 * register interest requests and retrieve previously submitted
 * register an interest requests. Creates supporting request xml/pdf files and 
 * confirmation messages. Submitted data validated by
 * org.emmanet.controllers.RequestFormValidator
 *
 * Dependencies - org.emmanet.jobs.WebRequests 
 * org.emmanet.model.WebRequestsDAO
 *
 * v1.0 released 04-02-2008
 * v1.1 released 14-02-2008 - xml files no longer need to be sent with e-mail message
 * Removal requested by sabine.fessele@helmholtz-muenchen.de
 * Removed by philw@ebi.ac.uk 14-02-2008
 * v1.2 released 25-02-2008 - xml files no longer created or stored on server.
 * No longer needed as caused problems with original EMMA workflow
 * (see emmadb discussions 15-22 February 2008).
 * xmlFileprefix added to determine stage of request in pdf file name
 * Added by philw@ebi.ac.uk
 * v1.3 released 10-04-2008 - Archiving centre mail addresses added to cc map
 * added by philw@ebi.ac.uk requested by sabine.fessele@helmholtz-muenchen.de
 * v1.4 released 11-06-2008 - Archiving centre id added to database entry
 * added by philw@ebi.ac.uk requested by sabine.fessele@helmholtz-muenchen.de
 */
package org.emmanet.controllers;

import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import java.awt.Color;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.velocity.app.VelocityEngine;

import org.emmanet.jobs.WebRequests;
import org.emmanet.model.StrainsDAO;
import org.emmanet.model.WebRequestsDAO;

import org.emmanet.util.Encrypter;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.ui.velocity.VelocityEngineUtils;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Phil Wilkinson
 */
public class RequestFormController extends SimpleFormController {
    
    private JavaMailSender javaMailSender;
    private WebRequests webRequest;
    private StrainsDAO sd;
    public static final String ID = "1";
    private List requestByID;
    private MailSender mailSender;
    private VelocityEngine velocityEngine;
    private String pathToXml;//TODO USE ACCESSOR METHOD TO SET FROM
    private String pathToMTA;
    private File file;
    private String xmlFileName;
    private String xmlFileprefix;
    private String pdfFile;
    private String xmlExt = ".xml";
    private String pdfExt = ".pdf";
    private String mailSubjectText;
    private String internalSuccessView;
    /* pdfTitle determined according to request being register
     * interest or new request 
    */
    private String pdfTitle;
    private boolean pdfConditions;
    private String Bcc;
    private String tetCc;
    private Map Cc;
    private Iterator it;
    private boolean mailSend;
    public static final String MAP_KEY = "returnedOut";
    private Map returnedOut = new HashMap();
    private Encrypter encrypter = new Encrypter();
    
@Override
    protected Object formBackingObject(HttpServletRequest request) {
        /*
         * ADDED TO SOLVE ISSUE WHERE MISSING PARAM CAUSES  formBackingObject() must not be null ERROR
         * THIS ALLOWS FORM TO DISPLAY FOR NEW REQUESTS
         */
        if (request.getParameter("ID") != null) {
            //System.out.println("ID PARAM= " + request.getParameter("ID"));
            String ID = request.getParameter("ID");
           /* try {
                ID = java.net.URLDecoder.decode(ID, "UTF-8");
            } catch (UnsupportedEncodingException ex) {
                Logger.getLogger(RequestFormController.class.getName()).log(Level.SEVERE, null, ex);
            }*/
            String decryptedID = encrypter.decrypt(ID);


            //System.out.println("REQFORMDECRYPTED==" + decryptedID);
            if (decryptedID == null) {
                WebRequestsDAO wrd = new WebRequestsDAO();
                String unencryptedID = request.getParameter("ID");    
                Pattern p = Pattern.compile("((-|\\+)?[0-9]+(\\.[0-9]+)?)+");
                Matcher m = p.matcher(unencryptedID);           
                System.out.println("unenc id == " + unencryptedID);
                request.getSession().setAttribute(
                        "message", null);
                if (request.getParameter("ID") == null) {
                    request.setAttribute(
                            "ERROR", "TRUE");
                    SimpleMailMessage webmasterMsg = new SimpleMailMessage();
                    webmasterMsg.setTo("webmaster@emmanet.org");
                    webmasterMsg.setFrom("emma@emmanet.org");
                    webmasterMsg.setSubject("Encrypted and unencrypted IDs invalid");
                    webmasterMsg.setText("This error has occurred, check server");
                   mailSender.send(webmasterMsg);
                } else if (unencryptedID != null) {
                    WebRequests wr = new WebRequests();
                    if(m.matches()){
                        System.out.println("MATCHES");
                        wrd = (WebRequestsDAO) wr.getReqByID(unencryptedID);
                    }else{
                        //not matching so unencrypted redirect to invalidurl page
                        wrd = new WebRequestsDAO();
                        request.setAttribute(
                            "ERROR", "TRUE");
                         System.out.println("NO MATCHES");
                         SimpleMailMessage webmasterMsg = new SimpleMailMessage();
                    webmasterMsg.setTo("webmaster@emmanet.org");
                    webmasterMsg.setFrom("emma@emmanet.org");
                    webmasterMsg.setSubject("Encrypted and unencrypted IDs invalid");
                    webmasterMsg.setText("This error has occurred, check server for unencryptedID:-" + unencryptedID);
                   mailSender.send(webmasterMsg);
                    }
                    
                }
                //REDIRECT TO NEW PAGE EXPLAINING SECURITY IMPROVEMENTS AND THAT A SECURE ENCRYPTED ID IN AN EMAIL HAS BEEN RESENT
                String preEncodedEncryptedID = encrypter.encrypt(ID);
                try {
                    ID = java.net.URLEncoder.encode(preEncodedEncryptedID, "UTF-8");
                } catch (UnsupportedEncodingException ex) {
                    Logger.getLogger(RequestFormController.class.getName()).log(Level.SEVERE, null, ex);
                }
                wrd.setEncryptedId_req(ID);
                Map model = new HashMap();
                model.put("id", wrd.getEncryptedId_req());
                model.put("strainname", wrd.getStrain_name());
                model.put("emmaid", wrd.getStrain_id());
                model.put("sci_e_mail", wrd.getSci_e_mail());
                
                 String rtoolsID = "";
                 WebRequests wreq = new WebRequests();
        List rtools = wreq.strainRToolID(wrd.getStr_id_str());
        System.out.println("rtools size=" + rtools.size());
        it = rtools.iterator();

        while (it.hasNext()) {
            //  II =  it.next().;
// rtoolsID = o[0].toString();
            Object oo = it.next();
            rtoolsID = oo.toString();
            System.out.println("rtoolsid=" + oo.toString() + "  -  labid=" + wrd.getLab_id_labo());
        }
     model.put("rtoolsID", rtoolsID);
     model.put("labID ", wrd.getLab_id_labo());
                
                returnedOut.put("model", model);

                ///now mail
                SimpleMailMessage msg = new SimpleMailMessage();
                msg.setReplyTo("emma@emmanet.org");
                msg.setFrom("emma@emmanet.org");
                msg.setBcc("philw@ebi.ac.uk");
                msg.setTo(wrd.getSci_e_mail());
                if(wrd.getSci_e_mail() == null) msg.setTo("webmaster@emmanet.org");
                msg.setSubject("Your EMMA Strain Interest Registration Form - "
                        + "strain can now be ordered: " + model.get("emmaid") + " (" + model.get("strainname") + ")");
                String content = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine,
                        "org/emmanet/util/velocitytemplates/regInt-Template.vm", model);

                msg.setText(content);
                try {
                    System.out.println(msg);
                   mailSender.send(msg);

                } catch (MailException ex) {
                    System.err.println(ex.getMessage());
                }

                wrd = new WebRequestsDAO();
                request.setAttribute("ID", null);
                request.setAttribute(
                        "UNENCRYPTEDID", "TRUE");
                return wrd;

        } else {
                return webRequest.getReqByID(decryptedID);
            }
        } else {
            WebRequestsDAO wr = new WebRequestsDAO();

            if (request.getParameter("wr") != null) {
                wr.setRegister_interest("1");
            }
            if (request.getParameter("sname") != null) {
                wr.setStrain_name(request.getParameter("sname").toString());
            }
            if (request.getParameter("cname") != null) {
                wr.setCommon_name_s(request.getParameter("cname").toString());
            }
            if (request.getParameter("id") != null) {
                wr.setStrain_id(request.getParameter("id").toString());
            }
            
            if (request.getParameter("new") != null) {
                //this is a new request show blank form
                wr.setRegister_interest("0");
                //wr.setEurophenome("no");
                //wr.setWtsi_mouse_portal("no");
            }

            //MOUSEBOOK UPDATES

            if (request.getParameter("source") != null) {
                //coming from an external source so check for parameters to pre-fill form

                wr.setSci_title(request.getParameter("sci_title"));
                wr.setSci_firstname(request.getParameter("sci_firstname"));
                wr.setSci_surname(request.getParameter("sci_surname"));
                wr.setSci_e_mail(request.getParameter("sci_e_mail"));
                wr.setSci_phone(request.getParameter("sci_phone"));
                wr.setSci_fax(request.getParameter("sci_fax"));
                wr.setCon_dept(request.getParameter("dept"));
                wr.setCon_institution(request.getParameter("con_institution"));
                wr.setCon_addr_1(request.getParameter("con_addr_1"));

            }

            //END MOUSEBOOK UPDATES

            if (request.getParameter("status") != null) {
                //this is an insert by an archiving centre and now mist have a site id
                String username = (request.getParameter("user"));

            }

            if (request.getParameter("q") != null) {
                //this is an jquery ajax call for autocomplete strain names from insert form
                int query = Integer.parseInt(request.getParameter("q"));
                setSuccessView("ajaxReturn.emma");
                returnedOut.put("ajaxReturn", webRequest.strainList(query));

                return new ModelAndView("ajaxReturn.emma", MAP_KEY, returnedOut);
                //  return "ajaxReturn.emma";
            }



            if (request.getParameter("str_id_str") == null || request.getParameter("str_id_str").equals("0") && request.getParameter("status") != null) {
                if (request.getParameter("status") != null) {
                    // this is an insert and therefore the id or str_id_str isnt yet known and will have to be pulled using ajax
                } else if (!request.getParameter("id").isEmpty()) {
                String toFormat = request.getParameter("id");
                int start = 3;
                int end = toFormat.length();
                toFormat = toFormat.substring(start, end);
                int i = Integer.parseInt(toFormat);
                wr.setStr_id_str(i);
            }
            }
            wr.setCvDAO(webRequest.isoCountries());
//added to rectify incorrect strain names appearing in 
         /*   int sID;
            System.out.println("STRAIN ID = " + wr.getStr_id_str() + " PARAM VAL=" + request.getParameter("str_id_str"));
            if (   request.getParameter("str_id_str") != null || !request.getParameter("str_id_str").equals("0")) {
            sID = Integer.parseInt(request.getParameter("str_id_str"));
            
            } else {
            sID = wr.getStr_id_str();
            }
            sd = (webRequest.getStrainByID( sID));
            wr.setStrain_name(sd.getName());
            /* New requirement to hide TA options from user requesting strains from SANG and FCG who don't have TA access units 
            now need to grab site id using strain id
             */
            WebRequests wReq = new WebRequests();
            List ccCentre = wReq.ccArchiveMailAddresses(wr.getStr_id_str());

            Object[] o = null;
            it = ccCentre.iterator();
            while (it.hasNext()) {
                o = (Object[]) it.next();
                wr.setLab_id_labo(o[0].toString());
                // System.out.println("o[0].toString() == " + o[0].toString());
                //System.out.println("o[0].toString() == " + wr.getLab_id_labo());
            }
            System.out.println("id_lab0" + wr.getLab_id_labo());
            return wr;
        }
    }

 @Override   
    protected ModelAndView onSubmit(
            HttpServletRequest request,
            HttpServletResponse response,
            Object command,
            BindException errors) {
        WebRequests wr = new WebRequests();
        WebRequestsDAO webRequest = (WebRequestsDAO) command;
        
        if(!webRequest.getStrain_name().toLowerCase().contains("wtsi")){
            //webRequest.setWtsi_mouse_portal("no");
           // webRequest.setEurophenome("no");
        }

        String strainID = "" + webRequest.getStr_id_str() + "";
        sd = (wr.getStrainByID(webRequest.getStr_id_str()));
        mailSend = true;
        Cc = new HashMap();

        // Late stage addition for multiple cc addresses TODO MAKE BETTER
        Cc.put("1", new String("info@emmanet.org"));
        Cc.put("2", new String("emma@emmanet.org"));
        Cc.put("3", new String(webRequest.getCon_e_mail()));
        // Get responsible centre mail address(es) and add to map
        List ccCentre = wr.ccArchiveMailAddresses(webRequest.getStr_id_str());
        //map key + 1
        int im = 4;
        Object[] o = null;
        it = ccCentre.iterator();
        while (it.hasNext()) {
            o = (Object[]) it.next();
            Cc.put("" + im + "", o[1].toString());
            webRequest.setLab_id_labo(o[0].toString());
            im++;
        }
        o = null;
        System.out.println("cccentre size=" + ccCentre.size());

        o = (Object[]) ccCentre.get(0);
        String ArchContactEmail = o[1].toString();
   /*     if (webRequest.getApplication_type().equals("ta_or_request")
                || webRequest.getApplication_type().equals("ta_only")) {
            //if one of these values then has passed the country eligibility controlled by javascript in requestFormView.emma/jsp
            webRequest.setEligible_country("yes");
        }*/
        System.out.println("wtsi mouse portal value is:: " + webRequest.getWtsi_mouse_portal());
        wr.saveRequest(webRequest);

        if (request.getParameter("status") != null) {
        } else {
        request.getSession().setAttribute(
                "message",
                getMessageSourceAccessor().getMessage("Message",
                    webRequest.getSci_firstname() + " " + webRequest.getSci_surname() + ", Your request submitted successfully, you will receive "
                    + "confirmation by e-mail sent to the address " + webRequest.getSci_e_mail()));
        }
        String rtoolsID = "";
        List rtools = wr.strainRToolID(webRequest.getStr_id_str());
        System.out.println("rtools size=" + rtools.size());
        it = rtools.iterator();

        while (it.hasNext()) {
            //  II =  it.next().;
// rtoolsID = o[0].toString();
            Object oo = it.next();
            rtoolsID = oo.toString();
            System.out.println("rtoolsid=" + oo.toString() + "  -  labid=" + webRequest.getLab_id_labo());
        }
        // Create hashmap for velocity 
        Map model = new HashMap();
        model.put("name", webRequest.getSci_firstname() + " " + webRequest.getSci_surname());
        model.put("emmaid", webRequest.getStrain_id().toString());
        model.put("strainname", sd.getName());//webRequest.getStrain_name());//change req by Sabine as strain name sometimes changed in strains table but remains static in web_requests
        model.put("timestamp", webRequest.getTimestamp());
        model.put("ftimestamp", webRequest.getFtimestamp());
        model.put("sci_title", webRequest.getSci_title());
        model.put("sci_firstname", webRequest.getSci_firstname());
        model.put("sci_surname", webRequest.getSci_surname());
        model.put("sci_e_mail", webRequest.getSci_e_mail());
        model.put("sci_phone", webRequest.getSci_phone());
        model.put("sci_fax", webRequest.getSci_fax());
        model.put("con_title", webRequest.getCon_title());
        model.put("con_firstname", webRequest.getCon_firstname());
        model.put("con_surname", webRequest.getCon_surname());
        model.put("con_e_mail", webRequest.getCon_e_mail());
        model.put("con_phone", webRequest.getCon_phone());
        model.put("con_fax", webRequest.getCon_fax());
        model.put("con_institution", webRequest.getCon_institution());
        model.put("con_dept", webRequest.getCon_dept());
        model.put("con_addr_1", webRequest.getCon_addr_1());
        model.put("con_addr_2", webRequest.getCon_addr_2());
        model.put("con_province", webRequest.getCon_province());
        model.put("con_town", webRequest.getCon_town());
        model.put("con_postcode", webRequest.getCon_postcode());
        model.put("con_country", webRequest.getCon_country());

        //billing details

        if (!webRequest.getRegister_interest().equals("1")) {
            model.put("bil_vat", webRequest.getBil_vat());
            model.put("PO_ref", webRequest.getPO_ref());
            model.put("bil_title", webRequest.getBil_title());
            model.put("bil_firstname", webRequest.getBil_firstname());
            model.put("bil_surname", webRequest.getBil_surname());
            model.put("bil_e_mail", webRequest.getBil_e_mail());
            model.put("bil_phone", webRequest.getBil_phone());
            model.put("bil_fax", webRequest.getBil_fax());
            model.put("bil_institution", webRequest.getBil_institution());
            model.put("bil_dept", webRequest.getBil_dept());
            model.put("bil_addr_1", webRequest.getBil_addr_1());
            model.put("bil_addr_2", webRequest.getBil_addr_2());
            model.put("bil_province", webRequest.getBil_province());
            model.put("bil_town", webRequest.getBil_town());
            model.put("bil_postcode", webRequest.getBil_postcode());
            model.put("bil_country", webRequest.getBil_country());
        }
        //end biling details

        model.put("strain_id", webRequest.getStrain_id());
        model.put("strain_name", sd.getName());//webRequest.getStrain_name());//change req by Sabine as strain name sometimes changed in strains table but remains static in web_requests
        model.put("common_name_s", webRequest.getCommon_name_s());//TODO need to take from backgrounds.name when all model files are incorporated
        model.put("req_material", webRequest.getReq_material());
        model.put("live_animals", webRequest.getLive_animals());
        model.put("frozen_emb", webRequest.getFrozen_emb());
        model.put("frozen_spe", webRequest.getFrozen_spe());
        // TA application details
        model.put("application_type", webRequest.getApplication_type());
        model.put("ta_eligible", webRequest.getEligible_country());
        model.put("ta_proj_desc", webRequest.getProject_description());
        model.put("ROI", webRequest.getRegister_interest());
        //new e-mail message requirements for eucomm
        model.put("ArchContactEmail", ArchContactEmail);
        model.put("requestID", webRequest.getId_req());
        model.put("labID", webRequest.getLab_id_labo());
        model.put("rtoolsID", rtoolsID);
        if (!webRequest.getLab_id_labo().equals("4")) {
            /*
             * FOR LEGAL REASONS MTA FILE AND USAGE TEXT SHOULD NOT BE SHOWN FOR MRC STOCK.
             * MRC WILL SEND MTA SEPARATELY (M.FRAY EMMA IT MEETING 28-29 OCT 2010)
             */
            model.put("mtaFile", sd.getMta_file());
        }
        //// end new e-mail message requirements for eucomm
        // E-mail content
        //Added to address final request email for reg int strains
        // System.out.println(webRequest.getRegister_interest());

        System.out.println("rtoolsid=" + model.get("rtoolsID") + "  -  labid=" + model.get("labID"));
        String velocTemplate = null;
        
        if (webRequest.getRegister_interest().equals("0")) {
            /* Set values for template to be used, PDF Title & mail message subject text
             * for strain submission
             */
            
            //uncomment here to release new mail templates for sanger awaiting go ahead from jo bottomley
            
           if (rtoolsID.equals("9") && webRequest.getLab_id_labo().equals("1961") ) {
                /*##NEW TEMPLATE FOR SANGER */
                velocTemplate = "org/emmanet/util/velocitytemplates/SangerSpecificSubmissionConfirmation-Template.vm";
            }else if (webRequest.getApplication_type().equals("ta_only")) {
                velocTemplate = "org/emmanet/util/velocitytemplates/taonlyRequest-Template.vm";
            } else if (webRequest.getApplication_type().equals("ta_or_request")) {
                velocTemplate = "org/emmanet/util/velocitytemplates/taorRequest-Template.vm";
            }else {
            velocTemplate = "org/emmanet/util/velocitytemplates/submissionConfirmation-Template.vm";
            }
            pdfTitle = "EMMA Mutant Request Form";
            //TODO SET MAIL SUBJECT LINE HERE
            xmlFileprefix = "req_";
            mailSubjectText = "Your Request from EMMA - confirmation of receipt: ";
            pdfConditions = true;
            //cc for TET systems strain

            List rtool = wr.strainRToolID(webRequest.getStr_id_str());
            for (Iterator it = rtool.listIterator(); it.hasNext();) {
                Integer I = (Integer) it.next();
                if (I == 5) {
                    //TET strain
                    Cc.put(Cc.size() + 1, tetCc);
                }
            }

        } else if (webRequest.getRegister_interest().equals("1")) {
            /* Set values for template to be used, PDF Title & mail message subject text
             * for registration of interest
             */

            // //uncomment here to release new mail templates for sanger awaiting go ahead from jo bottomley
           if (rtoolsID.equals("9") && webRequest.getLab_id_labo().equals("1961") ) {
                  /*##NEW TEMPLATE FOR SANGER */
               velocTemplate = "org/emmanet/util/velocitytemplates/SangerSpecificInterestSubmission-Template.vm";
            }else{
            velocTemplate = "org/emmanet/util/velocitytemplates/interestSubmission-Template.vm";
           }
            
            
             pdfTitle = "EMMA Strain Interest Registration Form";
            xmlFileprefix = "roi_";
             mailSubjectText = "Your EMMA Strain Interest Registration Form - confirmation of receipt: "; 
            pdfConditions = false;
        }

        System.out.println("Templ8 to be used is : " + velocTemplate);//"org/emmanet/util/velocitytemplates/interestSubmission-Template.vm"
        String content = VelocityEngineUtils.mergeTemplateIntoString(getVelocityEngine(),
               velocTemplate, model);
        // XML file content Template created by Velocity
        String xmlFileContent = VelocityEngineUtils.mergeTemplateIntoString(getVelocityEngine(),
                "org/emmanet/util/velocitytemplates/requestXml-Template.vm", model);
        //System.out.print(xmlFileContent);
        // Format strain id to correct 0 padding TODO MAY NEED TO GO NOT NECESSARY HERE AS DONE ABOVE
        NumberFormat formatter = new DecimalFormat("00000");
        String formattedID = formatter.format(webRequest.getStr_id_str());
        // try { REMOVED AS XML NO LONGER REQUIRED
            /* Create request XML file from velocity template
             * named to format req_TIMESTAMP_FIRSTNAME_SURNAME_STRAINID.xml
             */
        // NB USED AS PDF FILENAME AS WELL
        xmlFileName = xmlFileprefix + webRequest.getTimestamp() + "_" + webRequest.getSci_firstname() + "_" + webRequest.getSci_surname() + "_" + formattedID;
        // file = new File(pathToXml + xmlFileName + xmlExt); REMOVED AS XML NO LONGER REQUIRED
            // Create pdf from model
            pdfFile =
                createPDF(model, pathToXml + xmlFileName + pdfExt);    // Create file if it does not exist
        // REMOVED AS XML NO LONGER REQUIRED
            /*boolean success = file.createNewFile(); REMOVED AS XML NO LONGER REQUIRED
        if (success) { REMOVED AS XML NO LONGER REQUIRED
        // File did not exist and was created REMOVED AS XML NO LONGER REQUIRED
        Writer out = new BufferedWriter(new OutputStreamWriter( REMOVED AS XML NO LONGER REQUIRED
        new FileOutputStream(file), "UTF8")); REMOVED AS XML NO LONGER REQUIRED
        out.write(xmlFileContent); REMOVED AS XML NO LONGER REQUIRED
        out.close(); REMOVED AS XML NO LONGER REQUIRED
            
        } else {REMOVED AS XML NO LONGER REQUIRED
        // File already exists REMOVED AS XML NO LONGER REQUIRED
        // Do nothing REMOVED AS XML NO LONGER REQUIRED
        } REMOVED AS XML NO LONGER REQUIRED
        } catch (IOException e) { REMOVED AS XML NO LONGER REQUIRED
        //TODO HANDLE REMOVED AS XML NO LONGER REQUIRED
        }  REMOVED AS XML NO LONGER REQUIRED */
        MimeMessage message = getJavaMailSender().createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setReplyTo("emma@emmanet.org");
            helper.setFrom("emma@emmanet.org");
            helper.setBcc(Bcc);
            System.out.println("bccADDRESS===== " + Bcc);
            Iterator i = Cc.values().iterator();
            String[] ccAddresses = new String[Cc.size()];
            int iK = 0;
            while (i.hasNext()) {
                String ccAddress = (String) i.next();
                ccAddress = ccAddress.trim();
                ccAddresses[iK] = ccAddress;
                iK++;
                        System.out.println("ccADDRESS===== " + ccAddress);
                //helper.addCc(ccAddress);
                    }
            helper.setCc(ccAddresses);//.addCc(ccAddresses);
            
            helper.setTo(webRequest.getSci_e_mail().trim());
            
            helper.setSubject(mailSubjectText + webRequest.getStrain_id() + " (" + sd.getName() /*webRequest.getStrain_name()*/ + ") Your request ID: " + webRequest.getId_req());
            helper.setText(content);
            /* xml files no longer need to be sent with e-mail message
             * Removal requested by sabine.fessele@helmholtz-muenchen.de
             * Removed by philw@ebi.ac.uk 14-02-2008
             */
            //helper.addAttachment(xmlFileName + xmlExt,file);
            FileSystemResource file = new FileSystemResource(new File(pdfFile));
            
            helper.addAttachment(xmlFileName + pdfExt, file);
            /*
             * FOR LEGAL REASONS MTA FILE AND USAGE TEXT SHOULD NOT BE SHOWN FOR MRC STOCK.
             * MRC WILL SEND MTA SEPARATELY (M.FRAY EMMA IT MEETING 28-29 OCT 2010)
             */
            
            System.out.println("app type is " + model.get("application_type"));
            System.out.println("model mta is " + model.get("mtaFile"));
            if (!model.get("application_type").equals("ta_only")) {
                //add mta file if associated with strain id
                String mtaFile = sd.getMta_file();
                // mtaFile="this is a test.pdf";
                System.out.println("mta file value is::" + mtaFile);
                //  System.out.println( " OK now mta file from model value is " + model.get("mtaFile").toString());
                // || model.get("mtaFile").toString().equals("")
                if (mtaFile != null || model.get("mtaFile") != null) {
                    FileSystemResource fileMTA = new FileSystemResource(new File(getPathToMTA() + mtaFile));
                    //System.out.println("file mta is-> " + fileMTA + " and to string is " + fileMTA.toString());
                    //need to check for a valid mta filename use period extension separator, all mtas are either .doc or .pdf
                    if (!fileMTA.exists()) {
                        System.out.println("MTA file " + mtaFile + " cannot be accessed");
                    }
                    if (fileMTA.exists() && fileMTA.toString().contains(".")) {
                        // System.out.println("NOW FILEmta EXISTS SO LETS ADD THE ATTACHMENT" + model.get("mtaFile").toString());//
                        if (!webRequest.getLab_id_labo().equals("4")) {
                            helper.addAttachment(model.get("mtaFile").toString(), fileMTA);
                        }
                    }
                }
            }

            if (request.getParameter("triggerMails") != null) {
                if (request.getParameter("triggerMails").equals("no")) {
                    //user has specified no mails to be sent from insert interface
                    System.out.println("NOT OK to send mail, the value submitted was : " + request.getParameter("triggerMails"));
                    mailSend = false;
                } else if (request.getParameter("triggerMails").equals("managersonly")) {
                    //remove to address and set to cc addresses
                    System.out.println("OK to send mail to managers only, the value submitted was : " + request.getParameter("triggerMails"));
                    helper.setTo(ccAddresses);
                    ccAddresses = null;
                }
            }
            System.out.println("mailSend value is : " + mailSend);
            if (mailSend) {
                System.out.println(content);
                System.out.println("OK to send mail, the value submitted was : " + mailSend);
            getJavaMailSender().send(message);
                System.out.println("Mail sent ");
            }
            Cc = null;
            
        } catch (MessagingException ex) {
            ex.printStackTrace();
        }
        

        if (request.getParameter(
                "status") != null) {
            if (request.getParameter("status").equals("insert")) {
                return new ModelAndView(getInternalSuccessView());
            }
        }
        return new ModelAndView(getSuccessView());
    }
    
    public String createPDF(
            Map model, String filePath) {
                /* I really don't like this iText library.
                 * Takes so much effort to figure it all out
                 * to get an acceptable pdf rendition. */
        
        Document doc = new Document();
        try {
            System.out.println(model.get("timestamp"));
            
            PdfWriter writer = PdfWriter.getInstance(doc, new FileOutputStream(
                    filePath));
            doc.open();
            Paragraph pHead = new Paragraph(
                    pdfTitle + "\n\n", FontFactory.getFont(
                    FontFactory.HELVETICA, 11));
            pHead.setAlignment(Element.ALIGN_CENTER);
            doc.add(pHead);
            doc.add(new Paragraph(pdfTitle + "\nRequest ID:" + model.get("requestID") + "\n\n",
                    FontFactory.getFont(FontFactory.HELVETICA_BOLD, 20)));
            Paragraph pSubHead = new Paragraph(
                    "Following data have been submitted to EMMA on " + model.get("ftimestamp"), FontFactory.getFont(
                    FontFactory.HELVETICA, 11));
            pSubHead.setAlignment(Element.ALIGN_CENTER);
            
            doc.add(pSubHead);
            doc.add(Chunk.NEWLINE);
            // Space padding underline
            Chunk underlined = new Chunk(
                    "                                                                                     " + "                                                                       ");
            underlined.setUnderline(new Color(0x00, 0x00, 0x00), 0.0f, 0.2f,
                    16.0f, 0.0f, PdfContentByte.LINE_CAP_BUTT);//Black line
            doc.add(underlined);
            Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11);
            // Set table cell widths equiv. to 25% and 75%
            float[] widths = {0.25f, 0.75f};
            PdfPTable table = new PdfPTable(widths);
            
            table.setWidthPercentage(100);
            
            PdfPCell cell = new PdfPCell(new Paragraph("Scientist\n\n", font));
            cell.setColspan(2);
            cell.setBorder(0);
            
            table.addCell(cell);
            table.addCell("Title");
            table.addCell("" + model.get("sci_title"));
            table.addCell("Firstname");
            table.addCell("" + model.get("sci_firstname"));
            table.addCell("Surname");
            table.addCell("" + model.get("sci_surname"));
            table.addCell("E-mail");
            table.addCell("" + model.get("sci_e_mail"));
            table.addCell("Phone");
            table.addCell("" + model.get("sci_phone"));
            table.addCell("Fax");
            table.addCell("" + model.get("sci_fax"));
            PdfPCell cellShip = new PdfPCell(new Paragraph(
                    "\nShipping Contact\n\n", font));
            cellShip.setColspan(2);
            cellShip.setBorder(0);
            table.addCell(cellShip);
            
            table.addCell("Title");
            table.addCell("" + model.get("con_title"));
            table.addCell("Firstname");
            table.addCell("" + model.get("con_firstname"));
            table.addCell("Surname");
            table.addCell("" + model.get("con_surname"));
            table.addCell("E-mail");
            table.addCell("" + model.get("con_e_mail"));
            table.addCell("Phone");
            table.addCell("" + model.get("con_phone"));
            table.addCell("Fax");
            table.addCell("" + model.get("con_fax"));
            table.addCell("Institution");
            table.addCell("" + model.get("con_institution"));
            table.addCell("Department");
            table.addCell("" + model.get("con_dept"));
            table.addCell("Address Line 1");
            table.addCell("" + model.get("con_addr_1"));
            table.addCell("Address Line 2");
            table.addCell("" + model.get("con_addr_2"));
            table.addCell("County/province");
            table.addCell("" + model.get("con_province"));
            table.addCell("Town");
            table.addCell("" + model.get("con_town"));
            table.addCell("Postcode");
            table.addCell("" + model.get("con_postcode"));
            table.addCell("Country");
            table.addCell("" + model.get("con_country"));
            
            if (!model.get("ROI").equals("1")) {
                //webrequest is not a roi so these were set above so send them to pdf
                PdfPCell cellBill = new PdfPCell(new Paragraph(
                        "\nBilling Details\n\n", font));
                cellBill.setColspan(2);
                cellBill.setBorder(0);
                table.addCell(cellBill);

                table.addCell("VAT reference");
                table.addCell("" + model.get("bil_vat"));
                table.addCell("Purchase Order Number");
                table.addCell("" + model.get("PO_ref"));
                table.addCell("Title");
                table.addCell("" + model.get("bil_title"));
                table.addCell("Firstname");
                table.addCell("" + model.get("bil_firstname"));
                table.addCell("Surname");
                table.addCell("" + model.get("bil_surname"));
                table.addCell("E-mail");
                table.addCell("" + model.get("bil_e_mail"));
                table.addCell("Phone");
                table.addCell("" + model.get("bil_phone"));
                table.addCell("Fax");
                table.addCell("" + model.get("bil_fax"));
                table.addCell("Institution");
                table.addCell("" + model.get("bil_institution"));
                table.addCell("Department");
                table.addCell("" + model.get("bil_dept"));
                table.addCell("Address Line 1");
                table.addCell("" + model.get("bil_addr_1"));
                table.addCell("Address Line 2");
                table.addCell("" + model.get("bil_addr_2"));
                table.addCell("County/province");
                table.addCell("" + model.get("bil_province"));
                table.addCell("Town");
                table.addCell("" + model.get("bil_town"));
                table.addCell("Postcode");
                table.addCell("" + model.get("bil_postcode"));
                table.addCell("Country");
                table.addCell("" + model.get("bil_country"));
            }

            PdfPCell cellStrain = new PdfPCell(new Paragraph(
                    "\nStrain Details\n\n", font));
            cellStrain.setColspan(2);
            cellStrain.setBorder(0);
            
            table.addCell(cellStrain);
            table.addCell("Strain ID");
            table.addCell("" + model.get("strain_id"));
            table.addCell("Strain name");
            table.addCell("" + model.get("strain_name"));
            table.addCell("Common Name(s)");
            table.addCell("" + model.get("common_name_s"));
            
            if (model.get("req_material") != null) {
                PdfPCell cellMaterial = new PdfPCell(new Paragraph(
                        "\nRequested Material\n\n", font));
                cellMaterial.setColspan(2);
                cellMaterial.setBorder(0);
                
                table.addCell(cellMaterial);
                table.addCell("Material");
                table.addCell("" + model.get("req_material"));
            }
            
            if (model.get("live_animals") != null) {
                table.addCell("Live Animals");
                table.addCell("Selected");
            }

            if (model.get("frozen_emb") != null) {
                table.addCell("Frozen Embryos");
                table.addCell("Selected");
            }

            if (model.get("frozen_spe") != null) {
                table.addCell("Frozen Sperm");
                table.addCell("Selected");
            }
            
            if (pdfConditions) {
                String text = "";
                String text1 = "";
                String text2 = "";
                String header = "";
                String header1 = "";
                String header2 = "";
                if (model.get("application_type").equals("request_only")) {
                    text =
                            new StringBuilder().append(text).append(
                            "\nYou have indicated that you have read the conditions and agree to pay the transmittal fee " + "plus shipping costs.").toString();
            
                    header =
                            new StringBuilder().append(header).append("\nStandard request\n").toString();

                } else if (!model.get("application_type").equals("request_only")) {
                    header1 =
                            new StringBuilder().append(header1).append("\nApplication for Transnational Access Activity").toString();
                    if (model.get("application_type").equals("ta_only")) {
                        text1 =
                                new StringBuilder().append(text1).append("\nYou have indicated that you have read the conditions and have applied for free of charge TA only. "
                                + "In the case of the TA application being rejected the request process will be terminated.").toString();
                        header1 =
                                new StringBuilder().append(header1).append(" (TA Option B)\n").toString();
                    } else {
                        text1 =
                                new StringBuilder().append(text1).append("\nYou have indicated that you have read the conditions and have applied for free of charge TA "
                                + "and have agreed to pay the service charge plus shipping cost if the TA application is rejected.").toString();
                        header1 =
                                new StringBuilder().append(header1).append(" (TA Option A)\n").toString();
                    }

                    header2 =
                            new StringBuilder().append(header2).append("\n\nDescription of project (1/2 page) involving requested EMMA mouse mutant resource. "
                            + "The project description will be used by the Evaluation Committee for selection of applicants:").toString();
                }

                if (!model.get("application_type").equals("request_only")) {
                    //  table.addCell("" + model.get("ta_proj_desc"));
                    text2 =
                            new StringBuilder().append(text2).append("\n\n "
                            + model.get("ta_proj_desc")).toString();
                }

                if (!model.get("application_type").equals("request_only")) {
                    //we have a ta header and text to add
                    PdfPCell cellConditions1 = new PdfPCell(new Paragraph(header1, font));

                    cellConditions1.setColspan(2);
                    cellConditions1.setBorder(0);
                    table.addCell(cellConditions1);

                    PdfPCell cellConditions2 = new PdfPCell(new Paragraph(text1));

                    cellConditions2.setColspan(2);
                    cellConditions2.setBorder(0);
                    table.addCell(cellConditions2);

                    PdfPCell cellConditions3 = new PdfPCell(new Paragraph(header2, font));

                    cellConditions3.setColspan(2);
                    cellConditions3.setBorder(0);
                    table.addCell(cellConditions3);

                    PdfPCell cellConditions4 = new PdfPCell(new Paragraph(text2));

                    cellConditions4.setColspan(2);
                    cellConditions4.setBorder(0);
                    table.addCell(cellConditions4);

                } else {

                    PdfPCell cellConditions = new PdfPCell(new Paragraph(header, font));
            cellConditions.setColspan(2);
            cellConditions.setBorder(0);
            table.addCell(cellConditions);

                    PdfPCell cellConditionsTxt = new PdfPCell(new Paragraph(text));
                    cellConditionsTxt.setColspan(2);
                    cellConditionsTxt.setBorder(0);
                    table.addCell(cellConditionsTxt);
            }

            }
            doc.add(table);
            
        } catch (DocumentException de) {
            System.err.println(de.getMessage());
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }

        doc.close();
        return filePath;
    }
    
    public WebRequests getWebRequest() {
        return webRequest;
    }
    
    public void setWebRequest(WebRequests webRequest) {
        this.webRequest = webRequest;
    }
    
    public MailSender getMailSender() {
        return mailSender;
    }
    
    public void setMailSender(MailSender mailSender) {
        this.mailSender = mailSender;
    }
    
    public VelocityEngine getVelocityEngine() {
        return velocityEngine;
    }
    
    public void setVelocityEngine(VelocityEngine velocityEngine) {
        this.velocityEngine = velocityEngine;
    }
    
    public String getPathToXml() {
        return pathToXml;
    }
    
    public void setPathToXml(String pathToXml) {
        this.pathToXml = pathToXml;
    }
    
    public JavaMailSender getJavaMailSender() {
        return javaMailSender;
    }
    
    public void setJavaMailSender(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public String getBcc() {
        return Bcc;
    }

    public void setBcc(String Bcc) {
        this.Bcc = Bcc;
    }

    public Map getCc() {
        return Cc;
    }

    public void setCc(Map Cc) {
        this.Cc = Cc;
    }

    public String getInternalSuccessView() {
        return internalSuccessView;
}

    public void setInternalSuccessView(String internalSuccessView) {
        this.internalSuccessView = internalSuccessView;
    }

    public String getTetCc() {
        return tetCc;
    }

    public void setTetCc(String tetCc) {
        this.tetCc = tetCc;
    }

    public String getPathToMTA() {
        return pathToMTA;
    }

    public void setPathToMTA(String pathToMTA) {
        this.pathToMTA = pathToMTA;
    }
}
