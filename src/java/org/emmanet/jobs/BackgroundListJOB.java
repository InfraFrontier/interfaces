/*
 * Creates an option list daily for inclusion in jsp pages where needed
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

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.lang.StringEscapeUtils;
import org.emmanet.model.BackgroundManager;
import org.emmanet.model.LaboratoriesManager;
import org.emmanet.model.LabsDAO;
import org.emmanet.model.StrainsManager;
import org.emmanet.util.Configuration;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 *
 * @author phil
 */
public class BackgroundListJOB extends QuartzJobBean {

    private String fileLocation=Configuration.get("TMPFILES");
    private BackgroundManager bm = new BackgroundManager();
    private StrainsManager sm = new StrainsManager();
    private LaboratoriesManager lm = new LaboratoriesManager();

    public void generateBGList() {
        try {

            List backGrounds = bm.getBackgrounds();
            BufferedWriter out = new BufferedWriter(new FileWriter(fileLocation + "bgNamesList.emma", false));
            for (Iterator it = backGrounds.listIterator(); it.hasNext();) {
                Object[] o = (Object[]) it.next();
                out.write(o[0].toString() + "||");
                String bgName = StringEscapeUtils.escapeHtml(o[1].toString().trim());
                bgName = bgName.replaceAll("(\\r|\\n)", "");//removing line breaks carraige returns within string, resolves issue seen in file.
                out.write(bgName);
                out.newLine();
            }
            out.close();
        } catch (IOException e) {
        }
    }

    public void generateFundingSourceList() {
        try {
             List backGrounds = bm.getCVSources();
             BufferedWriter out = new BufferedWriter(new FileWriter(fileLocation + "fslist"));

            out.write("<option value=\"0\" selected>Please select...</option>\n");
            for (Iterator it = backGrounds.listIterator(); it.hasNext();) {
                Object[] o = (Object[]) it.next();
                String id = o[0].toString();
                String code = StringEscapeUtils.escapeHtml(o[1].toString());
                String desc = StringEscapeUtils.escapeHtml(o[2].toString().trim());
                out.write("<option value=\"" + id + "\">(" + code + ") " + desc + "</option>\n");
            }
            out.close();
        } catch (IOException e) {
        }
    }

    public void generateReqFundingSourceList() {
        //Generates the list used for request related funding sources
        try {
            List backGrounds = sm.getReqRelatedFundingSource();
            BufferedWriter out = new BufferedWriter(new FileWriter(fileLocation + "reqfslist"));

            out.write("<option value=\"0\" selected>Please select...</option>\n");
            for (Iterator it = backGrounds.listIterator(); it.hasNext();) {
                Object[] o = (Object[]) it.next();
                String id = o[0].toString();
                String code = StringEscapeUtils.escapeHtml(o[1].toString());
                String desc = StringEscapeUtils.escapeHtml(o[2].toString().trim());
                out.write("<option value=\"" + id + "\">(" + code + ") " + desc + "</option>\n");
            }
            out.close();
        } catch (IOException e) {
        }
    }

    public void generateProjectList() {
        try {
                        List projects = bm.getCVProjects();
BufferedWriter out = new BufferedWriter(new FileWriter(fileLocation + "plist"));

            out.write("<option value=\"0\" selected>Please select...</option>\n");
            for (Iterator it = projects.listIterator(); it.hasNext();) {
                Object[] o = (Object[]) it.next();
                String id = o[0].toString();
                String code = StringEscapeUtils.escapeHtml(o[1].toString());
                String desc = StringEscapeUtils.escapeHtml(o[2].toString().trim());
                out.write("<option value=\"" + id + "\">" + code + " (" + desc + ")</option>\n");
            }
            out.close();
        } catch (IOException e) {
        }
    }

    public void generateCodeInternalList() {
        try {
            List codeInt = sm.getCodeInternal();
            BufferedWriter out = new BufferedWriter(new FileWriter(fileLocation + "cilist"));

            out.write("<option value=\"\" selected>Please select...</option>\n");
            for (Iterator it = codeInt.listIterator(); it.hasNext();) {
                Object o = it.next();
                String code = StringEscapeUtils.escapeHtml(o.toString().trim());
                out.write("<option value=\"" + code + "\">" + code + "</option>\n");
            }
            out.close();
        } catch (IOException e) {
        }
    }

    public void generateNameStatusList() {
        try {
                        List nameStatus = sm.getNameStatusList();
                        BufferedWriter out = new BufferedWriter(new FileWriter(fileLocation + "nslist"));

            out.write("<option value=\"\" selected>Please select...</option>\n");
            for (Iterator it = nameStatus.listIterator(); it.hasNext();) {
                //Object o = it.next();
                Object[] o = (Object[]) it.next();
                String code = StringEscapeUtils.escapeHtml(o[0].toString().trim());
                String desc = StringEscapeUtils.escapeHtml(o[1].toString().trim());
                out.write("<option value=\"" + code + "\">" + desc + "</option>\n");
            }
            out.close();
        } catch (IOException e) {
        }
    }

    public void generateArchivesList() {
        try {
             List archives = lm.getArchivesByCode();
             BufferedWriter out = new BufferedWriter(new FileWriter(fileLocation + "archlist"));

            out.write("<option value=\"\" selected>Please select...</option>\n");
            for (Iterator it = archives.listIterator(); it.hasNext();) {
                LabsDAO ld = (LabsDAO) it.next();

                out.write("<option value=\"" + ld.getId_labo() + "\">" + ld.getName() + " (" + ld.getCode() + ")</option>\n");
            }
            out.close();
        } catch (IOException e) {
        }
    }

    @Override
    protected void executeInternal(JobExecutionContext arg0) throws JobExecutionException {
        generateBGList();
        generateFundingSourceList();
        generateProjectList();
        generateReqFundingSourceList();
        generateCodeInternalList();
        generateNameStatusList();
        generateArchivesList();

    }

    public String getFileLocation() {
        return fileLocation;
    }

    public void setFileLocation(String fileLocation) {
        this.fileLocation = fileLocation;
    }
}
