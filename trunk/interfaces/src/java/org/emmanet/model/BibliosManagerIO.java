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
     * Return a <code>list&lt;BibliosDAO&gt;</code> of all pubmed_ids that need
     * to be updated.
     * @return a <code>list&lt;BibliosDAO&gt;</code> of all pubmed_ids that need
     *         to be updated
     */
    List<BibliosDAO> getPubmedID();
    
    /**
     * Persists the [valid] <code>BibliosDAO</code> object to the database.
     * @param bibliosDAO the [valid] object to be saved
     */
    void save(BibliosDAO bibliosDAO);
}
