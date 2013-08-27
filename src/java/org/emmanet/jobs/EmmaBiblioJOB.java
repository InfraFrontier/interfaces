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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.MimeMessage;
import org.emmanet.model.BibliosDAO;
import org.emmanet.model.BibliosManagerIO;
import org.emmanet.util.Constants;
import org.emmanet.util.Utils;
import org.springframework.mail.MailSender;

/**
 *
 * @author ckchen
 * @author philw modified BY PHILW 08MAR2012 to run in Spring using quartz
 * scheduling Modified by philw 19122012 to use new webservice.
 */
public class EmmaBiblioJOB {

    private String wsdlLocation;
    private String schedulerMsg;
    private MailSender mailSender;
    private String[] from;
    private String[] to;
    private String[] cc;
    private String[] bcc;

    public EmmaBiblioJOB() {
        wsdlLocation = null;
        schedulerMsg = "";
        mailSender = null;
        
        // The 'from', 'to', 'cc', and 'bcc' names can be set programatically
        // as well as in the emma 'config.properties' file. Initialize them here.
        from = Utils.join(from, Utils.getEmmaProperty(Constants.FROM, ","));
        to = Utils.join(to, Utils.getEmmaProperty(Constants.TO, ","));
        cc = Utils.join(cc, Utils.getEmmaProperty(Constants.CC, ","));
        bcc = Utils.join(bcc, Utils.getEmmaProperty(Constants.BCC, ","));
    }
        // THIS METHOD IS NOT CALLED ANYWHERE.
//    public BibliosDAO check_pmid_exists(int pmid) {
//        BibliosManager bm = new BibliosManager();
//        BibliosDAO bdao = (BibliosDAO) bm.getPubmedIDByID(pmid);
//        if (bdao == null) {
//            System.out.println("PMID " + pmid + " does not exist in database ... exiting");
//            schedulerMsg = (new StringBuilder()).append(schedulerMsg).append("\nPMID ").append(pmid).append(" does not exist in database ... exiting\n").toString();
//        }
//        return bdao;
//    }
    
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
                paper.issue = citationGetIssue(citation);
                paper.pages = citationGetPages(citation);
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
    
//    @Override
//    protected void executeInternal(JobExecutionContext jec) throws JobExecutionException {
//        executeInternalPrivate();
//    }
    public static void main(String[] args) {
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
        // The 'from', 'to', 'cc', and 'bcc' names can be set programatically
        // as well as in the emma properties file. For each name type, combine
        // the two sources here so all requested users are notified. 
        Address[] fromAddress = Utils.toAddress(from);
        Address[] toAddress = Utils.toAddress(to);
        Address[] ccAddress = Utils.toAddress(cc);
        Address[] bccAddress = Utils.toAddress(bcc);
        
        if ((toAddress == null) && (ccAddress == null) && (bccAddress == null))
            throw new RuntimeException("Unable to send webmaster message because no recipients were specified. Message: " + schedulerMsg);
        
        try {
            String mailserver = Utils.getEmmaProperty(Constants.MAILSERVER);    // Add mailserver to session.
            if (mailserver == null)
                throw new RuntimeException("Unable to send webmaster message because no mailserver is defined.");
            Properties properties = new Properties();
            properties.put("mail.smtp.host", mailserver);
            Session session = Session.getInstance(properties);
            
            Message message = new MimeMessage(session);                         // Compose the message.
            if (fromAddress != null)
                message.addFrom(fromAddress);
            
            if (toAddress != null)
                message.addRecipients(Message.RecipientType.TO, toAddress);
            if (ccAddress != null)
                message.addRecipients(Message.RecipientType.CC, ccAddress);
            if (bccAddress != null)
                message.addRecipients(Message.RecipientType.BCC, bccAddress);
           
            String subject = "Cron - Update Biblios Job Report";
            message.setSubject(subject);
            message.setText(schedulerMsg);
            Transport.send(message);
        }
        catch (MessagingException me) {
            System.out.println("send failed: " + me.getLocalizedMessage());
        }
    }


    // GETTERS AND SETTERS
    

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
     * Returns an array of <code>String</code> containing the e-mail <em>TO</em>
     * names
     * @return an array of <code>String</code> containing the e-mail <em>TO</em>
     * names
     */
    public String[] getTo() {
        return to;
    }

    /**
     * This method sets the e-mail <em>TO</em> addresses to the array of <code>
     * String</code> provided in <em>to</em>.
     * @param to the list of e-mail addresses
     */
    public void addTo(String[] to) {
        this.to = Utils.join(this.to, to);
    }

    /**
     * This method sets the e-mail <em>CC</em> addresses to the array of <code>
     * String</code> provided in <em>cc</em>.
     * @param cc the list of e-mail addresses
     */
    public String[] getCc() {
        return cc;
    }

    /**
     * This method sets the e-mail <em>CC</em> addresses to the array of <code>
     * String</code> provided in <em>cc</em>
     * CC</em> to the list of e-mail addresses
     * @param to the list of e-mail addresses
     */
    public void addCc(String[] cc) {
        this.cc = Utils.join(this.cc, cc);
    }

    /**
     * Returns an array of <code>String</code> containing the e-mail <em>BCC</em>
     * names
     * @return an array of <code>String</code> containing the e-mail <em>BCC</em>
     * names
     */
    public String[] getBcc() {
        return bcc;
    }

    /**
     * This method sets the e-mail <em>BCC</em> addresses to the array of <code>
     * String</code> provided in <em>bcc</em>.
     * @param bcc the list of e-mail addresses
     */
    public void addBcc(String[] bcc) {
        this.bcc = Utils.join(this.bcc, bcc);
    }
    
    /**
     * Returns an array of <code>String</code> containing the e-mail <em>FROM</em>
     * names
     * @return an array of <code>String</code> containing the e-mail <em>FROM</em>
     * names
     */
    public String[] getFrom() {
        return from;
    }

    /**
     * This method sets the e-mail <em>FROM</em> addresses to the array of <code>
     * String</code> provided in <em>from</em>.
     * @param from the list of e-mail addresses
     */
    public void addFrom(String[] from) {
        this.from = Utils.join(this.from, from);
    }


    // INTERNAL CLASSES
    
    
    /**
     *
     * @author mrelac
     * This class was created from org.emmanet.model.BibliosManager, which uses
     * Hibernate and the Spring Quartz scheduler. This class serves up the same
     * functionality (with the same name) but uses jdbc instead, with a main.
     */
    protected class BibliosManager implements BibliosManagerIO {

        /**
         * This method returns all of the rows from the <em>biblios</em> table whose
         * <em>updated</em> field value is not 'Y' (including <code>null</code>) and
         * whose <em>pubmed_id</em> field is not empty or <code>null</code>.
         * @return all of the rows from the <em>biblios</em> table whose
         * <em>updated</em> field value is not 'Y' (including <code>null</code>) and
         * whose <em>pubmed_id</em> field is not empty or <code>null</code>
         */
        @Override
        public List<BibliosDAO> getPubmedID() {
            ArrayList<BibliosDAO> results = new ArrayList<>();
            final String statement = 
                      "SELECT * FROM biblios\n"
                    + "WHERE (updated IS NULL OR updated != 'Y')\n"
                    + "  AND (pubmed_id IS NOT NULL AND pubmed_id != '')\n";

            try (
                Connection connection = Utils.connectDB();
                PreparedStatement preparedStatement = connection.prepareStatement(statement);
                ResultSet rs = preparedStatement.executeQuery();
            ) {
                while (rs.next()) {
                    BibliosDAO biblio = loadBibliosDao(rs);
                    results.add(biblio);
                }
            }
            catch (SQLException se) {
                System.out.println(se.getLocalizedMessage());
            }

            return results;
        }

        /**
         * This method returns a new <code>BibliosRawDAO</code> instance loaded with the
         * values from the given <code>ResultSet</code> object.
         * @param rs The <code>ResultSet</code> from which to load
         * @return a new <code>BibliosRawDAO</code> instance populated with the results
         */
        private BibliosDAO loadBibliosDao(ResultSet rs) {
            BibliosDAO bibliosDAO = new BibliosDAO();

            bibliosDAO.setId_biblio(Integer.parseInt(Utils.getDbValue(rs, "id_biblio")));
            bibliosDAO.setTitle(Utils.getDbValue(rs, "title"));
            bibliosDAO.setAuthor1(Utils.getDbValue(rs, "author1"));
            bibliosDAO.setAuthor2(Utils.getDbValue(rs, "author2"));
            bibliosDAO.setYear(Utils.getDbValue(rs, "year"));
            bibliosDAO.setJournal(Utils.getDbValue(rs, "journal"));
            bibliosDAO.setUsername(Utils.getDbValue(rs, "username"));
            bibliosDAO.setVolume(Utils.getDbValue(rs, "volume"));
            bibliosDAO.setPages(Utils.getDbValue(rs, "pages"));
            bibliosDAO.setPubmed_id(Utils.getDbValue(rs, "pubmed_id"));
            bibliosDAO.setUpdated(Utils.getDbValue(rs, "updated"));
            bibliosDAO.setLast_change(Utils.getDbValue(rs, "last_change"));
            bibliosDAO.setNotes(Utils.getDbValue(rs, "notes"));

            return bibliosDAO;
        }

        /**
         * Given a <code>BibliosRawDAO</code> object containing a valid id_biblio primary
         * key, this method updates the corresponding database record with the <em>
         * bibliosDAO</em> values.
         * @param bibliosDAO The DAO containing the primary key and the values to
         *                   be updated
         */
        @Override
        public void save(BibliosDAO bibliosDAO) {
            final String statement = 
                      "UPDATE biblios SET title = ?, author1 = ?, author2 = ?, year = ?, journal = ?, username = ?, "
                    + "volume = ?, pages = ?, pubmed_id = ?, updated = ?, last_change = ?, notes = ?"
                    + "WHERE id_biblio = ?";

            try (
                Connection connection = Utils.connectDB();
                PreparedStatement preparedStatement = connection.prepareStatement(statement);
            ) {
                preparedStatement.setString(1, bibliosDAO.getTitle());
                preparedStatement.setString(2, bibliosDAO.getAuthor1());
                preparedStatement.setString(3, bibliosDAO.getAuthor2());
                preparedStatement.setString(4, bibliosDAO.getYear());
                preparedStatement.setString(5, bibliosDAO.getJournal());
                preparedStatement.setString(6, bibliosDAO.getUsername());
                preparedStatement.setString(7, bibliosDAO.getVolume());
                preparedStatement.setString(8, bibliosDAO.getPages());
                preparedStatement.setInt(9, Utils.tryParseInt(bibliosDAO.getPubmed_id()));
                preparedStatement.setString(10, bibliosDAO.getUpdated());
                preparedStatement.setString(11, bibliosDAO.getLast_change());
                preparedStatement.setString(12, bibliosDAO.getNotes());
                preparedStatement.setInt(13, bibliosDAO.getId_biblio());

                preparedStatement.executeUpdate();
            }
            catch (SQLException se) {
                System.out.println(se.getLocalizedMessage());
            }
        }
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
    
    
}

