/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import org.apache.log4j.Logger;
import org.emmanet.healthcheck.EmmaBiblioValidator;
import org.emmanet.model.BibliosDAO;
import org.emmanet.model.BibliosManagerIO;
import org.emmanet.util.PubMed;
import org.emmanet.util.Utils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

/**
 *
 * @author ckchen
 * @author philw modified BY PHILW 08MAR2012 to run in Spring using quartz
 * scheduling Modified by philw 19122012 to use new webservice.
 */
public class EmmaBiblioJOB {
    private ApplicationContext ac;
    private JdbcTemplate jdbcTemplate;
    private PlatformTransactionManager jdbcTran;
    protected Logger logger = Logger.getLogger(EmmaBiblioJOB.class);
    private EmmaBiblioValidator validator;
    private BibliosManager bibliosManager;
    private ReloadableResourceBundleMessageSource messages;
    
    private final Object[] BEGIN = new Object[] { "BEGIN" };
    private final Object[] END = new Object[] { "END" };

    private static final String[] heading1 = { " id_biblio ", " pubmed_id ", " year ", " journal ", " volume ", " pages ", " username ", " updated ", " last_change ", " title ", " author1 ", " author2 " };
    private static final String[] heading2 = { " --------- ", " --------- ", " ---- ", " ------- ", " ------ ", " ----- ", " -------- ", " ------- ", " ----------- ", " ----- ", " ------- ", " ------- " };
    // Used for accumulating the maximum field widths for each update candidate for display purposes.
    private int[] maxWidths = new int[] {
          heading1[0].length()
        , heading1[1].length()
        , heading1[2].length()
        , heading1[3].length()
        , heading1[4].length()
        , heading1[5].length()
        , heading1[6].length()
        , heading1[7].length()
        , heading1[8].length()
        , heading1[9].length()
        , heading1[10].length()
        , heading1[11].length()
    };
    
    public EmmaBiblioJOB() {
        ac = new ClassPathXmlApplicationContext("/jobApplicationContext.xml");  // Get job application context.
        messages = (ReloadableResourceBundleMessageSource)ac.getBean("messageSource");    // Get message resource file.
        jdbcTemplate = (JdbcTemplate)ac.getBean("jdbcTemplate");                // Get JdbcTemplate.
        jdbcTran = (PlatformTransactionManager)ac.getBean("transactionManager");// Get JdbcTransactionManager.
        bibliosManager = new BibliosManager();
        validator = new EmmaBiblioValidator();
        logger.info(bibliosManager.getDbInfo());
    }
    
    // For passing args in Netbeans 7.3, see http://forums.netbeans.org/ptopic46048.html. Doesn't work as of 17-09-2013 (it is ignored).
    public static void main(String[] args) {
        EmmaBiblioJOB emmaBiblioJob = new EmmaBiblioJOB();
        int retVal = 0;
        OptionParser optionParser = new OptionParser();
        optionParser.accepts( "a" );                                            // validate all biblios.
        optionParser.accepts( "v" );                                            // validate update candidate biblios (whose 'updated' flag is not 'Y').
        optionParser.accepts( "u" );                                            // update candidate biblios that pass validation.

        OptionSet options = optionParser.parse(args);
        if (options.has("a"))
            retVal = Math.max(retVal, emmaBiblioJob.validateAll());
        
        if (options.has("v"))
            retVal = Math.max(retVal, emmaBiblioJob.validateUpdateCandidates());
        
        if (options.has("u"))
            retVal = Math.max(retVal, emmaBiblioJob.update());
        
        if ( ! ((options.has("a")) || (options.has("v")) || (options.has("u"))))
            usage();

        System.exit(retVal);
    }

    /**
     * Given a pubmed_id which may or may not be valid, this method uses the
     * pubmed id web service to try to look up the supplied value.
     * 
     * @param pubmed_id the pubmed_id
     * 
     * @return
     * <ul>
     *   <li>If the pubmed_id is found:
     *     <ul>
     *       <li>the matching bibliographic information is returned</li>
     *       <li><code>FetchBiblio.status</code> is set to <code>OK</code></li>
     *       <li><code>FetchBiblio.errorMessage</code> is set to an empty string</li>
     *     </ul>
     *   </li>
     *   <li>If the pubmed_id is <i>not</i> found:
     *     <ul>
     *       <li>no bibliographic information is returned</li>
     *       <li><code>FetchBiblio.status</code> is set to <code>NOT_FOUND</code></li>
     *       <li><code>FetchBiblio.errorMessage</code> is set to an empty string</li>
     *     </ul>
     *   </li>
     *   <li>If the pubmed_id web service throws an exception:
     *     <ul>
     *       <li>no bibliographic information is returned</li>
     *       <li><code>FetchBiblio.status</code> is set to <code>FAIL</code></li>
     *       <li><code>FetchBiblio.errorMessage</code> is set to the exception's localized message.</li>
     *     </ul>
     *   </li>
     * </ul>
     */
    public FetchBiblio fetchPaper(int pubmed_id) {
        FetchBiblio paper = new FetchBiblio();
        
        PubMed pubmed = new PubMed(pubmed_id);
        if (pubmed.isValid()) {
            paper.paperid = pubmed.getPubmedId();
            paper.title = (pubmed.getTitle());
            paper.year = pubmed.getYear();
            paper.journal = pubmed.getJournal();
            paper.volume = pubmed.getVolume();
            paper.issue = pubmed.getIssue();
            paper.pages = pubmed.getPages();
            paper.author1 = pubmed.getAuthor1();
            paper.author2 = pubmed.getOtherAuthors();
            paper.authors = pubmed.getAuthorsString();
            paper.status = OK;
            paper.errorMessage = "";
        } else {            
            logger.info("No pubmed_id found for value " + pubmed_id + ".");
            paper.status = NOT_FOUND;
            paper.errorMessage = "";
        }

        return paper;
    }
    
    public String getDbInfo() {
        return bibliosManager.getDbInfo();
    }
    
    /**
     * Updates the database with the given biblios. Since each biblio is not
     * related to its sibling, we commit after each update.
     * 
     * @param biblioList The biblios to be udpated
     * @return the number of biblio records updated
     */
    public int save(final List<BibliosDAO> biblioList) {
        int count = 0;
        for (BibliosDAO biblio : biblioList) {
            DefaultTransactionDefinition paramTransactionDefinition = new DefaultTransactionDefinition();
            TransactionStatus status = jdbcTran.getTransaction(paramTransactionDefinition);
            bibliosManager.save(biblio);
            jdbcTran.commit(status);
            count++;
        }
        
        return count;
    }
    
    /**
     * Validates all biblios.
     * 
     * @return 0 if there are no validation errors; 1 otherwise.
     */
    public int validateAll() {
        String logMsg = messages.getMessage("Biblios.ValidateAll", BEGIN, Locale.UK);
        logger.info(logMsg);
        
        List<BibliosDAO> bibliosList = bibliosManager.getAllBiblios();
        Map<Integer, BibliosDAO> errorMap = validator.validate(bibliosList);
        
        logMsg = messages.getMessage("Biblios.ValidateAll", END, Locale.UK);
        logger.info(logMsg);
        
        return (errorMap.isEmpty() ? 0 : 1);
    }
    
    /**
     * This method gets the list of update candidates, validates it, removes
     * any failed candidates from the update list, then updates the database
     * with the [validated] update candidate list.
     * 
     * @return The number of records updated
     */
    public int validateAndUpdateCandidates() {
        int updateCount = 0;
        List<BibliosDAO> bibliosList = bibliosManager.getUpdateCandidateBiblios();  // Get update candidates.
        Map<Integer, BibliosDAO> errorMap = validator.validate(bibliosList);        // Validate the list.
        
        if ( ! bibliosList.isEmpty()) {                                             // If there are update candidates ...
            if ( ! errorMap.isEmpty()) {                                            //     If there were validation errors ...
                Iterator<BibliosDAO> iterator = bibliosList.iterator();
                while (iterator.hasNext()) {                                        //     ... remove the failed candidates from the update list.
                    BibliosDAO biblio = iterator.next();
                    int pk = biblio.getId_biblio();
                    if (errorMap.containsKey(pk)) {
                        Object[] parms = new Object[] { biblio.getId_biblio() };
                        String logMsg = messages.getMessage("Biblios.RemoveFailedCandidate", parms, Locale.UK);
                        logger.warn(logMsg);
                        iterator.remove();
                    }
                }
            }
            
            bibliosList = updateBibliosFromWebService(bibliosList);             // Update any biblios with valid pubmed_ids from web service.
            updateCount = save(bibliosList);                                    // Save the changes.
        }

        return updateCount;
    }

    /**
     * Validates update candidate biblios (biblios whose <code>update</code>
     * database field is either null or is not equal to 'Y')
     * 
     * @return 0 if there are no validation errors; 1 otherwise.
     */
    public int validateUpdateCandidates() {
        String logMsg = messages.getMessage("Biblios.ValidateUpdateCandidates", BEGIN, Locale.UK);
        logger.info(logMsg);
        
        List<BibliosDAO> bibliosList = bibliosManager.getUpdateCandidateBiblios();
        Map<Integer, BibliosDAO> errorMap = validator.validate(bibliosList);
        
        logMsg = messages.getMessage("Biblios.ValidateUpdateCandidates", END, Locale.UK);
        logger.info(logMsg);
        
        return (errorMap.isEmpty() ? 0 : 1);
    }

    
    // PRIVATE METHODS
    
    
    /**
     * This method computes the maximum field widths for all logged fields. It
     * is needed for proper logfile formatting output for correctly aligning the
     * old and new <code>BibliosDAO</code> records.
     * @param oldBiblio the old [existing] biblio record
     * @param newBiblio the new biblio record
     * @param allowForQuotes If true, two extra bytes are added to each required 
     *        field length to account for leading and trailing quotation marks.
     */
    private void accumulateMaxWidths(BibliosDAO oldBiblio, BibliosDAO newBiblio) {
        String sId_biblio = Integer.toString(oldBiblio.getId_biblio());
        
        // Add two bytes to each biblio field (old and new) to account for leading and trailing quotes.
        maxWidths[0] = Math.max(Math.max(sId_biblio.length(), heading1[0].length()), maxWidths[0]);
        maxWidths[1] = Math.max(Math.max(Math.max(oldBiblio.getPubmed_id().length() + 2, newBiblio.getPubmed_id().length() + 2), heading1[1].length()), maxWidths[1]);
        maxWidths[2] = Math.max(Math.max(Math.max(oldBiblio.getYear().length() + 2, newBiblio.getYear().length() + 2), heading1[2].length()), maxWidths[2]);
        maxWidths[3] = Math.max(Math.max(Math.max(oldBiblio.getJournal().length() + 2, newBiblio.getJournal().length() + 2), heading1[3].length()), maxWidths[3]);
        maxWidths[4] = Math.max(Math.max(Math.max(oldBiblio.getVolume().length() + 2, newBiblio.getVolume().length() + 2), heading1[4].length()), maxWidths[4]);
        maxWidths[5] = Math.max(Math.max(Math.max(oldBiblio.getPages().length() + 2, newBiblio.getPages().length() + 2), heading1[5].length()), maxWidths[5]);
        maxWidths[6] = Math.max(Math.max(Math.max(oldBiblio.getUsername().length() + 2, newBiblio.getUsername().length() + 2), heading1[6].length()), maxWidths[6]);
        maxWidths[7] = Math.max(Math.max(Math.max(oldBiblio.getUpdated().length() + 2, newBiblio.getUpdated().length() + 2), heading1[7].length()), maxWidths[7]);
        maxWidths[8] = Math.max(Math.max(Math.max(oldBiblio.getLast_change().length() + 2, newBiblio.getLast_change().length() + 2), heading1[8].length()), maxWidths[8]);
        maxWidths[9] = Math.max(Math.max(Math.max(oldBiblio.getTitle().length() + 2, newBiblio.getTitle().length()), heading1[9].length() + 2), maxWidths[9]);
        maxWidths[10] = Math.max(Math.max(Math.max(oldBiblio.getAuthor1().length() + 2, newBiblio.getAuthor1().length()), heading1[10].length() + 2), maxWidths[10]);
        maxWidths[11] = Math.max(Math.max(Math.max(oldBiblio.getAuthor2().length() + 2, newBiblio.getAuthor2().length()), heading1[11].length() + 2), maxWidths[11]);
    }

    /**
     * Given a <code>List&lt;BibliosDAO&gt;</code>, this method returns a new
     * list, which is a copy of the original list, with updates applied from the
     * biblios web service for valid pubmed_id values.
     * 
     * @param oldBiblioList the original list of biblios
     * @return a new list, which is a copy of the original list, with updates
     * applied from the biblios web service for valid pubmed_id values
     */
    private List<BibliosDAO> createNewBiblioListFromWebService(List<BibliosDAO> oldBiblioList) {
        String volume;
        List<BibliosDAO> newBiblioList = new ArrayList<>();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String last_change = dateFormat.format(new Date());
                    
        for (BibliosDAO oldBiblio : oldBiblioList) {
            BibliosDAO newBiblio = new BibliosDAO();                            // Clone the original BibliosDAO.
            newBiblio.setAuthor1(oldBiblio.getAuthor1());
            newBiblio.setAuthor2(oldBiblio.getAuthor2());
            newBiblio.setId_biblio(oldBiblio.getId_biblio());
            newBiblio.setJournal(oldBiblio.getJournal());
            newBiblio.setLast_change(last_change);
            newBiblio.setNotes(oldBiblio.getNotes());
            newBiblio.setPages(oldBiblio.getPages());
            newBiblio.setPubmed_id(oldBiblio.getPubmed_id());
            newBiblio.setTitle(oldBiblio.getTitle());
            newBiblio.setUpdated(oldBiblio.getUpdated());
            newBiblio.setUsername(oldBiblio.getUsername());
            newBiblio.setVolume(oldBiblio.getVolume());
            newBiblio.setYear(oldBiblio.getYear());
            
            Integer pubmed_id = Utils.tryParseInt(oldBiblio.getPubmed_id());    // If we have a valid pubmed_id, load the new BibliosDAO with the paper data.
            if (pubmed_id != null) {
                FetchBiblio paperFromWs = fetchPaper(pubmed_id);
                if (paperFromWs != null) {
                    if ( ! paperFromWs.issue.isEmpty()) {
                        volume = paperFromWs.volume + "(" + paperFromWs.issue + ")";
                    } else {
                        volume = paperFromWs.volume;
                    }
                    
                    newBiblio.setTitle(paperFromWs.title);
                    newBiblio.setAuthor1(paperFromWs.author1);
                    newBiblio.setAuthor2(paperFromWs.author2);
                    newBiblio.setYear("" + paperFromWs.year);
                    newBiblio.setJournal(paperFromWs.journal);
                    newBiblio.setVolume(volume);
                    newBiblio.setPages(paperFromWs.pages);
                    newBiblio.setUsername("EMMA");
                    newBiblio.setUpdated("Y");
                    
                    newBiblioList.add(newBiblio);
                }
            }
        }
        
        return newBiblioList;
    }

    /**
     * Returns the format string for displaying old and new biblio records.
     * Each field is sized to the widest record value in the record set.
     * @return 
     */
    private String getFormatString() {
        String formatString =
             "     %-" + maxWidths[0] + "s  "
                + "%-" + maxWidths[1] + "s  "
                + "%-" + maxWidths[2] + "s  "
                + "%-" + maxWidths[3] + "s  "
                + "%-" + maxWidths[4] + "s  "
                + "%-" + maxWidths[5] + "s  "
                + "%-" + maxWidths[6] + "s  "
                + "%-" + maxWidths[7] + "s  "
                + "%-" + maxWidths[8] + "s  "
                + "%-" + maxWidths[9] + "s  "
                + "%-" + maxWidths[10] + "s  "
                + "%-" + maxWidths[11] + "s  ";
        
        return formatString;
    }
    
    /**
     * This method logs the old and new biblio record changes.
     * Assumption: the old and new lists are of equal size and are both ordinally
     * identical (e.g. old[0] and new[0] describe the same base biblio record)
     * @param oldBiblioList old biblio list
     * @param newBiblioList new biblio list
     */
    private void logChanges(List<BibliosDAO> oldBiblioList, List<BibliosDAO> newBiblioList) {
        BibliosDAO[] oldBiblioArray = new BibliosDAO[oldBiblioList.size()];
        BibliosDAO[] newBiblioArray = new BibliosDAO[newBiblioList.size()];
        
        // Compute the maximum size of each biblio column for logging alignment.
        for (int i = 0; i < oldBiblioArray.length; i++) {                       
            oldBiblioArray[i] = patchNullFields(oldBiblioList.get(i));
            newBiblioArray[i] = patchNullFields(newBiblioList.get(i));
            accumulateMaxWidths(oldBiblioArray[i], newBiblioArray[i]);
        }
        // Log the heading.
        String formatString = getFormatString();
        String formattedRecord;
        formattedRecord = String.format(
                  formatString
                , heading1[0]
                , heading1[1]
                , heading1[2]
                , heading1[3]
                , heading1[4]
                , heading1[5]
                , heading1[6]
                , heading1[7]
                , heading1[8]
                , heading1[9]
                , heading1[10]
                , heading1[11]
                );
        logger.info(formattedRecord);
        formattedRecord = String.format(
                  formatString
                , heading2[0]
                , heading2[1]
                , heading2[2]
                , heading2[3]
                , heading2[4]
                , heading2[5]
                , heading2[6]
                , heading2[7]
                , heading2[8]
                , heading2[9]
                , heading2[10]
                , heading2[11]
                );
        logger.info(formattedRecord);
        
        // Log each of the old and new records.
        for (int i = 0; i < oldBiblioArray.length; i++) {
            formattedRecord = String.format(
                      formatString
                    , " " + oldBiblioArray[i].getId_biblio() + " "
                    , "\"" + oldBiblioArray[i].getPubmed_id() + "\""
                    , "\"" + oldBiblioArray[i].getYear() + "\""
                    , "\"" + oldBiblioArray[i].getJournal() + "\""
                    , "\"" + oldBiblioArray[i].getVolume() + "\""
                    , "\"" + oldBiblioArray[i].getPages() + "\""
                    , "\"" + oldBiblioArray[i].getUsername() + "\""
                    , "\"" + oldBiblioArray[i].getUpdated() + "\""
                    , "\"" + oldBiblioArray[i].getLast_change() + "\""
                    , "\"" + oldBiblioArray[i].getTitle() + "\""
                    , "\"" + oldBiblioArray[i].getAuthor1() + "\""
                    , "\"" + oldBiblioArray[i].getAuthor2() + "\""
                    );
            formattedRecord = formattedRecord.replaceFirst("     ", "Old: ");
            logger.info(formattedRecord);

            formattedRecord = String.format(
                      formatString
                    , " " + newBiblioArray[i].getId_biblio() + " "
                    , "\"" + newBiblioArray[i].getPubmed_id() + "\""
                    , "\"" + newBiblioArray[i].getYear() + "\""
                    , "\"" + newBiblioArray[i].getJournal() + "\""
                    , "\"" + newBiblioArray[i].getVolume() + "\""
                    , "\"" + newBiblioArray[i].getPages() + "\""
                    , "\"" + newBiblioArray[i].getUsername() + "\""
                    , "\"" + newBiblioArray[i].getUpdated() + "\""
                    , "\"" + newBiblioArray[i].getLast_change() + "\""
                    , "\"" + newBiblioArray[i].getTitle() + "\""
                    , "\"" + newBiblioArray[i].getAuthor1() + "\""
                    , "\"" + newBiblioArray[i].getAuthor2() + "\""
                    );
            formattedRecord = formattedRecord.replaceFirst("     ", "New: ");
            logger.info(formattedRecord + "\n");
        }
    }
    
    /**
     * This method replaces any null <code>BibliosDAO</code> fields with the
     * string '&lt;null&gt;' so that proper length computations and display can
     * be performed against null fields. A throw-away copy of the biblio is
     * returned and may be used for field length computations and display without
     * fear of null pointer exceptions.
     *
     * @param biblio the biblio record to be patched
     * @return a new <code>BibliosDAO</code> instance with any null fields replaced
     * by the string '&lt;null&gt;'
     */
    private BibliosDAO patchNullFields(BibliosDAO biblio) {
        BibliosDAO newBiblio = new BibliosDAO();
        
        newBiblio.setId_biblio(biblio.getId_biblio());
        newBiblio.setPubmed_id(biblio.getPubmed_id() == null ? "<null>" : biblio.getPubmed_id());
        newBiblio.setYear(biblio.getYear() == null ? "<null>" : biblio.getYear());
        newBiblio.setJournal(biblio.getJournal() == null ? "<null>" : biblio.getJournal());
        newBiblio.setVolume(biblio.getVolume() == null ? "<null>" : biblio.getVolume());
        newBiblio.setPages(biblio.getPages() == null ? "<null>" : biblio.getPages());
        newBiblio.setUsername(biblio.getUsername() == null ? "<null>" : biblio.getUsername());
        newBiblio.setUpdated(biblio.getUpdated() == null ? "<null>" : biblio.getUpdated());
        newBiblio.setLast_change(biblio.getLast_change() == null ? "<null>" : biblio.getLast_change());
        newBiblio.setTitle(biblio.getTitle() == null ? "<null>" : biblio.getTitle());
        newBiblio.setAuthor1(biblio.getAuthor1() == null ? "<null>" : biblio.getAuthor1());
        newBiblio.setAuthor2(biblio.getAuthor2() == null ? "<null>" : biblio.getAuthor2());
        
        return newBiblio;
    }
    
    /**
     * Parameterless method that validates and updates the database with the
     * valid update candidates.
     * 
     * @return 0 (typically passed on to the java exec command-line invoker)
     */
    private int update() {
        Object[] parms;
        String logMsg = messages.getMessage("Biblios.Update", BEGIN, Locale.UK);
        logger.info(logMsg);
        
        int updateCount = validateAndUpdateCandidates();
        parms = new Object[] { updateCount };
        logMsg = messages.getMessage("Biblios.UpdateCount", parms, Locale.UK);
        logger.info(logMsg);
        
        logMsg = messages.getMessage("Biblios.Update", END, Locale.UK);
        logger.info(logMsg);
        
        return 0;
    }
     
    /**
     * Given a list of biblios typically extracted from the database, this
     * method updates each biblio <i>record</i> that has a valid pubmed_id 
     * (NOTE: does NOT update the database) with latest biblio information as
     * reported by the web service, which is the definitive biblio source.
     * 
     * @param biblioList The <code>BibliosDAO</code> instances to be updated
     * @return the updated list of biblios
     */
    private List<BibliosDAO> updateBibliosFromWebService(List<BibliosDAO> biblioList) {
        if ((biblioList == null) || (biblioList.isEmpty()))
            return new ArrayList<>();
        
        List<BibliosDAO> newBiblioList = createNewBiblioListFromWebService(biblioList);
        
        // Log the changes about to be made.
        logChanges(biblioList, newBiblioList);
        
        return newBiblioList;
    }

    /**
     * Displays the 'usage' message.
     */
    private static void usage() {
        String USAGE = "Usage: " + EmmaBiblioJOB.class.getName() + " [-v] [-a] [-u]"
                + "\n\n-a: validate all biblios"
                + "\n-v: validate biblio update candidates"
                + "\n-u: update valid biblio candidates\n";
        System.out.println(USAGE);
    }


    // CLASSES
    
    
    /**
     * @author mrelac
     * This class was created from org.emmanet.model.BibliosManager, which uses
     * Hibernate and the Spring Quartz scheduler. This class serves up the same
     * functionality (with the same name) but uses jdbc instead, with a main.
     */
    protected class BibliosManager implements BibliosManagerIO {

        /**
         * This method returns all of the rows from the <em>biblios</em> table.
         * @return all of the rows from the <em>biblios</em> table
         */
        public List<BibliosDAO> getAllBiblios() {
            final String statement = "SELECT * FROM biblios\n";
            
            return jdbcTemplate.query(statement, new BibliosDAO());
        }

        /**
         * Return interesting database information.
         * @return interesting database information
         */
        public String getDbInfo() {
            String statement = "SELECT database() AS dbname, @@hostname, @@port, @@version\n";
            SqlRowSet rs = jdbcTemplate.queryForRowSet(statement);
            if (rs.next()) {
                String dbname = (String)rs.getObject("dbname").toString();
                String hostname = (String)rs.getObject("@@hostname");
                String port = rs.getObject("@@port").toString();
                String version = (String)rs.getObject("@@version");
                return ("Database " + dbname + ": hostname: " + hostname + ". Database version: " + version + ". Database port: " + port + ".");
            }

            return "";
        }
        
        /**
         * This method returns all of the rows from the <em>biblios</em> table whose
         * <em>updated</em> field value is not 'Y' (including <code>null</code>) and
         * whose <em>pubmed_id</em> field is not empty or <code>null</code>.
         * @return all of the rows from the <em>biblios</em> table whose
         * <em>updated</em> field value is not 'Y' (including <code>null</code>) and
         * whose <em>pubmed_id</em> field is not empty or <code>null</code>
         * 
         * NOTE: There is a unique constraint on pubmed_id (see the index); thus
         *       we must exclude all biblios with null/empty pubmed_id's.
         */
        @Override
        public List<BibliosDAO> getUpdateCandidateBiblios() {
            final String statement = 
                      "SELECT * FROM biblios\n"
                    + "WHERE (updated IS NULL OR updated != 'Y')\n"
                    + "  AND (pubmed_id IS NOT NULL AND pubmed_id != '')\n";
            
            return jdbcTemplate.query(statement, new BibliosDAO());
        }
        
        /**
     * Save the given biblio record in the database. Transaction management is
     * the caller's responsibility.
         * 
         * @param bibliosDAO the data to be saved
         */
        @Override
        public void save(BibliosDAO bibliosDAO) {
            java.sql.Date sqlDate = Utils.tryParseToDbDate(bibliosDAO.getLast_change());
            
            final String statement = 
                      "UPDATE biblios SET title = ?, author1 = ?, author2 = ?, year = ?, journal = ?, username = ?, "
                    + "volume = ?, pages = ?, pubmed_id = ?, updated = ?, last_change = ?, notes = ?"
                    + "WHERE id_biblio = ?";
            
            jdbcTemplate.update(statement, new Object[] { 
                bibliosDAO.getTitle()
              , bibliosDAO.getAuthor1()
              , bibliosDAO.getAuthor2()
              , bibliosDAO.getYear()
              , bibliosDAO.getJournal()
              , bibliosDAO.getUsername()
              , bibliosDAO.getVolume()
              , bibliosDAO.getPages()
              , bibliosDAO.getPubmed_id()
              , bibliosDAO.getUpdated()
              , sqlDate
              , bibliosDAO.getNotes()
              , bibliosDAO.getId_biblio()
            });
        }
    }
     
    public static final int OK = 0;
    public static final int NOT_FOUND = -1;
    public static final int FAIL = -2;

    /**
     * This class is a DTO used to transfer data between the biblio web service
     * and its clients.
     */
    public class FetchBiblio {
        public String title;
        public String author1;
        public String author2;
        public String authors;              // A concatenation of author1 and author2 [e.g. all other authors].
        public String journal;
        public String volume;
        public String issue;
        public String pages;
        public String paperid;
        public int year;
        public int status = NOT_FOUND;
        public String errorMessage = "";
    }

    
}