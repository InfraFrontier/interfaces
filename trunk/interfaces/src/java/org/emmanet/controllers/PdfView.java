/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.emmanet.controllers;

import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.emmanet.jobs.WebRequests;
import org.emmanet.model.WebRequestsDAO;
import org.springframework.web.servlet.view.document.AbstractPdfView;

import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Image;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import java.awt.Color;
import java.io.File;
import java.io.FilenameFilter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.servlet.ServletContext;
import org.emmanet.model.BibliosManager;
import org.emmanet.model.BibliosStrainsDAO;
import org.emmanet.model.CategoriesStrainsDAO;
import org.emmanet.model.MutationsStrainsDAO;
import org.emmanet.model.PeopleDAO;
import org.emmanet.model.PeopleManager;
import org.emmanet.model.RToolsDAO;
import org.emmanet.model.StrainsDAO;
import org.emmanet.model.StrainsManager;
import org.emmanet.model.Strains_OmimDAO;
import org.emmanet.model.Strains_OmimManager;
import org.emmanet.model.Syn_StrainsDAO;
import org.emmanet.util.Configuration;
import org.emmanet.util.Utils;

/**
 *
 * @author phil
 */
public class PdfView extends AbstractPdfView {

    private String pdfTitle;
    private boolean pdfConditions;
    final static String SUBFORMUPLOAD = Configuration.get("SUBFORMUPLOAD");
    final static String UPLOADEDFILEURL = Configuration.get("UPLOADEDFILEURL");

    @Override
    protected void buildPdfDocument(Map map, Document doc, PdfWriter writer, HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (map.get("WebRequestsDAO") != null && request.getParameter("type").equals("req")) {
            WebRequestsDAO wrd = new WebRequestsDAO();
            WebRequests wr = new WebRequests();
            wrd = (WebRequestsDAO) map.get("WebRequestsDAO");

            if (wrd.getRegister_interest() != null) {
                if (wrd.getRegister_interest().equals("1")) {
                    pdfTitle = "EMMA Strain Interest Registration Form";
                    pdfConditions = false;
                } else if (wrd.getRegister_interest().equals("0")) {
                    pdfTitle = "EMMA Mutant Request Form";
                    pdfConditions = true;
                }
            }

            Paragraph pHead = new Paragraph(
                    pdfTitle + "\n\n", FontFactory.getFont(
                    FontFactory.HELVETICA, 11));
            pHead.setAlignment(Element.ALIGN_CENTER);
            doc.add(pHead);
            doc.add(new Paragraph(pdfTitle + "\nRequest ID:" + wrd.getId_req() + "\n\n",
                    FontFactory.getFont(FontFactory.HELVETICA_BOLD, 20)));
            Paragraph pSubHead = new Paragraph(
                    "Following data have been submitted to EMMA on " + wrd.getTimestamp(), FontFactory.getFont(
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
            table.addCell("" + wrd.getSci_title());
            table.addCell("Firstname");
            table.addCell("" + wrd.getSci_firstname());
            table.addCell("Surname");
            table.addCell("" + wrd.getSci_surname());
            table.addCell("E-mail");
            table.addCell("" + wrd.getSci_e_mail());
            table.addCell("Phone");
            table.addCell("" + wrd.getSci_phone());
            table.addCell("Fax");
            table.addCell("" + wrd.getSci_fax());
            PdfPCell cellShip = new PdfPCell(new Paragraph(
                    "\nShipping Contact\n\n", font));
            cellShip.setColspan(2);
            cellShip.setBorder(0);
            table.addCell(cellShip);

            table.addCell("Title");
            table.addCell("" + wrd.getCon_title());
            table.addCell("Firstname");
            table.addCell("" + wrd.getCon_firstname());
            table.addCell("Surname");
            table.addCell("" + wrd.getCon_surname());
            table.addCell("E-mail");
            table.addCell("" + wrd.getCon_e_mail());
            table.addCell("Phone");
            table.addCell("" + wrd.getCon_phone());
            table.addCell("Fax");
            table.addCell("" + wrd.getCon_fax());
            table.addCell("Institution");
            table.addCell("" + wrd.getCon_institution());
            table.addCell("Department");
            table.addCell("" + wrd.getCon_dept());
            table.addCell("Address Line 1");
            table.addCell("" + wrd.getCon_addr_1());
            table.addCell("Address Line 2");
            table.addCell("" + wrd.getCon_addr_2());
            table.addCell("County/province");
            table.addCell("" + wrd.getCon_province());
            table.addCell("Town");
            table.addCell("" + wrd.getCon_town());
            table.addCell("Postcode");
            table.addCell("" + wrd.getCon_postcode());
            table.addCell("Country");
            table.addCell("" + wrd.getCon_country());

            if (!wrd.getRegister_interest().equals("1")) {
                //webrequest is not a roi so these were set above so send them to pdf
                PdfPCell cellBill = new PdfPCell(new Paragraph(
                        "\nBilling Details\n\n", font));
                cellBill.setColspan(2);
                cellBill.setBorder(0);
                table.addCell(cellBill);

                table.addCell("VAT reference");
                table.addCell("" + wrd.getBil_vat());
                table.addCell("Purchase Order Number");
                table.addCell("" + wrd.getPO_ref());
                table.addCell("Title");
                table.addCell("" + wrd.getBil_title());
                table.addCell("Firstname");
                table.addCell("" + wrd.getBil_firstname());
                table.addCell("Surname");
                table.addCell("" + wrd.getBil_surname());
                table.addCell("E-mail");
                table.addCell("" + wrd.getBil_e_mail());
                table.addCell("Phone");
                table.addCell("" + wrd.getBil_phone());
                table.addCell("Fax");
                table.addCell("" + wrd.getBil_fax());
                table.addCell("Institution");
                table.addCell("" + wrd.getBil_institution());
                table.addCell("Department");
                table.addCell("" + wrd.getBil_dept());
                table.addCell("Address Line 1");
                table.addCell("" + wrd.getBil_addr_1());
                table.addCell("Address Line 2");
                table.addCell("" + wrd.getBil_addr_2());
                table.addCell("County/province");
                table.addCell("" + wrd.getBil_province());
                table.addCell("Town");
                table.addCell("" + wrd.getBil_town());
                table.addCell("Postcode");
                table.addCell("" + wrd.getBil_postcode());
                table.addCell("Country");
                table.addCell("" + wrd.getBil_country());

                // TODO add europhenome and wtsi_mouse_portal info
            }

            PdfPCell cellStrain = new PdfPCell(new Paragraph(
                    "\nStrain Details\n\n", font));
            cellStrain.setColspan(2);
            cellStrain.setBorder(0);

            table.addCell(cellStrain);
            table.addCell("Strain ID");
            table.addCell("" + wrd.getStrain_id());
            table.addCell("Strain name");
            table.addCell("" + wrd.getStrain_name());
            table.addCell("Common Name(s)");
            table.addCell("" + wrd.getCommon_name_s());

            if (wrd.getReq_material() != null) {
                PdfPCell cellMaterial = new PdfPCell(new Paragraph(
                        "\nRequested Material\n\n", font));
                cellMaterial.setColspan(2);
                cellMaterial.setBorder(0);

                table.addCell(cellMaterial);
                table.addCell("Material");
                table.addCell("" + wrd.getReq_material());
            }

            if (wrd.getLive_animals() != null) {
                table.addCell("Live Animals");
                table.addCell("Selected");
            }

            if (wrd.getFrozen_emb() != null) {
                table.addCell("Frozen Embryos");
                table.addCell("Selected");
            }

            if (wrd.getFrozen_spe() != null) {
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
                if (wrd.getApplication_type().equals("request_only")) {
                    text =
                            new StringBuilder().append(text).append(
                            "\nYou have indicated that you have read the conditions and agree to pay the transmittal fee " + "plus shipping costs.").toString();

                    header =
                            new StringBuilder().append(header).append("\nStandard request\n").toString();

                } else if (!wrd.getApplication_type().equals("request_only")) {
                    header1 =
                            new StringBuilder().append(header1).append("\nApplication for Transnational Access Activity").toString();
                    if (wrd.getApplication_type().equals("ta_only")) {
                        text1 =
                                new StringBuilder().append(text1).append("\nYou have indicated that you have read the conditions and have applied for free of charge TA only. "
                                + "In the case of the TA application being rejected the request process will be terminated.").toString();
                        header1 =
                                new StringBuilder().append(header1).append(" (TA Option B)\n").toString();
                    } else {
                        text1 =
                                new StringBuilder().append(text1).append("\nYou have indicated that you have read the conditions and have applied for free of charge TA "
                                + "and have agreed to pay the service charge plus shipping cost if the TA application is rejected.").toString();
                        header1 =
                                new StringBuilder().append(header1).append(" (TA Option A)\n").toString();
                    }

                    header2 =
                            new StringBuilder().append(header2).append("\n\nDescription of project (1/2 page) involving requested EMMA mouse mutant resource. "
                            + "The project description will be used by the Evaluation Committee for selection of applicants:").toString();
                }

                if (!wrd.getApplication_type().equals("request_only")) {
                    //  table.addCell("" + model.get("ta_proj_desc"));
                    text2 =
                            new StringBuilder().append(text2).append("\n\n ").append(wrd.getProject_description()).toString();
                }

                if (!wrd.getApplication_type().equals("request_only")) {
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
        }

        if (map.get("StrainsDAO") != null && request.getParameter("type").equals("sub")) {
            StrainsDAO sd;
            StrainsManager sm = new StrainsManager();
            PeopleDAO pd;
            PeopleDAO subPDAO = new PeopleDAO();
            PeopleManager pm = new PeopleManager();
            BibliosManager bm = new BibliosManager();

            sd = (StrainsDAO) map.get("StrainsDAO");
            pd = pm.getPerson(sd.getPer_id_per_contact());//For shipping details

            if (sd.getPer_id_per_sub() != null) {
                subPDAO = pm.getPerson(sd.getPer_id_per_sub());
            }
            
            //RETRIEVE ASSOCIATED SUBMISSION FILES IF PRESENT

            final String submissionID = sd.getSub_id_sub();
            System.out.println("SUBMISSION ID IS::" + submissionID);

            List assocFilesADDITIONAL = new ArrayList();
            List assocFilesSANITARY = new ArrayList();
            List assocFilesCHARACTERISATION = new ArrayList();

            File dir = new File(SUBFORMUPLOAD);
            File[] files;
            files = dir.listFiles(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    //only add to list if submission id is conatined within file name (which includes path and file name)
                    if (submissionID != null && name.startsWith(submissionID)) {
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
                    if (file.startsWith(submissionID) && file.contains((CharSequence) "ADDITIONAL")) {
                        assocFilesADDITIONAL.add(file);
                        System.out.println("ADDITIONAL file " + file);
                    } else if (file.startsWith(submissionID) && file.contains((CharSequence) "SANITARYSTATUS")) {
                        assocFilesSANITARY.add(file);
                        System.out.println("SANITARY file " + file);
                    } else {
                        assocFilesCHARACTERISATION.add(file);
                        System.out.println("CHARACTERISATION file " + file);
                    }
                }
            }


            ServletContext servletContext = request.getSession().getServletContext();
            URL infrafrontierIconURL = servletContext.getResource("/images/infrafrontier/icon/footerlogo.jpg");
            logger.debug("infrafrontierIconURL = " + infrafrontierIconURL);
            URL emmaIconURL = servletContext.getResource("/images/infrafrontier/icon/emma-logo-soft.png");
            logger.debug("emmaIconURL = " + emmaIconURL);
            Image infrafrontierIcon = Image.getInstance(infrafrontierIconURL);
            Image emmaIcon = Image.getInstance(emmaIconURL);
            
            pdfTitle = "EMMA Mutant Submission Form";
            doc.add(new Chunk(infrafrontierIcon, 0, 0));
            doc.add(new Chunk(emmaIcon, 320, 0));
            doc.add(Chunk.NEWLINE);
            doc.add(Chunk.NEWLINE);
            Paragraph paragraph = new Paragraph(pdfTitle, FontFactory.getFont(FontFactory.HELVETICA_BOLD, 20));
            paragraph.setAlignment(Element.ALIGN_CENTER);
            doc.add(paragraph);
            
            float[] widths = {0.25f, 0.75f};
            PdfPTable table = new PdfPTable(widths);
            table.setWidthPercentage(100);
            PdfPCell cell;

            Paragraph pSubHead = new Paragraph("\nThe Following data have been submitted to EMMA on " + sd.getArchiveDAO().getSubmitted(), FontFactory.getFont(
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
            table = new PdfPTable(widths);
            table.setWidthPercentage(100);

            // Genotype preparation - strain name [actually, synonym] and description
            // Jira request EMMA-586 - use strain.name instead of synonym (requested by Sabine)
            String strainName = sd.getName();
            pSubHead = new Paragraph(
                    strainName + " / " + sd.getEmma_id() + "\n\n", FontFactory.getFont(
                    FontFactory.HELVETICA_BOLD, 11));
            pSubHead.setAlignment(Element.ALIGN_CENTER);
            doc.add(pSubHead);

            // Submitter
            cell = new PdfPCell(new Paragraph("\nSubmitter (Steps 1 and 2 of 11)\n\n", font));
            cell.setColspan(2);
            cell.setBorder(0);
            if(sd.getPer_id_per_sub() != null) {
                table.addCell(cell);
                table.addCell("Title");
                table.addCell("" + subPDAO.getTitle());
                table.addCell("Firstname");
                table.addCell("" + subPDAO.getFirstname());
                table.addCell("Surname");
                table.addCell("" + subPDAO.getSurname());
                table.addCell("E-mail");
                table.addCell("" + subPDAO.getEmail());
                table.addCell("Phone");
                table.addCell("" + subPDAO.getPhone());
                table.addCell("Fax");
                table.addCell("" + subPDAO.getFax());
            }
            if(subPDAO.getLabsDAO() != null) {
                table.addCell("Institution");
                table.addCell("" + subPDAO.getLabsDAO().getName());
                table.addCell("Department");
                table.addCell("" + subPDAO.getLabsDAO().getDept());
                table.addCell("Address Line 1");
                table.addCell("" + subPDAO.getLabsDAO().getAddr_line_1());
                table.addCell("Address Line 2");
                table.addCell("" + subPDAO.getLabsDAO().getAddr_line_2());
                table.addCell("County/province");
                table.addCell("" + subPDAO.getLabsDAO().getProvince());
                table.addCell("Town");
                table.addCell("" + subPDAO.getLabsDAO().getTown());
                table.addCell("Postcode");
                table.addCell("" + subPDAO.getLabsDAO().getPostcode());
                table.addCell("Country");
                table.addCell("" + subPDAO.getLabsDAO().getCountry());
            }
            doc.add(table);
            doc.add(Chunk.NEWLINE);
            doc.add(Chunk.NEWLINE);
            doc.add(underlined);
            table = new PdfPTable(widths);
            table.setWidthPercentage(100);
            
            // Producer
            cell = new PdfPCell(new Paragraph("Producer (Step 3 of 11)\n\n", font));
            cell.setColspan(2);
            cell.setBorder(0);
            table.addCell(cell);

            table.addCell("Title");
            table.addCell("" + sd.getPeopleDAO().getTitle());
            table.addCell("Firstname");
            table.addCell("" + sd.getPeopleDAO().getFirstname());
            table.addCell("Surname");
            table.addCell("" + sd.getPeopleDAO().getSurname());
            table.addCell("E-mail");
            table.addCell("" + sd.getPeopleDAO().getEmail());
            table.addCell("Phone");
            table.addCell("" + sd.getPeopleDAO().getPhone());
            table.addCell("Fax");
            table.addCell("" + sd.getPeopleDAO().getFax());
            table.addCell("Institution");
            table.addCell("" + sd.getPeopleDAO().getLabsDAO().getName());
            table.addCell("Department");
            table.addCell("" + sd.getPeopleDAO().getLabsDAO().getDept());
            table.addCell("Address Line 1");
            table.addCell("" + sd.getPeopleDAO().getLabsDAO().getAddr_line_1());
            table.addCell("Address Line 2");
            table.addCell("" + sd.getPeopleDAO().getLabsDAO().getAddr_line_2());
            table.addCell("County/province");
            table.addCell("" + sd.getPeopleDAO().getLabsDAO().getProvince());
            table.addCell("Town");
            table.addCell("" + sd.getPeopleDAO().getLabsDAO().getTown());
            table.addCell("Postcode");
            table.addCell("" + sd.getPeopleDAO().getLabsDAO().getPostcode());
            table.addCell("Country");
            table.addCell("" + sd.getPeopleDAO().getLabsDAO().getCountry());
            Integer id_ilar = Utils.tryParseInt(sd.getPeopleDAO().getId_ilar());
            if ((id_ilar != null) && (id_ilar.intValue() > 0)) {                // If there is a valid ILAR, display it.
                table.addCell("ILAR");
                table.addCell(sd.getPeopleDAO().getIlarDAO().getLabcode());
            }

            doc.add(table);
            doc.add(Chunk.NEWLINE);
            doc.add(Chunk.NEWLINE);
            doc.add(underlined);
            table = new PdfPTable(widths);
            table.setWidthPercentage(100);
            
            // Shipper
            cell = new PdfPCell(new Paragraph("\nShipper (Step 4 of 11)\n\n", font));
            cell.setColspan(2);
            cell.setBorder(0);
            table.addCell(cell);

            table.addCell("Title");
            table.addCell("" + pd.getTitle());
            table.addCell("Firstname");
            table.addCell("" + pd.getFirstname());
            table.addCell("Surname");
            table.addCell("" + pd.getSurname());
            table.addCell("E-mail");
            table.addCell("" + pd.getEmail());
            table.addCell("Phone");
            table.addCell("" + pd.getPhone());
            table.addCell("Fax");
            table.addCell("" + pd.getFax());
            table.addCell("Institution");
            table.addCell("" + pd.getLabsDAO().getName());
            table.addCell("Department");
            table.addCell("" + pd.getLabsDAO().getDept());
            table.addCell("Address Line 1");
            table.addCell("" + pd.getLabsDAO().getAddr_line_1());
            table.addCell("Address Line 2");
            table.addCell("" + pd.getLabsDAO().getAddr_line_2());
            table.addCell("County/province");
            table.addCell("" + pd.getLabsDAO().getProvince());
            table.addCell("Town");
            table.addCell("" + pd.getLabsDAO().getTown());
            table.addCell("Postcode");
            table.addCell("" + pd.getLabsDAO().getPostcode());
            table.addCell("Country");
            table.addCell("" + pd.getLabsDAO().getCountry());

            doc.add(table);
            doc.add(Chunk.NEWLINE);
            doc.add(Chunk.NEWLINE);
            doc.add(underlined);
            table = new PdfPTable(widths);
            table.setWidthPercentage(100);

            String origBgName = sd.getBackgroundDAO().getName();
            
            // Genotype
            cell = new PdfPCell(new Paragraph("\nGenotype (Step 5 of 11)\n\n", font));
            cell.setColspan(2);
            cell.setBorder(0);
            table.addCell(cell);

            table.addCell("Strain Name");
            table.addCell("" + strainName);
            
            table.addCell("Genetic description");
            table.addCell("" + cleanNULLS(sd.getCharact_gen(), false));

            table.addCell("Current genetic background");
            table.addCell("" + cleanNULLS(origBgName, false));
            
            table.addCell("Number of generations backcrossed");
            table.addCell("" + cleanNULLS(sd.getGeneration(), false));
            
            table.addCell("Number of generations sib-mated");
            table.addCell("" + cleanNULLS(sd.getSibmatings(), false));
            
            table.addCell("Breeding history");
            table.addCell("" + sd.getMaintenance());
            
            doc.add(table);
            doc.add(Chunk.NEWLINE);
            table = new PdfPTable(widths);
            table.setWidthPercentage(100);

            // Genotype - Mutations
            cell = new PdfPCell(new Paragraph("Mutation(s)\n\n", font));
            cell.setColspan(2);
            cell.setBorder(0);
            table.addCell(cell);
            Set sMutations = sd.getMutationsStrainsDAO();

            StringBuffer sDom = new StringBuffer();
            for (Iterator it = sMutations.iterator(); it.hasNext();) {
                MutationsStrainsDAO mutDAO = (MutationsStrainsDAO) it.next();
                if (mutDAO.getMutationsDAO().getDominance() != null) {
                    sDom = new StringBuffer(sDom).append(mutDAO.getMutationsDAO().getDominance().toString());
                }

                cell = new PdfPCell(new Paragraph("Type: " + cleanNULLS(mutDAO.getMutationsDAO().getMain_type(), false), FontFactory.getFont(FontFactory.HELVETICA, 11)));
                cell.setColspan(2);
                cell.setBorder(0);
                table.addCell(cell);

                cell = new PdfPCell(new Paragraph("Subtype: " + cleanNULLS(mutDAO.getMutationsDAO().getSub_type(), false), FontFactory.getFont(FontFactory.HELVETICA, 11)));
                cell.setColspan(2);
                cell.setBorder(0);
                table.addCell(cell);

                cell = new PdfPCell(new Paragraph("Affected gene: " + cleanNULLS(mutDAO.getMutationsDAO().getAllelesDAO().getGenesDAO().getName(), false), FontFactory.getFont(FontFactory.HELVETICA, 11)));
                cell.setColspan(2);
                cell.setBorder(0);
                table.addCell(cell);

                cell = new PdfPCell(new Paragraph("MGI of affected gene: " + cleanNULLS(mutDAO.getMutationsDAO().getAllelesDAO().getGenesDAO().getMgi_ref(), false), FontFactory.getFont(FontFactory.HELVETICA, 11)));
                cell.setColspan(2);
                cell.setBorder(0);
                table.addCell(cell);

                cell = new PdfPCell(new Paragraph("Affected allele: " + cleanNULLS(mutDAO.getMutationsDAO().getAllelesDAO().getName(), false), FontFactory.getFont(FontFactory.HELVETICA, 11)));
                cell.setColspan(2);
                cell.setBorder(0);
                table.addCell(cell);

                cell = new PdfPCell(new Paragraph("MGI of affected allele: " + cleanNULLS(mutDAO.getMutationsDAO().getAllelesDAO().getMgi_ref(), false), FontFactory.getFont(FontFactory.HELVETICA, 11)));
                cell.setColspan(2);
                cell.setBorder(0);
                table.addCell(cell);

                cell = new PdfPCell(new Paragraph("Affected chromosome: " + cleanNULLS(mutDAO.getMutationsDAO().getAllelesDAO().getGenesDAO().getChromosome(), false), FontFactory.getFont(FontFactory.HELVETICA, 11)));
                cell.setColspan(2);
                cell.setBorder(0);
                table.addCell(cell);

                cell = new PdfPCell(new Paragraph("Dominance pattern: " + cleanNULLS(mutDAO.getMutationsDAO().getDominance(), false), FontFactory.getFont(FontFactory.HELVETICA, 11)));
                cell.setColspan(2);
                cell.setBorder(0);
                table.addCell(cell);

                if (mutDAO.getMutationsDAO().getBackgroundDAO() != null) {
                    cell = new PdfPCell(new Paragraph("Original genetic background: " + cleanNULLS(mutDAO.getMutationsDAO().getBackgroundDAO().getName(), false), FontFactory.getFont(FontFactory.HELVETICA, 11)));
                } else {
                    cell = new PdfPCell(new Paragraph("\n"));
                }
                cell.setColspan(2);
                cell.setBorder(0);
                table.addCell(cell);
                
                if (mutDAO.getMutationsDAO().getCh_ano_name() != null) {
                    cell = new PdfPCell(new Paragraph("Chromosomal anomaly name: " + cleanNULLS(mutDAO.getMutationsDAO().getCh_ano_name(), false), FontFactory.getFont(FontFactory.HELVETICA, 11)));
                } else {
                    cell = new PdfPCell(new Paragraph("\n"));
                }
                cell.setColspan(2);
                cell.setBorder(0);
                table.addCell(cell);
                
                if (mutDAO.getMutationsDAO().getCh_ano_desc()!= null) {
                    cell = new PdfPCell(new Paragraph("Chromosomal anomaly description: " + cleanNULLS(mutDAO.getMutationsDAO().getCh_ano_desc(), false), FontFactory.getFont(FontFactory.HELVETICA, 11)));
                } else {
                    cell = new PdfPCell(new Paragraph("\n"));
                }
                cell.setColspan(2);
                cell.setBorder(0);
                table.addCell(cell);

                cell = new PdfPCell(new Paragraph("ES cell line used: " + cleanNULLS(mutDAO.getMutationsDAO().getTm_esline(), false) + "\n\n", FontFactory.getFont(FontFactory.HELVETICA, 11)));
                cell.setColspan(2);
                cell.setBorder(0);
                table.addCell(cell);
            }

            /* END MUTATIONS*/

            doc.add(table);
            doc.add(Chunk.NEWLINE);
            doc.add(Chunk.NEWLINE);
            doc.add(underlined);
            table = new PdfPTable(widths);
            table.setWidthPercentage(100);
            
            cell = new PdfPCell(new Paragraph("\nONE mutant strain is defined by its specific set of mutation(s) "
            + "and its specific genetic background. Therefore strains with the same set of mutation(s) but different "
            + "backgrounds do require DISTINCT submission forms (i.e) ONE form for each background\n\n", font));
            cell.setColspan(2);
            cell.setBorder(0);
            table.addCell(cell);

            doc.add(table);
            doc.add(Chunk.NEWLINE);
            doc.add(Chunk.NEWLINE);
            doc.add(underlined);
            table = new PdfPTable(widths);
            table.setWidthPercentage(100);

            // Phenotype
            cell = new PdfPCell(new Paragraph("\nPhenotype (Step 6 of 11)\n\n", font));
            cell.setColspan(2);
            cell.setBorder(0);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph("Phenotypic description of homozygous mice\n\n", font));
            cell.setColspan(2);
            cell.setBorder(0);
            table.addCell(cell);
            cell = new PdfPCell(new Paragraph("" + cleanNULLS(sd.getPheno_text(), true)));
            cell.setColspan(2);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph("\nPhenotypic description of heterozygous mice\n\n", font));
            cell.setColspan(2);
            cell.setBorder(0);
            table.addCell(cell);
            cell = new PdfPCell(new Paragraph("" + cleanNULLS(sd.getPheno_text_hetero(), true)));
            cell.setColspan(2);
            table.addCell(cell);
            doc.add(table);
            
            doc.add(Chunk.NEWLINE);
            doc.add(Chunk.NEWLINE);
            doc.add(underlined);
            table = new PdfPTable(widths);
            table.setWidthPercentage(100);
            
            // References
            List bibliosStrains = bm.bibliosStrains(sd.getId_str());
            String acceptedString = (( ! bibliosStrains.isEmpty()) && (bibliosStrains.size() > 0) ? "Yes/Accepted" : "No/Not known");
            cell = new PdfPCell(new Paragraph("\nReferences (Step 7 of 11)\n\n", font));
            cell.setColspan(2);
            cell.setBorder(0);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph("Has this mouse mutant strain been published or accepted for publication? " + acceptedString + "\n\n", FontFactory.getFont(FontFactory.HELVETICA, 11)));
            cell.setColspan(2);
            cell.setBorder(0);
            table.addCell(cell);

            for (Iterator it = bibliosStrains.iterator(); it.hasNext();) {
                BibliosStrainsDAO bsdao = (BibliosStrainsDAO) it.next();
            
                cell = new PdfPCell(new Paragraph("Short description\n\n", FontFactory.getFont(FontFactory.HELVETICA, 11)));
                cell.setColspan(2);
                cell.setBorder(0);
                table.addCell(cell);

                if (bsdao.getBibliosDAO().getNotes() != null) {
                    cell = new PdfPCell(new Paragraph("" + cleanNULLS(bsdao.getBibliosDAO().getNotes(), true)));
                } else {
                    cell = new PdfPCell(new Paragraph("\n"));
                }
                cell.setColspan(2);
                table.addCell(cell);
                
                String pubmedId = (bsdao.getBibliosDAO().getPubmed_id() == null ? "" : bsdao.getBibliosDAO().getPubmed_id());
                cell = new PdfPCell(new Paragraph("\nPubMed ID: " + pubmedId, FontFactory.getFont(FontFactory.HELVETICA, 11)));
                cell.setColspan(2);
                cell.setBorder(0);
                table.addCell(cell);

                cell = new PdfPCell(new Paragraph("Title: " + bsdao.getBibliosDAO().getTitle(), FontFactory.getFont(FontFactory.HELVETICA, 11)));
                cell.setColspan(2);
                cell.setBorder(0);
                table.addCell(cell);

                String authors = "Authors: " + bsdao.getBibliosDAO().getAuthor1();
                if (bsdao.getBibliosDAO().getAuthor2() != null)
                    authors = authors + ", " + bsdao.getBibliosDAO().getAuthor2();
                cell = new PdfPCell(new Paragraph(authors, FontFactory.getFont(FontFactory.HELVETICA, 11)));
                cell.setColspan(2);
                cell.setBorder(0);
                table.addCell(cell);

                cell = new PdfPCell(new Paragraph("Journal/Book: " + bsdao.getBibliosDAO().getJournal(), FontFactory.getFont(FontFactory.HELVETICA, 11)));
                cell.setColspan(2);
                cell.setBorder(0);
                table.addCell(cell);

                cell = new PdfPCell(new Paragraph("Year: " + cleanNULLS(bsdao.getBibliosDAO().getYear(), false), FontFactory.getFont(FontFactory.HELVETICA, 11)));
                cell.setColspan(2);
                cell.setBorder(0);
                table.addCell(cell);

                cell = new PdfPCell(new Paragraph("Volume: " + bsdao.getBibliosDAO().getVolume(), FontFactory.getFont(FontFactory.HELVETICA, 11)));
                cell.setColspan(2);
                cell.setBorder(0);
                table.addCell(cell);

                cell = new PdfPCell(new Paragraph("Pages: " + bsdao.getBibliosDAO().getPages() + "\n\n", FontFactory.getFont(FontFactory.HELVETICA, 11)));
                cell.setColspan(2);
                cell.setBorder(0);
                table.addCell(cell);
            }
            doc.add(table);
            
            doc.add(Chunk.NEWLINE);
            doc.add(Chunk.NEWLINE);
            doc.add(underlined);
            table = new PdfPTable(widths);
            table.setWidthPercentage(100);
            
            // Characterization
            cell = new PdfPCell(new Paragraph("\nCharacterization (Step 8 of 11)\n\n", font));
            cell.setColspan(2);
            cell.setBorder(0);
            table.addCell(cell);
            cell = new PdfPCell(new Paragraph("By genotyping \n"
                    + "(e.g. sequence of PCR primers and PCR settings,Southern probes and "
                    + "hybridization protocol)\n\n", FontFactory.getFont(FontFactory.HELVETICA, 11)));
            cell.setColspan(2);
            cell.setBorder(0);
            table.addCell(cell);

            if (sd.getResiduesDAO() != null) {
                cell = new PdfPCell(new Paragraph("" + cleanNULLS(sd.getResiduesDAO().getChar_genotyping(), true)));
            } else {
                cell = new PdfPCell(new Paragraph("\n"));
            }
            cell.setColspan(2);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph("\nBy phenotyping (e.g. coat colour)\n\n", FontFactory.getFont(FontFactory.HELVETICA, 11)));
            cell.setColspan(2);
            cell.setBorder(0);
            table.addCell(cell);

            if (sd.getResiduesDAO() != null) {
                cell = new PdfPCell(new Paragraph("" + cleanNULLS(sd.getResiduesDAO().getChar_phenotyping(), true)));
            } else {
                cell = new PdfPCell(new Paragraph("\n"));
            }
            cell.setColspan(2);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph("\nBy any other means that are not genotyping or phenotyping\n\n", FontFactory.getFont(FontFactory.HELVETICA, 11)));
            cell.setColspan(2);
            cell.setBorder(0);
            table.addCell(cell);

            if (sd.getResiduesDAO() != null) {
                cell = new PdfPCell(new Paragraph("" + cleanNULLS(sd.getResiduesDAO().getChar_other(), true)));
            } else {
                cell = new PdfPCell(new Paragraph(""));
            }
            cell.setColspan(2);
            table.addCell(cell);
            
                     
                 //assocFilesCHARACTERISATION
            
            StringBuffer additionalCharacFiles = new StringBuffer("");
            for (Iterator it = assocFilesCHARACTERISATION.iterator(); it.hasNext();) {
                String fileName = it.next().toString();
                additionalCharacFiles = new StringBuffer(additionalCharacFiles).append("\n        ").append(fileName);
            }

            cell = new PdfPCell(new Paragraph("\nAdditional files uploaded:",
                        FontFactory.getFont(FontFactory.HELVETICA, 11)));
            cell.setColspan(2);
            cell.setBorder(0);
            table.addCell(cell);
            
            cell = new PdfPCell(new Paragraph("" + additionalCharacFiles,
            FontFactory.getFont(FontFactory.HELVETICA, 11)));
            cell.setColspan(2);
            cell.setBorder(0);
            table.addCell(cell);
            doc.add(table);
            
            doc.add(Chunk.NEWLINE);
            doc.add(Chunk.NEWLINE);
            doc.add(Chunk.NEWLINE);
            doc.add(underlined);
            table = new PdfPTable(widths);
            table.setWidthPercentage(100);
            
            // Breeding
            cell = new PdfPCell(new Paragraph("\nBreeding (Step 9 of 11)\n\n", font));
            cell.setColspan(2);
            cell.setBorder(0);
            table.addCell(cell);
            
            cell = new PdfPCell(new Paragraph("Are homozygous mice viable? " + cleanNULLS(sd.getMutant_viable(), false), FontFactory.getFont(
                    FontFactory.HELVETICA, 11)));
            cell.setColspan(2);
            cell.setBorder(0);
            table.addCell(cell);
            cell = new PdfPCell(new Paragraph("Are homozygous mice fertile? " + cleanNULLS(sd.getMutant_fertile(), false), FontFactory.getFont(
                    FontFactory.HELVETICA, 11)));
            cell.setColspan(2);
            cell.setBorder(0);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph("Are heterozygous/hemizygous mice fertile? " + cleanNULLS(sd.getHethemi_fertile(), false), FontFactory.getFont(FontFactory.HELVETICA, 11)));
            cell.setColspan(2);
            cell.setBorder(0);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph("Are homozygous matings required? " + cleanNULLS(sd.getRequire_homozygous() + "\n\n", false), FontFactory.getFont(FontFactory.HELVETICA, 11)));
            cell.setColspan(2);
            cell.setBorder(0);
            table.addCell(cell);
            if (sd.getResiduesDAO() != null) {
                cell = new PdfPCell(new Paragraph("" + cleanNULLS(sd.getResiduesDAO().getHomozygous_matings_required_text(), true)));
            } else {
                cell = new PdfPCell(new Paragraph(""));
            }
            cell.setColspan(2);
            table.addCell(cell);
            
            cell = new PdfPCell(new Paragraph("\nAverage age of reproductive maturity (weeks): " + cleanNULLS(sd.getResiduesDAO().getReproductive_maturity_age(), false), FontFactory.getFont(FontFactory.HELVETICA, 11)));
            cell.setColspan(2);
            cell.setBorder(0);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph("Average age of reproductive decline (months): " + cleanNULLS(sd.getResiduesDAO().getReproductive_decline_age(), false), FontFactory.getFont(FontFactory.HELVETICA, 11)));
            cell.setColspan(2);
            cell.setBorder(0);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph("Average length of gestation (days): " + cleanNULLS(sd.getResiduesDAO().getGestation_length(), false), FontFactory.getFont(FontFactory.HELVETICA, 11)));
            cell.setColspan(2);
            cell.setBorder(0);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph("Average number of pups at birth: " + cleanNULLS(sd.getResiduesDAO().getPups_at_birth(), false), FontFactory.getFont(FontFactory.HELVETICA, 11)));
            cell.setColspan(2);
            cell.setBorder(0);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph("Average number of pups surviving to weaning: " + cleanNULLS(sd.getResiduesDAO().getPups_at_weaning(), false), FontFactory.getFont(FontFactory.HELVETICA, 11)));
            cell.setColspan(2);
            cell.setBorder(0);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph("Recommended weaning age (days): " + cleanNULLS(sd.getResiduesDAO().getWeaning_age(), false), FontFactory.getFont(FontFactory.HELVETICA, 11)));
            cell.setColspan(2);
            cell.setBorder(0);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph("Average number of litters in lifetime: " + cleanNULLS(sd.getResiduesDAO().getLitters_in_lifetime(), false), FontFactory.getFont(FontFactory.HELVETICA, 11)));
            cell.setColspan(2);
            cell.setBorder(0);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph("Breeding performance: " + cleanNULLS(sd.getResiduesDAO().getBreeding_performance(), false), FontFactory.getFont(FontFactory.HELVETICA, 11)));
            cell.setColspan(2);
            cell.setBorder(0);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph("Husbandry requirements:\n\n", FontFactory.getFont(FontFactory.HELVETICA, 11)));
            cell.setColspan(2);
            cell.setBorder(0);
            table.addCell(cell);
            if (sd.getResiduesDAO() != null) {
                cell = new PdfPCell(new Paragraph("" + cleanNULLS(sd.getResiduesDAO().getAnimal_husbandry() + "\n", true)));
            } else {
                cell = new PdfPCell(new Paragraph(""));
            }
            cell.setColspan(2);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph("\nAre mice immunicompromised? " + cleanNULLS(sd.getImmunocompromised(), false), FontFactory.getFont(FontFactory.HELVETICA, 11)));
            cell.setColspan(2);
            cell.setBorder(0);
            table.addCell(cell);
            
            cell = new PdfPCell(new Paragraph("\nSanitary status:\n\n", FontFactory.getFont(FontFactory.HELVETICA, 11)));
            cell.setColspan(2);
            cell.setBorder(0);
            table.addCell(cell);
            if (sd.getResiduesDAO() != null) {
                cell = new PdfPCell(new Paragraph("" + cleanNULLS(sd.getResiduesDAO().getCurrent_sanitary_status(), true)));
            } else {
                cell = new PdfPCell(new Paragraph("\n"));
            }
            cell.setColspan(2);
            
            table.addCell(cell);
            cell = new PdfPCell(new Paragraph("\nAnimal welfare:\n\n", FontFactory.getFont(FontFactory.HELVETICA, 11)));
            cell.setColspan(2);
            cell.setBorder(0);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph("" + cleanNULLS(sd.getResiduesDAO().getWelfare(), true)));
            cell.setColspan(2);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph("\nRemedial actions:\n\n", FontFactory.getFont(FontFactory.HELVETICA, 11)));
            cell.setColspan(2);
            cell.setBorder(0);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph("" + cleanNULLS(sd.getResiduesDAO().getRemedial_actions(), true)));
            cell.setColspan(2);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph("\nPlease note that under certain circumstances "
                    + "(e.g.: long-term cryopreservation by sperm freezing) the strain's original genotype will not "
                    + "always be available for future reconstitution of live colonies. Therefore, the original genetic "
                    + "background cannot be guaranteed.\n\n", font));
            cell.setColspan(2);
            cell.setBorder(0);
            table.addCell(cell);
            
                 //assocFilesSANITARY
            
            StringBuffer additionalSanitaryFiles = new StringBuffer("");
            for (Iterator it = assocFilesSANITARY.iterator(); it.hasNext();) {
                String fileName = it.next().toString();
                additionalSanitaryFiles = new StringBuffer(additionalSanitaryFiles).append("\n        ").append(fileName);
            }

            cell = new PdfPCell(new Paragraph("\nAdditional files uploaded:",
                        FontFactory.getFont(FontFactory.HELVETICA, 11)));
            cell.setColspan(2);
            cell.setBorder(0);
            table.addCell(cell);
            
            cell = new PdfPCell(new Paragraph("" + additionalSanitaryFiles,
            FontFactory.getFont(FontFactory.HELVETICA, 11)));
            cell.setColspan(2);
            cell.setBorder(0);
            table.addCell(cell);
            doc.add(table);
            
            doc.add(Chunk.NEWLINE);
            doc.add(Chunk.NEWLINE);
            doc.add(underlined);
            table = new PdfPTable(widths);
            table.setWidthPercentage(100);
            doc.add(table);

            // Research value
            cell = new PdfPCell(new Paragraph("\n\n\nResearch value (Step 10 of 11)\n\n", font));
            cell.setColspan(2);
            cell.setBorder(0);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph("Does this strain model a human condition or disease? " + cleanNULLS(sd.getHuman_model(), false), FontFactory.getFont(FontFactory.HELVETICA, 11)));
            cell.setColspan(2);
            cell.setBorder(0);
            table.addCell(cell);

            Strains_OmimManager strainsOmimManager = new Strains_OmimManager();
            List<Strains_OmimDAO> strains_omimDAOList = strainsOmimManager.findById_Strains(sd.getId_str());         
            
            if ( ! strains_omimDAOList.isEmpty()) {
                cell = new PdfPCell(new Paragraph("\nOMIM IDs:\n\n", FontFactory.getFont(FontFactory.HELVETICA, 11)));
                cell.setColspan(2);
                cell.setBorder(0);
                table.addCell(cell);
                
                for (Strains_OmimDAO strainsOmimDAO : strains_omimDAOList) {
                    cell = new PdfPCell(new Paragraph("" + cleanNULLS(strainsOmimDAO.getOmimDAO().getOmim(), true)));
                    cell.setColspan(2);
                    table.addCell(cell);
                }
            }

            cell = new PdfPCell(new Paragraph("\n\nIf OMIM IDs are not available, please describe the human condition or disease:\n\n", FontFactory.getFont(FontFactory.HELVETICA, 11)));
            cell.setColspan(2);
            cell.setBorder(0);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph("" + cleanNULLS(sd.getHuman_model_desc(), true)));
            cell.setColspan(2);
            table.addCell(cell);

            // Prep for Research areas
            Set cs = sd.getCategoriesStrainsDAO();
            String otherCategory = "";
            StringBuffer categories = new StringBuffer();
            for (Iterator it = cs.iterator(); it.hasNext();) {
                CategoriesStrainsDAO cd = (CategoriesStrainsDAO) it.next();
                if (cd.getCategoriesDAO().getCurated().compareTo("Y") == 0)
                    categories.append("\n        ").append(cd.getCategoriesDAO().getMain_cat());
                else
                    otherCategory = cd.getCategoriesDAO().getMain_cat();
            }

            cell = new PdfPCell(new Paragraph("\n\nResearch areas:\n        " + categories + "\n", FontFactory.getFont(FontFactory.HELVETICA, 11)));
            cell.setColspan(2);
            cell.setBorder(0);
            table.addCell(cell);

            Set sRTools = sd.getRtoolsDAO();
            StringBuffer rtools = new StringBuffer("");
            System.out.println("size of rtoolsdao set is :- " + sRTools.size());
            for (Iterator it = sRTools.iterator(); it.hasNext();) {
                RToolsDAO rtd = (RToolsDAO) it.next();
                rtools = new StringBuffer(rtools).append("\n        ").append(rtd.getCvrtoolsDAO().getDescription());
                //System.out.println("Rtools set iterator value is: " + rtools);
            }

            if ((otherCategory != null) && (otherCategory.trim().length() > 0)) {
                cell = new PdfPCell(new Paragraph("\n\nOther Research areas:\n\n        " + otherCategory + "\n", FontFactory.getFont(FontFactory.HELVETICA, 11)));
                cell.setColspan(2);
                cell.setBorder(0);
                table.addCell(cell);
            }
            cell = new PdfPCell(new Paragraph("\n\nResearch tools:\n" + rtools, FontFactory.getFont(FontFactory.HELVETICA, 11)));
            cell.setColspan(2);
            cell.setBorder(0);
            table.addCell(cell);

            /* END OF Research value */

            doc.add(table);
            doc.add(Chunk.NEWLINE);
            doc.add(Chunk.NEWLINE);
            doc.add(underlined);
            table = new PdfPTable(widths);

            table.setWidthPercentage(100);

            /* Additional information */

            cell = new PdfPCell(new Paragraph("\n\nAdditional information (Step 11 of 11)\n\n", font));
            cell.setColspan(2);
            cell.setBorder(0);
            table.addCell(cell);

            if (sd.getResiduesDAO() != null) {
                cell = new PdfPCell(new Paragraph("How many requests for this strain have you received in the last 6 months? "
                        + cleanNULLS(sd.getResiduesDAO().getNumber_of_requests(), false), FontFactory.getFont(FontFactory.HELVETICA, 11)));
            } else {
                cell = new PdfPCell(new Paragraph("How many requests for this strain have you received in the last 6 months? ", FontFactory.getFont(FontFactory.HELVETICA, 11)));
            }

            cell.setColspan(2);
            cell.setBorder(0);
            table.addCell(cell);

            if (sd.getResiduesDAO() != null) {
                cell = new PdfPCell(new Paragraph("Is this strain being deposited with any other institution or biotechnology company? "
                        + cleanNULLS(sd.getResiduesDAO().getDeposited_elsewhere() + "\n\n", false), FontFactory.getFont(FontFactory.HELVETICA, 11)));
            } else {
                cell = new PdfPCell(new Paragraph("Is this strain being deposited with any other institution or biotechnology company? ", FontFactory.getFont(FontFactory.HELVETICA, 11)));
            }
            cell.setColspan(2);
            cell.setBorder(0);
            table.addCell(cell);

            if (sd.getResiduesDAO() != null) {
                if (sd.getResiduesDAO().getIpr_description() != null) {
                    cell = new PdfPCell(new Paragraph(cleanNULLS(sd.getResiduesDAO().getDeposited_elsewhere_text(), true)));
                    cell.setColspan(2);
                    table.addCell(cell);
                }
            }
            
            if (sd.getResiduesDAO() != null) {
                cell = new PdfPCell(new Paragraph("\n\nAre other laboratories producing similar strains? "
                        + cleanNULLS(sd.getResiduesDAO().getOther_labos(), false), FontFactory.getFont(FontFactory.HELVETICA, 11)));
            } else {
                cell = new PdfPCell(new Paragraph("\n\nAre other laboratories producing similar strains? ", FontFactory.getFont(FontFactory.HELVETICA, 11)));
            }
            cell.setColspan(2);
            cell.setBorder(0);
            table.addCell(cell);
            
            String IPRights = "";
            if (sd.getResiduesDAO() != null) {
                IPRights = sd.getResiduesDAO().getIp_rights();
            } else {
                //do nothing
            }
            cell = new PdfPCell(new Paragraph("Are there any intellectual property rights or patented technologies linked to this strain? "
                    + IPRights + "\n\n", FontFactory.getFont(FontFactory.HELVETICA, 11)));
            cell.setColspan(2);
            cell.setBorder(0);
            table.addCell(cell);

            if (sd.getResiduesDAO() != null) {
                if (sd.getResiduesDAO().getIpr_description() != null) {
                    cell = new PdfPCell(new Paragraph("" + cleanNULLS(sd.getResiduesDAO().getIpr_description(), true)));
                    cell.setColspan(2);
                    table.addCell(cell);
                }
            }

            cell = new PdfPCell(new Paragraph("\nIs the producer the exclusive owner of this strain? "
                    + cleanNULLS(sd.getExclusive_owner(), false) + "\n\n", FontFactory.getFont(FontFactory.HELVETICA, 11)));
            cell.setColspan(2);
            cell.setBorder(0);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph("" + cleanNULLS(sd.getEx_owner_description(), true)));
            cell.setColspan(2);
            table.addCell(cell);
            
            cell = new PdfPCell(new Paragraph("\nDo you have permission from all owners to deposit this strain in the EMMA repository? "
                    + cleanNULLS(sd.getResiduesDAO().getOwner_permission(), false) + "\n\n", FontFactory.getFont(FontFactory.HELVETICA, 11)));
            cell.setColspan(2);
            cell.setBorder(0);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph("" + cleanNULLS(sd.getResiduesDAO().getOwner_permission_text(), true)));
            cell.setColspan(2);
            table.addCell(cell);
            
            
            String delayedRelease = "";
            if (sd.getResiduesDAO() != null) {
                delayedRelease = cleanNULLS(sd.getResiduesDAO().getDelayed_wanted(), false);
            }
            if (delayedRelease != null && delayedRelease.startsWith("yes")) {
                delayedRelease = new StringBuffer(delayedRelease).append(" (briefly explain below)\n\n").toString();
            }

            cell = new PdfPCell(new Paragraph("\nDo you require delayed release for your strain? " + delayedRelease, FontFactory.getFont(FontFactory.HELVETICA, 11)));
            cell.setColspan(2);
            cell.setBorder(0);
            table.addCell(cell);
            if (delayedRelease != null && delayedRelease.startsWith("yes")) {
                cell = new PdfPCell(new Paragraph("" + sd.getResiduesDAO().getDelayed_description()));
                cell.setColspan(2);
                table.addCell(cell);
            }
            
            if (sd.getResiduesDAO() != null) {
                cell = new PdfPCell(new Paragraph("\nHow many mice of breeding age could you provide and when?"
                        + "\n\nEstimated date of shipping:\n\nMonth: " + cleanNULLS(sd.getResiduesDAO().getWhen_how_many_month(), false)
                        + "\n\nYear: " + cleanNULLS(sd.getResiduesDAO().getWhen_how_many_year(), false)
                        + "\n\nNumber of males: " + cleanNULLS(sd.getResiduesDAO().getWhen_how_many_males(), false)
                        + "\n\nNumber of females: " + cleanNULLS(sd.getResiduesDAO().getWhen_how_many_females(), false),
                        FontFactory.getFont(FontFactory.HELVETICA, 11)));
            } else {
                cell = new PdfPCell(new Paragraph("\n\nHow many mice of breeding age could you provide and when?"
                        + "\n\nEstimated date of shipping:\n\nMonth: "
                        + "\n\nYear: "
                        + "\n\nNumber of males: "
                        + "\n\nNumber of females: ",
                        FontFactory.getFont(FontFactory.HELVETICA, 11)));
            }
            cell.setColspan(2);
            cell.setBorder(0);
            table.addCell(cell);
            
                     
                 //assocFilesADDITIONAL
            
            StringBuffer additionalFiles = new StringBuffer("");
            for (Iterator it = assocFilesADDITIONAL.iterator(); it.hasNext();) {
                String fileName = it.next().toString();
                additionalFiles = new StringBuffer(additionalFiles).append("\n        ").append(fileName);
            }

            cell = new PdfPCell(new Paragraph("\nAdditional files uploaded:",
                        FontFactory.getFont(FontFactory.HELVETICA, 11)));
            cell.setColspan(2);
            cell.setBorder(0);
            table.addCell(cell);
            
            cell = new PdfPCell(new Paragraph("" + additionalFiles,
            FontFactory.getFont(FontFactory.HELVETICA, 11)));
            cell.setColspan(2);
            cell.setBorder(0);
            table.addCell(cell);
            
//            cell = new PdfPCell(new Paragraph("\n\nWere any of the following techniques used in the construction of this mutant?\n\n", FontFactory.getFont(FontFactory.HELVETICA, 11)));
//            cell.setColspan(2);
//            cell.setBorder(0);
//            table.addCell(cell);
//
//            if (sd.getResiduesDAO() != null) {
//                cell = new PdfPCell(new Paragraph("Cre recombinase-loxP technology? " + cleanNULLS(sd.getResiduesDAO().getCrelox(), false), FontFactory.getFont(FontFactory.HELVETICA, 11)));
//            } else {
//                cell = new PdfPCell(new Paragraph("Cre recombinase-loxP technology? ", FontFactory.getFont(FontFactory.HELVETICA, 11)));
//            }
//            cell.setColspan(2);
//            cell.setBorder(0);
//            table.addCell(cell);
//
//            if (sd.getResiduesDAO() != null) {
//                cell = new PdfPCell(new Paragraph("FLP recombinase technology? " + cleanNULLS(sd.getResiduesDAO().getFlp(), false), FontFactory.getFont(FontFactory.HELVETICA, 11)));
//            } else {
//                cell = new PdfPCell(new Paragraph("FLP recombinase technology? ", FontFactory.getFont(FontFactory.HELVETICA, 11)));
//            }
//            cell.setColspan(2);
//            cell.setBorder(0);
//            table.addCell(cell);
//
//            if (sd.getResiduesDAO() != null) {
//                cell = new PdfPCell(new Paragraph("TET-system technology? " + cleanNULLS(sd.getResiduesDAO().getTet(), false), FontFactory.getFont(FontFactory.HELVETICA, 11)));
//            } else {
//                cell = new PdfPCell(new Paragraph("TET-system technology? ", FontFactory.getFont(FontFactory.HELVETICA, 11)));
//            }
//            cell.setColspan(2);
//            cell.setBorder(0);
//            table.addCell(cell);
//
//
//            if (sd.getResiduesDAO() != null) {
//                cell = new PdfPCell(new Paragraph("\n\nIf the submission request is accepted when could you provide "
//                        + "10 females and 6 males (5-12 weeks old)?"
//                        + "\nEstimated date of shipping\nMonth: " + cleanNULLS(sd.getResiduesDAO().getWhen_mice_month(), false) + "\nYear: " + cleanNULLS(sd.getResiduesDAO().getWhen_mice_year(), false), FontFactory.getFont(FontFactory.HELVETICA, 11)));
//            } else {
//                cell = new PdfPCell(new Paragraph("\n\nIf the submission request is accepted when could you provide "
//                        + "10 females and 6 males (5-12 weeks old)?"
//                        + "\nEstimated date of shipping\nMonth: \nYear:", FontFactory.getFont(FontFactory.HELVETICA, 11)));
//            }
//
//            cell.setColspan(2);
//            cell.setBorder(0);
//            table.addCell(cell);


//            if (sd.getResiduesDAO() != null) {
//                cell = new PdfPCell(new Paragraph("\n\nIf unable to provide this number of mice how many could you provide and when?"
//                        + "\nEstimated date of shipping\nMonth: " + cleanNULLS(sd.getResiduesDAO().getWhen_how_many_month(), false)
//                        + "\nYear: " + cleanNULLS(sd.getResiduesDAO().getWhen_how_many_year(), false)
//                        + "\nNumber of males: " + cleanNULLS(sd.getResiduesDAO().getWhen_how_many_males(), false)
//                        + "\nNumber of females: " + cleanNULLS(sd.getResiduesDAO().getWhen_how_many_females(), false),
//                        FontFactory.getFont(FontFactory.HELVETICA, 11)));
//            } else {
//                cell = new PdfPCell(new Paragraph("\n\nIf unable to provide this number of mice how many could you provide and when?"
//                        + "\nEstimated date of shipping\nMonth: "
//                        + "\nYear: "
//                        + "\nNumber of males: "
//                        + "\nNumber of females: ",
//                        FontFactory.getFont(FontFactory.HELVETICA, 11)));
//            }
//            cell.setColspan(2);
//            cell.setBorder(0);
//            table.addCell(cell);

            /* END OF ADDITIONAL INFORMATION */
            doc.add(table);
            
            doc.add(Chunk.NEWLINE);
            // Space padding underline
            underlined = new Chunk(
                    "                                                                                     " + "                                                                       ");
                    underlined.setUnderline(new Color(0x00, 0x00, 0x00), 0.0f, 0.2f,
                            16.0f, 0.0f, PdfContentByte.LINE_CAP_BUTT);//Black line
            doc.add(underlined);
            
            pSubHead = new Paragraph(
                    "The terms and conditions have been accepted.\n\n", FontFactory.getFont(
                    FontFactory.HELVETICA, 11));
            pSubHead.setAlignment(Element.ALIGN_CENTER);
            doc.add(pSubHead);
        }
    }

    public String cleanNULLS(String toClean, boolean cell) {
        String cleaned = "";
        if (toClean == null || toClean.isEmpty() && cell) {
            cleaned = "\n";
        } else if (toClean == null || toClean.isEmpty() && !cell) {
            cleaned = "";
        } else {
            cleaned = toClean;
        }
        return cleaned;
    }
}
