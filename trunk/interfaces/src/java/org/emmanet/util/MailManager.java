/*
 * MailManager.java
 *
 * Interface Defines methods for Mailer
 *
 * Created on 29 November 2007, 15:45
 *
 * Currently not being used by Register an Interest functionality but released as
 * part of Register Interest work.
 *
 */

package org.emmanet.util;

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
import java.util.Map;
import org.springframework.mail.MailException;

/**
 *
 * @author phil
 */
public interface MailManager {
	
    void send(
    		String senderName, 
    		String senderAddress, 
    		Map from, 
    		Map to, 
            Map cc, 
            Map bcc, 
            String subject, 
            Map mergeObjects, 
            String template
            ) throws MailException;
    
    void send(Map to, List objects, String template)
    throws MailException;   
}
