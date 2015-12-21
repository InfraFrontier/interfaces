/*
 * This interface defines the contract for the core methods that EmmaBiblioJOB
 * requires to fetch and to persist the biblio data. The original implementation
 * of BibliosManager (still used) uses hibernate. A more recent version, local
 * to the EmmaBiblioJOB class, uses jdbc to fetch and to persist the data.
 */
package org.emmanet.model;

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

/**
 *
 * @author mrelac
 */
public interface BibliosManagerIO {
    /**
     * This method returns all of the rows from the <em>biblios</em> table whose
     * <em>updated</em> field value is not 'Y' (including <code>null</code>) and
     * whose <em>pubmed_id</em> field is not empty or <code>null</code>.
     * @return all of the rows from the <em>biblios</em> table whose
     * <em>updated</em> field value is not 'Y' (including <code>null</code>) and
     * whose <em>pubmed_id</em> field is not empty or <code>null</code>
     * 
     * NOTE: There is a unique constraint on pubmed_id (see the index); thus
     *       we must exclude all biblios with null/empty pubmed_id's.
     */
    public List<BibliosDAO> getUpdateCandidateBiblios();
    
    /**
     * Save the given biblio record in the database. Transaction management is
     * the caller's responsibility.
     * 
     * @param bibliosDAO the data to be saved
     */
    public void save(BibliosDAO bibliosDAO);
}
