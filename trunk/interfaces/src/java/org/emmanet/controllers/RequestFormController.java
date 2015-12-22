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
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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
import javax.servlet.http.HttpSession;
import org.emmanet.model.LaboratoriesManager;
import org.emmanet.model.NkiEsCellsDAO;
import org.emmanet.model.StrainsManager;
import org.emmanet.util.Configuration;

/**
 *
 * @author Phil Wilkinson
 */
public class RequestFormController extends SimpleFormController {

    private JavaMailSender javaMailSender;
    private WebRequests webRequest;
    private StrainsDAO sd;
    private NkiEsCellsDAO esd;
    public static final String ID = "1";
    private List requestByID;
    private MailSender mailSender;
    private VelocityEngine velocityEngine;
    private String pathToXml;//TODO USE ACCESSOR METHOD TO SET FROM
    private String pathToMTA;
    private File file;
    private File mtaFromURL;
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
    private String[] nkiescellCc;
    private String[] xmlMailTo;
    private Map Cc;
    private Iterator it;
    private boolean mailSend;
    public static final String MAP_KEY = "returnedOut";
    final static String FILELOCATION = Configuration.get("TMPFILES");
    private static String BASEURL;
    private Map returnedOut = new HashMap();
    private Encrypter encrypter = new Encrypter();
    private HttpSession session;
    private List ccCentre;
    private String frozenCost;
    private String liveCost;
    private String frozenDelivery;
    private String liveDelivery;
    private String liveShelfDelivery;
    private String liveShelfCost;
    private LaboratoriesManager lm = new LaboratoriesManager();
    private String impcDataExists;

    @Override
    protected Object formBackingObject(HttpServletRequest request) {
        /*
         * ADDED TO SOLVE ISSUE WHERE MISSING PARAM CAUSES  formBackingObject() must not be null ERROR
         * THIS ALLOWS FORM TO DISPLAY FOR NEW REQUESTS
         */
        StrainsManager sm = new StrainsManager();
        sd = new StrainsDAO();

        session = request.getSession(true);
        session.setAttribute("BASEURL", getBASEURL());

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
                    webmasterMsg.setTo("philw@ebi.ac.uk");
                    webmasterMsg.setFrom("emma@infrafrontier.eu");
                    webmasterMsg.setSubject("Encrypted and unencrypted IDs invalid");
                    webmasterMsg.setText("This error has occurred, check server");
                    mailSender.send(webmasterMsg);
                } else if (unencryptedID != null) {
                    WebRequests wr = new WebRequests();
                    if (m.matches()) {
                        System.out.println("MATCHES");
                        wrd = (WebRequestsDAO) wr.getReqByID(unencryptedID);
                    } else {
                        //not matching so unencrypted redirect to invalidurl page
                        wrd = new WebRequestsDAO();
                        request.setAttribute(
                                "ERROR", "TRUE");
                        System.out.println("NO MATCHES");
                        SimpleMailMessage webmasterMsg = new SimpleMailMessage();
                        webmasterMsg.setTo("philw@ebi.ac.uk");
                        webmasterMsg.setFrom("emma@infrafrontier.eu");
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
                model.put("BASEURL", BASEURL);
                model.put("id", wrd.getEncryptedId_req());
                model.put("strainname", wrd.getStrain_name());
                model.put("emmaid", wrd.getStrain_id());
                model.put("sci_e_mail", wrd.getSci_e_mail());

                String rtoolsID = "";
                WebRequests wreq = new WebRequests();
                List rtools = wreq.strainRToolID(wrd.getStr_id_str());
                sd = sm.getStrainByID(wrd.getStr_id_str());
                System.out.println("STRAINSDAO VALUE IS " + sd.getEmma_id());
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
                msg.setReplyTo("emma@infrafrontier.eu");
                msg.setFrom("emma@infrafrontier.eu");
                msg.setBcc("philw@ebi.ac.uk");
                msg.setTo(wrd.getSci_e_mail());
                if (wrd.getSci_e_mail() == null) {
                    msg.setTo("webmaster@infrafrontier.eu");
                }
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
                WebRequestsDAO wr = new WebRequestsDAO();
                wr = webRequest.getReqByID(decryptedID);
                wr.setAvailabilities(webRequest.availabilitiesList(wr.getStr_id_str()));//to satisfy JIRA-219
                wr.setDistributionCentre(lm.getLabByStrainID(wr.getLab_id_labo()));
                wr.setLiveCost(liveCost);
                wr.setFrozenCost(frozenCost);
                wr.setFrozenDelivery(frozenDelivery);
                wr.setLiveDelivery(liveDelivery);
                wr.setLiveShelfCost(liveShelfCost);
                wr.setLiveShelfDelivery(liveShelfDelivery);
                // return webRequest.getReqByID(decryptedID); //RETURNING WEBREQUEST WITH NO PARAMS SET
                return wr;
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
                setSuccessView("../ajaxReturn.emma");
                returnedOut.put("ajaxReturn", webRequest.strainList(query));

                return new ModelAndView("../ajaxReturn.emma", MAP_KEY, returnedOut);
                //  return "ajaxReturn.emma";
            }

            if (request.getParameter("str_id_str") == null || request.getParameter("str_id_str").equals("0") && request.getParameter("status") != null) {
                if (request.getParameter("status") != null) {
                    // this is an insert and therefore the id or str_id_str isnt yet known and will have to be pulled using ajax
                } else if (!request.getParameter("id").isEmpty()) {
                    //  if (!request.getParameter("id").isEmpty()) {
                    String toFormat = request.getParameter("id");
                    int start = 3;
                    int end = toFormat.length();
                    toFormat = toFormat.substring(start, end);
                    int i = Integer.parseInt(toFormat);
                    wr.setStr_id_str(i);
                }
            }

            if (wr.getStr_id_str() != 0) {
                wr.setAvailabilities(webRequest.availabilitiesList(wr.getStr_id_str()));//to satisfy JIRA-219
            } else if (request.getParameter("str_id_str") != null) {
                wr.setAvailabilities(webRequest.availabilitiesList(Integer.parseInt(request.getParameter("str_id_str"))));
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
            if (request.getParameter("type") != null && request.getParameter("type").equals("nkiescells")) {
                ccCentre = wReq.ccArchiveMailAddresses(wr.getStrain_id(), "nki_es_cells");
                wr.setReq_material("ES Cells");
            } else {
                ccCentre = wReq.ccArchiveMailAddresses("" + wr.getStr_id_str(), "strains");
            }
            // List ccCentre = wReq.ccArchiveMailAddresses(wr.getStr_id_str());

            Object[] o = null;
            it = ccCentre.iterator();
            while (it.hasNext()) {
                o = (Object[]) it.next();
                wr.setLab_id_labo(o[0].toString());
                // System.out.println("o[0].toString() == " + o[0].toString());
                //System.out.println("o[0].toString() == " + wr.getLab_id_labo());
            }
            sd = sm.getStrainByID(wr.getStr_id_str());
            System.out.println("405 STRAINSDAO VALUE IS " + wr.getStr_id_str());

            if (sd != null) {
                System.out.println("405 STRAINSDAO VALUE IS " + sd.getLs_consortium());
                System.out.println("405 STRAINSDAO VALUE IS " + sd.getName());
                System.out.println("sd values two is " + sd.getLs_consortium() + " + " + sd.getId_str());
            } else {
                int iPhenoExists;
                iPhenoExists = wr.getStr_id_str();
                if (iPhenoExists == 0 && request.getParameter("str_id_str") != null) {
                    iPhenoExists = Integer.parseInt(request.getParameter("str_id_str"));
                }
                System.out.println("pheno exists  IS " + wr.getStr_id_str());
                sd = sm.getStrainByID(iPhenoExists);
                // System.out.println("412 STRAINSDAO VALUE IS " + sd.getEmma_id());
            }

            //sd=sm.getStrainByID(wr.getStr_id_str());
            //System.out.println("STRAINSDAO value for name is" + sd.getName ());
            //sd.getArchiveDAO().getLab_id_labo();
            // System.out.println("lab id  is" + wr.getLab_id_labo() + " OR its taken from request " + request.getParameter("lab_id_labo"));
            if (wr.getLab_id_labo() != null) {
                wr.setDistributionCentre(lm.getLabByStrainID(wr.getLab_id_labo()));
            } else if (request.getParameter("lab_id_labo") != null) {
                wr.setDistributionCentre(lm.getLabByStrainID(request.getParameter("lab_id_labo")));
            }

            wr.setLiveCost(liveCost);
            wr.setFrozenCost(frozenCost);
            wr.setFrozenDelivery(frozenDelivery);
            wr.setLiveDelivery(liveDelivery);
            wr.setLiveShelfCost(liveShelfCost);
            wr.setLiveShelfDelivery(liveShelfDelivery);

            System.out.println("Mouse portal val is " + wr.getWtsi_mouse_portal());
            System.out.println("europhenome  val is " + wr.getEurophenome());
            wr.setWtsi_mouse_portal(null);
            wr.setEurophenome(null);
            impcDataExists = "";
            if (sd != null) {
                impcDataExists = sd.getLs_consortium();//.getImpc_phenotype_data_exists();
                if (impcDataExists != null && impcDataExists.contains("IMPC")) {
                    impcDataExists = "yes";
                }
            }

            wr.setImpc_phenotype_data_exists(impcDataExists);
            System.out.println("impc data is...." + impcDataExists);
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
        StrainsDAO sdSubmit = new StrainsDAO();
        WebRequestsDAO webRequest = (WebRequestsDAO) command;
        session.setAttribute("req_material", null);

        mailSubjectText = "";
        sdSubmit = null;
        esd = null;
        String type = request.getParameter("type");
        System.out.println("TYPE is set to " + type);

        if (!webRequest.getStrain_name().toLowerCase().contains("wtsi")) {
            //webRequest.setWtsi_mouse_portal("no");
            // webRequest.setEurophenome("no");
        }

        String strainID = "" + webRequest.getStr_id_str() + "";

        if (type != null && type.equals("nkiescells")) {
            esd = (NkiEsCellsDAO) (wr.getESCellsByID("" + webRequest.getStrain_id()));
        } else {
            sdSubmit = (wr.getStrainByID(webRequest.getStr_id_str()));
        }
        mailSend = true;
        Cc = new HashMap();
        int im = 0;
        // Late stage addition for multiple cc addresses TODO MAKE BETTER
        Cc.put("1", new String("emma@infrafrontier.eu"));
        //Cc.put("2", new String(""));

        if (request.getParameter("triggerMails") != null && request.getParameter("triggerMails").equals("managersonly")) {
            //map key + 1
            im = 2;
        } else {
            Cc.put("2", new String(webRequest.getCon_e_mail().trim()));
            //map key + 1
            im = 3;
        }

        // Get responsible centre mail address(es) and add to map
        List ccCentre = new ArrayList();
        if (request.getParameter("type") != null && request.getParameter("type").equals("nkiescells")) {
            ccCentre = wr.ccArchiveMailAddresses(webRequest.getStrain_id(), "nki_es_cells");
        } else {
            ccCentre = wr.ccArchiveMailAddresses("" + webRequest.getStr_id_str(), "strains");
        }

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
        String ArchContactEmail = "";
        if (ccCentre.size() > 0) {
            o = (Object[]) ccCentre.get(0);
            ArchContactEmail = o[1].toString();
        }

        /*     if (webRequest.getApplication_type().equals("ta_or_request")
         || webRequest.getApplication_type().equals("ta_only")) {
         //if one of these values then has passed the country eligibility controlled by javascript in requestFormView.emma/jsp
         webRequest.setEligible_country("yes");
         }*/
        System.out.println("wtsi mouse portal value is:: " + webRequest.getWtsi_mouse_portal());
        //  webRequest.setBil_addr_1("testaddress1");
        wr.saveRequest(webRequest);

        if (request.getParameter("status") != null) {
        } else {
            request.getSession().setAttribute(
                    "message",
                    getMessageSourceAccessor().getMessage("Message",
                    webRequest.getSci_firstname() + " " + webRequest.getSci_surname() + ", Your request submitted successfully, you will receive "
                    + "confirmation by e-mail sent to the address " + webRequest.getSci_e_mail().trim()));
        }
        String rtoolsID = "";
        List rtools = wr.strainRToolID(webRequest.getStr_id_str());
        System.out.println("rtools size=" + rtools.size());
        it = rtools.iterator();

        while (it.hasNext()) {
            Object oo = it.next();
            rtoolsID = oo.toString();
            System.out.println("rtoolsid=" + oo.toString() + "  -  labid=" + webRequest.getLab_id_labo());
        }
        // Create hashmap for velocity 
        Map model = new HashMap();
        model.put("name", webRequest.getSci_firstname() + " " + webRequest.getSci_surname());
        model.put("emmaid", webRequest.getStrain_id().toString());

        model.put("timestamp", webRequest.getTimestamp());
        model.put("ftimestamp", webRequest.getFtimestamp());
        model.put("sci_title", webRequest.getSci_title());
        model.put("sci_firstname", webRequest.getSci_firstname());
        model.put("sci_surname", webRequest.getSci_surname());
        model.put("sci_e_mail", webRequest.getSci_e_mail().trim());
        model.put("sci_phone", webRequest.getSci_phone());
        model.put("sci_fax", webRequest.getSci_fax());
        model.put("con_title", webRequest.getCon_title());
        model.put("con_firstname", webRequest.getCon_firstname());
        model.put("con_surname", webRequest.getCon_surname());
        model.put("con_e_mail", webRequest.getCon_e_mail().trim());
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
            model.put("bil_e_mail", webRequest.getBil_e_mail().trim());
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

        if (sdSubmit != null && type == null) {

            System.out.println("OK THIS IS FROM STRAINS TABLE");

            model.put("strain_name", sdSubmit.getName());//webRequest.getStrain_name());//change req by Sabine as strain name sometimes changed in strains table but remains static in web_requests
            model.put("strainname", sdSubmit.getName());//webRequest.getStrain_name());//change req by Sabine as strain name sometimes changed in strains table but remains static in web_requests
            model.put("consortium", sdSubmit.getLs_consortium());//added to satisfy 
        } else if (esd != null && type.equals("nkiescells")) {

            System.out.println("OK THIS IS FROM NKIESCELLS TABLE");
            model.put("strain_name", esd.getStrain_name());
            model.put("strainname", esd.getStrain_name());
        }
        System.out.println("STRAIN_NAME VAL IS " + model.get("strain_name") + " " + model.get("strainname"));
        model.put("common_name_s", webRequest.getCommon_name_s());//TODO need to take from backgrounds.name when all model files are incorporated
        model.put("req_material", webRequest.getReq_material());
        model.put("live_animals", webRequest.getLive_animals());
        model.put("frozen_emb", webRequest.getFrozen_emb());
        model.put("frozen_spe", webRequest.getFrozen_spe());
        // TA application details
        model.put("application_type", webRequest.getApplication_type());

        WebRequestsDAO vatEligbleCheck = new WebRequestsDAO();
        vatEligbleCheck = wr.getReqByID(webRequest.getId_req());
        String EUEligible = vatEligbleCheck.getEligible_country();
        model.put("ta_eligible", EUEligible/*webRequest.getEligible_country()*/);
        // if(webRequest.getEligible_country() != null && webRequest.getEligible_country().equals("yes")) {
        if (EUEligible.equals("yes")) {
            model.put("EU", Integer.parseInt("1"));
        } else {
            model.put("EU", Integer.parseInt("0"));
        }
        model.put("ta_proj_desc", webRequest.getProject_description());
        model.put("ROI", webRequest.getRegister_interest());
        //new e-mail message requirements for eucomm
        model.put("ArchContactEmail", ArchContactEmail.trim());
        model.put("requestID", webRequest.getId_req());
        model.put("labID", webRequest.getLab_id_labo());
        model.put("rtoolsID", rtoolsID);
        model.put("BASEURL", BASEURL);
        if (webRequest.getDistributionCentre() != null) {
            model.put("DistributionCentre", webRequest.getDistributionCentre().getName() + ", " + webRequest.getDistributionCentre().getCountry());
        }
        if (webRequest.getLab_id_labo() != null && !webRequest.getLab_id_labo().equals("4")) {
            /*
             * FOR LEGAL REASONS MTA FILE AND USAGE TEXT SHOULD NOT BE SHOWN FOR MRC STOCK.
             * MRC WILL SEND MTA SEPARATELY (M.FRAY EMMA IT MEETING 28-29 OCT 2010)
             */
            if (sdSubmit != null) {
                model.put("mtaFile", sdSubmit.getMta_file());
            } else if (esd != null) {
                model.put("mtaFile", esd.getMta_file());
            }
        }

        System.out.println("rtoolsid=" + model.get("rtoolsID") + "  -  labid=" + model.get("labID"));
        String velocTemplate = null;

        if (webRequest.getRegister_interest().equals("0")) {
            /* Set values for template to be used, PDF Title & mail message subject text
             * for strain submission
             */
            System.out.println("lab id == " + webRequest.getLab_id_labo());
            if (rtoolsID.equals("9") && webRequest.getLab_id_labo().equals("1961")) {
                velocTemplate = "org/emmanet/util/velocitytemplates/SangerSpecificSubmissionConfirmation-Template.vm";
            } else if (type != null && type.equals("nkiescells")) {
                session.setAttribute("req_material", webRequest.getReq_material());
                velocTemplate = "org/emmanet/util/velocitytemplates/escellRequest-Template.vm";

            } else if ((sdSubmit.getLs_consortium() != null) && sdSubmit.getLs_consortium().equals("EUCOMMToolsCre") && webRequest.getLab_id_labo().equals("1961")) {
                velocTemplate = "org/emmanet/util/velocitytemplates/SangerSpecificSubmissionConfirmation-Template_eucommtoolscre.vm";
            } else if (sdSubmit.getName().matches(".*em\\dWtsi.*") && webRequest.getLab_id_labo().equals("1961")) {
                /* Request for a Crispr line
                need to apply different mail template
                */
                System.out.println("\n\n\nTHIS IS A CRISPR LINE\n\n\n:");
                velocTemplate = "org/emmanet/util/velocitytemplates/SangerSpecificSubmissionConfirmation-Template_crispr.vm";

            } else {
                session.setAttribute("req_material", webRequest.getReq_material());
                velocTemplate = "org/emmanet/util/velocitytemplates/submissionConfirmation-Template.vm";
            }
            pdfTitle = "EMMA Mutant Request Form";
            //TODO SET MAIL SUBJECT LINE HERE
            xmlFileprefix = "req_";
            mailSubjectText = "Your Request from EMMA - confirmation of receipt: ";
            pdfConditions = true;
            //cc for TET systems strain

            List rtool = new ArrayList();
            //to avoid tet cc being used if str_id_str (numeric only) for nki es cells is the same as a tet strain id
            if (type != null && type.equals("nkiescells")) {
                //do nothing
            } else {
                rtool = wr.strainRToolID(webRequest.getStr_id_str());
            }

            for (Iterator it = rtool.listIterator(); it.hasNext();) {
                Integer I = (Integer) it.next();
                if (I == 5) {
                    //TET strain
                    Cc.put(Cc.size() + 1, tetCc);
                }
            }

        } else if (webRequest.getRegister_interest().equals("1")) {
            if (rtoolsID.equals("9") && webRequest.getLab_id_labo().equals("1961")) {
                /*##NEW TEMPLATE FOR SANGER */
                velocTemplate = "org/emmanet/util/velocitytemplates/SangerSpecificInterestSubmission-Template.vm";

            } else if (sdSubmit.getLs_consortium().equals("EUCOMMToolsCre") && webRequest.getLab_id_labo().equals("1961")) {

                velocTemplate = "org/emmanet/util/velocitytemplates/SangerSpecificInterestSubmission-Template_eucommtoolscre.vm";
            } else if (sdSubmit.getName().matches(".*em\\dWtsi.*") && webRequest.getLab_id_labo().equals("1961")) {
                /* Request for a Crispr line
                need to apply different mail template
                */
                System.out.println("\n\n\nTHIS IS A CRISPR LINE\n\n\n:");
                velocTemplate = "org/emmanet/util/velocitytemplates/SangerSpecificInterestSubmission-Template_crispr.vm";
            } else {
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

        // Format strain id to correct 0 padding TODO MAY NEED TO GO NOT NECESSARY HERE AS DONE ABOVE
        NumberFormat formatter = new DecimalFormat("00000");
        String formattedID = formatter.format(webRequest.getStr_id_str());
        // NB USED AS PDF FILENAME AS WELL
        xmlFileName = xmlFileprefix + webRequest.getTimestamp() + "_" + webRequest.getSci_firstname() + "_" + webRequest.getSci_surname() + "_" + formattedID;
        pdfFile = createPDF(model, pathToXml + xmlFileName + pdfExt);
        if (webRequest.getLab_id_labo() != null && webRequest.getLab_id_labo().equals("3")) {
            //a Hemholtz request so need to create xml file and mail to Susan EMMA-539
            //better check to make sure it isn't a ROI
            System.out.println("Send xml file to Helmholtz");
            System.out.println("req material from model is: " + model.get("req_material"));
            if (!webRequest.getRegister_interest().equals("1")) {

                model.put("con_country_code_iso", wr.isoCountryCode(webRequest.getCon_country()));//Added EMMA-539 

                CharSequence csFE = "embryos";
                CharSequence csFS = "sperm";
                CharSequence csL = "live";
                CharSequence csR = "Rederived";

                if (model.get("req_material").toString().contains(csFE) || model.get("req_material").toString().contains(csFS)) {
                    model.put("cost", frozenCost);
                    System.out.println("cost from model is: " + frozenCost);
                } else if (model.get("req_material").toString().startsWith(csL.toString())) {
                    model.put("cost", liveCost);
                    System.out.println("cost from model is: " + liveCost);
                } else if (model.get("req_material").toString().startsWith(csR.toString())) {
                    model.put("cost", liveCost);
                    System.out.println("cost from model is: " + liveCost);
                } else {
                    model.put("cost", null);
                    System.out.println("being set to null");
                }
                System.out.println("cost model is : " + model.get("cost"));
                // XML file content Template created by Velocity
                String xmlFileContent = VelocityEngineUtils.mergeTemplateIntoString(getVelocityEngine(),
                        "org/emmanet/util/velocitytemplates/requestXml-Template.vm", model);
                try {
                    /* Create request XML file from velocity template
                     * named to format req_TIMESTAMP_FIRSTNAME_SURNAME_STRAINID.xml
                     */

                    File fileXML = new File(pathToXml + xmlFileName + xmlExt);
                    // Create pdf from model
                    // pdfFile = createPDF(model, pathToXml + xmlFileName + pdfExt);    // Create file if it does not exist

                    // REMOVED AS XML NO LONGER REQUIRED
                    //boolean success = file.createNewFile();
                    boolean success = fileXML.createNewFile();
                    if (success) {
                        // File did not exist and was created
                        Writer out = new BufferedWriter(new OutputStreamWriter(
                                new FileOutputStream(fileXML), "UTF8"));
                        out.write(xmlFileContent);
                        out.close();
//NOW MAIL HELMHOLTZ/SUSAN EMMA-539
                        MimeMessage xmlMessage = getJavaMailSender().createMimeMessage();
                        try {
                            MimeMessageHelper helper = new MimeMessageHelper(xmlMessage, true, "UTF-8");
                            helper.setReplyTo("emma@infrafrontier.eu");
                            helper.setFrom("emma@infrafrontier.eu");
                            helper.setTo(xmlMailTo);
                            //helper.setTo("philw@ebi.ac.uk");
                            helper.setSubject("XML request file for request id " + model.get("requestID") + " (" + model.get("emmaid") + "). ");
                            helper.addAttachment(xmlFileName + xmlExt, fileXML);
                            getJavaMailSender().send(xmlMessage);
                            System.out.println("Sent xml file to Helmholtz");
                        } catch (MessagingException ex) {
                        }
                    } else {
                        // File already exists 
                        // Do nothing
                    }
                } catch (IOException e) {
                }
            }
        }
        MimeMessage message = getJavaMailSender().createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setReplyTo("emma@infrafrontier.eu");
            helper.setFrom("emma@infrafrontier.eu");
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
            if (request.getParameter("type") != null && request.getParameter("type").equals("nkiescells")) {
                helper.setCc(nkiescellCc);
                //helper.setCc(ccAddresses);
            } else if (request.getParameter("triggerMails") != null && request.getParameter("triggerMails").equals("managersonly")) {
                //no cc addresses else managers only email repeats
            } else {
                helper.setCc(ccAddresses);//.addCc(ccAddresses);
            }

            //ccCentre = Arrays.asList(nkiescellCc);

            helper.setTo(webRequest.getSci_e_mail().trim());
            String correctStrainname = "";
            if (sdSubmit != null) {
                correctStrainname = sdSubmit.getName();
            } else if (esd != null) {
                correctStrainname = esd.getStrain_name();
            }
            helper.setSubject(mailSubjectText + webRequest.getStrain_id() + " (" + correctStrainname /*webRequest.getStrain_name()*/ + ") Your request ID: " + webRequest.getId_req());
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
            String mtaFile = "";
            if (webRequest.getLab_id_labo() != null && !webRequest.getLab_id_labo().equals("4")) {

                if (sdSubmit != null) {
                    mtaFile = sdSubmit.getMta_file();
                } else if (esd != null) {
                    mtaFile = esd.getMta_file();
                }
                // if (sdSubmit.getMta_file() != null && !sdSubmit.getMta_file().equals("")) {
                if (mtaFile != null && !mtaFile.isEmpty()) {
                    if (!model.get("application_type").equals("ta_only")) {
                        //add mta file if associated with strain id
                        //String mtaFile = sdSubmit.getMta_file();
                        // mtaFile="this is a test.pdf";
                        System.out.println("mta file value is::" + mtaFile);
                        //  System.out.println( " OK now mta file from model value is " + model.get("mtaFile").toString());
                        // || model.get("mtaFile").toString().equals("")
                        if (mtaFile != null || model.get("mtaFile") != null) {
                            FileSystemResource fileMTA = new FileSystemResource(new File(getPathToMTA() + mtaFile));
                            System.out.println("file mta is-> " + fileMTA + " and to string is " + fileMTA.toString());
                            //need to check for a valid mta filename use period extension separator, all mtas are either .doc or .pdf
                            if (!fileMTA.exists()) {
                                System.out.println("MTA file " + mtaFile + " cannot be accessed");
                            } else {
                                helper.addAttachment(model.get("mtaFile").toString(), fileMTA);
                            }
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

                System.out.println(message);
helper.setCc("philw@ebi.ac.uk");
helper.setBcc("philw@ebi.ac.uk");
helper.setTo("philw@ebi.ac.uk");

                getJavaMailSender().send(message);
                System.out.println(model.get("consortium"));

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

        //test logging of previous refferal URIs for Sanger lines
        //to improve europhenome/mouse portal interest logging
        // <c:if test="${fn:containsIgnoreCase(command.strain_name,'wtsi')}">
        //System.out.println(model.get("strain_name").toString());
        if (model.get("strain_name") != null
                && model.get("strain_name").toString().toLowerCase().contains("wtsi")) {
            //System.out.println("CONDITION IS TRUE");
            BufferedWriter out = null;
            Date date = new Date();
            SimpleDateFormat ldf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMM");
            try {
                out = new BufferedWriter(new FileWriter(FILELOCATION + "/wtsiRefererLogging." + sdf.format(date), true));
                //System.out.println(out.hashCode() + " - " + FILELOCATION + "wtsiRefererLogging." + sdf.format(date));
                String referer = request.getHeader("Referer");
                if (referer.contains("sanger.ac.uk/mouseportal")) {
                    //log to file as from wtsi mouse portal
                    out.write(ldf.format(date) + "-" + referer);
                } else {
                    //Need to log a message so all requests are logged for metrics
                    out.write(ldf.format(date) + "-" + "WTSI request not generated from another site. " + referer);
                }
            } catch (IOException ex) {
                ex.printStackTrace();
                //Logger.getLogger(RequestFormController.class.getName()).log(Level.WARNING, null, ex);
            } finally {
                try {
                    out.newLine();
                    out.flush();
                    out.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                    //Logger.getLogger(RequestFormController.class.getName()).log(Level.WARNING, null, ex);
                }
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
                    text = new StringBuilder().append(text).append(
                            "\nYou have indicated that you have read the conditions and agree to pay the transmittal fee " + "plus shipping costs.").toString();

                    header = new StringBuilder().append(header).append("\nStandard request\n").toString();

                } else if (!model.get("application_type").equals("request_only")) {
                    header1 = new StringBuilder().append(header1).append("\nApplication for Transnational Access Activity").toString();
                    if (model.get("application_type").equals("ta_only")) {
                        text1 = new StringBuilder().append(text1).append("\nYou have indicated that you have read the conditions and have applied for free of charge TA only. "
                                + "In the case of the TA application being rejected the request process will be terminated.").toString();
                        header1 = new StringBuilder().append(header1).append(" (TA Option B)\n").toString();
                    } else {
                        text1 = new StringBuilder().append(text1).append("\nYou have indicated that you have read the conditions and have applied for free of charge TA "
                                + "and have agreed to pay the service charge plus shipping cost if the TA application is rejected.").toString();
                        header1 = new StringBuilder().append(header1).append(" (TA Option A)\n").toString();
                    }

                    header2 = new StringBuilder().append(header2).append("\n\nDescription of project (1/2 page) involving requested EMMA mouse mutant resource. "
                            + "The project description will be used by the Evaluation Committee for selection of applicants:").toString();
                }

                if (!model.get("application_type").equals("request_only")) {
                    //  table.addCell("" + model.get("ta_proj_desc"));
                    text2 = new StringBuilder().append(text2).append("\n\n "
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

    /**
     * @return the BASEURL
     */
    public String getBASEURL() {
        return BASEURL;
    }

    /**
     * @param BASEURL the BASEURL to set
     */
    public void setBASEURL(String BASEURL) {
        this.BASEURL = BASEURL;
    }

    
    /**
     * @return the xmlMailTo
     */
    public String[] getXmlMailTo() {
        return xmlMailTo;
    }

    /**
     * @param xmlMailTo the xmlMailTo to set
     */
    public void setXmlMailTo(String[] xmlMailTo) {
        this.xmlMailTo = xmlMailTo;
    }

    /**
     * @return the nkiescellCc
     */
    public String[] getNkiescellCc() {
        return nkiescellCc;
    }

    /**
     * @param nkiescellCc the nkiescellCc to set
     */
    public void setNkiescellCc(String[] nkiescellCc) {
        this.nkiescellCc = nkiescellCc;
    }

    /**
     * @return the frozenCost
     */
    public String getFrozenCost() {
        return frozenCost;
    }

    /**
     * @param frozenCost the frozenCost to set
     */
    public void setFrozenCost(String frozenCost) {
        this.frozenCost = frozenCost;
    }

    /**
     * @return the liveCost
     */
    public String getLiveCost() {
        return liveCost;
    }

    /**
     * @param liveCost the liveCost to set
     */
    public void setLiveCost(String liveCost) {
        this.liveCost = liveCost;
    }

    /**
     * @return the frozenDelivery
     */
    public String getFrozenDelivery() {
        return frozenDelivery;
    }

    /**
     * @param frozenDelivery the frozenDelivery to set
     */
    public void setFrozenDelivery(String frozenDelivery) {
        this.frozenDelivery = frozenDelivery;
    }

    /**
     * @return the liveDelivery
     */
    public String getLiveDelivery() {
        return liveDelivery;
    }

    /**
     * @param liveDelivery the liveDelivery to set
     */
    public void setLiveDelivery(String liveDelivery) {
        this.liveDelivery = liveDelivery;
    }

    /**
     * @return the liveShelfDelivery
     */
    public String getLiveShelfDelivery() {
        return liveShelfDelivery;
    }

    /**
     * @param liveShelfDelivery the liveShelfDelivery to set
     */
    public void setLiveShelfDelivery(String liveShelfDelivery) {
        this.liveShelfDelivery = liveShelfDelivery;
    }

    /**
     * @return the liveShelfCost
     */
    public String getLiveShelfCost() {
        return liveShelfCost;
    }

    /**
     * @param liveShelfCost the liveShelfCost to set
     */
    public void setLiveShelfCost(String liveShelfCost) {
        this.liveShelfCost = liveShelfCost;
    }
}
