package org.emmanet.controllers;

/**
 *
 * @author phil
 */
import java.io.UnsupportedEncodingException;
import java.net.BindException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.emmanet.jobs.WebRequests;
import org.emmanet.model.ArchiveDAO;
import org.emmanet.model.ArchiveManager;
import org.emmanet.model.AvailabilitiesStrainsDAO;
import org.emmanet.model.BackgroundDAO;
import org.emmanet.model.BackgroundManager;
import org.emmanet.model.BibliosDAO;
import org.emmanet.model.BibliosManager;
import org.emmanet.model.BibliosStrainsDAO;
import org.emmanet.model.CVRtoolsDAO;
import org.emmanet.model.CategoriesStrainsDAO;
import org.emmanet.model.LabsDAO;
import org.emmanet.model.MutationsDAO;
import org.emmanet.model.MutationsStrainsDAO;
import org.emmanet.model.PeopleDAO;
import org.emmanet.model.PeopleManager;
import org.emmanet.model.ResiduesDAO;
import org.emmanet.model.Sources_StrainsDAO;
import org.emmanet.model.StrainsDAO;
import org.emmanet.model.StrainsManager;
import org.emmanet.model.SubmissionBibliosDAO;
import org.emmanet.model.SubmissionMutationsDAO;
import org.emmanet.model.SubmissionsDAO;
import org.emmanet.model.SubmissionsManager;
import org.emmanet.util.Encrypter;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractWizardFormController;
import org.json.simple.*;

public class SubmissionFormController extends AbstractWizardFormController {

    private List cvDAO;
    private HttpSession session;
    private JSONObject obj;
    private List JSONobjects = new LinkedList();
    private WebRequests wr;
    private Encrypter encrypter = new Encrypter();
    private List stepTitles;

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
                            peopleDAOs.add(pd);
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

        if (this.isFinishRequest(request)) {
            //SAVE IT
            //DEAL WITH INTEGER FIELDS THAT MAY NOT HAVE BEEN SET HAVING '' VALUES
            SubmissionsManager sm = new SubmissionsManager();
            System.out.println("AT THE POINT OF SAVING SUBMISSION");
            sm.save(sd);
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
        nsd.setCharact_gen("" /* if (!sd.getGenotyping().isEmpty()) {
                 sd.getGenotyping();
                 } else if (!sd.getPhenotyping().isEmpty()) {
                 sd.getPhenotyping();
                 } else if (!sd.getOthertyping().isEmpty()) {
                 sd.getOthertyping();
                 }*/);//TODO NEED TO CHECK THESE AREE THE RIGHT FIELDS TO INSERT IN TO CHARACT_GEN USING OTHERTYPING/PHENO/GENO OR SUPPORTING FILE INFO

        nsd.setCode_internal("CODE INTERNAL VALUE");//TODO NEED TO FIND OUT WHERE THIS COMES FROM

       // nsd.setDate_published(/*sd.getSubmissionBibliosDAO().getYear()*/"");
        nsd.setDate_published(null);//TODO GET VALUE
        //int i = Integer.parseInt(nsd.getId_str();
        
        nsd.setEx_owner_description(sd.getExclusive_owner_text());
        nsd.setExclusive_owner(sd.getExclusive_owner());
        nsd.setGeneration(sd.getBackcrosses());
        nsd.setGp_release(sd.getDelayed_release());
        nsd.setHealth_status(sd.getSanitary_status());
        nsd.setHuman_model(sd.getHuman_condition());
        nsd.setHuman_model_desc(sd.getHuman_condition_text());
        nsd.setImmunocompromised(sd.getImmunocompromised());
        //nsd.setLast_change(null);
        nsd.setMaintenance(sd.getHusbandry_requirements());//TODO CHECK RIGHT FIELD
        nsd.setMgi_ref(null);//TODO GET VALUE
        
        nsd.setMutant_fertile(/*"Heterozygous fertile: " + sd.getHeterozygous_fertile() + "Homozygous fertile: " + sd.getHomozygous_fertile()*/null);//todo get values
        nsd.setMutant_viable(sd.getHomozygous_viable());
        nsd.setName(sd.getStrain_name());
        nsd.setName_status(null);
        nsd.setPer_id_per("" + sd.getPer_id_per());
        nsd.setPer_id_per_contact("" + sd.getPer_id_per_contact());
        nsd.setPer_id_per_sub("" + sd.getPer_id_per_sub());
        nsd.setPheno_text(sd.getPhenotyping());
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
        rd.setDelayed_release(null);//date + 2 years TODO do we need to add this??
        rd.setDelayed_wanted(sd.getDelayed_release());
        rd.setDeposited_elsewhere(sd.getDeposited_elsewhere());//TODO MAKE NEW FIELD IN RESIDUES FOR IP TEXT  sd.getDeposited_elsewhere_text()
        rd.setFlp(null);
        rd.setIp_rights(sd.getIp_rights());
        rd.setIpr_description(sd.getIp_rights_text());
        rd.setNumber_of_requests(sd.getPast_requests());
        rd.setOmim_ids(null);
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


        nsd.setRes_id("" + rd.getId());//RESIDUES ID
        //nsd.setResiduesDAO(rd); TODO MOVE LATER ON AFTER TESTING AFTER LINE536 MOST LIKELY
        //nsd.setRtoolsDAO(null);
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
      
        StrainsManager stm=new StrainsManager();
        stm.save(nsd);
          System.out.println("THE ID STR OF THE NEW STRAINS DAO IS::-" + nsd.getId_str());
          String emmaID = String.format("%05d", nsd.getId_str());//String.format("%05d", result);
        System.out.println("EMMA ID IS ::- EM:" + emmaID);
        nsd.setEmma_id("EM:" + emmaID);//tTODO NEED TO GET OBJECT ID BUT NEEDS TO BE SAVED FIRST
        
        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //ADDITIONAL OBJECTS
        
        SubmissionsManager sm = new SubmissionsManager();
        
        System.out.println("ADDITIONAL OBJECTS SECTION LINE 537::" + sd.getId_sub());
        List smd = sm.getSubMutationsBySUBID(Integer.parseInt(sd.getId_sub()));
        SubmissionMutationsDAO smdao = new SubmissionMutationsDAO();
        SubmissionBibliosDAO sbdao = new SubmissionBibliosDAO();
        List sbd = sm.getSubBibliosBySUBID(Integer.parseInt(sd.getId_sub()));
        
            //SUBMISSIONMUTATIONSDAO
            for (Iterator it = smd.listIterator(); it.hasNext();) {
                MutationsDAO mud = new MutationsDAO();
                smdao = (SubmissionMutationsDAO) it.next();
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
                // <property column="mu_cause" name="mu_cause"/>
                // <property column="genotype" name="genotype"/>
                
                //now update strains_mutations with new id
            MutationsStrainsDAO msd = new MutationsStrainsDAO();
                msd.setMut_id(mud.getId());
                msd.setStr_id_str(nsd.getId_str());
            }
            
        //SET BIBLIOSDAO
            BibliosManager bm = new BibliosManager();
        for (Iterator it = sbd.listIterator(); it.hasNext();) {
            sbdao = (SubmissionBibliosDAO) it.next();
            //need to check pubmedid not already in database
            System.out.println("TRIMMED PUBMED ID IS::- " + sbdao.getPubmed_id().trim());
            String trimmedPubmedId = sbdao.getPubmed_id();
            int pmid=Integer.parseInt(trimmedPubmedId);
            BibliosDAO chkBibDAO = new BibliosDAO();
            chkBibDAO = (BibliosDAO)bm.getPubmedIDByID(pmid);
            BibliosDAO bud = new BibliosDAO();
            if(chkBibDAO != null) {
                bud.setId_biblio(chkBibDAO.getId_biblio());
                //do nothing else
         } else {
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
            BibliosStrainsDAO bsd = new BibliosStrainsDAO();
                bsd.setBib_id_biblio(bud.getId_biblio());
                bsd.setStr_id_str(nsd.getId_str());

        }
        Sources_StrainsDAO ss = new Sources_StrainsDAO();
        ss.setSour_id(5);
        ss.setStr_id_str(nsd.getId_str());
        Set sourcesStrainsDAO = nsd.getSources_StrainsDAO();
        sourcesStrainsDAO.add(ss);
        //add DAO to StrainsDAO
        nsd.setSources_StrainsDAO(sourcesStrainsDAO);
stm.save(nsd);
        return new ModelAndView("/success");

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
                    obj.put("ilar", pd.getIlar_code());
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
}
