/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
