/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.emmanet.controllers;

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


import java.util.ArrayList;
import org.springframework.web.servlet.view.document.AbstractExcelView;

import java.util.List;
import java.util.Map;
import javax.servlet.http.*;


import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 *
 * @author phil
 */
public class wp4SpreadsheetController extends AbstractExcelView {

    private HSSFSheet sheet;
    private statsSpreadsheetController sc = new statsSpreadsheetController();
    

   
    
    
    @Override
    protected void buildExcelDocument(Map arg0, HSSFWorkbook arg1, HttpServletRequest arg2, HttpServletResponse arg3) throws Exception {
        System.out.println("build excel doc");
// "In Stock",
//"Mutation Subtype",
        
        sheet = arg1.createSheet("WP4 Full Strain Details");
        String[] sheetFields = {"Strain ID", "EMMA ID", "Strain Name", "Repository","Reporting Count", "Strain Status",
            "Available to Order", "Strain Access", "Grace Period Expiry", "Notes","Stock",
            "Mutation Main Type/Subtype",  "Project", "Funding Source", "Submitted", "Evaluated","Mice arrived (received)","GLT","Freezing started","Archived",
            "Duration (days) submitted->evaluated", "Duration (days) evaluated->received/GLT", "Duration (days) received/GLT->freezing started",
            "Duration (days) freezing started-> archived", "Duration (days) submitted->archived","Depositor surname", "Depositor institution", "Depositor country"
        };
        if (arg2.getParameter("report").equals("r")){
            //redundant report with mut subtype and mut type removed
           String[] sheetFields_r = {"Strain ID", "EMMA ID", "Strain Name", "Repository","Reporting Count", "Strain Status",
            "Available to Order", "Strain Access", "Grace Period Expiry", "Notes",
             "Project", "Funding Source", "Submitted", "Evaluated","Mice arrived (received)","GLT","Freezing started","Archived",
            "Duration (days) submitted->evaluated", "Duration (days) evaluated->received/GLT", "Duration (days) received/GLT->freezing started",
            "Duration (days) freezing started-> archived", "Duration (days) submitted->archived","Depositor surname", "Depositor institution", "Depositor country"
        };
           sheetFields=sheetFields_r;
        }

//System.out.println(arg0.size());


        sc.createHeaderCells(arg1, sheet, sheetFields);
 
        List results =  new ArrayList();
        results=(List) arg0.get("wp4SpreadsheetQuery");
        //System.out.println("wp4 controller results size = " + results.size());
        sc.populateDataCells(sheet, results, 1, true);

    }
}
