/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.emmanet.model;

/**
 *
 * @author phil
 */
public class MgiAlleleDAO {
    
private String acc;
private String marker_acc;
private String symbol;
private String name;  

    /**
     * @return the acc
     */
    public String getAcc() {
        return acc;
    }

    /**
     * @param acc the acc to set
     */
    public void setAcc(String acc) {
        this.acc = acc;
    }

    /**
     * @return the marker_acc
     */
    public String getMarker_acc() {
        return marker_acc;
    }

    /**
     * @param marker_acc the marker_acc to set
     */
    public void setMarker_acc(String marker_acc) {
        this.marker_acc = marker_acc;
    }

    /**
     * @return the symbol
     */
    public String getSymbol() {
        return symbol;
    }

    /**
     * @param symbol the symbol to set
     */
    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }
}
