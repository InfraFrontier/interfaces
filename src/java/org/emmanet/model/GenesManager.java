/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.emmanet.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.log4j.Logger;
import org.emmanet.jobs.EmmaBiblioJOB;
import org.emmanet.util.Filter;
import org.emmanet.util.HibernateUtil;
import org.emmanet.util.Utils;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.google.gson.Gson;


/**
 *
 * @author phil
 */
public class GenesManager {

    protected Logger logger = Logger.getLogger(EmmaBiblioJOB.class);
    
    public List<GenesDAO> getGenes() {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        List<GenesDAO> genesList = null;
        try {
            genesList = session.createQuery(
                    "FROM GenesDAO").list();
            session.getTransaction().commit();
        } catch (HibernateException e) {
            session.getTransaction().rollback();
            throw e;
        }
        return genesList;
    }
    
    public void save(GenesDAO gDAO) {

        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();

        try {
            session.saveOrUpdate(gDAO);
            session.getTransaction().commit();

        } catch (HibernateException e) {
            session.getTransaction().rollback();
            throw e;
        }
    }
    
    /**
     * Deletes the named <code>GenesDAO</code> object.
     * @param gDAO the <code>GenesDAO</code> object to be deleted
     */
    public void delete(GenesDAO gDAO) {

        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();

        try {
            session.delete(gDAO);
            session.getTransaction().commit();

        } catch (HibernateException e) {
            session.getTransaction().rollback();
            throw e;
        }
    }

    /**
     * Returns a <code>List&lt;String&gt;</code> of distinct gene ids suitable for
     * autocomplete sourcing.
     * @return a <code>List&lt;String&gt;</code> of gene ids suitable for
     *         autocomplete sourcing.
     */
    public List<String> getGeneIds() {
        List<GenesDAO> genesDAOList = getGenes();
        List<String> targetList = new ArrayList();
        
        if (genesDAOList != null) {
            for (GenesDAO genesDAO : genesDAOList) {
                String sId_gene = Integer.toString(genesDAO.getId_gene());
                targetList.add(Utils.wrap(sId_gene, "\""));
            }
        }
        
        logger.debug("targetList count = " + targetList.size());
        return targetList;
    }
    
    /**
     * Returns a <code>List&lt;String&gt;</code> of distinct gene names suitable for
     * autocomplete sourcing.
     * @return a <code>List&lt;String&gt;</code> of distinct gene names suitable for
     *         autocomplete sourcing.
     */
    public List<String> getNames() {
        List<String> targetList = new ArrayList();
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        List sourceList = null;
        try {
            sourceList = session.createSQLQuery(
                    "SELECT DISTINCT name FROM genes").list();
            session.getTransaction().commit();
        } catch (HibernateException e) {
            session.getTransaction().rollback();
            throw e;
        }

        if (sourceList != null) {
            Iterator iterator = sourceList.iterator();
            while (iterator.hasNext()) {
                String name = (String)iterator.next();
                targetList.add(Utils.wrap(name, "\""));
            }
        }
            
        return targetList;
    }
    
    /**
     * Returns a <code>List&lt;String&gt;</code> of distinct gene symbols suitable
     * for autocomplete sourcing.
     * @return a <code>List&lt;String&gt;</code> of distinct gene symbols suitable
     *         for autocomplete sourcing.
     */
    public List<String> getSymbols() {
        List<String> targetList = new ArrayList();
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        List sourceList = null;
        try {
            sourceList = session.createSQLQuery(
                    "SELECT DISTINCT symbol FROM genes").list();
            session.getTransaction().commit();
        } catch (HibernateException e) {
            session.getTransaction().rollback();
            throw e;
        }

        if (sourceList != null) {
            Iterator iterator = sourceList.iterator();
            while (iterator.hasNext()) {
                String symbol = (String)iterator.next();
                targetList.add(Utils.wrap(symbol, "\""));
            }
        }
            
        return targetList;
    }
    
    /**
     * Returns a <code>List&lt;String&gt;</code> of distinct gene chromosomes suitable
     * for autocomplete sourcing.
     * @return a <code>List&lt;String&gt;</code> of distinct gene chromosomes suitable
     *         for autocomplete sourcing.
     */
    public List<String> getChromosomes() {
        List<String> targetList = new ArrayList();
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        List sourceList = null;
        try {
            sourceList = session.createSQLQuery(
                    "SELECT DISTINCT chromosome FROM genes").list();
            session.getTransaction().commit();
        } catch (HibernateException e) {
            session.getTransaction().rollback();
            throw e;
        }

        if (sourceList != null) {
            Iterator iterator = sourceList.iterator();
            while (iterator.hasNext()) {
                String chromosome = (String)iterator.next();
                targetList.add(Utils.wrap(chromosome, "\""));
            }
        }
            
        return targetList;
    }
    
    /**
     * Returns a <code>List&lt;String&gt;</code> of distinct MGI references suitable
     * for autocomplete sourcing.
     * @return a <code>List&lt;String&gt;</code> of distinct MGI references suitable
     *         for autocomplete sourcing.
     */
    public List<String> getMGIReferences() {
        List<String> targetList = new ArrayList();
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        List sourceList = null;
        try {
            sourceList = session.createSQLQuery(
                    "SELECT DISTINCT mgi_ref FROM genes").list();
            session.getTransaction().commit();
        } catch (HibernateException e) {
            session.getTransaction().rollback();
            throw e;
        }

        if (sourceList != null) {
            Iterator iterator = sourceList.iterator();
            while (iterator.hasNext()) {
                String mgiRef = (String)iterator.next();
                targetList.add(Utils.wrap(mgiRef, "\""));
            }
        }
            
        return targetList;
    }
    
    public static GenesDAO getGene(int id) {
        GenesDAO genesDAO = null;
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        try {
            genesDAO = (GenesDAO)session.createQuery(
                    "FROM GenesDAO WHERE id_gene = ?").setParameter(0, id).uniqueResult();
            session.getTransaction().commit();
        } catch (HibernateException e) {
            session.getTransaction().rollback();
        }
        
        // Re-map null fields to empty strings.
        if (genesDAO != null) {
            if (genesDAO.getCentimorgan() == null)
                    genesDAO.setCentimorgan("");
            
            
            if (genesDAO.getChromosome()== null)
                    genesDAO.setChromosome("");
            if (genesDAO.getCytoband()== null)
                    genesDAO.setCytoband("");
            if (genesDAO.getEnsembl_ref()== null)
                    genesDAO.setEnsembl_ref("");
            if (genesDAO.getFounder_line_number()== null)
                    genesDAO.setFounder_line_number("");
            if (genesDAO.getLast_change()== null)
                    genesDAO.setLast_change("");
            if (genesDAO.getMgi_ref()== null)
                    genesDAO.setMgi_ref("");
            if (genesDAO.getName() == null)
                    genesDAO.setName("");
            if (genesDAO.getPlasmid_construct()== null)
                    genesDAO.setPlasmid_construct("");
            if (genesDAO.getPromoter()== null)
                    genesDAO.setPromoter("");
            if (genesDAO.getSpecies()== null)
                    genesDAO.setSpecies("");
            if (genesDAO.getSymbol() == null)
                    genesDAO.setSymbol("");
            if (genesDAO.getUsername()== null)
                    genesDAO.setUsername("");
        }
        return genesDAO;
    }
    
    /**
     * Given a <code>Filter</code> object describing any/all of the following
     * fields:
     * <ul><li>gene chromosome</li>
     * <li>gene ID</li>
     * <li>gene name</li>
     * <li>gene symbol</li>
     * <li>MGI reference</li></ul>,
     * this method performs a query, ANDing all non-empty fields in a WHERE
     * clause against the genes table. The result is a <code>List&lt;GenesDAO
     * &gt;</code> of qualifying results. A list is always returned, even if
     * there are no results.
     * 
     * @param filter values to filter by
     * @return a list of <code>GenesDAO</code>.
     */
    public List<GenesDAO> getFilteredGenesList(Filter filter) {
        String chromosomeWhere = "";
        String geneIdWhere = "";
        String geneNameWhere = "";
        String geneSymbolWhere = "";
        String mgiReferenceWhere = "";
        List<GenesDAO> targetList = new ArrayList();
        int geneId = -1;
        
        String queryString = "SELECT * FROM genes\nWHERE (1 = 1)";
        if ((filter.getChromosome() != null) && ( ! filter.getChromosome().isEmpty())) {
            chromosomeWhere = "  AND (chromosome LIKE :chromosome)\n";
            queryString += chromosomeWhere;
        }
        Integer iGeneId = Utils.tryParseInt(filter.getGeneId());
        if ((iGeneId != null) && (iGeneId.intValue() > 0)) {
            geneId = iGeneId.intValue();
            geneIdWhere = "  AND (id_gene = :id_gene)\n";
            queryString += geneIdWhere;
        }
        if ((filter.getGeneName() != null) && ( ! filter.getGeneName().isEmpty())) {
            geneNameWhere = "  AND (name LIKE :name)\n";
            queryString += geneNameWhere;
        }
        if ((filter.getGeneSymbol() != null) && ( ! filter.getGeneSymbol().isEmpty())) {
            geneSymbolWhere = "  AND (symbol LIKE :symbol)\n";
            queryString += geneSymbolWhere;
        }
        if ((filter.getMgiReference()!= null) && ( ! filter.getMgiReference().isEmpty())) {
            mgiReferenceWhere = "  AND (mgi_ref LIKE :mgi_ref)\n";
            queryString += mgiReferenceWhere;
        }
        
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        try {
            SQLQuery query = session.createSQLQuery(queryString);
            if ( ! chromosomeWhere.isEmpty())
                query.setParameter("chromosome", "%" + filter.getChromosome() + "%");
            if ( ! geneIdWhere.isEmpty())
                query.setParameter("id_gene", geneId);
            if ( ! geneNameWhere.isEmpty())
                query.setParameter("name", "%" + filter.getGeneName() + "%");
            if ( ! geneSymbolWhere.isEmpty())
                query.setParameter("symbol", "%" + filter.getGeneSymbol() + "%");
            if ( ! mgiReferenceWhere.isEmpty())
                query.setParameter("mgi_ref", "%" + filter.getMgiReference() + "%");
                
            targetList = query.addEntity(GenesDAO.class).list();
            session.getTransaction().commit();
        } catch (HibernateException e) {
            session.getTransaction().rollback();
            throw e;
        }
            
        return targetList;
    }

    /**
     * Transforms a <code>List&lt;GenesDAO&gt;</code> to a JSON string.
     * @param genesDAOList the list to be transformed
     * @return the transformed JSON string
     */
    public static String toJSON(List<GenesDAO> genesDAOList) {
        JSONArray jsonList = new JSONArray();
        for (GenesDAO gene : genesDAOList) {
            JSONObject jsonGeneDAO = new JSONObject();
            jsonGeneDAO.put("centimorgan",         gene.getCentimorgan() == null ? "" : gene.getCentimorgan());
            jsonGeneDAO.put("chromosome",          gene.getChromosome() == null ? "" : gene.getChromosome());
            jsonGeneDAO.put("cytoband",            gene.getCytoband() == null ? "" : gene.getCytoband());
            jsonGeneDAO.put("ensembl_ref",         gene.getEnsembl_ref() == null ? "" : gene.getEnsembl_ref());
            jsonGeneDAO.put("founder_line_number", gene.getFounder_line_number() == null ? "" : gene.getFounder_line_number());
            jsonGeneDAO.put("id_gene",             Integer.toString(gene.getId_gene()));
            jsonGeneDAO.put("mgi_ref",             gene.getMgi_ref() == null ? "" : gene.getMgi_ref());
            jsonGeneDAO.put("name",                gene.getName() == null ? "" : gene.getName());
            jsonGeneDAO.put("plasmid_construct",   gene.getPlasmid_construct() == null ? "" : gene.getPlasmid_construct());
            jsonGeneDAO.put("promoter",            gene.getPromoter()== null ? "" : gene.getPromoter());
            jsonGeneDAO.put("species",             gene.getSpecies() == null ? "" : gene.getSpecies());
            jsonGeneDAO.put("symbol",              gene.getSymbol() == null ? "" : gene.getSymbol());
            
            if ((gene.getSyn_genesDAO() != null) && (gene.getSyn_genesDAO().size() > 0)) {
                JSONArray synonyms = new JSONArray();
                Iterator<Syn_GenesDAO> iterator = gene.getSyn_genesDAO().iterator();
                while (iterator.hasNext()) {
                    Syn_GenesDAO syn_genesDAO = iterator.next();
                    JSONObject synonym = new JSONObject();
                    synonym.put("id_syn", Integer.toString(syn_genesDAO.getId_syn()));
                    synonym.put("name",   syn_genesDAO.getName());
                    synonym.put("symbol", syn_genesDAO.getSymbol());
                    synonyms.add(synonym);
                }
                jsonGeneDAO.put("synonyms", synonyms);
            }
            
            jsonList.add(jsonGeneDAO);
        }
        

        // Gson dosn't reserve space for fields with null values!!!!
////////        Gson gson = new Gson();
////////            String s = gson.toJson(genesDAOList);
////////            System.out.println(s);
////////        return s;
        
        return jsonList.toString();
    }
    
    public static String toJson(GenesDAO genesDAO) {
        return new Gson().toJson(genesDAO);
    }
    
    /**
     * Returns a <code>List&lt;AllelesDAO&gt;</code> of allele records matching
     * <code>id_gene</code>.
     * @param id_gene the gene id to match
     * @return  a <code>List&lt;AllelesDAO&gt;</code> of allele records matching
     * <code>id_gene</code>.
     */
    public List<AllelesDAO> getBoundAlleles(int id_gene) {
        List<AllelesDAO> allelesDAOList = null;
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        try {
            // NOTE: AllelesDAO's id_gene parameter is defined as a STRING! It would be
            // more appropriate to change it to an int, but might well break existing code.
            allelesDAOList = session.createQuery(
                    "FROM AllelesDAO WHERE gen_id_gene = ?").setParameter(0, Integer.toString(id_gene)).list();
            session.getTransaction().commit();
        } catch (HibernateException e) {
            session.getTransaction().rollback();
        }

        return allelesDAOList;
    }
    
    
    // PRIVATE METHODS
    

}
