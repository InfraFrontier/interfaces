/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.emmanet.controllers;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.emmanet.model.MutationsManager;
import org.emmanet.model.SubmissionMutationsDAO;
import org.emmanet.util.Encrypter;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;

/**
 *
 * @author phil
 */
public class SubmissionMutationsController /* implements Controller*/ extends SimpleFormController {

    private SubmissionMutationsDAO smd;
    private List mutdaos;
    private MutationsManager mm = new MutationsManager();
    private static String MAP_KEY = "returnedOut";
    private Map returnedOut = new HashMap();
    private Encrypter enc = new Encrypter();
    private String alleleMGI;
    private String chrom;
    private String chromName;
    private String chromDesc;
    private String domPattern;
    private String esCell;
    private String lineNumber;
    private String geneMGI;
    private String mut;
    private String bg;
    private String bgText;
    private String plasmid;
    private String promoter;
    private String type;
    private String subtype;
    private String transMGI;
    private int ID = 0;

    //  public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
    @Override
    protected Object formBackingObject(HttpServletRequest request) {
        System.out.println("THE BACKGROUND OBJECT HAS BEEN CALLED DURING VALIDATION");

        setSmd(new SubmissionMutationsDAO());
        alleleMGI = request.getParameter("mutation_allele_mgi_symbol");
        chrom = request.getParameter("mutation_chrom");
        chromName = request.getParameter("mutation_chrom_anomaly_name");
        chromDesc = request.getParameter("mutation_chrom_anomaly_descr");
        domPattern = request.getParameter("mutation_dominance_pattern");
        esCell = request.getParameter("mutation_es_cell_line");
        lineNumber = request.getParameter("mutation_founder_line_number");
        geneMGI = request.getParameter("mutation_gene_mgi_symbol");
        mut = request.getParameter("mutation_mutagen");
        bg = request.getParameter("mutation_original_backg");
        bgText = request.getParameter("mutation_original_backg_text");
        plasmid = request.getParameter("mutation_plasmid");
        promoter = request.getParameter("mutation_promoter");
        type = request.getParameter("mutation_type");
        System.out.println("TTYPE=" + type);
        //subtype = "";
        getSmd().setMutation_type(getType());
          if (getType() != null) {
                switch (getType()) {
                    case "CH":
                        setSubtype(request.getParameter("mutation_subtypeCH"));
                        System.out.println("subTYPE=" + subtype);
                        getSmd().setMutation_subtype(subtype);
                        System.out.println(type + " CH type so mut sub is " + request.getParameter("mutation_subtypeCH"));
                        break;
                    case "IN":
                        setSubtype(request.getParameter("mutation_subtypeIN"));
                        getSmd().setMutation_subtype(subtype);
                        System.out.println(type + " IN type so mut sub is " + request.getParameter("mutation_subtypeIN"));
                        break;
                    case "TM":
                        setSubtype(request.getParameter("mutation_subtypeTM"));
                        getSmd().setMutation_subtype(subtype);
                        System.out.println(type + " TM type so mut sub is " + request.getParameter("mutation_subtypeTM"));
                        break;
                }
            }
        //  System.out.println("controller 93 " + getSubtype() + "--" + smd.);
       return getSmd();
    }

    @Override
    protected ModelAndView onSubmit(
            HttpServletRequest request,
            HttpServletResponse response,
            Object command,
            BindException errors) {

        setSmd(new SubmissionMutationsDAO());
        String IDFromSession = request.getParameter("IDFromSession");

        setMutdaos(new ArrayList());

        if (request.getParameter("action").equals("add")) {

            if (getType() != null) {
                switch (getType()) {
                    case "CH":
                        setSubtype(request.getParameter("mutation_subtypeCH"));
                        
                        System.out.println(type + " CH type so mut sub is " + request.getParameter("mutation_subtypeCH"));
                        break;
                    case "IN":
                        setSubtype(request.getParameter("mutation_subtypeIN"));
                        System.out.println(type + " IN type so mut sub is " + request.getParameter("mutation_subtypeIN"));
                        break;
                    case "TM":
                        setSubtype(request.getParameter("mutation_subtypeTM"));
                        System.out.println(type + " TM type so mut sub is " + request.getParameter("mutation_subtypeTM"));
                        break;
                        
                        default:  setSubtype(null);
                }
            }
            transMGI = request.getParameter("mutation_transgene_mgi_symbol");

            // String IDFromSession = request.getParameter("IDFromSession");


            String encryptedID = request.getParameter("Id_sub");
            if (encryptedID.isEmpty()) {
                encryptedID = IDFromSession;//request.getParameter("IDFromSession");
            }
            System.out.println("idfromsession==" + IDFromSession);//request.getParameter("IDFromSession"));
            System.out.println("## id==" + encryptedID);
            try {
                // encryptedID = java.net.URLEncoder.encode(encryptedID, "UTF-8");
                encryptedID = java.net.URLDecoder.decode(encryptedID, "UTF-8");
            } catch (UnsupportedEncodingException ex) {
                Logger.getLogger(SubmissionMutationsController.class.getName()).log(Level.SEVERE, null, ex);
            }
            encryptedID = getEnc().decrypt(encryptedID);
            int decryptedID = Integer.parseInt(encryptedID);
            System.out.println("DECRYPTED++==" + decryptedID);
            getSmd().setId_sub(decryptedID);


            getSmd().setMutation_allele_mgi_symbol(getAlleleMGI());
            getSmd().setMutation_chrom(getChrom());
            getSmd().setMutation_chrom_anomaly_name(getChromName());
            getSmd().setMutation_chrom_anomaly_descr(getChromDesc());
            getSmd().setMutation_dominance_pattern(getDomPattern());
            getSmd().setMutation_es_cell_line(getEsCell());
            getSmd().setMutation_founder_line_number(getLineNumber());

            getSmd().setMutation_gene_mgi_symbol(getGeneMGI());
            getSmd().setMutation_mutagen(getMut());
            getSmd().setMutation_original_backg(getBg());
            getSmd().setMutation_original_backg_text(getBgText());
            getSmd().setMutation_plasmid(getPlasmid());
            getSmd().setMutation_promoter(getPromoter());
            getSmd().setMutation_subtype(getSubtype());
            //System.out.println("MUTATION SUBTYPE == ++ " + subtype);
            getSmd().setMutation_transgene_mgi_symbol(transMGI);
            getSmd().setMutation_type(getType());
            //mutdaos.add(smd);
            //System.out.println("DEBUG INTEGER ISSUE LINE 72::" + smd.getId_sub());
            //int id_sub
            setID(getSmd().getId_sub());
            //System.out.println("DEBUG LINE 134::" + type);
            if (!type.isEmpty()) {
                // SubmissionMutationsValidator smv = new SubmissionMutationsValidator();
                // smv.supports(null);
                // Errors e = new Errors();
                // getSmv().validate(getSmd(), getE());
                //System.out.println("ERRORS + " + getE().getAllErrors().toString());
                getMm().save(getSmd());
                setMutdaos(getMm().getSubMutationBySubID(getID()));
            }
        } else if (request.getParameter("action").equals("edit")) {
            //edit records
            //get record from id
            //mutdaos = mm.getSubMutationBySubID(Integer.parseInt(enc.decrypt(request.getParameter("Id_sub"))));
            System.out.print("id_Mut==" + Integer.parseInt(request.getParameter("Id_mut")));



            System.out.println("EDITING, ");
            if (request.getParameter("action2").equals("editRecord")) {
             smd = getMm().getSubMutationBySubMutID(Integer.parseInt(request.getParameter("Id_mut")));
                getReturnedOut().put("SubMutDAO", smd);
                smd.setMutation_allele_mgi_symbol(getAlleleMGI());
                smd.setMutation_chrom(getChrom());
                smd.setMutation_chrom_anomaly_name(getChromName());
                smd.setMutation_chrom_anomaly_descr(getChromDesc());
                smd.setMutation_dominance_pattern(getDomPattern());
                smd.setMutation_es_cell_line(getEsCell());
                smd.setMutation_founder_line_number(getLineNumber());

                smd.setMutation_gene_mgi_symbol(getGeneMGI());
                smd.setMutation_mutagen(getMut());
                smd.setMutation_original_backg(getBg());
                smd.setMutation_original_backg_text(getBgText());
                smd.setMutation_plasmid(getPlasmid());
                smd.setMutation_promoter(getPromoter());
                smd.setMutation_subtype(getSubtype());
                smd.setMutation_transgene_mgi_symbol(getTransMGI());
                smd.setMutation_type(getType());

                System.out.println("CHECK FOR  CURRENT DAO AND THAT IT IS CORRECT: " + smd.getMutation_type() + " == " + smd.getMutation_original_backg());
                //save
                getMm().save(smd);
                setMutdaos(getMm().getSubMutationBySubID(Integer.parseInt(getEnc().decrypt(request.getParameter("Id_sub")))));
            }
            //load record in fields

        } else if (request.getParameter("action").equals("get")) {
            if (request.getParameter("Id_sub").isEmpty()) {
                //ID = Integer.parseInt(enc.decrypt(request.getParameter("sessencID")));
                setID(Integer.parseInt(getEnc().decrypt(request.getParameter("IDFromSession"))));
            } else {
                setID(Integer.parseInt(getEnc().decrypt(request.getParameter("Id_sub"))));
            }

            System.out.println("ID||| " + getID());
            setMutdaos(getMm().getSubMutationBySubID(getID()));

        } else if (request.getParameter("action").equals("delete")) {
            System.out.println("DELETING " + request.getParameter("Id_mut"));
            getMm().delete(Integer.parseInt(request.getParameter("Id_mut")));
            setMutdaos(getMm().getSubMutationBySubID(Integer.parseInt(getEnc().decrypt(request.getParameter("Id_sub")))));
        }
        //return new ModelAndView("ajaxMutations.emma");
        System.out.println("mutdaos size==" + getMutdaos().size());
        int count = getMutdaos().size();
        if (getSmd() != null) {
            getSmd().setMutationCount(getMutdaos().size());
        }

        getReturnedOut().put("count", count);
        getReturnedOut().put("mutdaos", getMutdaos());
        return new ModelAndView("/publicSubmission/ajaxMutations", getMAP_KEY(), getReturnedOut());
    }
    
    /**
     * @return the MAP_KEY
     */
    public static String getMAP_KEY() {
        return MAP_KEY;
    }

    /**
     * @param aMAP_KEY the MAP_KEY to set
     */
    public static void setMAP_KEY(String aMAP_KEY) {
        MAP_KEY = aMAP_KEY;
    }

    /**
     * @return the smd
     */
    public SubmissionMutationsDAO getSmd() {
        return smd;
    }

    /**
     * @param smd the smd to set
     */
    public void setSmd(SubmissionMutationsDAO smd) {
        this.smd = smd;
    }

    /**
     * @return the mutdaos
     */
    public List getMutdaos() {
        return mutdaos;
    }

    /**
     * @param mutdaos the mutdaos to set
     */
    public void setMutdaos(List mutdaos) {
        this.mutdaos = mutdaos;
    }

    /**
     * @return the mm
     */
    public MutationsManager getMm() {
        return mm;
    }

    /**
     * @param mm the mm to set
     */
    public void setMm(MutationsManager mm) {
        this.mm = mm;
    }

    /**
     * @return the returnedOut
     */
    public Map getReturnedOut() {
        return returnedOut;
    }

    /**
     * @param returnedOut the returnedOut to set
     */
    public void setReturnedOut(Map returnedOut) {
        this.returnedOut = returnedOut;
    }

    /**
     * @return the enc
     */
    public Encrypter getEnc() {
        return enc;
    }

    /**
     * @param enc the enc to set
     */
    public void setEnc(Encrypter enc) {
        this.enc = enc;
    }

    /**
     * @return the alleleMGI
     */
    public String getAlleleMGI() {
        return alleleMGI;
    }

    /**
     * @param alleleMGI the alleleMGI to set
     */
    public void setAlleleMGI(String alleleMGI) {
        this.alleleMGI = alleleMGI;
    }

    /**
     * @return the chrom
     */
    public String getChrom() {
        return chrom;
    }

    /**
     * @param chrom the chrom to set
     */
    public void setChrom(String chrom) {
        this.chrom = chrom;
    }

    /**
     * @return the chromName
     */
    public String getChromName() {
        return chromName;
    }

    /**
     * @param chromName the chromName to set
     */
    public void setChromName(String chromName) {
        this.chromName = chromName;
    }

    /**
     * @return the chromDesc
     */
    public String getChromDesc() {
        return chromDesc;
    }

    /**
     * @param chromDesc the chromDesc to set
     */
    public void setChromDesc(String chromDesc) {
        this.chromDesc = chromDesc;
    }

    /**
     * @return the domPattern
     */
    public String getDomPattern() {
        return domPattern;
    }

    /**
     * @param domPattern the domPattern to set
     */
    public void setDomPattern(String domPattern) {
        this.domPattern = domPattern;
    }

    /**
     * @return the esCell
     */
    public String getEsCell() {
        return esCell;
    }

    /**
     * @param esCell the esCell to set
     */
    public void setEsCell(String esCell) {
        this.esCell = esCell;
    }

    /**
     * @return the lineNumber
     */
    public String getLineNumber() {
        return lineNumber;
    }

    /**
     * @param lineNumber the lineNumber to set
     */
    public void setLineNumber(String lineNumber) {
        this.lineNumber = lineNumber;
    }

    /**
     * @return the geneMGI
     */
    public String getGeneMGI() {
        return geneMGI;
    }

    /**
     * @param geneMGI the geneMGI to set
     */
    public void setGeneMGI(String geneMGI) {
        this.geneMGI = geneMGI;
    }

    /**
     * @return the mut
     */
    public String getMut() {
        return mut;
    }

    /**
     * @param mut the mut to set
     */
    public void setMut(String mut) {
        this.mut = mut;
    }

    /**
     * @return the bg
     */
    public String getBg() {
        return bg;
    }

    /**
     * @param bg the bg to set
     */
    public void setBg(String bg) {
        this.bg = bg;
    }

    /**
     * @return the bgText
     */
    public String getBgText() {
        return bgText;
    }

    /**
     * @param bgText the bgText to set
     */
    public void setBgText(String bgText) {
        this.bgText = bgText;
    }

    /**
     * @return the plasmid
     */
    public String getPlasmid() {
        return plasmid;
    }

    /**
     * @param plasmid the plasmid to set
     */
    public void setPlasmid(String plasmid) {
        this.plasmid = plasmid;
    }

    /**
     * @return the promoter
     */
    public String getPromoter() {
        return promoter;
    }

    /**
     * @param promoter the promoter to set
     */
    public void setPromoter(String promoter) {
        this.promoter = promoter;
    }

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return the subtype
     */
    public String getSubtype() {
        return subtype;
    }

    /**
     * @param subtype the subtype to set
     */
    public void setSubtype(String subtype) {
        this.subtype = subtype;
    }

    /**
     * @return the ID
     */
    public int getID() {
        return ID;
    }

    /**
     * @param ID the ID to set
     */
    public void setID(int ID) {
        this.ID = ID;
    }

    /**
     * @return the transMGI
     */
    public String getTransMGI() {
        return transMGI;
    }

    /**
     * @param transMGI the transMGI to set
     */
    public void setTransMGI(String transMGI) {
        this.transMGI = transMGI;
    }
}
