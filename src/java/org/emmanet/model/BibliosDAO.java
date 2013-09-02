/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.emmanet.model;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.emmanet.util.Utils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

/**
 *
 * @author phil
 * 02-Sept-2013 (mrelac) extended for JdbcTemplate use by adding RowMapper,
 *   JdbcTemplate getter/setter, and Serializable attribute.
 */
public class BibliosDAO implements RowMapper, Serializable {
    
private int id_biblio;
private String title;
private String author1;
private String author2;
private String year;
private String journal;
private String username;
private String volume;
private String pages;
//private int pubmed_id;
//changed to String to resolve issue with null valued pubmed id's in database and field is varchar
private String pubmed_id;
private String last_change;
private String notes;
private String updated;
private JdbcTemplate jdbcTemplate;

    public int getId_biblio() {
        return id_biblio;
    }

    public void setId_biblio(int id_biblio) {
        this.id_biblio = id_biblio;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor1() {
        return author1;
    }

    public void setAuthor1(String author1) {
        this.author1 = author1;
    }

    public String getAuthor2() {
        return author2;
    }

    public void setAuthor2(String author2) {
        this.author2 = author2;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getJournal() {
        return journal;
    }

    public void setJournal(String journal) {
        this.journal = journal;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public String getPages() {
        return pages;
    }

    public void setPages(String pages) {
        this.pages = pages;
    }

    public String getPubmed_id() {
        return pubmed_id;
    }

    public void setPubmed_id(String pubmed_id) {
        this.pubmed_id = pubmed_id;
    }

    public String getLast_change() {
        return last_change;
    }

    public void setLast_change(String last_change) {
        this.last_change = last_change;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getUpdated() {
        return updated;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }
    
    /**
     * This getter/setter pair permits spring JdbcTemplate to be used in non-
     * Hibernate situations where jdbc is the preferred pattern.
     * @return the <code>JdbcTemplatee</code> object instantiated by spring (see
     * jobApplicationContext.xml).
     */
    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    
    /**
     * This method implements spring's JdbcTemplate RowMapper interface, allowing
     * BibliosDAO to be used in the spring jdbc template pattern.
     * @param rs the result set to be mapped
     * @param rowNum required by the interface
     * @return a list of <code>BibliosDAO</code> matching the result set
     * @throws SQLException if an sql exception occurs
     */
    @Override
    public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
            BibliosDAO bibliosDAO = new BibliosDAO();

            bibliosDAO.setId_biblio(Integer.parseInt(Utils.getDbValue(rs, "id_biblio")));
            bibliosDAO.setTitle(Utils.getDbValue(rs, "title"));
            bibliosDAO.setAuthor1(Utils.getDbValue(rs, "author1"));
            bibliosDAO.setAuthor2(Utils.getDbValue(rs, "author2"));
            bibliosDAO.setYear(Utils.getDbValue(rs, "year"));
            bibliosDAO.setJournal(Utils.getDbValue(rs, "journal"));
            bibliosDAO.setUsername(Utils.getDbValue(rs, "username"));
            bibliosDAO.setVolume(Utils.getDbValue(rs, "volume"));
            bibliosDAO.setPages(Utils.getDbValue(rs, "pages"));
            bibliosDAO.setPubmed_id(Utils.getDbValue(rs, "pubmed_id"));
            bibliosDAO.setUpdated(Utils.getDbValue(rs, "updated"));
            bibliosDAO.setLast_change(Utils.getDbValue(rs, "last_change"));
            bibliosDAO.setNotes(Utils.getDbValue(rs, "notes"));

            return bibliosDAO;
    }

}
