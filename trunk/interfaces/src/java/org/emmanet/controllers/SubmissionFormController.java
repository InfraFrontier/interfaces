package org.emmanet.controllers;

/**
 *
 * @author phil
 */
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import org.emmanet.model.CategoriesDAO;
import org.emmanet.model.CategoriesManager;
import org.emmanet.model.CategoriesStrainsDAO;
import org.emmanet.model.GenesDAO;
import org.emmanet.model.GenesManager;
import org.emmanet.model.LaboratoriesManager;
import org.emmanet.model.LabsDAO;
import org.emmanet.model.MutationsDAO;
import org.emmanet.model.MutationsManager;
import org.emmanet.model.MutationsStrainsDAO;
import org.emmanet.model.OmimDAO;
import org.emmanet.model.OmimManager;
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
import org.emmanet.model.Strains_OmimDAO;
import org.emmanet.model.Strains_OmimManager;
import org.emmanet.model.SubmissionBibliosDAO;
import org.emmanet.model.SubmissionMutationsDAO;
import org.emmanet.model.SubmissionsDAO;
import org.emmanet.model.SubmissionsManager;
import org.emmanet.model.Syn_StrainsDAO;
import org.emmanet.model.Syn_StrainsManager;
import org.emmanet.util.Encrypter;
import org.emmanet.util.PubmedID;
import org.json.simple.*;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.ui.velocity.VelocityEngineUtils;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractWizardFormController;

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
    private boolean action;
    private static String BASEURL;// = Configuration.get("BASEURL");
    private static String GOOGLEANAL;// = Configuration.get("GOOGLEANAL");
    private String[] Cc;

    public SubmissionFormController() {
        setCommandName("command");
        setAllowDirtyBack(true);
        // setAllowDirtyForward(true);
        action = true;
    }

    @Override
    protected Object formBackingObject(HttpServletRequest request) {

        action = true;
        setBindOnNewForm(true);
        String idDecrypt = "";
        System.out.println("AT THE FORMBACKING OBJECT");
        StrainsDAO sd = new StrainsDAO();
        SubmissionsDAO sda = new SubmissionsDAO();
        wr = new WebRequests();
        SubmissionsManager sm = new SubmissionsManager();

        System.out.println("FBO ACTION = " + action);
        if (request.getParameter("getprev") != null) {
            idDecrypt = encrypter.decrypt(request.getParameter("getprev"));
            if (request.getParameter("recall_window").equals("Yes") && action) {
                sda = sm.getSubByID(Integer.parseInt(idDecrypt));

                if (sda.getStep().equals("-2")) {
                    System.out.println("ok prevsub is -1");
                    //lets give them their user details only
                    SubmissionsDAO newSub = new SubmissionsDAO();
                    newSub.setPer_id_per(sda.getPer_id_per());
                    newSub.setPer_id_per_contact(sda.getPer_id_per_contact());
                    newSub.setPer_id_per_sub(sda.getPer_id_per_sub());
                    // request.setAttribute("previousSub", newSub);
                    newSub.setStep("4");
                    sda = newSub;
                }
                System.out.println("NEW RECALLED SUBMISSIONS DAO ID IS:: " + sda.getId_sub());
                System.out.println("g e t p r e v::" + idDecrypt);
                System.out.println("PREVIOUS STRAIN NAME IS :: " + sda.getStrain_name());
                System.out.println("PREVIOUS STEP IS :: " + sda.getStep());
            }
        }

        BackgroundManager bm = new BackgroundManager();
        String getprev = "";

        session = request.getSession(true);
        session.setAttribute("BASEURL", getBASEURL());

        session.setAttribute("GOOGLEANAL", getGOOGLEANAL());
//total steps in wizard for use in view
        int iPageCount = getPageCount() - 1;
        session.setAttribute("totalStepCount", "" + iPageCount);
        session.setAttribute("stepTitles", "" + stepTitles);
        sda.setCvDAO(wr.isoCountries());
        sd.setPeopleDAO(new PeopleDAO());
        sd.getPeopleDAO().setLabsDAO(new LabsDAO());

        sda.setCvDAO(wr.isoCountries());

        CategoriesManager categoriesManager = new CategoriesManager();
        session.setAttribute("backgroundsDAO", bm.getCuratedBackgrounds());
        sda.setBgDAO(bm.getCuratedBackgrounds());
        session.setAttribute("categoriesDAO", categoriesManager.getCategoryListCurated("Y"));
        sda.setCatDAO(categoriesManager.getCategoryListCurated("Y"));

        return sda;
    }

    @Override
    protected Map referenceData(HttpServletRequest request,
            Object command,
            Errors errors,
            int page)
            throws Exception {

        System.out.println("reference data method called - getprev is " + request.getParameter("getprev"));

        SubmissionsDAO sda = new SubmissionsDAO();
        sda = (SubmissionsDAO) command;
        System.out.println("SDA sent to command value of id_sub is::" + sda.getId_sub());
        SubmissionsManager sm = new SubmissionsManager();
        encrypter = new Encrypter();

        System.out.println("REF DATA ACTION = " + action);
        if (request.getParameter("getprev") != null) {

            if (request.getParameter("recall_window").equals("Yes") && action) {

                System.out.println("REF DATA NEW RECALLED SUBMISSIONS DAO ID IS:: " + sda.getId_sub());
                System.out.println("REF DATA PREVIOUS STRAIN NAME IS :: " + sda.getStrain_name());
                System.out.println("REF DATAPREVIOUS STEP IS :: " + sda.getStep());
                int stepRef = Integer.parseInt(sda.getStep());
                page = stepRef - 1;
                action = false;
            }
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

                if (sda.getSubmitter_email()/*.getPeopleDAO().getEmail()*/ != null) {
                    wr = new WebRequests();
                    SubmissionsDAO prevSub = new SubmissionsDAO();
                    List submitterDAO = new LinkedList();
                    System.out.println(" email for prevsub = " + sda.getSubmitter_email().toString());
                    /*prevSub*/ submitterDAO = sm.getSubsByEmail(sda.getSubmitter_email().toString());
                    if (/*prevSub*/submitterDAO != null) {
                        for (Iterator it = submitterDAO.listIterator(); it.hasNext();) {
                            prevSub = (SubmissionsDAO) it.next();
                            System.out.println("TIMESTAMP :: " + prevSub.getTimestamp() + " step is ::- " + prevSub.getStep());

                            encrypter = new Encrypter();

                            // Encrypt
                            String encrypted = encrypter.encrypt(prevSub.getId_sub());
                            if (encrypted.isEmpty() || encrypted != null) {
                                session.setAttribute("getprev", encrypted);
                            } else {
                                //do nothing
                            }

                            if (prevSub.getStep().equals("-1")) {
                                System.out.println("ok prevsub is -1");
                                //lets give them their user details only
                                SubmissionsDAO newSub = new SubmissionsDAO();
                                newSub.setPer_id_per(prevSub.getPer_id_per());
                                newSub.setPer_id_per_contact(prevSub.getPer_id_per_contact());
                                newSub.setPer_id_per_sub(prevSub.getPer_id_per_sub());
                                // request.setAttribute("previousSub", newSub);
                                newSub.setStep("5");
                                prevSub = newSub;
                            }

                            prevSub.setEncryptedId_sub(encrypted);
                            prevSub.setCvDAO(wr.isoCountries());
                            request.setAttribute("previousSub", prevSub);
                        }
                    }
                    //request.setAttribute("previousSub", /*prevSub*/);
                    //LOOK TO PULL USER/LAB DATA TO POPULATE STRAINS
                    pd = new PeopleDAO();
                    pm = new PeopleManager();
                    System.out.println("SUBMITTER EMAIL IS:::---" + sda.getSubmitter_email());
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
                        peopleListLoop:
                        for (Iterator it = people.listIterator(); it.hasNext();) {
                            pd = (PeopleDAO) it.next();
                            System.out.println("A U T H O R I T Y  : :  " + pd.getLabsDAO().getAuthority());
                            //RESTRICT PEOPLE LISTING ONLY TO AUTHORISED LIST
                            if (pd.getLabsDAO().getAuthority() != null) {
                                peopleDAOs.add(pd);
                            } else {
                                // peopleDAOs.add(pd);
                                //person not authorised to pick addresses but we need to populate submitter per_id_per_sub with id
                                sda.setPer_id_per_sub(Integer.parseInt(pd.getId_per()));
                                break peopleListLoop;
                            }

                        }
                        /*session*/ request.setAttribute("userdaos", peopleDAOs);

                    }
                }

                System.out.println("STEP==" + sda.getStep());
                sda.setStep("2");
                if (!errors.hasErrors()) {
                   // sm.save(sda);
                }
                break;
            case 3:

                org.emmanet.controllers.SubmissionFormValidator sfv = new SubmissionFormValidator();
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
                        peopleListLoopProducer:
                        for (Iterator it = people.listIterator(); it.hasNext();) {
                            pd = (PeopleDAO) it.next();
                            if (pd.getLabsDAO().getAuthority() != null) {
                                peopleDAOs.add(pd);
                            } else {
                                // peopleDAOs.add(pd);
                                //person not authorised to pick addresses but we need to populate producer per_id_per with id
                                //we'll take the first and discard the rest
                                sda.setPer_id_per(Integer.parseInt(pd.getId_per()));
                                break peopleListLoopProducer;
                            }
                        }
                        session.setAttribute("pidaos", peopleDAOs);
                    }
                }
                sda.setStep("3");
                //sfv.validateSubmissionForm2(sda, errors, BASEURL);
                System.out.println("ABOUT TO SAVE AT STEP 3");
                if (!errors.hasErrors()) {
                    sm.save(sda);
                }

                break;

            case 4:
                if (sda.getShipper_email() != null) {
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
                        peopleListLoopShipper:
                        for (Iterator it = people.listIterator(); it.hasNext();) {
                            pd = (PeopleDAO) it.next();
                            if (pd.getLabsDAO().getAuthority() != null) {
                                peopleDAOs.add(pd);
                            } else {
                                // peopleDAOs.add(pd);
                                //person not authorised to pick addresses but we need to populate shipper per_id_per with id
                                //we'll take the first and discard the rest
                                sda.setPer_id_per_contact(Integer.parseInt(pd.getId_per()));
                                break peopleListLoopShipper;
                            }
                        }
                        session.setAttribute("pidaos", peopleDAOs);
                        //refData.put("pi", peopleDAOs);
                    }
                }
                sda.setStep("4");
                System.out.println("ABOUT TO SAVE AT STEP 4");
                                if (!errors.hasErrors()) {
                    sm.save(sda);
                }
                break;

            case 5:

                encrypter = new Encrypter();

                // Encrypt
                String encrypted = encrypter.encrypt(sda.getId_sub());
                session.setAttribute("getprev", encrypted);

                sda.setStep("5");
                if (!errors.hasErrors()) {
                    sm.save(sda);
                }
                break;

            case 6:
                sda.setStep("6");
                if (!errors.hasErrors()) {
                        sm.save(sda);
                    }
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
                    if (!errors.hasErrors()) {
                        sm.save(sda);
                    }
                }
                break;

            case 8:
                sda.setStep("8");
                if (!errors.hasErrors()) {
                    sm.save(sda);
                }
                break;

            case 9:

                sda.setStep("9");
                if (!errors.hasErrors()) {
                    sm.save(sda);
                }
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

                sda.setStep("10");
                if (!errors.hasErrors()) {
                    sm.save(sda);
                }
                break;
            case 11:
                DateFormat dateFormat = new SimpleDateFormat("yyyy");

                Date date = new Date();
                session.setAttribute("startYear", dateFormat.format(date));
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

                if (request.getParameterValues("research_areas") != null) {
                    String[] categories = request.getParameterValues("research_areas");
                    String cats = "";
                    StringBuffer catsConcat = new StringBuffer("");
                    System.out.println("CATS VALUES::");
                    for (String s : categories) {

                        System.out.println(s);
                        catsConcat = new StringBuffer(catsConcat).append(s).append(":");

                    }
                    if (catsConcat.toString().endsWith(":")) {
                        int i = catsConcat.lastIndexOf(":");
                        cats = catsConcat.toString().substring(0, i);
                        System.out.println(cats);
                    }
                    sda.setResearch_areas(cats);
                }
                sda.setStep("11");
                if (!errors.hasErrors()) {
                    sm.save(sda);
                }
                break;

        }

        refData.put("command", command);
        return refData;
    }

    @Override
    protected ModelAndView processFinish(HttpServletRequest request, HttpServletResponse response,
            Object command, org.springframework.validation.BindException be) throws Exception {
        RToolsManager rtm = new RToolsManager();
        SubmissionsDAO sd = (SubmissionsDAO) command;
       

        StrainsManager stm = new StrainsManager();
        System.out.println("CHECKING RECALLED SUBMISSIONDAO :-");
        System.out.println(sd.getId_sub());
        System.out.println(sd.getShipper_country());
        System.out.println(sd.getSubmitter_firstname());
        System.out.println(sd.getBreeding_performance());
        System.out.println("END CHECKING RECALLED SUBMISSIONDAO :-");
        if (this.isFinishRequest(request)) {
            //SAVE IT
            //DEAL WITH INTEGER FIELDS THAT MAY NOT HAVE BEEN SET HAVING '' VALUES
            SubmissionsManager sm = new SubmissionsManager();
            System.out.println("AT THE POINT OF SAVING SUBMISSION");
            sm.save(sd);
        }
        String ilarCode = "";
        ilarCode = sd.getProducer_ilar();
        //LOOKUP IN ILAR TABLE FOR ID
        // ## IMPORTANT ## INTERNAL USE ONLY DO NOT DISPLAY, LEGAL COMPLICATIONS
        System.out.println("ILAR CODE SUBMITTED==" + ilarCode);
        PeopleManager pm = new PeopleManager();
        PeopleDAO pd = pm.getPerson("" + sd.getPer_id_per());
        String ilarID = "";
        if (ilarCode != null) {
            if (ilarCode != "" || !ilarCode.isEmpty()) {
                ilarID = pm.ilarID(ilarCode);
            }
            if (pd != null) {
                if (ilarID != "" || !ilarID.isEmpty()) {
                    System.out.println("ILAR ID==" + ilarID);

                    pd.setId_ilar(ilarID);
                    pm.save(pd);
                }
            } else {
                //set ilar in submission to ilar id
            }
        }
        //OK now we need to take the submissions dao/submissions mutations dao and submissions biblios dao data
        //and start to populate a new strains dao.
        //might need to break this out into its own method to be accesible by restful web service for future bulk uploads
        StrainsDAO nsd = new StrainsDAO();

        //  stm.save(nsd);//need to save to get ID but this causes stalobject error when updating 
        Date dt = new Date();
        SimpleDateFormat sdf
                = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        String currentDate = sdf.format(dt);
        nsd.setAdditional_owner(sd.getExclusive_owner_text());//TODO not exclusive owner!!!!

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
        nsd.setCode_internal(sd.getStrain_name());
        // nsd.setDate_published(/*sd.getSubmissionBibliosDAO().getYear()*/"");
        nsd.setDate_published(null);//TODO GET VALUE
        //int i = Integer.parseInt(nsd.getId_str();
        nsd.setEx_owner_description(sd.getExclusive_owner_text());
        nsd.setExclusive_owner(sd.getExclusive_owner());
        if (sd.getBackcrosses().equals("")) {
            sd.setBackcrosses(null);
        }
        nsd.setGeneration(sd.getBackcrosses());
        if (sd.getSibmatings().equals("")) {
            sd.setSibmatings(null);
        }
        nsd.setSibmatings(sd.getSibmatings());

        if (sd.getDelayed_release() != null && sd.getDelayed_release().equals("yes")) {
            //current date + 2 years
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.YEAR, 2);
            sdf
                    = new SimpleDateFormat("yyyy-MM-dd");

            // String releaseDate = sdf.format(sdf.format(cal.getTime()));
            String releaseDate = sdf.format(cal.getTime());
            nsd.setGp_release(releaseDate);
        } else {
            nsd.setGp_release(null);
        }

        //nsd.setHealth_status(sd.getHealth_status());
        nsd.setHuman_model(sd.getHuman_condition());
        nsd.setHuman_model_desc(sd.getHuman_condition_text());                  // The human condition or disease
        nsd.setImmunocompromised(sd.getImmunocompromised());
        //nsd.setLast_change(null);
        nsd.setMaintenance(sd.getBreeding_history());
        nsd.setMgi_ref(null);//TODO GET VALUE

        String mutantFertile = "";
        String hetHemiFertile = "";
        //TODO FAILS HERE WHEN RECALLING SUBMISSION AND SUBMITTING CHECK
        //  if (sd.getHeterozygous_fertile() != null && !sd.getHeterozygous_fertile().isEmpty() || !sd.getHomozygous_fertile().isEmpty()) {
        if (!sd.getHeterozygous_fertile().isEmpty()) {
            hetHemiFertile = sd.getHeterozygous_fertile();
        }
        if (!sd.getHomozygous_fertile().isEmpty()) {
            mutantFertile = sd.getHomozygous_fertile();
        }
        // }
        nsd.setMutant_fertile(mutantFertile);
        nsd.setHethemi_fertile(hetHemiFertile);
        nsd.setMutant_viable(sd.getHomozygous_viable());
        nsd.setName(sd.getStrain_name());
        nsd.setName_status(null);

        //if per ids are 0 then need to create new people/laboratory refs
        SubmissionsManager sMan = new SubmissionsManager();
        if (sd.getPer_id_per() == 0) {
            int idProd = addUser(sd, "producer");
            sd.setPer_id_per(idProd);
            //save submission
            sMan.save(sd);
            nsd.setPer_id_per("" + idProd);

        } else {
            nsd.setPer_id_per("" + sd.getPer_id_per());
        }

        if (sd.getPer_id_per_contact() == 0) {
            int idShip = addUser(sd, "shipper");
            sd.setPer_id_per_contact(idShip);
            //save submission
            sMan.save(sd);
            nsd.setPer_id_per_contact("" + idShip);
        } else {
            nsd.setPer_id_per_contact("" + sd.getPer_id_per_contact());
        }

        if (sd.getPer_id_per_sub() == 0) {
            int idSub = addUser(sd, "submitter");
            sd.setPer_id_per_sub(idSub);
            //save submission
            sMan.save(sd);
            nsd.setPer_id_per_sub("" + idSub);
        } else {
            nsd.setPer_id_per_sub("" + sd.getPer_id_per_sub());
        }

        stm.save(nsd);

        String phenoTextHomo = (sd.getHomozygous_phenotypic_descr().isEmpty() ? "" : sd.getHomozygous_phenotypic_descr());
        nsd.setPheno_text(phenoTextHomo.trim());
        String phenoTextHetero = (sd.getHeterozygous_phenotypic_descr().isEmpty() ? "" : sd.getHeterozygous_phenotypic_descr());
        nsd.setPheno_text_hetero(phenoTextHetero.trim());
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
        rd.setDelayed_description(sd.getDelayed_release_text());
        rd.setDelayed_release(nsd.getGp_release());
        rd.setDeposited_elsewhere(sd.getDeposited_elsewhere());
        rd.setDeposited_elsewhere_text(sd.getDeposited_elsewhere_text());
        rd.setOwner_permission(sd.getOwner_permission());
        rd.setOwner_permission_text(sd.getOwner_permission_text());
        rd.setFlp(null);
        rd.setIp_rights(sd.getIp_rights());
        rd.setIpr_description(sd.getIp_rights_text());
        rd.setNumber_of_requests(sd.getPast_requests());
        rd.setOther_labos(sd.getSimilar_strains());
        rd.setSpecific_info(null);
        rd.setTet(null);
        rd.setWhen_how_many_females(sd.getMice_avail_females());
        rd.setWhen_how_many_males(sd.getMice_avail_males());
        rd.setWhen_how_many_month(sd.getMice_avail_month());
        rd.setWhen_how_many_year(sd.getMice_avail_year());
        rd.setWhen_mice_month(sd.getMice_avail_month());
        rd.setWhen_mice_year(sd.getMice_avail_year());
        rd.setHomozygous_matings_required_text(sd.getHomozygous_matings_required_text());
        if (sd.getReproductive_maturity_age().equals("")) {
            sd.setReproductive_maturity_age(null);
        }
        rd.setReproductive_maturity_age(sd.getReproductive_maturity_age());
        if (sd.getReproductive_decline_age().equals("")) {
            sd.setReproductive_decline_age(null);
        }
        rd.setReproductive_decline_age(sd.getReproductive_decline_age());
        rd.setGestation_length(sd.getGestation_length());
        rd.setPups_at_birth(sd.getPups_at_birth());
        rd.setPups_at_weaning(sd.getPups_at_weaning());
        if (sd.getWeaning_age().equals("")) {
            rd.setWeaning_age(null);
        } else {
            rd.setWeaning_age(sd.getWeaning_age());
        }
        if (sd.getLitters_in_lifetime().equals("")) {
            sd.setLitters_in_lifetime(null);
        }
        rd.setLitters_in_lifetime(sd.getLitters_in_lifetime());
        if (sd.getBreeding_performance().equals("")) {
            sd.setBreeding_performance(null);
        }
        rd.setBreeding_performance(sd.getBreeding_performance());

        rd.setWelfare(sd.getWelfare());
        rd.setRemedial_actions(sd.getRemedial_actions());

        //END RESIDUES OBJECT
//SAVE RESIDUES
        ResiduesManager rm = new ResiduesManager();
        rm.save(rd);
        System.out.println("R E S I D U E S  I D = = " + rd.getId());
        nsd.setRes_id("" + rd.getId());//RESIDUES ID
        nsd.setResiduesDAO(rd);

        Set setRtools = new LinkedHashSet();
        String rToolsToParse = sd.getResearch_tools();
        rtm = new RToolsManager();
        if (rToolsToParse != null) {
            String[] parsedRtools = rToolsToParse.split(":");
            for (String s : parsedRtools) {
                if (s != null && !s.isEmpty()) {
                    RToolsDAO rtd = new RToolsDAO();
                    System.out.println("parsed value==" + s);
                    rtd.setRtls_id(Integer.parseInt(s));
                    rtd.setStr_id_str(nsd.getId_str());
                    System.out.println("RTOOLS STRAINS VALUES == STR_ID_STR==" + rtd.getStr_id_str() + "    RTOOLS ID==" + rtd.getRtls_id());

                    rtm.saveUsingJDBCSQL/*saveSQL*/(Integer.parseInt(s), nsd.getId_str());//(rtd.getRtls_id(), rtd.getStr_id_str());
//rtm.saveOnly(rtd);
                    //set add dao here
                    setRtools.add(rtd);
                }
            }
        }

        // The OMIM ids are presented as a comma-separated list of alphanumeric characters in sd.getHuman_condition_more().
        if (sd.getHuman_condition_more() != null) {
            OmimManager omimManager = new OmimManager();
            Strains_OmimManager strains_OmimManager = new Strains_OmimManager();
            String[] omims = sd.getHuman_condition_more().split(",");
            for (String omimRaw : omims) {
                String omim = omimRaw.trim();
                OmimDAO omimDAO = OmimManager.findByOmim(omim);
                if (omimDAO == null) {                                          // The omim doesn't yet exist. Add it to the omim table.
                    omimDAO = new OmimDAO();
                    omimDAO.setOmim(omim);
                    omimManager.save(omimDAO);
                }
                Strains_OmimDAO strainsOmniDAO = new Strains_OmimDAO();
                strainsOmniDAO.setId_omim(omimDAO.getId_omim());
                strainsOmniDAO.setId_strains(nsd.getId_str());
                strains_OmimManager.save(strainsOmniDAO);                       // Add the strain/omim combination to the Strains_Omim lookup table.
            }
        }

        // OMIM and STRAINS_OMIM
        String s1 = sd.getHuman_condition();
        String s2 = sd.getHuman_conditionNo();
        String s3 = sd.getHuman_conditionUnknown();
        String s4 = sd.getHuman_condition_more();
        String s5 = sd.getHuman_condition_omim();
        String s6 = sd.getHuman_condition_text();

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

        stm.save(nsd);//TODO TRY/CATCH EXCEPTION

        System.out.println("THE ID STR OF THE NEW STRAINS DAO IS::-" + nsd.getId_str());
        String emmaID = String.format("%05d", nsd.getId_str());//String.format("%05d", result);
        System.out.println("EMMA ID IS ::- EM:" + emmaID);
        nsd.setEmma_id("EM:" + emmaID);//tTODO NEED TO GET OBJECT ID BUT NEEDS TO BE SAVED FIRST
        stm.save(nsd);
        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //ADDITIONAL OBJECTS

        SubmissionsManager sm = new SubmissionsManager();
        MutationsManager mm = new MutationsManager();

        System.out.println("ADDITIONAL OBJECTS SECTION LINE 537::" + sd.getId_sub());
        List smd = sm.getSubMutationsBySUBID(Integer.parseInt(sd.getId_sub()));
        SubmissionMutationsDAO smdao = new SubmissionMutationsDAO();
        List sbd = sm.getSubBibliosBySUBID(Integer.parseInt(sd.getId_sub()));
//Set setMutationsStrainsDAO = new LinkedHashSet();

        //CATEGORIES STRAINS
        if (sd.getResearch_areas() != null || sd.getResearch_areas() != "0") {

            Set setCategories = new LinkedHashSet();
            String catsToParse = sd.getResearch_areas();
            SubmissionsManager sbm = new SubmissionsManager();
            if (catsToParse != null) {
                String[] parsedCats = catsToParse.split(":");
                for (String s : parsedCats) {
                    if (s != null && !s.isEmpty() && !s.equals("0")) {
                        CategoriesStrainsDAO csd = new CategoriesStrainsDAO();
                        System.out.println("parsed value==" + s);
                        System.out.println("Unparsed string is " + sd.getResearch_areas().toString());
                        csd.setStr_id_str(nsd.getId_str());
                        csd.setCat_id_cat(Integer.parseInt(s));
                		//sbm.save(csd);//ONLY TRIES TO UPDATE THEN FAILS BECAUSE OF UNIQUE KEY CONSTRAINT USE SQL INSERT INSTEAD
                        //OK sql insert isn't working either, same issue as rtools need to completely bypass hibernate!!
                        rtm.saveCategoriesUsingJDBCSQL(csd.getCat_id_cat(), csd.getStr_id_str());
                		//sbm.saveSQL(csd.getCat_id_cat(), csd.getStr_id_str());
                        //set add dao here
                        setCategories.add(csd);
                        //nsd.setCategoriesStrainsDAO(setCategories);
                    }
                }
                //~~~~~~~~~nsd.setCategoriesStrainsDAO(setCategories);
            }
        }

        // If 'Other research areas' was specified, query the Categories table. If it doesn't exist, add it to the Categories
        // table (making sure the 'curated' flag is set to 'N') and return the pk. If it already exists, save the pk.
        // The pk is then used to add the category to the categories_strains table.
        if ((sd.getResearch_areas_other_text() != null) && (sd.getResearch_areas_other_text().trim().length() > 0)) {
            CategoriesManager categoriesManager = new CategoriesManager();
            CategoriesDAO categoriesDAO;
            List<CategoriesDAO> categoriesDAOList = categoriesManager.findByCategoryName(sd.getResearch_areas_other_text());
            if ((categoriesDAOList != null) && (categoriesDAOList.size() > 0)) {
                categoriesDAO = categoriesDAOList.get(0);
            } else {
                categoriesDAO = new CategoriesDAO();
                categoriesDAO.setCurated("N");
                categoriesDAO.setDescription(sd.getResearch_areas_other_text());
                categoriesDAO.setLast_change(currentDate);
                categoriesDAO.setMain_cat(sd.getResearch_areas_other_text());
                categoriesDAO.setUsername("EMMA");
                categoriesManager.save(categoriesDAO);
            }
            rtm.saveCategoriesUsingJDBCSQL(categoriesDAO.getId_cat(), nsd.getId_str());
        }

        //SUBMISSIONMUTATIONSDAO
        Set setMutationsStrainsDAO = new LinkedHashSet();
        for (Iterator it = smd.listIterator(); it.hasNext();) {

            smdao = (SubmissionMutationsDAO) it.next();

            GenesManager genesManager = new GenesManager();
            String transgeneName = smdao.getMutation_transgene_mgi_symbol().trim();
            GenesDAO genesDAO = genesManager.getGene(transgeneName);

            if (genesDAO == null) {
                genesDAO = new GenesDAO();                                      // The transgene does not yet exist in the genes table. Create a new instance.
                genesDAO.setName(transgeneName.isEmpty() ? "Unknown at present" : smdao.getMutation_transgene_mgi_symbol());
                genesDAO.setSymbol(genesDAO.getName());
                genesDAO.setChromosome(smdao.getMutation_chrom());
                genesDAO.setMgi_ref(smdao.getMutation_gene_mgi_symbol());
                genesDAO.setPromoter(smdao.getMutation_promoter());
                genesDAO.setFounder_line_number(smdao.getMutation_founder_line_number());
                genesDAO.setPlasmid_construct(smdao.getMutation_plasmid());
                genesDAO.setCentimorgan(null);
            }

            genesDAO.setLast_change(currentDate);
            genesDAO.setUsername("EMMA");
            mm.save(genesDAO);

            //Oh crap alleles needs an entry now :(
            AllelesDAO ald = new AllelesDAO();
            ald.setUsername("EMMA");
            ald.setLast_change(currentDate);
            ald.setName("Unknown at present");
            ald.setAlls_form("Unknown at present");
            ald.setGen_id_gene("" + genesDAO.getId_gene());
            ald.setMgi_ref(smdao.getMutation_allele_mgi_symbol());
            ald.setGenesDAO(genesDAO);
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

            //SET call method
            MutationsStrainsDAO msd = new MutationsStrainsDAO();
            msd.setMut_id(mud.getId());
            msd.setStr_id_str(nsd.getId_str());
            //createMutationStrain(mud.getId(), nsd.getId_str());
            //Set msdao = (Set) mm.getMutationIDsByStrain(nsd.getId_str());
            //mm.save(msd);
            rtm.saveMutsStrainsUsingJDBCSQL(mud.getId(), nsd.getId_str());
            setMutationsStrainsDAO.add(msd);
        }

        /**
         * Loop through all of the bibliographic records in this strain
         * submission, adding each reference to the biblios table. Before
         * updating the pubmed_id, check first to make sure it's valid. If it is
         * not valid, store the biblio record but leave the pubmed_id NULL.
         */
        for (Iterator it = sbd.listIterator(); it.hasNext();) {
            BibliosManager bibliosManager = new BibliosManager();

            SubmissionBibliosDAO submissionBiblioDAO = (SubmissionBibliosDAO) it.next();
            PubmedID pubmedID = new PubmedID(submissionBiblioDAO.getPubmed_id());
            if (!pubmedID.isValid()) {
                submissionBiblioDAO.setPubmed_id(null);
            }

            BibliosDAO bibliosDAO = new BibliosDAO();

            bibliosDAO.setAuthor1(submissionBiblioDAO.getAuthor1());
            bibliosDAO.setAuthor2(submissionBiblioDAO.getAuthor2());
            bibliosDAO.setJournal(submissionBiblioDAO.getJournal());
            bibliosDAO.setLast_change(currentDate);
            // getNotes() is a cv. The 'Other' option causes a free-text box to open.
            String notes = (submissionBiblioDAO.getNotes().compareTo("Other") == 0 ? submissionBiblioDAO.getNotesadditional() : submissionBiblioDAO.getNotes());
            bibliosDAO.setNotes(notes);
            bibliosDAO.setPages(submissionBiblioDAO.getPages());
            bibliosDAO.setPubmed_id(submissionBiblioDAO.getPubmed_id());
            bibliosDAO.setTitle(submissionBiblioDAO.getTitle());
            bibliosDAO.setUpdated(null);
            bibliosDAO.setUsername("EMMA");
            bibliosDAO.setVolume(submissionBiblioDAO.getVolume());
            bibliosDAO.setYear(submissionBiblioDAO.getYear());

            bibliosManager.save(bibliosDAO);

            BibliosStrainsDAO biblioStrainsDAO = new BibliosStrainsDAO();
            biblioStrainsDAO.setBib_id_biblio(bibliosDAO.getId_biblio());
            biblioStrainsDAO.setStr_id_str(nsd.getId_str());
            bibliosManager.save(biblioStrainsDAO);
        }

        //Syn_strains/////////////////////////////////////////////////////
        Set synStrains = new LinkedHashSet();
        Syn_StrainsDAO ssd = new Syn_StrainsDAO();
        ssd.setStr_id_str(nsd.getId_str());
        System.out.println("STRING ID FROM STRAINS OBJECT==" + ssd.getStr_id_str());
        ssd.setName(nsd.getName());
        ssd.setUsername("EMMA");
        ssd.setLast_change(currentDate);

        //stm.save(nsd);
        //need a syn_strains manager to save new Syn_StrainsDAO
        Syn_StrainsManager ssm = new Syn_StrainsManager();
        ssm.save(ssd);
        synStrains.add(ssd);
        //~~~~~~~~~~~~~nsd.setSyn_strainsDAO(synStrains);
        /////////////////////////////////////////////////////////////////////////
        /////////stm.save(nsd);
        //projects - set all to unknown(id 1) or COMMU(id 2)
        Set projectsStrains = new LinkedHashSet();
        ProjectsStrainsDAO psd = new ProjectsStrainsDAO();
        psd.setProject_id(2);
        psd.setStr_id_str(nsd.getId_str());

        ProjectsStrainsManager psm = new ProjectsStrainsManager();
        psm.save(psd);
        projectsStrains.add(psd);
        //~~~~~~~~~~~~~~~~ nsd.setProjectsDAO(projectsStrains);

        //associate uploaded file prefix with new strain id by adding sub_id_sub
        nsd.setSub_id_sub(sd.getId_sub());

        //need to save and recall saved strains object as for some reason if I try to set the source strains here without doing this it throws an error.
        // stm.save(nsd);
        nsd = stm.getStrainByID(nsd.getId_str());

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
        //~~~~~~~~~~~nsd.setSources_StrainsDAO(sourcesStrains);

        System.out.println("" + srcsd.getStr_id_str());

        System.out.println("F I N A L  S A V E  :: -- " + nsd.getId_str() + " EMMA ID ++ " + nsd.getEmma_id());
        stm.save(nsd);
        //MAIL OUT AND PDF ATTACHMENT + PDF LINK
        System.out.println("STARTING MAILOUT");
        Map model = new HashMap();
        model.put("emailsubmitter", sd.getSubmitter_email());
        model.put("strainname", nsd.getName());
        model.put("strainid", nsd.getId_str());
        model.put("encid", encrypter.encrypt("" + nsd.getId_str()));//encryptedstrainid
        model.put("BASEURL", BASEURL);

        String velocTemplate = "org/emmanet/util/velocitytemplates/SubmissionFormReceipt-Template.vm";
        String content = VelocityEngineUtils.mergeTemplateIntoString(getVelocityEngine(),
                velocTemplate, model);
        MimeMessage message = getJavaMailSender().createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setReplyTo("emma@infrafrontier.eu");
            helper.setFrom("emma@infrafrontier.eu");
            helper.setBcc("webmaster@infrafrontier.eu");
            helper.setCc(Cc);
            helper.setTo(model.get("emailsubmitter").toString().trim());
            helper.setSubject("Your submission to EMMA of strain " + model.get("strainname").toString());
            helper.setText(content);
            System.out.println(message.getContent().toString());
            getJavaMailSender().send(message);
        } catch (MessagingException ex) {
            ex.printStackTrace();
        } catch (Exception ce) {
            logger.warn("Unable to send submission e-mail: " + ce.getLocalizedMessage());
        }
        //ok submission pretty much complete, let's now set the step to the last position for user details
        //sd.setStep("-1");
        //System.out.println("Step is::-" + sd.getStep());
        sm.save(sd);
        return new ModelAndView("/publicSubmission/success");
    }

    @Override
    protected ModelAndView processCancel(HttpServletRequest request, HttpServletResponse response, Object command, org.springframework.validation.BindException errors) throws Exception {
        return new ModelAndView("/publicSubmission/cancel");
    }

    @Override
    protected void validatePage(Object command, Errors errors, int page) {

        SubmissionFormValidator validator = (SubmissionFormValidator) getValidator();
        SubmissionsDAO sd = (SubmissionsDAO) command;

        //page is 0-indexed
        switch (page) {
            case 0: //if page 1 , go validate with validatePage1Form
                validator.validateSubmissionForm(sd, errors);

                break;
            case 1: //if page 2 , go validate with validatePage2Form
                //validator.validatePage2Form(command, errors);
                //validator.validateSubmissionForm2(sd, errors,"submitter");
                //validator.validateSubmissionForm0(sd, errors);
                validator.validateSubmissionForm0(sd, errors);
                break;
            case 2:
                validator.validateSubmissionForm1(sd, errors, "submitter");
                break;
            case 3:
                validator.validateSubmissionForm1(sd, errors, "producer");
                break;
            case 4:
                //uses validator 2 to redice code duplication 
                validator.validateSubmissionForm1(sd, errors, "shipper");
                break;
            case 5:
                validator.validateSubmissionForm4(sd, errors);
                break;
            case 6:
                validator.validateSubmissionForm5(sd, errors);
                break;
            case 7:
                validator.validateSubmissionForm6(sd, errors);
                break;
            case 8:
                validator.validateSubmissionForm7(sd, errors);
                break;
            case 9:
                validator.validateSubmissionForm8(sd, errors);
                break;
            case 10:
                validator.validateSubmissionForm9(sd, errors);
                break;
            case 11:
                validator.validateSubmissionForm10(sd, errors);
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
        mutMan.saveSQL(strainID, mutID, msd);
    }

    public int addUser(SubmissionsDAO sda, String type) {

        PeopleDAO pd = new PeopleDAO();
        LaboratoriesManager lm = new LaboratoriesManager();
        PeopleManager pm = new PeopleManager();
        String email = "";
        String fax = "";
        String phone = "";
        String firstname = "";
        String surname = "";
        String title = "";
        int ilarID = 0;
        int labID = 0;
        int per_id_per = 0;

        //lab info
        String department = "";
        String name = "";
        String town = "";
        String postcode = "";
        String country = "";
        String province = "";
        String address1 = "";
        String address2 = "";

        //List checkPerson = pm.getPeopleByEMail(sda.getSubmitter_email());
        //Removed after discussion with PO (Sabine) as we may aswell add all users to database rather than re-use old
        //when user accounts are implemented we then have nice new people data
        List checkPerson = new ArrayList();;//now always going to be empty so line 1309 always adds to database

        if (type.equals("submitter")) {
            email = sda.getSubmitter_email();
            fax = sda.getSubmitter_fax();
            phone = sda.getSubmitter_tel();
            surname = sda.getSubmitter_lastname();
            title = sda.getSubmitter_title();
            firstname = sda.getSubmitter_firstname();

            department = sda.getSubmitter_dept();
            name = sda.getSubmitter_inst();
            town = sda.getSubmitter_city();
            postcode = sda.getSubmitter_postcode();
            country = sda.getSubmitter_country();
            province = sda.getSubmitter_county();
            address1 = sda.getSubmitter_addr_1();
            address2 = sda.getSubmitter_addr_2();
        } else if (type.equals("shipper")) {
            email = sda.getShipper_email();
            fax = sda.getShipper_fax();
            phone = sda.getShipper_tel();
            surname = sda.getShipper_lastname();
            title = sda.getShipper_title();
            firstname = sda.getShipper_firstname();

            department = sda.getShipper_dept();
            name = sda.getShipper_inst();
            town = sda.getShipper_city();
            postcode = sda.getShipper_postcode();
            country = sda.getShipper_country();
            province = sda.getShipper_county();
            address1 = sda.getShipper_addr_1();
            address2 = sda.getShipper_addr_2();
        } else if (type.equals("producer")) {
            email = sda.getProducer_email();
            fax = sda.getProducer_fax();
            phone = sda.getProducer_tel();
            surname = sda.getProducer_lastname();
            title = sda.getProducer_title();
            firstname = sda.getProducer_firstname();

            //need to take ilar code and get id
            //pm = new PeopleManager();
            //make sure ilar isn't null which will cause a null pointer exception
            String ilarToCheck = sda.getProducer_ilar();
            System.out.println("PRODUCER ILAR FOR NEW USER == " + ilarToCheck.length());
            if (ilarToCheck.length() > 0) {
                System.out.println("PRODUCER ILAR is > 0 == " + ilarToCheck.length());
                String checkedIlarID = pm.ilarID(ilarToCheck);
                if (checkedIlarID != null && !checkedIlarID.isEmpty()) {
                    ilarID = Integer.parseInt(checkedIlarID);
                }
            }
            department = sda.getProducer_dept();
            name = sda.getProducer_inst();
            town = sda.getProducer_city();
            postcode = sda.getProducer_postcode();
            country = sda.getProducer_country();
            province = sda.getProducer_county();
            address1 = sda.getProducer_addr_1();
            address2 = sda.getProducer_addr_2();
        }
        //new lm method
        pd.setEmail(email);
        pd.setFax(fax);
        pd.setFirstname(firstname);
        pd.setPhone(phone);
        pd.setSurname(surname);
        pd.setTitle(title);
        pd.setUsername("EMMA");

        if (ilarID != 0) {
            pd.setId_ilar("" + ilarID);
        }
        //now a lab check

        LabsDAO checkLab = new LabsDAO();
        checkLab = lm.getLabCheck(postcode, "postcode");
        System.out.println("line 1 of checklab==" + checkLab.getAddr_line_1());
        if (checkLab.getId_labo() != null) {
            if (checkLab.getCountry().equals(country)) {
                System.out.println("pretty sure same laboratory but let's check some more");
                if (checkLab.getName().contains(name)) {
                    System.out.println("more sure same laboratory but let's check once more");
                    if (checkLab.getAddr_line_1().contains(address1)) {
                        System.out.println("deffo same so let us set the labID var to the dao lab id");
                        //Removed after discussion with PO (Sabine) as we may aswell add all laboratories to database rather than re-use old
                        //when user accounts are implemented we then have nice new laboratory data
                        //labID = Integer.parseInt(checkLab.getId_labo());
                    }
                }
            }
        }
        System.out.println("labID value is::" + labID);
        if (labID != 0) {
            pd.setLab_id_labo("" + labID);
        } else {
            //we have a new lab let's populate a labsDao object
            System.out.println("this is a new lab so we are creating it in the db");
            LabsDAO ld = new LabsDAO();

            ld.setAddr_line_1(address1);
            ld.setAddr_line_2(address2);
            ld.setCountry(country);
            ld.setDept(department);
            ld.setName(name);
            ld.setPostcode(postcode);
            ld.setProvince(province);
            ld.setTown(town);
            lm.save(ld);

            pd.setLab_id_labo(ld.getId_labo());
        }
        //save person?
        if (checkPerson.isEmpty()) {
            //no person exists with this er-mail so save now
            pm.save(pd);
            per_id_per = Integer.parseInt(pd.getId_per());
        } else {
            //iterate over list but only the first one to get 
            peopleListLoop:
            for (Iterator it = checkPerson.listIterator(); it.hasNext();) {
                PeopleDAO personFound = (PeopleDAO) it.next();
                per_id_per = Integer.parseInt(personFound.getId_per());
                break peopleListLoop;
            }
        }

        //return person id?
        System.out.println("Type and returning " + type + " - " + per_id_per);
        return per_id_per;
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

    /**
     * @return the BASEURL
     */
    public String getBASEURL() {
        return BASEURL;
    }

    /**
     * @param aBASEURL the BASEURL to set
     */
    public void setBASEURL(String aBASEURL) {
        BASEURL = aBASEURL;
    }

    /**
     * @return the GOOGLEANAL
     */
    public String getGOOGLEANAL() {
        return GOOGLEANAL;
    }

    /**
     * @param GOOGLEANAL the GOOGLEANAL to set
     */
    public void setGOOGLEANAL(String aGOOGLEANAL) {
        GOOGLEANAL = aGOOGLEANAL;
    }

    /**
     * @return the Cc
     */
    public String[] getCc() {
        return Cc;
    }

    /**
     * @param Cc the Cc to set
     */
    public void setCc(String[] Cc) {
        this.Cc = Cc;
    }
}
