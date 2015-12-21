/*
 * sqlIssueJOB.java
 *
 * Created on 20 February 2008, 15:34
 *
 * To change this template, choose Tools | Template Manager
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

import java.util.List;
import org.emmanet.model.WebRequestsDAO;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 *
 * @author phil
 */
public class sqlIssueJOB extends QuartzJobBean {
    
    
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        
         WebRequestsDAO webReq;
                WebRequests    wr = new WebRequests();
                
               List check = wr.requestByID(3);
               System.out.println("Checking retrieval of request ID 3 from database: result size is :: " + check.size());
    }
    

    
}
