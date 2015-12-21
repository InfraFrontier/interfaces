/*
 * RegInterestJOB.java
 *
 * Created on 30 November 2007, 13:56
 *
 * Scheduled job to check for publicly available strains and alert subscribers who have registered an interest.
 *
 * Runs nightly according to schedule in applicationContext.xml to search database for strain records modified during the day
 * that have a str_status of 'ARCHD' and are publicly available. Compares these records to the list created when the job last
 * ran, held in file strainsAccepted, these records were not publicly available, having a str_status of 'ARRD'. If a match is
 * found then when they were last updated their status was changed to that of being publicly available. The database table
 * web_requests is then searched to determine if a user registered an interest in that strain, if so a personalized mail
 * message is sent via Spring framework mail utlising Velocity templating for the message body.
 *
 * v1.0 released 04-02-2008
 * v1.1 released 14-02-2008 - Added file copy code to readstrain() method 
 * to create copy of strain list to aid debugging in the event of failure
 *
 */
package org.emmanet.jobs;

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

//~--- non-JDK imports --------------------------------------------------------
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.apache.velocity.app.VelocityEngine;
import org.emmanet.model.StrainsDAO;
import org.emmanet.model.WebRequestsDAO;
import org.emmanet.util.Configuration;
import org.emmanet.util.Encrypter;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.ui.velocity.VelocityEngineUtils;

/**
 *
 * @author phil
 */
public class RegInterestJOB extends QuartzJobBean {
    // Strainlist file constant

    final static String ACCCEPTEDSTRAINS = Configuration.get("ACCCEPTEDSTRAINS");//\ "/data/web/TESTsubmissions/strainsAccepted";
    List list = new LinkedList();
    private MailSender mailSender;
    private String theDate;
    private String theTime;
    private VelocityEngine velocityEngine;
    private Map Cc = new HashMap();
    private String Bcc;
    private String webmasterMsg = "";
    private Encrypter encrypter = new Encrypter();
    private static String BASEURL=Configuration.get("BASEURL");

    public void generateStrainList(String emmaId) {
        try {

            BufferedWriter out = new BufferedWriter(new FileWriter(ACCCEPTEDSTRAINS, true));
            out.write(emmaId);
            out.close();
        } catch (IOException e) {
            webmasterMsg = (new StringBuilder()).append(webmasterMsg).append(e.getMessage()).append("\n\n").toString();
            webmasterJobMessage();
            e.printStackTrace();

        }
    }

    public void readStrainList() {
        list = new ArrayList();
        webmasterMsg = (new StringBuilder()).append(webmasterMsg).append("Reading strain list...\n\n").toString();
        try {
            BufferedReader in = new BufferedReader(new FileReader(ACCCEPTEDSTRAINS));
            String str;
            while ((str = in.readLine()) != null) {
                list.add(str);
            }
            in.close();
            /* 
             * Added to create copy of strain list to aid debugging in the event of failure
             * philw@ebi.ac.uk 14-02-2008
             */
            // Create channel on the source
            FileChannel srcChannel = new FileInputStream(ACCCEPTEDSTRAINS).getChannel();
            // Create channel on the destination
            FileChannel dstChannel = new FileOutputStream(ACCCEPTEDSTRAINS + "." + theDate).getChannel();
            // Copy file contents from source to destination
            dstChannel.transferFrom(srcChannel, 0, srcChannel.size());
            // Close the channels
            srcChannel.close();
            dstChannel.close();
            webmasterMsg = (new StringBuilder()).append(webmasterMsg).append("Read strain list...\n\n").toString();

        } catch (IOException e) {
            webmasterMsg = (new StringBuilder()).append(webmasterMsg).append(e.getMessage()).append("\n\n").toString();
            webmasterJobMessage();
            e.printStackTrace();
        }
    }

    public void RegInterest() {
        webmasterMsg = (new StringBuilder()).append(webmasterMsg).append("Starting RegisterInterest Job\n\n").toString();
                Map model = new HashMap();
                model.put("BASEURL",BASEURL);
                System.out.println("BASEURL VALUE FROM MODEL IS::" + model.get("BASEURL"));
                webmasterMsg = (new StringBuilder()).append(webmasterMsg).append("BASEURL VALUE FROM MODEL IS::" + model.get("BASEURL") + "\n\n").toString();
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setReplyTo("emma@infrafrontier.eu");
        msg.setFrom("emma@infrafrontier.eu");
        Format formatter;
        // The four digit year
        formatter = new SimpleDateFormat("yyyy");
        // The two digit Month
        formatter = new SimpleDateFormat("MM");
        // The two digit day
        formatter = new SimpleDateFormat("dd");
        // Get today's date
        Date date = new Date();
        // Format date
        formatter = new SimpleDateFormat("yyyy-MM-dd");
        theDate = formatter.format(date);
        // TIME FORMAT
        // Get current hour in 24hr format
        formatter = new SimpleDateFormat("HH");
        // Get current minutes
        formatter = new SimpleDateFormat("mm");
        // Get current seconds
        formatter = new SimpleDateFormat("ss");
        // Format time (with leading space)
        formatter = new SimpleDateFormat(" HH:mm:ss");    // NOTE: Leading space
        theTime = formatter.format(date);
        // Start to check modified strains from db query against list of accepted files in db
        // READ FILE AND POP ARRAY
        readStrainList();
        StrainsDAO strainARCHD;
        WebRequests r = new WebRequests();
        List res = r.strainsUpdates(theDate, theDate + theTime);
        System.out.println("Size = " + res.size());
        for (int i = 0; i < res.size(); i++) {
            strainARCHD = (StrainsDAO) res.get(i);
            System.out.println(strainARCHD.getEmma_id());
            String s = strainARCHD.getEmma_id();
            // Check against listarray
            int index = Collections.binarySearch(list, s);
            // If index is negative then doesn't exist
            if (index >= 0) {
                System.out.print(index);
                System.out.println("ID in Collection: " + s + "\n");
                // GRAB EMAIL FROM REQUESTS TABLE
                WebRequestsDAO webReq;
                WebRequests wr = new WebRequests();
                // List result = wr.webRequests("EM:01985");
                List result = wr.webRequests(s);
                System.out.println("Result size = " + result.size());
                // SEND EMAIL
                // Create hashmap for velocity templates

                for (int ii = 0; ii < result.size(); ii++) {
                    webReq = (WebRequestsDAO) result.get(ii);
                    System.out.println(webReq.getSci_firstname() + " " + webReq.getSci_surname() + " " + webReq.getSci_e_mail() + " " + webReq.getStrain_name() + " " + webReq.getStr_id_str() + "  " + webReq.getStrain_id());
                    webReq.toString();
                    model.put("name", webReq.getSci_firstname() + " " + webReq.getSci_surname());
                    model.put("email", webReq.getSci_e_mail()/*"philw@ebi.ac.uk")*/);
                    model.put("emmaid", s);
                    model.put("strainname", webReq.getStrain_name());
                    //webReq.getId_req() /* new Integer(*/
                    System.out.println("encrypted id_req==" + encrypter.encrypt(webReq.getId_req()));
                    model.put("id", encrypter.encrypt(webReq.getId_req()));

                    /* NEW CODE TO SERVE REQUIREMENTS FOR SANGER EMAILS. 
                     * NEED RTOOLSID TO DETERMINE IF A SANGER STRAIN TOGETHER 
                     * WITH LAB_ID_LABO */

                    String rtoolsID = "";
                    List rtools = wr.strainRToolID(webReq.getStr_id_str());
                    System.out.println("rtools size=" + rtools.size());
                    Iterator itRTool = rtools.iterator();

                    while (itRTool.hasNext()) {
                        Object oo = itRTool.next();
                        rtoolsID = oo.toString();
                        System.out.println("rtoolsid=" + oo.toString() + "  -  labid=" + webReq.getLab_id_labo());
                    }

                    model.put("rtoolsID", rtoolsID);

                    model.put("labID", webReq.getLab_id_labo());
                    
                    /* END */

                    // TODO: my email for testing - pull from database/hibernate

                    /*
                     * 
                     * http://www.ebi.ac.uk/panda/jira/browse/EMMA-273 add cc for shipping contact
                     * 
                     */

                    String shipperMail = webReq.getCon_e_mail();
                    msg.setBcc("philw@ebi.ac.uk");
                    Cc.clear();
                    Cc.put("1", new String("emma@infrafrontier.eu"));
                    Cc.put("2", shipperMail);//EMMA-273 above
                    // Get responsible centre mail address(es) and add to map
                    List ccCentre = r.ccArchiveMailAddresses("" + webReq.getStr_id_str(),"strains");
                    String[] ccAddress = null;
                    //map key + 1
                    int im = 3;
                    Object[] o = null;
                    Iterator it = ccCentre.iterator();
                    while (it.hasNext()) {
                        o = (Object[]) it.next();
                        Cc.put("" + im + "", o[1].toString());
                        webReq.setLab_id_labo(o[0].toString());
                        im++;
                    }

                    ccAddress = new String[Cc.size()];
                    //TODO REMOVE String ceeCeeAddress = "";
                    Iterator it1 = Cc.entrySet().iterator();
                    int iii = 0;
                    while (it1.hasNext()) {
                        Map.Entry mvalue = (Map.Entry) it1.next();
                        ccAddress[iii] = (String) mvalue.getValue();
                        iii++;
                    }
                    msg.setCc(ccAddress);
                    msg.setTo(webReq.getSci_e_mail());
                    msg.setSubject("Your EMMA Strain Interest Registration Form - "
                            + "strain can now be ordered: " + model.get("emmaid") + " (" + model.get("strainname") + ")");
                    String content = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine,
                            "org/emmanet/util/velocitytemplates/regInt-Template.vm", model);

                    msg.setText(content);
                    try {
                        webmasterMsg = (new StringBuilder()).append(webmasterMsg).append("Sending mail message to " + webReq.getSci_e_mail() + " for request ID " + webReq.getId_req() + " ...\n\n").toString();
                        //getMailSender().send(msg);
                    } catch (MailException ex) {
                        webmasterMsg = (new StringBuilder()).append(webmasterMsg).append(ex.getMessage() + "\n\n").toString();
                        webmasterJobMessage();
                        System.err.println(ex.getMessage());
                    }
                    msg.setCc("");
                }
            }
        }
        // POPULATE ACCEPTED STRAIN FILE
        StrainsDAO strainARRD;
        WebRequests r2 = new WebRequests();
        List res2 = r2.strainListArrd();

        new File(ACCCEPTEDSTRAINS).delete();
        webmasterMsg = (new StringBuilder()).append(webmasterMsg).append("Generating strain list...\n\n").toString();
        for (int i = 0; i < res2.size(); i++) {
            strainARRD = (StrainsDAO) res2.get(i);
            generateStrainList(strainARRD.getEmma_id() + "\n");
        }
        webmasterMsg = (new StringBuilder()).append(webmasterMsg).append("Generated strain list...\n\n").toString();
    }

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {

        // FOR LOGS
        System.out.println("Job Started");
        webmasterMsg = (new StringBuilder()).append(webmasterMsg).append("OK kicking off....\n\n").toString();
        // Call main method that handles functionality
        RegInterest();
        webmasterMsg = (new StringBuilder()).append(webmasterMsg).append("Job finished....\n\n").toString();
        webmasterJobMessage();


    }

    public void webmasterJobMessage() {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo("webmaster@infrafrontier.eu");
        msg.setSubject("Cron - Register Interest Job Report");
        msg.setText(webmasterMsg);
        try {
            getMailSender().send(msg);
        } catch (MailException ex) {
            //System.err.println(ex.getMessage());
        }
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

    public Map getCc() {
        return Cc;
    }

    public void setCc(Map Cc) {
        this.Cc = Cc;
    }

    public String getBcc() {
        return Bcc;
    }

    public void setBcc(String Bcc) {
        this.Bcc = Bcc;
    }
}
