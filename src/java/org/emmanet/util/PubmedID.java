/*
 * This class implements the pubmed_id object as defined by the ncbi (see
 * http://www.ncbi.nlm.nih.gov/pubmed)
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

/**
 *
 * @author mrelac
 * @deprecated PubmedID has been replaced by the more powerful PubMed class.
 */
@Deprecated
public class PubmedID {
    String pubmed_id;
    
    public PubmedID() {
        
    }
    
    public PubmedID(Object pubmed_id) {
        if (pubmed_id == null)
            this.pubmed_id = null;
        else
            this.pubmed_id = pubmed_id.toString();
    }
    
    /**
     * Check that this pubmed_id instance is valid.
     * @return true if this pubmed_id instance is null or is a valid integer or
     * <code>Double</code>; returns false otherwise
     */
    public boolean isValid() {
        if (pubmed_id == null)
            return true;
        
        Double dPubmed_id = Utils.tryParseDouble(pubmed_id);
        if ((dPubmed_id != null) && (dPubmed_id > 1.0))
            return true;
        else
            return false;
    }
    
    /**
     * Convert pubmed_id to String.
     * @return the pubmed_id as a <code>String</code>
     */
    @Override
    public String toString() {
        return pubmed_id;
    }
}
