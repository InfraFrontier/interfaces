<%-- 
    Document   : geneManagementDetail
    Created on : Dec 2, 2013, 1:43:09 PM
    Author     : mrelac
--%>
<!DOCTYPE html>
<%@ taglib prefix="c"      uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt"    uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn"     uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec"    uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="dt"     uri="http://jakarta.apache.org/taglibs/datetime-1.0" %>
<%@ taglib prefix="req"    uri="http://jakarta.apache.org/taglibs/request-1.0" %>
<%@ taglib prefix="form"   uri="http://www.springframework.org/tags/form" %>

<%@page import="org.emmanet.util.Configuration" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<spring:bind path="command.*"></spring:bind>
<c:set var="keyRef" value='${command}'></c:set>
<c:set var="filteredGenesDAOList" value='${filteredGenesDAOList}'></c:set>
<c:set var="filteredGenesDAOListSize" value = '${filteredGenesDAOListSize}'></c:set>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="http://code.jquery.com/ui/1.10.3/themes/smoothness/jquery-ui.css" />
        <script src="http://code.jquery.com/jquery-1.9.1.js"></script>
        <script src="http://code.jquery.com/ui/1.10.3/jquery-ui.js"></script>
        <script type="text/javascript" src="../js/json2.js" charset="UTF-8"></script>
        <link rel="stylesheet" href="http://jqueryui.com/jquery-wp-content/themes/jqueryui.com/style.css">
        <script>
            
            $(document).ready(function() {

            });
            
        </script>
        <title>Gene Management - add/edit</title>
    </head>
    <body>
        <h2>Gene Management - add/edit</h2>
        <span id="loginHeader">Allele Update Interface - Logged in as user "<sec:authentication property='principal.username'/>"</span>
        
        <br />
        <br />

        <!-- Placeholders for error messages. -->
        <center>
            <c:if test="${not empty status.errorMessages}">
                <c:forEach var="error" items="${status.errorMessages}">
                    <font color="red"><c:out value="${error}" escapeXml="false" /> </font>
                    <br />
                </c:forEach>
            </c:if>

            <c:if test="${not empty message}">
                <font color="green"><c:out value="${message}" /></font>
                <c:set var="message" value="" scope="session" />
            </c:if>
        </center>
        
        <form:form commandName="command">
                <table style="border: 1px solid black">
                    <tr>
                        <td><label id="labGeneId">Gene ID:</label></td>
                        <td style="border: 0"><form:input id="geneId" tabindex="0" path="id_gene" readonly="true"></form:input></td>
                        <td><form:label for="mgiReference" path="mgi_ref">MGI reference:</form:label></td>
                        <td><form:input id="mgiReference" tabindex="6" path="mgi_ref" /></td>
                        <td rowspan="6">
                            <table style="border: 1px solid black">
                                <tr style="border: 1px solid black">
                                    <td>Synonyms:</td>
                                    <td colspan="3" align="right"><input type="submit" value="New" formaction="geneManagementDetail.emma?id=${synonyms.id_syn}&action=newSynonym" /></td>
                                </tr>
                                <tr>
                                    <th>Actions</th>
                                    <th>Id</th>
                                    <th>Name</th>
                                    <th>Symbol</th>
                                </tr>       
                                <c:forEach var="synonyms" items="${command.syn_genesDAO}" varStatus="status">
                                    <tr>
                                        <td style="border: 1px solid black">
                                            <table>
                                                <tr>
                                                    <td><a href="geneManagementDetail.emma?id=${synonyms.id_syn}&action=editSynonym">Edit</a></td>
                                                    <td><a href="geneManagementDetail.emma?id=${synonyms.id_syn}&action=deleteSynonym">Delete</a></td>
                                                </tr>
                                            </table>
                                        </td>
                                        <td style="border: 1px solid black" valign="top">${synonyms.id_syn}</td>
                                        <td style="border: 1px solid black" valign="top">${synonyms.name}</td>
                                        <td style="border: 1px solid black" valign="top">${synonyms.symbol}</td>
                                    </tr>
                                </c:forEach>
                            </table>
                        </td>
                    </tr>
                    <tr>
                        <td><form:label for="geneName" path="name">Gene Name:</form:label></td>
                        <td><form:input id="geneName" tabindex="1" path="name" /></td>
                        <td><form:label for="ensemblReference" path="ensembl_ref">Ensembl reference:</form:label></td>
                        <td colspan="2"><form:input id="ensemblReference" tabindex="7" path="ensembl_ref" /></td>
                    </tr>
                    <tr>
                        <td><form:label for="geneSymbol" path="symbol">Gene Symbol:</form:label></td>
                        <td><form:input id="geneSymbol" tabindex="2" path="symbol" /></td>
                        <td><form:label for="promoter" path="promoter">Promoter:</form:label></td>
                        <td colspan="2"><form:input id="promoter" tabindex="8" path="promoter" /></td>
                    </tr>
                    <tr>
                        <td><form:label for="chromosome" path="chromosome">Chromosome:</form:label></td>
                        <td><form:input id="chromosome" tabindex="3" path="chromosome" /></td>
                        <td><form:label for="founderLineNumber" path="founder_line_number">Founder line number:</form:label></td>
                        <td colspan="2"><form:input id="founderLineNumber" tabindex="9" path="founder_line_number" /></td>
                    </tr>
                    <tr>
                        <td><form:label for="species" path="species">Species:</form:label></td>
                        <td><form:input id="species" tabindex="4" path="species" /></td>
                        <td><form:label for="plasmidConstruct" path="plasmid_construct">Plasmid Construct:</form:label></td>
                        <td colspan="2"><form:input id="plasmidConstruct" tabindex="10" path="plasmid_construct" /></td>
                    </tr>
                    <tr>
                        <td><form:label for="centimorgan" path="centimorgan">Centimorgan:</form:label></td>
                        <td><form:input id="centimorgan" tabindex="5" path="centimorgan" /></td>
                        <td><form:label for="cytoband" path="cytoband">Cytoband:</form:label></td>
                        <td colspan="2"><form:input id="cytoband" tabindex="11" path="cytoband" /></td>
                    </tr >
                    <tr>
                        <td align="left"><input type="submit" value="Back" formaction="geneManagementList.emma" /></td>
                        <td colspan="2" align="center"><input type="submit" value="Cancel" formaction="geneManagementList.emma" /></td>
                        <td colspan="2" align="right"><input type="submit" value="Save" formaction="geneManagementDetail.emma?id=${synonyms.id_syn}&action=save" /></td>
                    </tr>
                </table>
        </form:form>
    </body>
</html>