/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.emmanet.model;

import java.util.Date;

/**
 *
 * @author mrelac
 */
public class Syn_GenesDAO {

    private int id_syn;
    private String name;
    private String symbol;
    private String username;
    private Date last_change;
    private int gen_id_gene;

    public int getId_syn() {
        return id_syn;
    }

    public void setId_syn(int id_syn) {
        this.id_syn = id_syn;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Date getLast_change() {
        return last_change;
    }

    public void setLast_change(Date last_change) {
        this.last_change = last_change;
    }

    public int getGen_id_gene() {
        return gen_id_gene;
    }

    public void setGen_id_gene(int gen_id_gene) {
        this.gen_id_gene = gen_id_gene;
    }


}
