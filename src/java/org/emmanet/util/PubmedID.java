/*
 * This class implements the pubmed_id object as defined by the ncbi (see
 * http://www.ncbi.nlm.nih.gov/pubmed)
 */
package org.emmanet.util;

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
