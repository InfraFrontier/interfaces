package org.emmanet.controllers;

/**
 *
 * @author phil
 */
import java.io.UnsupportedEncodingException;
import java.net.BindException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.velocity.app.VelocityEngine;
import org.emmanet.jobs.WebRequests;
import org.emmanet.model.AllelesDAO;
import org.emmanet.model.ArchiveDAO;
import org.emmanet.model.ArchiveManager;
import org.emmanet.model.BackgroundManager;
import org.emmanet.model.BibliosDAO;
import org.emmanet.model.BibliosManager;
import org.emmanet.model.BibliosStrainsDAO;
import org.emmanet.model.CVRtoolsDAO;
import org.emmanet.model.CategoriesStrainsDAO;
import org.emmanet.model.GenesDAO;
import org.emmanet.model.LaboratoriesManager;
import org.emmanet.model.LabsDAO;
import org.emmanet.model.MutationsDAO;
import org.emmanet.model.MutationsManager;
import org.emmanet.model.MutationsStrainsDAO;
import org.emmanet.model.PeopleDAO;
import org.emmanet.model.PeopleManager;
import org.emmanet.model.ProjectsStrainsDAO;
import org.emmanet.model.ProjectsStrainsManager;
import org.emmanet.model.RToolsDAO;
import org.emmanet.model.RToolsManager;
import org.emmanet.model.ResiduesDAO;
import org.emmanet.model.ResiduesManager;
import org.emmanet.model.SourcesStrainsManager;
import org.emmanet.model.Sources_StrainsDAO;
import org.emmanet.model.StrainsDAO;
import org.emmanet.model.StrainsManager;
import org.emmanet.model.SubmissionBibliosDAO;
import org.emmanet.model.SubmissionMutationsDAO;
import org.emmanet.model.SubmissionsDAO;
import org.emmanet.model.SubmissionsManager;
import org.emmanet.model.Syn_StrainsDAO;
import org.emmanet.model.Syn_StrainsManager;
import org.emmanet.util.Encrypter;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractWizardFormController;
import org.json.simple.*;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.ui.velocity.VelocityEngineUtils;

public class SubmissionFormController extends AbstractWizardFormController {

    private List cvDAO;
    private HttpSession session;
    private JSONObject obj;
    private List JSONobjects = new LinkedList();
    private WebRequests wr;
    private Encrypter encrypter = new Encrypter();
    private List stepTitles;
    private JavaMailSender javaMailSender;
    private VelocityEngine velocityEngine;
    public SubmissionFormController() {
        setCommandName("command");
        setAllowDirtyBack(true);
        setAllowDirtyForward(true);
    }

    @Override
    protected Object formBackingObject(HttpServletRequest request) {

        StrainsDAO sd = new StrainsDAO();
        SubmissionsDAO sda = new SubmissionsDAO();//#################
        wr = new WebRequests();

        //////////////////////////////////////////////
        BackgroundManager bm = new BackgroundManager();
        String getprev = "";
        ////////////////////////////////////////////

        session = request.getSession(true);
//total steps in wizard for use in view
        int iPageCount = getPageCount() - 1;
        session.setAttribute("totalStepCount", "" + iPageCount);
        session.setAttribute("stepTitles", "" + stepTitles);
        sda.setCvDAO(wr.isoCountries());
        sd.setPeopleDAO(new PeopleDAO());
        sd.getPeopleDAO().setLabsDAO(new LabsDAO());

        /*   if (request.getParameter("getprev") != null && !request.getParameter("getprev").isEmpty()) {
         Encrypter encrypter = new Encrypter();
         try {
         // Encrypt
         //String encrypted = encrypter.encrypt(request.getParameter("getprev"));
         // System.out.println("ENCRYPTEDSTRING==" + encrypted);
        
         // Decrypt
         String decrypted = encrypter.decrypt(request.getParameter("getprev"));
        
         //System.out.println("DECRYPTEDSTRING==" + decrypted);
         } catch (Exception e) {
         }*/

        encrypter = new Encrypter();
        //#####sda = new SubmissionsDAO();

        SubmissionsManager sm = new SubmissionsManager();

        /*/decode
         try {
         getprev = java.net.URLDecoder.decode(request.getParameter("getprev"), "UTF-8");
         } catch (UnsupportedEncodingException ex) {
         Logger.getLogger(RequestFormController.class.getName()).log(Level.SEVERE, null, ex);
         }
         System.out.println("decoded==" + request.getParameter("getprev") + "--" + getprev);
         getprev = encrypter.decrypt(request.getParameter("getprev"));
         int idDecrypt = Integer.parseInt(getprev);
         System.out.println("decrypted id_str==" + idDecrypt);
        
         sda = sm.getSubByID(idDecrypt);///decrypt here
        
         }*/

        if (request.getParameter("getprev") != null) {
            String idDecrypt = encrypter.decrypt(request.getParameter("getprev"));
            System.out.println("g e t p r e v::" + idDecrypt);
            sda = sm.getSubByID(Integer.parseInt(idDecrypt));
            System.out.println("SUBID::FBO2 = " + sda.getEncryptedId_sub());
        }

        sda.setCvDAO(wr.isoCountries());


        session.setAttribute("backgroundsDAO", bm.getCuratedBackgrounds());
        sda.setBgDAO(bm.getCuratedBackgrounds());
        //session.setAttribute("categoriesDAO", sm.getCategories() );
        sda.setCatDAO(sm.getCategories());

        return sda;
    }

    @Override
    protected Map referenceData(HttpServletRequest request,
            Object command,
            Errors errors,
            int page)
            throws Exception {
        SubmissionsDAO sda = (SubmissionsDAO) command;
        SubmissionsManager sm = new SubmissionsManager();
        encrypter = new Encrypter();

        if (request.getParameter("getprev") != null) {
            String idDecrypt = encrypter.decrypt(request.getParameter("getprev"));
            System.out.println("g e t p r e v::" + idDecrypt);
            sda = sm.getSubByID(Integer.parseInt(idDecrypt));

            System.out.println("SUBID::FBO = " + sda.getEncryptedId_sub());
        }



        Map refData = new HashMap();
        int pageCount = page;
        session.setAttribute("pageCount", "" + pageCount);
        session.setAttribute("userdaos", null);
        PeopleDAO pd;
        PeopleManager pm;
        List people;
        List peopleDAOs;
        System.out.println("SUBID:: = " + sda.getId_sub());
        switch (page) {
            case 0: //if page 1 , do summat
                sda.setStep("0");
//sm.save(sda);
                break;
            case 1: //if page 2 do summat
//sm.save(sda);
                sda.setStep("1");
                break;
            case 2:
                encrypter = new Encrypter();
//System.out.println("toencrypt==" + sda.getId_sub());
                // Encrypt
                //String encrypted = encrypter.encrypt(sda.getId_sub());

                if (sda.getSubmitter_email()/*.getPeopleDAO().getEmail()*/ != null) {
                    wr = new WebRequests();
                    SubmissionsDAO prevSub = new SubmissionsDAO();
                    List submitterDAO = new LinkedList();
                    System.out.println(" email for prevsub = " + sda.getSubmitter_email().toString());
                    /*prevSub*/ submitterDAO = sm.getSubsByEmail(sda.getSubmitter_email().toString());
                    if (/*prevSub*/submitterDAO != null) {
                        for (Iterator it = submitterDAO.listIterator(); it.hasNext();) {
                            prevSub = (SubmissionsDAO) it.next();
                            System.out.println("TIMESTAMP :: " + prevSub.getTimestamp());

                            encrypter = new Encrypter();

                            // Encrypt
                            String encrypted = encrypter.encrypt(prevSub.getId_sub());
                            if (encrypted.isEmpty() || encrypted != null) {
                                session.setAttribute("getprev", encrypted);
                            } else {
                                //TODO need to come up with something to use as a prefix for files etc if decryptions errors out, usually because of + chars in the encrypte string
                            }


                            /*      try {
                             encrypted = java.net.URLEncoder.encode(encrypted, "UTF-8");
                             } catch (UnsupportedEncodingException ex) {
                             Logger.getLogger(RequestFormController.class.getName()).log(Level.SEVERE, null, ex);
                             }*/

                            prevSub.setEncryptedId_sub(encrypted);
                            prevSub.setCvDAO(wr.isoCountries());
                            request.setAttribute("previousSub", prevSub);
                        }
                    }
                    //request.setAttribute("previousSub", /*prevSub*/);
                    //LOOK TO PULL USER/LAB DATA TO POPULATE STRAINS
                    pd = new PeopleDAO();
                    pm = new PeopleManager();
                    people = pm.getPeopleByEMail(sda.getSubmitter_email()/*sd.getPeopleDAO().getEmail()*/);
                    peopleDAOs = new LinkedList();
                    if (people.isEmpty()) {
                        //OK no email address registered most likely a new user so let them submit their details
                        //need to populate a new people entry to grab per_id_per
                    } else {
                        //OK email exists and likely user so present a view of user'lab details to choose from
                        //NEED TO PULL LAST SUBMISSION FROM DATABASE SUBMISSIONS TABLE USING E-MAIL SUBMITTER ADDRESS
                        //RETURN TO USER TO CHOOSE WHETHER TO RE-USE DATA.
                        // sda=submissions manager.getlast submission.

                        System.out.println("PERSON LIST SIZE " + people.size());
                        JSONobjects = new LinkedList();
                        for (Iterator it = people.listIterator(); it.hasNext();) {
                            pd = (PeopleDAO) it.next();
                            System.out.println("A U T H O R I T Y  : :  " + pd.getLabsDAO().getAuthority());
                            //RESTRICT PEOPLE LISTING ONLY TO AUTHORISED LIST
                            if (pd.getLabsDAO().getAuthority() != null) {
                                peopleDAOs.add(pd);
                            }
                            // peopleDAOs.add(pd);
                        }
                        /*session*/ request.setAttribute("userdaos", peopleDAOs);

                        // refData.put("user", peopleDAOs);
                    }
                }

                System.out.println("STEP==" + sda.getStep());
                sda.setStep("2");
                //sm.save(sda);
                break;
            case 3:


                if (sda.getProducer_email()/*.getPeopleDAO().getEmail()*/ != null) {
                    //LOOK TO PULL USER/LAB DATA TO POPULATE STRAINS
                    pd = new PeopleDAO();
                    pm = new PeopleManager();
                    people = pm.getPeopleByEMail(sda.getProducer_email()/*.getPeopleDAO().getEmail()*/);
                    peopleDAOs = new LinkedList();
                    if (people.isEmpty()) {
                        //OK no email address registered most likely a new user so let them submit their details
                    } else {
                        //OK email exists and likely user so present a view of user'lab details to choose from
                        System.out.println("PERSON LIST SIZE " + people.size());
                        JSONobjects = new LinkedList();
                        for (Iterator it = people.listIterator(); it.hasNext();) {
                            pd = (PeopleDAO) it.next();
                            peopleDAOs.add(pd);
                        }
                        session.setAttribute("pidaos", peopleDAOs);
                        //refData.put("pi", peopleDAOs);
                    }
                }
                sda.setStep("3");
                sm.save(sda);
                break;

            case 4:


                if (sda.getShipper_email()/*.getPeopleDAO().getEmail()*/ != null) {
                    //LOOK TO PULL USER/LAB DATA TO POPULATE STRAINS
                    pd = new PeopleDAO();
                    pm = new PeopleManager();
                    people = pm.getPeopleByEMail(sda.getShipper_email()/*.getPeopleDAO().getEmail()*/);
                    peopleDAOs = new LinkedList();
                    if (people.isEmpty()) {
                        //OK no email address registered most likely a new user so let them submit their details
                    } else {
                        //OK email exists and likely user so present a view of user'lab details to choose from
                        System.out.println("PERSON LIST SIZE " + people.size());
                        JSONobjects = new LinkedList();
                        for (Iterator it = people.listIterator(); it.hasNext();) {
                            pd = (PeopleDAO) it.next();
                            peopleDAOs.add(pd);
                        }
                        session.setAttribute("pidaos", peopleDAOs);
                        //refData.put("pi", peopleDAOs);
                    }
                }
                sda.setStep("4");
                sm.save(sda);
                break;


            case 5:

                encrypter = new Encrypter();

                // Encrypt
                String encrypted = encrypter.encrypt(sda.getId_sub());
                session.setAttribute("getprev", encrypted);
 
                sda.setStep("5");
                sm.save(sda);
                break;

            case 6:
                sda.setStep("6");
                sm.save(sda);
                break;
            case 7:
                System.out.println("DAO ref==" + sda.hashCode());
                if (request.getParameter("getprev") != null && !request.getParameter("getprev").isEmpty()) {
                    // sda = new SubmissionsDAO();
                    sda = previousSubmission(sda, request.getParameter("getprev"));
                    //command..equals(sda);
                    sda = (SubmissionsDAO) command;
                    System.out.println("DAO ref2==" + sda.hashCode());
                    System.out.println("new ID sub==" + sda.getId_sub());
                    //sda.setStep("7");
                    //sm.save(sda);
                } else {
                    sda.setStep("7");
                    sm.save(sda);
                }
                break;

            case 8:

                sda.setStep("8");
                sm.save(sda);
                break;

            case 9:

                sda.setStep("9");
                sm.save(sda);
                break;

            case 10:

                CVRtoolsDAO rt = new CVRtoolsDAO();
                StrainsManager smg = new StrainsManager();
                List rtl = new LinkedList();
                List CVRtoolsDAO = new LinkedList();
                rtl = smg.getRTools();//.getRToolsDAO();

                for (Iterator it = rtl.listIterator(); it.hasNext();) {
                    rt = (CVRtoolsDAO) it.next();
                    if (!rt.getDescription().isEmpty()) {
                        CVRtoolsDAO.add(rt);
                    }
                }


                session.setAttribute("CVRToolsDAO", CVRtoolsDAO);
                DateFormat dateFormat = new SimpleDateFormat("yyyy");

                Date date = new Date();
                session.setAttribute("startYear", dateFormat.format(date));



                sda.setStep("10");
                sm.save(sda);
                break;
            case 11:
                //get possible multiple rtools values from previous step 10
                if (request.getParameterValues("research_tools") != null) {
                    String[] rtoolsValues = request.getParameterValues("research_tools");
                    String RTools = "";
                    StringBuffer RToolsConcat = new StringBuffer("");
                    System.out.println("RTOOLS VALUES::");
                    for (String s : rtoolsValues) {

                        System.out.println(s);
                        RToolsConcat = new StringBuffer(RToolsConcat).append(s).append(":");

                    }
                    if (RToolsConcat.toString().endsWith(":")) {
                        int i = RToolsConcat.lastIndexOf(":");
                        RTools = RToolsConcat.toString().substring(0, i);
                        System.out.println(RTools);
                    }
                    sda.setResearch_tools(RTools);
                }
                sda.setStep("11");
                break;

        }

        //  }
        return refData;
    }

    @Override
    protected ModelAndView processFinish(HttpServletRequest request, HttpServletResponse response,
            Object command, org.springframework.validation.BindException be) throws Exception {

        System.out.println("COMMAND==" + command.toString());
        SubmissionsDAO sd = (SubmissionsDAO) command;
        StrainsManager stm = new StrainsManager();

        if (this.isFinishRequest(request)) {
            //SAVE IT
            //DEAL WITH INTEGER FIELDS THAT MAY NOT HAVE BEEN SET HAVING '' VALUES
            SubmissionsManager sm = new SubmissionsManager();
            System.out.println("AT THE POINT OF SAVING SUBMISSION");
            sm.save(sd);
        }

        String ilarCode = sd.getProducer_ilar();
        //LOOKUP IN ILAR TABLE FOR ID
        // ## IMPORTANT ## INTERNAL USE ONLY DO NOT DISPLAY, LEGAL COMPLICATIONS
        System.out.println("ILAR CODE SUBMITTED==" + ilarCode);
        PeopleManager pm = new PeopleManager();
        PeopleDAO pd = pm.getPerson("" + sd.getPer_id_per());
        String ilarID = "";
        if(ilarCode != null){
            ilarID = pm.ilarID(ilarCode);
        }
        
        if (ilarID != null) {
            System.out.println("ILAR ID==" + ilarID);

            pd.setId_ilar(ilarID);
            pm.save(pd);
        }

        //OK now we need to take the submissions dao/submissions mutations dao and submissions biblios dao data
        //and start to populate a new strains dao.
        //might need to break this out into its own method to be accesible by restful web service for future bulk uploads
        StrainsDAO nsd = new StrainsDAO();
        //sm.save(nsd);
        Date dt = new Date();
        SimpleDateFormat sdf =
                new SimpleDateFormat("yyyy-MM-dd");

        String currentDate = sdf.format(dt);
        nsd.setAdditional_owner(sd.getExclusive_owner_text());//TODO CHECK CORRECT FIELD TO PULL DATA FROM

        //ARCHIVE OBJECT

        ArchiveDAO ad = new ArchiveDAO();
        ad.setArchived(null);
        ad.setArchiving_method_id(null);
        ad.setBreeding(null);
        ad.setEmbryo_state(null);
        ad.setEvaluated(currentDate);
        ad.setFemale_bg_id(null);
        ad.setFemales(null);
        ad.setFreezing_started(null);
        ad.setFreezingtime(null);
        ad.setLab_id_labo(null);
        ad.setMale_bg_id(null);
        ad.setMales(null);
        ad.setNotes(null);
        ad.setReceived(null);
        ad.setStr_id_str("0");
        ad.setSubmitted(currentDate);
        ad.setTimetoarchive(null);
        ad.setWt_received(null);
        ad.setWt_rederiv_started(null);
        ArchiveManager am = new ArchiveManager();
        am.save(ad);
        //END ARCHIVE OBJECT
        System.out.println("ARCHIVE ID IS::-" + ad.getId());
        // nsd.setArchiveDAO(ad);
        nsd.setArchive_id(ad.getId());

        //nsd.setAvailDAO(new AvailabilitiesStrainsDAO());

        nsd.setAvailable_to_order("no");
        nsd.setBg_id_bg(sd.getCurrent_backg() + "");
        //nsd.setBackgroundDAO(new BackgroundDAO());
        nsd.setBibliosstrainsDAO(new BibliosStrainsDAO());
        //nsd.setCategoriesStrainsDAO(new CategoriesStrainsDAO());
        nsd.setCharact_gen(sd.getGenetic_descr());//TODO NEED TO CHECK THESE AREE THE RIGHT FIELDS TO INSERT IN TO CHARACT_GEN USING OTHERTYPING/PHENO/GENO OR SUPPORTING FILE INFO

        nsd.setCode_internal("CODE INTERNAL VALUE");//TODO NEED TO FIND OUT WHERE THIS COMES FROM

        // nsd.setDate_published(/*sd.getSubmissionBibliosDAO().getYear()*/"");
        nsd.setDate_published(null);//TODO GET VALUE
        //int i = Integer.parseInt(nsd.getId_str();

        nsd.setEx_owner_description(sd.getExclusive_owner_text());
        nsd.setExclusive_owner(sd.getExclusive_owner());
        nsd.setGeneration(sd.getBackcrosses());

        if (sd.getDelayed_release() != null && sd.getDelayed_release().equals("yes")) {
            //current date + 2 years
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.YEAR, 2);
            sdf =
                    new SimpleDateFormat("yyyy-MM-dd");

            // String releaseDate = sdf.format(sdf.format(cal.getTime()));
            String releaseDate = sdf.format(cal.getTime());
            nsd.setGp_release(releaseDate);
        } else {
            nsd.setGp_release(null);
        }

        //nsd.setHealth_status(sd.getHealth_status());
        nsd.setHuman_model(sd.getHuman_condition());
        nsd.setHuman_model_desc(sd.getHuman_condition_text() + sd.getHuman_condition_more());// + sd.getHuman_condition_omim()
        nsd.setImmunocompromised(sd.getImmunocompromised());
        //nsd.setLast_change(null);
        nsd.setMaintenance(sd.getBreeding_history());
        nsd.setMgi_ref(null);//TODO GET VALUE

        String mutantFertile = "";
        if (!sd.getHeterozygous_fertile().isEmpty() || !sd.getHomozygous_fertile().isEmpty()) {
            if (!sd.getHeterozygous_fertile().isEmpty()) {
                mutantFertile = sd.getHeterozygous_fertile();
            } else {
                mutantFertile = sd.getHomozygous_fertile();
            }
        }


        nsd.setMutant_fertile(mutantFertile);
        nsd.setMutant_viable(sd.getHomozygous_viable());
        nsd.setName(sd.getStrain_name());
        nsd.setName_status(null); 
       nsd.setPer_id_per("" + sd.getPer_id_per());
        nsd.setPer_id_per_contact("" + sd.getPer_id_per_contact());
        nsd.setPer_id_per_sub("" + sd.getPer_id_per_sub());

        stm.save(nsd);

        StringBuffer PhenoText = new StringBuffer("");

        if (!/*sd.getGenotyping()*/sd.getHomozygous_phenotypic_descr().isEmpty()) {
            PhenoText = new StringBuffer().append(PhenoText).append(/*sd.getGenotyping()*/sd.getHomozygous_phenotypic_descr() + " ");
        } else if (!/*sd.getPhenotyping()*/sd.getHeterozygous_phenotypic_descr().isEmpty()) {
            PhenoText = new StringBuffer().append(PhenoText).append(/*sd.getPhenotyping()*/sd.getHeterozygous_phenotypic_descr() + " ");
            /* } else if (!sd.getOthertyping().isEmpty()) {
             PhenoText = new StringBuffer().append(PhenoText).append(sd.getOthertyping() + " ");*/
        }
        nsd.setPheno_text(PhenoText.toString().trim());
        nsd.setRequire_homozygous(sd.getHomozygous_matings_required());

        //RESIDUES OBJECT

        ResiduesDAO rd = new ResiduesDAO();
        rd.setAccepted(null);
        rd.setAccepted_date(null);
        rd.setAnimal_husbandry(sd.getHusbandry_requirements());
        //Below already collected for strains object but still placing in residues object
        rd.setChar_genotyping(sd.getGenotyping());
        rd.setChar_other(sd.getOthertyping());
        rd.setChar_phenotyping(sd.getPhenotyping());
        //Above already collected for strains object but still placing in residues object
        rd.setCrelox(null);
        rd.setCurrent_sanitary_status(sd.getSanitary_status());
        rd.setDelayed_description(sd.getDelayed_release_text());
        //rd.setDelayed_release(null);//date + 2 years TODO do we need to add this??
        rd.setDelayed_wanted(sd.getDelayed_release());
        rd.setDeposited_elsewhere(sd.getDeposited_elsewhere());//TODO MAKE NEW FIELD IN RESIDUES FOR IP TEXT  sd.getDeposited_elsewhere_text()
        rd.setFlp(null);
        rd.setIp_rights(sd.getIp_rights());
        rd.setIpr_description(sd.getIp_rights_text());
        rd.setNumber_of_requests(sd.getPast_requests());
        rd.setOmim_ids(sd.getHuman_condition_more());
        rd.setOther_labos(sd.getSimilar_strains());
        rd.setSpecific_info(null);
        rd.setTet(null);
        rd.setWhen_how_many_females(sd.getMice_avail_females());
        rd.setWhen_how_many_males(sd.getMice_avail_males());
        rd.setWhen_how_many_month(sd.getMice_avail_month());
        rd.setWhen_how_many_year(sd.getMice_avail_year());
        rd.setWhen_mice_month(sd.getMice_avail_month());
        rd.setWhen_mice_year(sd.getMice_avail_year());

        //END RESIDUES OBJECT
//SAVE RESIDUES
        ResiduesManager rm = new ResiduesManager();
        rm.save(rd);
        System.out.println("R E S I D U E S  I D = = " + rd.getId());
        nsd.setRes_id("" + rd.getId());//RESIDUES ID
        nsd.setResiduesDAO(rd);


        Set setRtools = new LinkedHashSet();
        String rToolsToParse = sd.getResearch_tools();
        RToolsManager rtm = new RToolsManager();
        if (rToolsToParse != null) {
            String[] parsedRtools = rToolsToParse.split(":");
            for (String s : parsedRtools) {
                RToolsDAO rtd = new RToolsDAO();
                System.out.println("parsed value==" + s);
                rtd.setRtls_id(Integer.parseInt(s));
                rtd.setStr_id_str(nsd.getId_str());
                System.out.println("RTOOLS STRAINS VALUES == STR_ID_STR==" + rtd.getStr_id_str() + "    RTOOLS ID==" + rtd.getRtls_id());

                rtm.saveSQL(rtd.getRtls_id(), rtd.getStr_id_str());

                //set add dao here
                setRtools.add(rtd);
            }
        }
        ///////////.........>>> nsd.setRtoolsDAO(setRtools);

        //nsd.setSources_StrainsDAO(null);
        if (sd.getDelayed_release() != null && sd.getDelayed_release().equals("yes")) {
            nsd.setStr_access("C");
        } else {
            //publicly visible after PO review
            nsd.setStr_access("P");
        }

        nsd.setStr_status("EVAL");
        nsd.setStr_type("MSR");
        //nsd.setSyn_strainsDAO(null);
        nsd.setUsername("EMMA");
        //nsd.setWrDAO(null);


        stm.save(nsd);

        System.out.println("THE ID STR OF THE NEW STRAINS DAO IS::-" + nsd.getId_str());
        String emmaID = String.format("%05d", nsd.getId_str());//String.format("%05d", result);
        System.out.println("EMMA ID IS ::- EM:" + emmaID);
        nsd.setEmma_id("EM:" + emmaID);//tTODO NEED TO GET OBJECT ID BUT NEEDS TO BE SAVED FIRST

        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //ADDITIONAL OBJECTS

        SubmissionsManager sm = new SubmissionsManager();
        MutationsManager mm = new MutationsManager();

        System.out.println("ADDITIONAL OBJECTS SECTION LINE 537::" + sd.getId_sub());
        List smd = sm.getSubMutationsBySUBID(Integer.parseInt(sd.getId_sub()));
        SubmissionMutationsDAO smdao = new SubmissionMutationsDAO();
        SubmissionBibliosDAO sbdao = new SubmissionBibliosDAO();
        List sbd = sm.getSubBibliosBySUBID(Integer.parseInt(sd.getId_sub()));
//Set setMutationsStrainsDAO = new LinkedHashSet();

        //CATEGORIES STRAINS
        if (sd.getResearch_areas() != null || sd.getResearch_areas() != "0") {


            Set setCategoriesStrains = new LinkedHashSet();
            CategoriesStrainsDAO csd = new CategoriesStrainsDAO();
            csd.setStr_id_str(nsd.getId_str());
            csd.setCat_id_cat(Integer.parseInt(sd.getResearch_areas()));
            //save cat strains
            System.out.println("categories strains cat id = " + csd.getCat_id_cat());
            System.out.println("categories strains str_id = " + csd.getStr_id_str());
            //  sm.save(csd);
            setCategoriesStrains.add(csd);

            nsd.setCategoriesStrainsDAO(setCategoriesStrains);

        }

        //SUBMISSIONMUTATIONSDAO

        for (Iterator it = smd.listIterator(); it.hasNext();) {

            smdao = (SubmissionMutationsDAO) it.next();

            //Oh crap genes needs an entry now :(
            GenesDAO gd = new GenesDAO();
            gd.setUsername("EMMA");
            gd.setLast_change(currentDate);
            gd.setName("Unknown at present");
            gd.setChromosome(smdao.getMutation_chrom());
            gd.setMgi_ref(smdao.getMutation_gene_mgi_symbol());
            mm.save(gd);

            //Oh crap alleles needs an entry now :(
            AllelesDAO ald = new AllelesDAO();
            ald.setUsername("EMMA");
            ald.setLast_change(currentDate);
            ald.setName(sd.getStrain_name());
            ald.setAlls_form("Unknown at present");
            ald.setGen_id_gene("" + gd.getId_gene());
            ald.setMgi_ref(smdao.getMutation_gene_mgi_symbol());
            ald.setGenesDAO(gd);
            mm.save(ald);

            MutationsDAO mud = new MutationsDAO();
            mud.setBg_id_bg(smdao.getMutation_original_backg());
            mud.setCh_ano_desc(smdao.getMutation_chrom_anomaly_descr());
            mud.setCh_ano_name(smdao.getMutation_chrom_anomaly_name());
            mud.setDominance(smdao.getMutation_dominance_pattern());
            mud.setMain_type(smdao.getMutation_type());
            mud.setSub_type(smdao.getMutation_subtype());
            mud.setMu_cause(smdao.getMutation_mutagen());
            mud.setTm_esline(smdao.getMutation_es_cell_line());
            mud.setChromosome(smdao.getMutation_chrom());
            //mud.setGenotype(smdao.); TODO
            mud.setStr_id_str(nsd.getId_str() + "");
            mud.setUsername("EMMA");
            mud.setLast_change(currentDate);
            mud.setAlls_id_allel(ald.getId_allel());
            mud.setAllelesDAO(ald);
            //SAVE MUTATION

            mm.save(mud);

            //now update strains_mutations with new id

//SET call method

            /////////////// createMutationStrain(mud.getId(),nsd.getId_str());

            //setMutationsStrainsDAO.add(msd);*/

        }
//nsd.setMutationsStrainsDAO(setMutationsStrainsDAO);
        //SET BIBLIOSDAO
        BibliosManager bm = new BibliosManager();
        Set BibliosStrains = new LinkedHashSet();
        for (Iterator it = sbd.listIterator(); it.hasNext();) {
            sbdao = (SubmissionBibliosDAO) it.next();
            //need to check pubmedid not already in database
            System.out.println("TRIMMED PUBMED ID IS::- " + sbdao.getPubmed_id().trim());
            String trimmedPubmedId = sbdao.getPubmed_id();
            int pmid = Integer.parseInt(trimmedPubmedId);
            BibliosDAO chkBibDAO = new BibliosDAO();
            chkBibDAO = (BibliosDAO) bm.getPubmedIDByID(pmid);
            BibliosDAO bud = new BibliosDAO();
            if (chkBibDAO != null) {
                System.out.println("chkBibDAO is not null " + chkBibDAO.getId_biblio());
                bud.setId_biblio(chkBibDAO.getId_biblio());
                //do nothing else
            } else {
                //new reference not in database
                System.out.println("chkBibDAO is null, sub biblios dao id for pubmed is " + sbdao.getPubmed_id());
                bud.setAuthor1(sbdao.getAuthor1());
                bud.setAuthor2(sbdao.getAuthor2());
                bud.setJournal(sbdao.getJournal());
                bud.setLast_change(currentDate);
                bud.setNotes(sbdao.getNotes());
                bud.setPages(sbdao.getPages());
                bud.setPubmed_id(sbdao.getPubmed_id());
                bud.setTitle(sbdao.getTitle());
                bud.setUpdated(null);
                bud.setUsername("EMMA");
                bud.setVolume(sbdao.getVolume());
                bud.setYear(sbdao.getYear());
            }

//now update strains_biblios with new id or add old exisitng biblio id
            //save new BibliosDAO then add reference to biblios_strains
            bm.save(bud);
            ////////////////////////
            BibliosStrainsDAO bsd = new BibliosStrainsDAO();
            bsd.setBib_id_biblio(bud.getId_biblio());
            bsd.setStr_id_str(nsd.getId_str());

//nsd.setBibliosstrainsDAO(bsd);

            BibliosStrains.add(bsd);



            bm.save(bsd);
            nsd.setSetBibliosStrainsDAO(BibliosStrains);
        }


        //Syn_strains/////////////////////////////////////////////////////
        Set synStrains = new LinkedHashSet();
        Syn_StrainsDAO ssd = new Syn_StrainsDAO();
        ssd.setStr_id_str(nsd.getId_str());
        ssd.setName(nsd.getName());
        ssd.setUsername("EMMA");
        ssd.setLast_change(currentDate);

        // stm.save(nsd);
        //need a syn_strains manager to save new Syn_StrainsDAO
        Syn_StrainsManager ssm = new Syn_StrainsManager();
        ssm.save(ssd);
        synStrains.add(ssd);
        ///>>> nsd.setSyn_strainsDAO(synStrains);
        /////////////////////////////////////////////////////////////////////////
        stm.save(nsd);
        //projects - set all to unknown(id 1) or COMMU(id 2)
        Set projectsStrains = new LinkedHashSet();
        ProjectsStrainsDAO psd = new ProjectsStrainsDAO();
        psd.setProject_id(1);
        psd.setStr_id_str(nsd.getId_str());

        ProjectsStrainsManager psm = new ProjectsStrainsManager();
        psm.save(psd);
        projectsStrains.add(psd);
        ////>>>>  nsd.setProjectsDAO(projectsStrains);
        
        
        //associate uploaded file prefix with new strain id by adding sub_id_sub
        nsd.setSub_id_sub(sd.getId_sub());
        
        stm.save(nsd);
        //sources strains set to 5 unknown
//TODO SOURCES STRAINS NEEDS TO SAVE MANUALLY THEN DO FINAL SAVE ON LINE 803
        Set sourcesStrains = new LinkedHashSet();
        Sources_StrainsDAO srcsd = new Sources_StrainsDAO();


        Calendar rightNow = Calendar.getInstance();

        // note that months start from 0 and days from 1 so 6 is July etc
        Calendar cal1start = Calendar.getInstance();
        cal1start.set(Calendar.YEAR, 2013);
        cal1start.set(Calendar.MONTH, 0);
        cal1start.set(Calendar.DAY_OF_MONTH, 1);

        Calendar cal1end = Calendar.getInstance();
        cal1end.set(Calendar.YEAR, 2014);
        cal1end.set(Calendar.MONTH, 5);
        cal1end.set(Calendar.DAY_OF_MONTH, 30);

        Calendar cal2start = Calendar.getInstance();
        cal2start.set(Calendar.YEAR, 2014);
        cal2start.set(Calendar.MONTH, 6);
        cal2start.set(Calendar.DAY_OF_MONTH, 1);

        Calendar cal2end = Calendar.getInstance();
        cal2end.set(Calendar.YEAR, 2015);
        cal2end.set(Calendar.MONTH, 11);
        cal2end.set(Calendar.DAY_OF_MONTH, 31);

        Calendar cal3start = Calendar.getInstance();
        cal3start.set(Calendar.YEAR, 2016);
        cal3start.set(Calendar.MONTH, 0);
        cal3start.set(Calendar.DAY_OF_MONTH, 1);

        Calendar cal3end = Calendar.getInstance();
        cal3end.set(Calendar.YEAR, 2016);
        cal3end.set(Calendar.MONTH, 11);
        cal3end.set(Calendar.DAY_OF_MONTH, 31);

        if (rightNow.after(cal1start) && rightNow.before(cal1end)) {
            srcsd.setSour_id(45);//I3-p1
        } else if (rightNow.after(cal2start) && rightNow.before(cal2end)) {
            srcsd.setSour_id(46);//I3-p2
        } else if (rightNow.after(cal3start) && rightNow.before(cal3end)) {
            srcsd.setSour_id(47);//I3-p3
        }
        System.out.println("source is " + srcsd.getSour_id());

        srcsd.setStr_id_str(nsd.getId_str());
        SourcesStrainsManager srcsm = new SourcesStrainsManager();
        srcsm.save(srcsd);
        sourcesStrains.add(srcsd);
        nsd.setSources_StrainsDAO(sourcesStrains);
        System.out.println("F I N A L  S A V E  :: -- " + nsd.getId_str());
        // stm.save(nsd);
        //MAIL OUT AND PDF ATTACHMENT + PDF LINK
        Map model = new HashMap();
        model.put("emailsubmitter", sd.getSubmitter_email());
        model.put("strainname", nsd.getName());
        model.put("strainid", nsd.getId_str());

        String velocTemplate = "org/emmanet/util/velocitytemplates/submissionFormReceipt-Template.vm";

        String content = VelocityEngineUtils.mergeTemplateIntoString(getVelocityEngine(),
                velocTemplate, model);
        MimeMessage message = getJavaMailSender().createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setReplyTo("emma@emmanet.org");//TODO SET REPLY TO EMMA@EMMANET.ORG
            helper.setFrom("emma@emmanet.org");
            helper.setBcc("webmaster@ebi.ac.uk");
            helper.setTo(model.get("emailsubmitter").toString().trim());
            helper.setSubject("TEST - Your submission to EMMA of strain " + model.get("strainname").toString());//todo remove test prefix
            helper.setText(content);
            getJavaMailSender().send(message);
        } catch (MessagingException ex) {
            ex.printStackTrace();
        }
        return new ModelAndView("/publicSubmission/success");
    }

    protected ModelAndView processCancel(HttpServletRequest request,
            HttpServletResponse response, Object command, BindException errors)
            throws Exception {

        //where is the cancel page?
        return new ModelAndView("/publicSubmission/submissionForm");
    }

    @Override
    protected void validatePage(Object command, Errors errors, int page) {

        SubmissionFormValidator validator = (SubmissionFormValidator) getValidator();
        //StrainsDAO sd = (StrainsDAO) command;
        SubmissionsDAO sd = (SubmissionsDAO) command;

        //page is 0-indexed
        switch (page) {
            case 0: //if page 1 , go validate with validatePage1Form
                //validator.validatePage1Form(command, errors);
                break;
            case 1: //if page 2 , go validate with validatePage2Form
                //validator.validatePage2Form(command, errors);
                validator.validateSubmissionForm1(sd, errors);
                break;
            case 2:
                //validator.validateSubmissionForm2(sd, errors);
                break;
            case 3:
                //validator.validateSubmissionForm3(sd, errors);
                break;
            case 4:
                //validator.validateSubmissionForm4(sd, errors);
                break;
            case 5:
                //validator.validateSubmissionForm5(sd, errors);
                break;
            case 6:
                //validator.validateSubmissionForm6(sd, errors);
                break;
            case 7:
                //validator.validateSubmissionForm7(sd, errors);
                break;
            case 8:
                //validator.validateSubmissionForm8(sd, errors);
                break;
            case 9:
                validator.validateSubmissionForm9(sd, errors);
                break;
            case 10:
                //validator.validateSubmissionForm10(sd, errors);
                break;
            case 11:
                //validator.validateSubmissionForm11(sd, errors);
                break;
        }

    }

    public List getCvDAO() {
        return cvDAO;
    }

    public void setCvDAO(List cvDAO) {
        this.cvDAO = cvDAO;
    }

    public SubmissionsDAO cleanInts(SubmissionsDAO sd) {


        return null;
    }

    public void PeopleData(HttpServletRequest request, String email, boolean returnJSON) {
        session = request.getSession(true);
        PeopleDAO pd = new PeopleDAO();
        PeopleManager pm = new PeopleManager();
        List people = pm.getPeopleByEMail(email);
        List peopleDAOs = new LinkedList();
        obj = new JSONObject();
        if (people.isEmpty()) {
            //Do nothing
        } else {
            System.out.println("PEOPLEDATA FUNCTION CALLED AND THE PERSON LIST SIZE IS " + people.size());
            JSONobjects = new LinkedList();
            for (Iterator it = people.listIterator(); it.hasNext();) {
                pd = (PeopleDAO) it.next();
                peopleDAOs.add(pd);
                if (returnJSON) {
                    //return a JSON String

                    obj.put("id", pd.getId_per());
                    obj.put("title", pd.getTitle());
                    obj.put("firstname", pd.getFirstname());
                    obj.put("surname", pd.getSurname());
                    obj.put("email", pd.getEmail());
                    obj.put("phone", pd.getPhone());
                    obj.put("fax", pd.getFax());
                    obj.put("ilar", pd.getId_ilar());
                    //laboratory details
                    obj.put("institution", pd.getLabsDAO().getName());
                    obj.put("dept", pd.getLabsDAO().getName());
                    obj.put("address1", pd.getLabsDAO().getAddr_line_1());
                    obj.put("address2", pd.getLabsDAO().getAddr_line_2());
                    obj.put("town", pd.getLabsDAO().getTown());
                    obj.put("county", pd.getLabsDAO().getProvince());
                    obj.put("postcode", pd.getLabsDAO().getPostcode());
                    obj.put("country", pd.getLabsDAO().getCountry());
                    obj.put("authority", pd.getLabsDAO().getAuthority());
                    System.out.println(obj.toString());
                    //return obj.toString();
                }
            }
            session.setAttribute("pidaos", peopleDAOs);
        }
    }

    public SubmissionsDAO previousSubmission(SubmissionsDAO sd, String encryptedID) throws UnsupportedEncodingException {
        //String getprev = "";
        Encrypter encrypter = new Encrypter();
//encrypter.decrypt(ID);
//System.out.println("to decode==" + encryptedID );

        String getprev = encrypter.decrypt(encryptedID);
        //System.out.println(getprev);
        int idDecrypt = Integer.parseInt(getprev);
        //System.out.println("decrypted id_str==" + idDecrypt);
        SubmissionsManager sman = new SubmissionsManager();
        sd = sman.getSubByID(idDecrypt);///decrypt here
        return sd;
    }

    public void createMutationStrain(int mutID, int strainID) {
        MutationsManager mutMan = new MutationsManager();
        MutationsStrainsDAO msd = new MutationsStrainsDAO();
        System.out.println("MUTATION ID IS::" + mutID);
        System.out.println("STRAIN ID IS::" + strainID);
        msd.setMut_id(mutID);
        msd.setStr_id_str(strainID);
        mutMan.saveSQL(strainID, mutID);
    }

    public void addUser(SubmissionsDAO sda) {
        PeopleDAO pd = new PeopleDAO();
        LaboratoriesManager lm = new LaboratoriesManager();
        //new lm method
        pd.setEmail(sda.getSubmitter_email());
        pd.setFax(sda.getSubmitter_fax());
        pd.setFirstname(sda.getSubmitter_firstname());
        pd.setPhone(sda.getSubmitter_tel());
        pd.setSurname(sda.getSubmitter_lastname());
        pd.setTitle(sda.getSubmitter_title());
        pd.setUsername("EMMA");

        //now a lab check
    }

    /**
     * @return the stepTitles
     */
    public List getStepTitles() {
        return stepTitles;
    }

    /**
     * @param stepTitles the stepTitles to set
     */
    public void setStepTitles(List stepTitles) {
        this.stepTitles = stepTitles;
    }

    /**
     * @return the velocityEngine
     */
    public VelocityEngine getVelocityEngine() {
        return velocityEngine;
    }

    /**
     * @param velocityEngine the velocityEngine to set
     */
    public void setVelocityEngine(VelocityEngine velocityEngine) {
        this.velocityEngine = velocityEngine;
    }

    /**
     * @return the javaMailSender
     */
    public JavaMailSender getJavaMailSender() {
        return javaMailSender;
    }

    /**
     * @param javaMailSender the javaMailSender to set
     */
    public void setJavaMailSender(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }
}
