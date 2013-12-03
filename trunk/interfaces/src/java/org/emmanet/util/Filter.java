/*
 * This class encapsulates the code and data necessary to represent and manage
 * EMMA GUI filtering. The intention is to use a Filter instance to pass
 * filter parameters to the back end for flexible query processing.
 */

package org.emmanet.util;

/**
 *
 * @author mrelac
 */
public class Filter {
    private String geneId;
    private String geneName;
    private String geneSymbol;
    private String chromosome;
    private String mgiReference;

    public String getGeneId() {
        return geneId;
    }

    public void setGeneId(String geneId) {
        this.geneId = geneId;
    }

    public String getGeneName() {
        return geneName;
    }

    public void setGeneName(String geneName) {
        this.geneName = geneName;
    }

    public String getGeneSymbol() {
        return geneSymbol;
    }

    public void setGeneSymbol(String geneSymbol) {
        this.geneSymbol = geneSymbol;
    }

    public String getChromosome() {
        return chromosome;
    }

    public void setChromosome(String chromosome) {
        this.chromosome = chromosome;
    }

    public String getMgiReference() {
        return mgiReference;
    }

    public void setMgiReference(String mgiReference) {
        this.mgiReference = mgiReference;
    }
    
    
}
