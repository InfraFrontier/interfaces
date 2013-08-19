/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.emmanet.jobs;

import ebi.ws.client.Authors;
import ebi.ws.client.QueryException_Exception;
import ebi.ws.client.ResponseWrapper;
import ebi.ws.client.Result;
import ebi.ws.client.WSCitationImpl;
import ebi.ws.client.WSCitationImplService;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.emmanet.model.BibliosDAO;
import org.emmanet.model.BibliosManager;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 *
 * @author ckchen
 * @author philw modified BY PHILW 08MAR2012 to run in Spring using quartz
 * scheduling Modified by philw 19122012 to use new webservice.
 */
public class EmmaBiblioJOB extends QuartzJobBean {

    /**
     * @param args the command line arguments
     */
    private String wsdlLocation;
    private String schedulerMsg = "";
    private MailSender mailSender;
    private String[] cc;

    /**
     * @return the mailSender
     */
    public MailSender getMailSender() {
        return mailSender;
    }

    /**
     * @param mailSender the mailSender to set
     */
    public void setMailSender(MailSender mailSender) {
        this.mailSender = mailSender;
    }

    /**
     * @return the cc
     */
    public String[] getCc() {
        return cc;
    }

    /**
     * @param cc the cc to set
     */
    public void setCc(String[] cc) {
        this.cc = cc;
    }

    public class FetchBiblio {
        public String title;
        public String author1;
        public String author2;
        public String journal;
        public String volume;
        public String issue;
        public String pages;
        public String paperid;
        public int year;
    }
    
    public BibliosDAO check_pmid_exists(int pmid) {
        BibliosManager bm = new BibliosManager();
        BibliosDAO bdao = (BibliosDAO) bm.getPubmedIDByID(pmid);
        if (bdao == null) {
            System.out.println("PMID " + pmid + " does not exist in database ... exiting");
            schedulerMsg = (new StringBuilder()).append(schedulerMsg).append("\nPMID ").append(pmid).append(" does not exist in database ... exiting\n").toString();
        }
        return bdao;
    }
    
    public void check_for_updates() {
        // select the pubmed ids of not updated biblios
        BibliosManager bm = new BibliosManager();
        BibliosDAO bdao = new BibliosDAO();
        List rset;
        rset = bm.getPubmedID();
       // System.out.println("rset size :: " + rset.size());
        schedulerMsg = (new StringBuilder()).append(schedulerMsg).append("Total number of records needing updating::- ").append(rset.size()).toString();
       // System.out.println(schedulerMsg);
        int pmid = 0;
        String spmid;
        int counter = 0;
        for (Iterator it = rset.listIterator(); it.hasNext();) {
            bdao = (BibliosDAO) it.next();
            counter++;
           // System.out.println("counter value==" + counter);
            spmid = bdao.getPubmed_id();
           // System.out.println("pubmedid==" + spmid);
            // removeleading non-digits of a pmid
            String goodPmid = spmid;//null;
            try {
                pmid = Integer.parseInt(goodPmid);
            } catch (NumberFormatException ex) {
                Pattern replace = Pattern.compile("[^\\d+]");
                Matcher matcher = replace.matcher(goodPmid/*spmid*/);
                
                while (matcher.find()) {
                    goodPmid = matcher.replaceAll("");
                    System.out.println("Corrected '" + spmid + "' as " + "'" + goodPmid + "'");
                    schedulerMsg = (new StringBuilder()).append(schedulerMsg).append("\nCorrected ").append(spmid).append(" as ").append(goodPmid).append("\n").toString();
                   // System.out.println(schedulerMsg);
                    pmid = Integer.parseInt(goodPmid);
                    bdao.setPubmed_id(""+ pmid);
                    bm.save(bdao);
                }
            }
            FetchBiblio paper = new FetchBiblio();
            if (pmid > 0) {
                paper = fetchPaper(pmid);
            } else {
               // System.out.println("pubmed issue ++ " + spmid);
            }
            //System.out.println("About to call updatebibrefs");
            updateBibliographicReferences(paper, bdao);
        }
        System.out.println("\nUpdated " + counter + " biblios records");
        schedulerMsg = (new StringBuilder()).append(schedulerMsg).append("\nUpdated ").append(counter).append(" biblios records").toString();
       // System.out.println(schedulerMsg);
    }
    
    public void updateBibliographicReferences(FetchBiblio paper, BibliosDAO bdao) {
       // System.out.println("line 157 paper title == " + paper.title.toString());
        String vol = "";
        if (paper.issue != null) {
          //  System.out.println("Updating for PMID " + paper.paperid + "...");
            schedulerMsg = (new StringBuilder()).append(schedulerMsg).append("\nUpdating for PMID ").append(paper.paperid).append("...").toString();
           // System.out.println(schedulerMsg);
//System.out.println("PAPER ISSUE= " + paper.issue);
            
            if (/*paper.issue != null &&*/ !paper.issue.isEmpty()) {
                vol = paper.volume + "(" + paper.issue + ")";
            } else {
                vol = paper.volume;
            }
        }
            bdao.setTitle(paper.title);
            bdao.setAuthor1(paper.author1);
            bdao.setAuthor2(paper.author2);
            bdao.setYear("" + paper.year);
            bdao.setJournal(paper.journal);
            bdao.setVolume(/*paper.volume*/vol);
            bdao.setPages(paper.pages);
            bdao.setUsername("EMMA");
            bdao.setUpdated("Y");
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = new Date();
            //System.out.println(dateFormat.format(date));
            bdao.setLast_change(dateFormat.format(date));

            BibliosManager bm = new BibliosManager();
            bm.save(bdao);
        
    }
    
    public FetchBiblio fetchPaper(int pmid) {
        System.out.println("fetching paper" + pmid);
        FetchBiblio paper = new FetchBiblio();
        wsdlLocation = getWsdlLocation();
        WSCitationImplService service = new WSCitationImplService();
        try {
            WSCitationImpl port = service.getWSCitationImplPort();
            ResponseWrapper resultsBean = port.searchPublications("EXT_ID:" + pmid, "metadata", "core", 0, false, "");
            List<Result> resultBeanCollection = resultsBean.getResultList().getResult().subList(0, 1);//philw: added a restriction here as we are not interested in citations
            /*
             * Moved size of results here and used resultBeanCollection.size() rather than resultListBean.getHitCount() which always returned 0 for some reason (lines 171/2)
             * Suggest that this was a result of porting the old code over to this new codeoh right
             * 
             * PJW 11SEP2012
             */
            System.out.println("\nNumber of hits:\t" + resultBeanCollection.size());
            schedulerMsg = (new StringBuilder()).append(schedulerMsg).append("Number of hits:\t").append(resultBeanCollection.size()).append("\n").toString();
            //System.out.println(schedulerMsg);
            for (Result citation : resultBeanCollection) {
                int size = citation.getAuthorList().getAuthor().size();
                int counter = -1;
                StringBuilder otherAuthors = new StringBuilder();
                //authors could be zero
                
                for (Authors author : citation.getAuthorList().getAuthor()) {
                    counter++;
                    if (!author.getFullName().isEmpty()) {
                        String fullname = author.getFullName();
                        
                        if (counter == 0) {
                            paper.author1 = fullname;
                        } else if (counter + 1 < size) {
                            otherAuthors.append(fullname).append(", ");
                        } else {
                            otherAuthors.append(fullname);
                        }
                    }
                }
                paper.paperid = "" + pmid;
                paper.title = (citation.getTitle() == null ? "" : citation.getTitle());
                paper.year = citationGetYear(citation);
                paper.journal = citationGetJournalTitle(citation);
                paper.volume = citationGetVolume(citation);
                paper.issue = citation.getJournalInfo().getIssue();
                paper.pages = citation.getPageInfo();
                paper.author2 = otherAuthors.toString();
                //TESTING
                System.out.println("Citation get date of pub-- " + citationGetDateOfPublication(citation));
                System.out.println("Citation get year of pub-- " + Integer.toString(paper.year));
                System.out.println("Citation get volume of pub-- " + citationGetVolume(citation));
                System.out.println("Citation get journal-- " + citationGetJournalTitle(citation));
                System.out.println("Citation get issue-- " + citationGetIssue(citation));
                System.out.println("Citation get author1-- " + paper.author1);
                System.out.println("Citation get author2-- " + paper.author2);
                
            }
        } catch (QueryException_Exception qex) {
            System.out.printf("Caught QueryException_Exception: %s\n", qex.getFaultInfo().getMessage());
            schedulerMsg = (new StringBuilder()).append(schedulerMsg).append("\nCaught QueryException_Exception: %s\n").append(qex.getFaultInfo().getMessage()).toString();
            System.out.println(schedulerMsg);
            webmasterJobMessage();
            
        }
        return paper;
    }
    private String citationGetDateOfPublication(Result citation) {
        if ((citation != null) && (citation.getJournalInfo() != null) && (citation.getJournalInfo().getDateOfPublication() != null))
            return citation.getJournalInfo().getDateOfPublication();
        else
            return "";
    }
    private String citationGetJournalTitle(Result citation) {
        if ((citation != null) && (citation.getJournalInfo() != null) && (citation.getJournalInfo().getJournal() != null) && (citation.getJournalInfo().getJournal().getTitle() != null))
            return citation.getJournalInfo().getJournal().getTitle();
        else
            return "";
    }
    private Short citationGetYear(Result citation) {
        if ((citation != null) && (citation.getJournalInfo() != null) && (citation.getJournalInfo().getYearOfPublication() != null))
            return citation.getJournalInfo().getYearOfPublication();
        else
            return 0;
    }
    private String citationGetVolume(Result citation) {
        if ((citation != null) && (citation.getJournalInfo() != null) && (citation.getJournalInfo().getVolume() != null))
            return citation.getJournalInfo().getVolume();
        else
            return "";
    }
    private String citationGetIssue(Result citation) {
        if ((citation != null) && (citation.getJournalInfo() != null) & (citation.getJournalInfo().getIssue() != null))
            return citation.getJournalInfo().getIssue();
        else
            return "";
    }
    private String citationGetPages(Result citation) {
        if ((citation != null) && (citation.getPageInfo() != null))
            return citation.getPageInfo();
        else
            return "";
    }
    private String citationGetOtherAuthors(String otherAuthors) {
        return (otherAuthors == null ? "" : otherAuthors);
    }
    
    @Override
    protected void executeInternal(JobExecutionContext jec) throws JobExecutionException {
        executeInternalPrivate();
    }
    public static void main(char[] args) {
        EmmaBiblioJOB emmaBiblioJob = new EmmaBiblioJOB();
        emmaBiblioJob.executeInternalPrivate();
    }
    private void executeInternalPrivate() {
        schedulerMsg = (new StringBuilder()).append(schedulerMsg).append("EmmaBiblioJOB Kicking Off").append("\n\n").toString();
        System.out.println("EmmaBiblioJOB Kicking Off");
        System.out.println("checking updates");
        check_for_updates();
        System.out.println("returned from checking updates");
        System.out.println(schedulerMsg);
        webmasterJobMessage();
    }

    /**
     * @return the wsdlLocationsystems.ebi.ac.uk
     *
     */
    public String getWsdlLocation() {
        return wsdlLocation;
    }

    /**
     * @param wsdlLocation the wsdlLocation to set
     */
    public void setWsdlLocation(String wsdlLocation) {
        this.wsdlLocation = wsdlLocation;
    }
    
    public void webmasterJobMessage() {
        SimpleMailMessage msg = new SimpleMailMessage();
        String[] cc = {"koscieln@ebi.ac.uk", "webmaster@emmanet.org"};
        msg.setTo(cc);
        msg.setSubject("Cron - Update Biblios Job Report");
        msg.setText(schedulerMsg);
        try {
            getMailSender().send(msg);
        } catch (MailException ex) {
            System.err.println(ex.getMessage());
        }
    }
}
