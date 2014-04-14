/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.emmanet.controllers;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.emmanet.model.ArchiveManager;
import org.emmanet.model.BibliosManager;
import org.emmanet.model.CategoriesStrainsDAO;
import org.emmanet.model.LabsDAO;
import org.emmanet.model.PeopleDAO;
import org.emmanet.model.PeopleManager;
import org.emmanet.model.StrainsDAO;
import org.emmanet.model.StrainsManager;
import org.emmanet.model.Strains_OmimDAO;
import org.emmanet.model.Strains_OmimManager;
import org.emmanet.util.Configuration;
import org.emmanet.util.DirFileList;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;

/**
 *
 * @author phil
 */
public class strainsUpdateInterfaceFormController extends SimpleFormController {

    private StrainsManager sm = new StrainsManager();
    private ArchiveManager am = new ArchiveManager();
    private PeopleManager pm = new PeopleManager();
    private BibliosManager bm = new BibliosManager();
    private Strains_OmimManager strainsOmimManager = new Strains_OmimManager();
    private Integer strainID;
    private String mtaPath;
    private String baseURL;
    private String recentBaseURL;
    private String serverPDFLocation;
    private String serverPDFLocationTail;
    private String serverPDFRecentLocation;
    private String thisisthebase;
    private String tmpDir;
    private List assocFiles;
    final static String SUBFORMUPLOAD = Configuration.get("SUBFORMUPLOAD");
    final static String UPLOADEDFILEURL = Configuration.get("UPLOADEDFILEURL");

    @Override
    protected Object formBackingObject(HttpServletRequest request) {

        //  if (request.getParameter("EditStrain") != null) {
        strainID = Integer.parseInt(request.getParameter("EditStrain"));



        /// END OF SUBMISSION FILE RETRIEVAL*/
        //}
        // SET IN SESSION THE MTAPATH
        HttpSession session = request.getSession(true);
        session.setAttribute("mtaPath", this.getMtaPath());
        //contact person display in http header

        StrainsDAO sd = sm.getStrainByID(strainID);
        String contactID = sd.getPer_id_per_contact();
        PeopleDAO pd = pm.getPerson(contactID);
        LabsDAO ld = pd.getLabsDAO();



        session.setAttribute("con_id", contactID);
        session.setAttribute("con_title", pd.getTitle());
        session.setAttribute("con_firstname", pd.getFirstname());
        session.setAttribute("con_surname", pd.getSurname());
        session.setAttribute("con_email", pd.getEmail());
        session.setAttribute("con_phone", pd.getPhone());
        session.setAttribute("con_fax", pd.getFax());
        session.setAttribute("con_institution", ld.getName());
        session.setAttribute("con_department", ld.getDept());
        session.setAttribute("con_address1", ld.getAddr_line_1());
        session.setAttribute("con_address2", ld.getAddr_line_2());
        session.setAttribute("con_town", ld.getTown());
        session.setAttribute("con_province", ld.getProvince());
        session.setAttribute("con_postcode", ld.getPostcode());
        session.setAttribute("con_country", ld.getCountry());



        //NOW SET NUMBER OF BIBLIOGRAPHIC REFS
        session.setAttribute("bibCount", bm.bibliosStrainCount(strainID));
        //NOW SET NUMBER OF RTOOLS REFS
        session.setAttribute("rtCount", sm.getRToolsCount(strainID));
        
        session.setAttribute("UPLOADEDFILEURL", UPLOADEDFILEURL);
        
        Set csd = sd.getCategoriesStrainsDAO();
        List cats = new ArrayList();

        for (Iterator i = csd.iterator(); i.hasNext();) {
            CategoriesStrainsDAO o = (CategoriesStrainsDAO) i.next();
            cats.add(o.getCategoriesDAO().getDescription());
        }
        request.setAttribute("categories", cats);

        // Create collection of OMIM ids.
        List<Strains_OmimDAO> strainsOmimList = strainsOmimManager.findById_Strains(strainID);
        List omims = new ArrayList();
        for (Strains_OmimDAO strains_omimDAO : strainsOmimList) {
            omims.add(strains_omimDAO.getOmimDAO().getOmim());
        }
        request.setAttribute("omims", omims);

//RETRIEVE ASSOCIATED SUBMISSION FILES IF PRESENT
        final String submissionID = sd.getSub_id_sub();
        System.out.println("SUBMISSION ID IS::" + submissionID);
        /*     request.setAttribute("associatedFiles", null);
         DirFileList filesx = new DirFileList();
         String fileList[];
         fileList = filesx.filteredFileList(SUBFORMUPLOAD, "pdf");
         System.out.println("filelist size IS::" + fileList.length);
         assocFiles = new ArrayList();
         if (fileList != null && submissionID != null) {
         for (int i = 0; i < fileList.length; i++) {
         System.out.println("FILELIST VALUE " + i + "==" + fileList[i]);
         if (fileList[i].startsWith(submissionID)) {
         assocFiles.add(fileList[i]);
         }
         }
         }*/

        request.setAttribute("associatedFiles", null);
        assocFiles = new ArrayList();
        File dir = new File(SUBFORMUPLOAD);
        File[] files;
        files = dir.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                //only add to list if submission id is conatined within file name (which includes path and file name)
                if (submissionID != null && name.contains(submissionID)) {
                    return name.toLowerCase().endsWith(".pdf");
                }
                return false;
            }
        });
        System.out.println("Files size is::" + files.length);
        if (files != null && submissionID != null) {
            for (int i = 0; i < files.length; i++) {
                System.out.println("FILEname VALUE " + i + "==" + files[i].getName());
                String file = files[i].getName();
                if (file.startsWith(submissionID)) {
                    assocFiles.add(file);
                }
            }
        }

        request.setAttribute("associatedFiles", assocFiles);

        // SET IN SESSION THE MTAPATH
        // HttpSession session = request.getSession(true);
        //session.setAttribute("mtaPath", this.getMtaPath());
        System.out.println("here follows different accessors to peopleDAOs:-");
        System.out.println(sd.getPeopleDAOCon().getLabsDAO().getDept());
        //    System.out.println(sd.getSubPeopleDAO().getFirstname());
        //      System.out.println(sd.getConPeopleDAO().getSurname());
        return sd;
    }
    // SAVE

    @Override
    protected ModelAndView onSubmit(
            HttpServletRequest request,
            HttpServletResponse response,
            Object command,
            BindException errors)
            throws ServletException, Exception {
        StrainsDAO sDAO = (StrainsDAO) command;
        //BackgroundDAO bDAO=(BackgroundDAO) sDAO.getBackgroundDAO();
//line below prevents java.sql.SQLException: Incorrect integer value: '' for column 'mgi_ref' at row 1
//errors caused by an empty string value
        if (sDAO.getMgi_ref() != null && sDAO.getMgi_ref().equals("")) {
            sDAO.setMgi_ref(null);
        }
        if (sDAO.getGeneration() != null && sDAO.getGeneration().equals("")) {
            sDAO.setGeneration(null);
        }
        if (sDAO.getResiduesDAO().getReproductive_maturity_age() != null && sDAO.getResiduesDAO().getReproductive_maturity_age().equals("")) {
            sDAO.getResiduesDAO().setReproductive_maturity_age(null);
        }
        if (sDAO.getResiduesDAO().getReproductive_decline_age() != null && sDAO.getResiduesDAO().getReproductive_decline_age().equals("")) {
            sDAO.getResiduesDAO().setReproductive_decline_age(null);
        }
        if (sDAO.getResiduesDAO().getGestation_length() != null && sDAO.getResiduesDAO().getGestation_length().equals("")) {
            sDAO.getResiduesDAO().setGestation_length(null);
        }
        if (sDAO.getResiduesDAO().getPups_at_birth() != null && sDAO.getResiduesDAO().getPups_at_birth().equals("")) {
            sDAO.getResiduesDAO().setPups_at_birth(null);
        }
        if (sDAO.getResiduesDAO().getPups_at_weaning() != null && sDAO.getResiduesDAO().getPups_at_weaning().equals("")) {
            sDAO.getResiduesDAO().setPups_at_weaning(null);
        }
        if (sDAO.getResiduesDAO().getWeaning_age() != null && sDAO.getResiduesDAO().getWeaning_age().equals("")) {
            sDAO.getResiduesDAO().setWeaning_age(null);
        }
        if (sDAO.getResiduesDAO().getLitters_in_lifetime() !=null && sDAO.getResiduesDAO().getLitters_in_lifetime().equals("")) {
            sDAO.getResiduesDAO().setLitters_in_lifetime(null);
        }
        if (sDAO.getResiduesDAO().getBreeding_performance() != null && sDAO.getResiduesDAO().getBreeding_performance().equals("")) {
            sDAO.getResiduesDAO().setBreeding_performance(null);
        }
        // System.out.println("before Residues char genotype is " + sDAO.getResiduesDAO().getChar_genotyping());
        // sDAO.setBackgroundDAO(bDAO);
        sm.save(sDAO);
        //  System.out.println("after Residues char genotype is " + sDAO.getResiduesDAO().getChar_genotyping());
        // System.out.println("saved");
        request.getSession().setAttribute(
                "message",
                getMessageSourceAccessor().getMessage("Message",
                "Your update submitted successfully"));

        return new ModelAndView(getSuccessView() + "?EditStrain=" + strainID);
        // ModelAndView mav = showForm(request, response, errors);
        // return mav;//

    }

    public String getMtaPath() {
        return mtaPath;
    }

    public void setMtaPath(String mtaPath) {
        this.mtaPath = mtaPath;
    }

    public String getBaseURL() {
        return baseURL;
    }

    public void setBaseURL(String baseURL) {
        this.baseURL = baseURL;
    }

    public String getRecentBaseURL() {
        return recentBaseURL;
    }

    public void setRecentBaseURL(String recentBaseURL) {
        this.recentBaseURL = recentBaseURL;
    }

    public String getServerPDFLocation() {
        return serverPDFLocation;
    }

    public void setServerPDFLocation(String serverPDFLocation) {
        this.serverPDFLocation = serverPDFLocation;
    }

    public String getServerPDFLocationTail() {
        return serverPDFLocationTail;
    }

    public void setServerPDFLocationTail(String serverPDFLocationTail) {
        this.serverPDFLocationTail = serverPDFLocationTail;
    }

    public String getServerPDFRecentLocation() {
        return serverPDFRecentLocation;
    }

    public void setServerPDFRecentLocation(String serverPDFRecentLocation) {
        this.serverPDFRecentLocation = serverPDFRecentLocation;
    }

    public String getTmpDir() {
        return tmpDir;
    }

    public void setTmpDir(String tmpDir) {
        this.tmpDir = tmpDir;
    }

    /**
     * @return the assocFiles
     */
    public List getAssocFiles() {
        return assocFiles;
    }

    /**
     * @param assocFiles the assocFiles to set
     */
    public void setAssocFiles(List assocFiles) {
        this.assocFiles = assocFiles;
    }
}
