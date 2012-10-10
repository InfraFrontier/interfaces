/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.emmanet.util;

import org.emmanet.jobs.EmmaBiblioJOB;

/**
 *
 * @author phil
 */
public class runEmmaBiblios {
    
    EmmaBiblioJOB ebj = new EmmaBiblioJOB();

    /**
     * @param args the command line arguments
     */
    public void main(String[] args) {
        // TODO code application logic here
        ebj.check_for_updates();
    }
}
