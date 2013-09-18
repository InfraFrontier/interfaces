/*
 * This interface defines the contract for the core methods that EmmaBiblioJOB
 * requires to fetch and to persist the biblio data. The original implementation
 * of BibliosManager (still used) uses hibernate. A more recent version, local
 * to the EmmaBiblioJOB class, uses jdbc to fetch and to persist the data.
 */
package org.emmanet.model;

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
