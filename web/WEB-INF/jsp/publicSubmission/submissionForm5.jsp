<%--
  #%L
  InfraFrontier
  $Id:$
  $HeadURL:$
  %%
  Copyright (C) 2015 EMBL-European Bioinformatics Institute
  %%
  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at
  
       http://www.apache.org/licenses/LICENSE-2.0
  
  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
  #L%
  --%>
<%-- 
    Document   : submissionForm5
    Created on : 30-Jan-2012, 14:48:00
    Author     : phil
--%>
<%    response.setHeader("Cache-Control", "no-cache");
    response.setHeader("Pragma", "no-cache");
    response.setDateHeader("Expires", -1);
    response.setHeader("Cache-Control", "no-store");
%>

<%@page import="java.io.BufferedReader"%>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<spring:bind path="command.*" />
<%@page import="java.io.*"%>
<c:set var="stepCurrent" value="${(sessionScope.pageCount)}" scope="page" />
<c:set var="stepTotal" value="${(sessionScope.totalStepCount)}" scope="page" />
<c:set var="BGDAO" value="${command.bgDAO}"/>
<c:if test="${empty BGDAO}"><c:set var="BGDAO" value="${(sessionScope.backgroundsDAO)}"/></c:if>



    <!DOCTYPE html>
    <html>
        <head>
            <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
            <title>EMMA Mutant Mouse Strain Submission Wizard - Step ${stepCurrent} of ${stepTotal}</title>
        <style type="text/css">@import url(../css/default.css);</style>
        <link rel="stylesheet" type="text/css" media="screen" href="../css/redmond/jquery-ui-1.8.4.custom.css"/>
        <style type="text/css" media="all">@import url("https://dev.infrafrontier.eu/sites/infrafrontier.eu/themes/custom/infrafrontier/css/ebi.css");</style>

        <script type="text/javascript" src="https://www.google.com/jsapi"></script>
        <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/1.7.1/jquery.min.js"></script>
        <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jqueryui/1.7.2/jquery-ui.js"></script>
        <script type="text/javascript" src="../js/jquery.parsequery.js"></script>
        <script type="text/javascript" src="../js/jquery.parsequery.min.js"></script>
        <script type="text/javascript" src="../js/submission.js"></script>
        <script type="text/javascript" src="../js/autocomplete/autocomplete.js"></script>
        <script type="text/javascript" src="../js/mutData.js?<%= new java.util.Date()%>"></script>

        <script type="text/javascript" src="../js/tooltip.js"></script>

        <link rel="stylesheet" type="text/css" href="../css/autocomplete/autocomplete.css">

        <script type="text/javascript">
            $(document).ready(function() {
                $("#mutation_es_cell_line").autocomplete("../ajaxReturn.emma?funct=esCellLineCall&query=es", {mustMatch: 1, max: 100});
                $('#mutation_es_cell_line').result(function(event, data, formatted) {
                    if (data) {
                        var name = data[1];
                        $('#mutation_es_cell_line').text(name);
                    }
                });
            });

        </script>
        
    </head>

    <body onKeyPress="return disableEnterKey(event)">
        <br/>
        <p><img src="" height="1" width="145"/><a href="${BASEURL}"><img src="../images/infrafrontier/logo-infrafrontier.png" border="0"/></a></p>
                <jsp:include flush="true" page="submissionFormHeader_inc.jsp"/>
                <form:form method="POST" commandName="command"> 
            <div id="wrapper">
                <div id="container">
                    <div class="region region-content">
                        <div id="block-infrablocks-infraformtest" class="block block-infrablocks">
                            <div class="form visible">
                                <div class="boxcontainer"><h4>Genotype (Step ${stepCurrent} of ${stepTotal})</h4>
                                    <p>
                                        Please enter the genotype information of the mouse mutant strain you want to deposit in EMMA. A mutant strain is defined by its specific mutation(s) AND genetic background. Therefore, <strong>strains with the same mutation(s) but different genetic backgrounds require distinct names and consequently separate submissions</strong>.
                                    </p>
                                    <p>
                                        For the definitions of terms <a target="IMSR" href="http://www.informatics.jax.org/imsr/glossary.jsp">see the IMSR glossary</a>. For gene/allele symbols and identifiers please <a target='MGI' href='http://www.informatics.jax.org/javawi2/servlet/WIFetch?page=markerQF'>search MGI</a>.
                                    </p>
                                    <p>&nbsp;</p>
                                    <%--  <form:form method="POST" commandName="command"> --%>
                                    <input type="hidden" name="encID" id="encID" value="${param.getprev}"/>
                                    <input type="hidden" name="sessencID" id="sessencID" value="${sessionScope.getprev}"/>
                                    <div >
                                        <spring:bind path="command.strain_name">
                                            <form:errors path="${status.expression}" cssClass="error" />
                                            <div class="field">
                                                <p><strong>Strain name<sup><font color="red">*</font></sup></strong>&nbsp;<span class="tooltip" data-tooltip="<p>Please enter the mutant mouse strain name. If you are submitting lines on more than one background, please include the background in the strain name and submit each line separately.</p>">? Help</span></p>
                                                <div class="input">
                                                    <form:textarea id="${status.expression}" path="${status.expression}"  cols="50" rows="10" 
                                                                   title=""></form:textarea>
                                                    </div>
                                                </div>


                                        </spring:bind>
                                    </div>
                                </div>
                                <div class="boxcontainer">
                                    <div class="clear"></div>
                                    <div >
                                        <spring:bind path="command.genetic_descr">
                                            <form:errors path="${status.expression}" cssClass="error" />
                                            <div class="field">
                                                <p><strong>Genetic description<sup><font color="red">*</font></sup></strong>&nbsp;<span class="tooltip" data-tooltip="<p>Please enter a short description of the mutant mouse strain genotype (this will be used in the public web listing).</p>">? Help</span></p>

                                                <div class="input">
                                                    <form:textarea id="${status.expression}" path="${status.expression}"  cols="50" rows="5" 
                                                                   title=""></form:textarea>
                                                    </div>

                                                </div>
                                        </spring:bind>
                                    </div>
                                    <div class="clear"></div>
                                    <div >
                                        <spring:bind path="command.current_backg">
                                            <div class="field">

                                                <p><strong>Current genetic background<sup><font color="red">*</font></sup></strong>&nbsp;<span class="tooltip" data-tooltip="<p>Please specify the current genetic background of the strain that is being submitted.</p>">? Help</span></p>
                                                            <form:errors path="${status.expression}" cssClass="error" />
                                                <div class="input">
                                                    <!--TODO BACKGROUNDS FROM DATABASE PRODUCED LIST-->
                                                    <form:select path="${status.expression}" id="${status.expression}"  title="">

                                                        <form:option value='7'>Please select...</form:option><%--  Set deafult to bg_id of 7 which is to be updated --%>
                                                        <c:forEach var="background" items="${BGDAO}">

                                                            <form:option value='${background[0]}'>${fn:escapeXml(background[1])}</form:option>
                                                        </c:forEach>               
                                                    </form:select>&nbsp;
                                                </div>

                                            </div>
                                        </spring:bind>   
                                        <div id="currentBGText"  style="display: none">
                                            <p>

                                                <spring:bind path="command.current_backg">
                                                    <form:errors path="${status.expression}" cssClass="error" />
                                                    <input type="text" name="command.current_backg" id="current_backg_text" />
                                                </spring:bind>
                                            </p>
                                        </div>
                                    </div>
                                    <script>
                                        $("#current_backg").click(function() {
                                            // var optionValue = $("#list option[value='2']").text();
                                            var optionValue = $("select#current_backg").val();
                                            if (optionValue == '3284') {
                                                //ID for Other (please specify below)
                                                $("#currentBGText").show("slow");
                                            } else {
                                                $("#currentBGText").hide("slow");
                                            }
                                        });
                                    </script>

                                    <div>
                                        <spring:bind path="command.backcrosses">         

                                            <div class="field">
                                                <p><strong>Number of generations backcrossed</strong>&nbsp;<span class="tooltip" data-tooltip="<p>Please enter the number of generations backcrossed to background strain (if applicable and known).</p>">? Help</span></p>
                                                <form:errors path="${status.expression}" cssClass="error" />
                                                <div class="input">
                                                    <form:input maxlength="3"  id="${status.expression}" path="${status.expression}" title="" />
                                                </div>
                                            </div>
                                        </spring:bind>
                                    </div>    

                                    <div >
                                        <spring:bind path="command.sibmatings"><%-- TODO DISCOVER FIELD --%>
                                            <form:errors path="${status.expression}" cssClass="error" />
                                            <div class="field">
                                                <p><strong>Number of generations sib-mated</strong>&nbsp;<span class="tooltip" data-tooltip="<p>Please enter the number of generations mated to a sibling (since inception or subsequent to any outcrosses or backcrosses and if applicable and known).</p>">? Help</span></p>
                                                <div class="input">
                                                    <form:input maxlength="3" id="${status.expression}" path="${status.expression}"  title=""/>
                                                </div>

                                            </div>
                                        </spring:bind>
                                    </div>
                                    <div >
                                        <spring:bind path="command.breeding_history">
                                            <p><strong>Breeding history</strong>&nbsp;<span class="tooltip" data-tooltip="<p>Please describe the breeding history (outcrosses, backcrosses, intercrosses, incrosses) from the original founder strain to the current genetic background.</p>">? Help</span></p>
                                            <form:errors path="${status.expression}" cssClass="error" />
                                            <div class="input">
                                                <form:textarea id="${status.expression}" path="${status.expression}"  cols="50" rows="5" title=""></form:textarea>
                                                </div>

                                            </div>
                                    </spring:bind>
                                </div>
                                <div class="boxcontainer">
                                    <div class="clear"></div>
                                    <fieldset class="mutation" id="mutation">
                                        <div id="mutRef" name="mutRef"></div>
                                        <h4>Mutation(s)</h4>
                                        <p>Please record at least one mutation with the button below.</p>
                                        <div class="field mutation_type">
                                            <label class="label" for="mutation_type"><strong>Type<sup><font color="red">*</font></sup></strong></label>
                                            <input type="hidden" name="sessencID" id="sessencID" value="${sessionScope.getprev}"/>
                                            <div class="input">
                                                <spring:bind path="command.mutation_type">
                                                <!--TODO MUTS FROM DATABASE-->
                                                <select name="mutation_type" id="mutation_type">
                                                    <option value="">
                                                        &nbsp;
                                                    </option>
                                                    <option value="CH">
                                                        Chromosomal Anomaly
                                                    </option>
                                                    <option value="GT">
                                                        Gene-trap
                                                    </option>
                                                    <option value="IN">
                                                        Induced
                                                    </option>
                                                    <option value="SP">
                                                        Spontaneous
                                                    </option>
                                                    <option value="TG">
                                                        Transgenic
                                                    </option>
                                                    <option value="TM">
                                                        Targeted
                                                    </option>
                                                    <option value="XX">
                                                        Undefined
                                                    </option>
                                                </select>
                                                 <br/><form:errors path="${status.expression}" cssClass="error" />
                                                </spring:bind>
                                            </div>
                                        </div>
                                        <div id="exposeCH" style="display: none">CH Exposed</div>
                                        <script>
                                            $("#mutation_type").change(mutFieldDisplay);
                                            mutFieldDisplay();
                                        </script>
                                        <div style="display: none" class="field mutation_subtype conditional CH" id="field mutation_subtype_conditionalCH">
                                            <spring:bind path="command.mutation_subtypeCH">
                                            <label class="label" for="mutation_subtype"><strong>Subtype<sup><font color="red">*</font></sup></strong></label>
                                            <div class="input">
                                                <!--TODO MUTS SUBS FROM DATABASE-->
                                                
                                                <select name="mutation_subtypeCH" id="mutation_subtypeCH">
                                                    <option value="">
                                                        &nbsp;
                                                    </option>
                                                    <option value="INS">
                                                        Insertion
                                                    </option>
                                                    <option value="INV">
                                                        Inversion
                                                    </option>
                                                    <option value="DEL">
                                                        Deletion
                                                    </option>
                                                    <option value="DUP">
                                                        Duplication
                                                    </option>
                                                    <option value="TRL">
                                                        Translocation
                                                    </option>
                                                    <option value="TRP">
                                                        Transposition
                                                    </option>
                                                </select>

                                            </div>
                                            <div class="validation_error_message">
                                                &nbsp;
                                            </div>
                                                </spring:bind>
                                        </div>
                                        <div style="display: none" class="field mutation_subtype conditional IN" id="field mutation_subtype conditional IN">
                                            <spring:bind path="command.mutation_subtypeIN">
                                            <label class="label" for="mutation_subtype"><strong>Subtype<sup><font color="red">*</font></sup></strong></label>
                                            <div class="input">

                                                <select name="mutation_subtypeIN" id="mutation_subtypeIN">
                                                    <option value="">
                                                        &nbsp;
                                                    </option>
                                                    <option value="CH">
                                                        Chemical
                                                    </option>
                                                    <option value="RD">
                                                        Radiation
                                                    </option>

                                                </select>
                                            </div>
                                            <div class="validation_error_message">
                                                &nbsp;
                                            </div>
                                            </spring:bind>
                                        </div>
                                        <div style="display: none" class="field mutation_subtype conditional TM" id="field mutation_subtype conditional TM">
                                            <spring:bind path="command.mutation_subtypeTM">
                                            <label class="label" for="mutation_subtype"><strong>Subtype<sup><font color="red">*</font></sup></strong></label>

                                            <div class="input">
                                                <!--TODO MUTS SUBS FROM DATABASE-->
                                                <select name="mutation_subtypeTM" id="mutation_subtypeTM">
                                                    <option value="">
                                                        &nbsp;
                                                    </option>
                                                    <option value="KO">
                                                        Knock-out
                                                    </option>
                                                    <option value="KI">
                                                        Knock-in
                                                    </option>
                                                    <option value="PM">
                                                        Point mutation
                                                    </option>
                                                    <option value="CM">
                                                        Conditional mutation
                                                    </option>
                                                    <option value="OTH">
                                                        Other targeted mutation
                                                    </option>

                                                </select>
                                            </div>

                                            <div class="validation_error_message">
                                                &nbsp;
                                            </div>
                                            </spring:bind>
                                        </div>
                                        <div style="display: none" class="field mutation_transgene_mgi_symbol conditional TG" id="field mutation_transgene_mgi_symbol conditional TG">
                                            <spring:bind path="command.mutation_transgene_mgi_symbol">
                                            <label class="label" for="mutation_transgene_mgi_symbol"><strong>Transgene</strong></label>

                                            <div class="input">
                                                <input type="text" name="mutation_transgene_mgi_symbol" maxlength="20" value="" id="mutation_transgene_mgi_symbol" />
                                            </div>
                                            <div class="validation_error_message">
                                                &nbsp;
                                            </div>
                                            </spring:bind>
                                        </div>
                                        <div style="display: none" class="field mutation_gene_mgi_symbol conditional CH GT IN SP TM XX" id="field mutation_gene_mgi_symbol conditional CH GT IN SP TM XX">
                                            <spring:bind path="command.mutation_gene_mgi_symbol">
                                            <label class="label" for="mutation_gene_mgi_symbol"><strong>Affected gene</strong></label>
                                            <div class="input">
                                                <input type="text" name="mutation_gene_mgi_symbol" maxlength="20" value="" id="mutation_gene_mgi_symbol" />
                                            </div>
                                            <div class="validation_error_message">
                                                &nbsp;
                                            </div>
                                            </spring:bind>
                                        </div>
                                        <div style="display: none" class="field mutation_allele_mgi_symbol conditional CH GT IN SP TM XX" id="field mutation_allele_mgi_symbol conditional CH GT IN SP TM XX">
                                            <spring:bind path="command.mutation_allele_mgi_symbol">
                                            <label class="label" for="mutation_allele_mgi_symbol"><strong>Affected allele</strong></label>

                                            <div class="input">
                                                <input type="text" name="mutation_allele_mgi_symbol" maxlength="20" value="" id="mutation_allele_mgi_symbol" />
                                            </div>
                                            <div class="validation_error_message">
                                                &nbsp;
                                            </div>
                                            </spring:bind>
                                        </div>
                                        <div style="display: none" class="field mutation_chrom conditional CH GT IN SP TM XX" id="field mutation_chrom conditional CH GT IN SP TM XX">
                                            <spring:bind path="command.mutation_chrom">
                                            <label class="label" for="mutation_chrom"><strong>Affected chromosome</strong></label>
                                            <div class="input">
                                                <select name="mutation_chrom" id="mutation_chrom">
                                                    <option value="">
                                                        &nbsp;
                                                    </option>
                                                    <option value="1">
                                                        1
                                                    </option>
                                                    <option value="2">
                                                        2
                                                    </option>
                                                    <option value="3">
                                                        3
                                                    </option>
                                                    <option value="4">
                                                        4
                                                    </option>
                                                    <option value="5">
                                                        5
                                                    </option>
                                                    <option value="6">
                                                        6
                                                    </option>
                                                    <option value="7">
                                                        7
                                                    </option>
                                                    <option value="8">
                                                        8
                                                    </option>
                                                    <option value="9">
                                                        9
                                                    </option>
                                                    <option value="10">
                                                        10
                                                    </option>
                                                    <option value="11">
                                                        11
                                                    </option>
                                                    <option value="12">
                                                        12
                                                    </option>
                                                    <option value="13">
                                                        13
                                                    </option>
                                                    <option value="14">
                                                        14
                                                    </option>
                                                    <option value="15">
                                                        15
                                                    </option>
                                                    <option value="16">
                                                        16
                                                    </option>
                                                    <option value="17">
                                                        17
                                                    </option>
                                                    <option value="18">
                                                        18
                                                    </option>
                                                    <option value="19">
                                                        19
                                                    </option>
                                                    <option value="X">
                                                        X
                                                    </option>
                                                    <option value="Y">
                                                        Y
                                                    </option>
                                                </select>
                                            </div>
                                            <div class="validation_error_message">
                                                &nbsp;
                                            </div>
                                            </spring:bind>
                                        </div>
                                        <br/>
                                        <div class="field mutation_dominance_pattern">
                                            <spring:bind path="command.mutation_dominance_pattern">
                                            <label class="label" for="mutation_dominance_pattern"><strong>Dominance pattern</strong></label>
                                            <div class="input">
                                                <select name="mutation_dominance_pattern" id="mutation_dominance_pattern">
                                                    <option value="">
                                                        &nbsp;
                                                    </option>
                                                    <option value="recessive">
                                                        recessive
                                                    </option>
                                                    <option value="dominant">
                                                        dominant
                                                    </option>
                                                    <option value="codominant">
                                                        codominant
                                                    </option>
                                                    <option value="semidominant">
                                                        semidominant
                                                    </option>
                                                    <option value="X-linked">
                                                        X-linked
                                                    </option>
                                                </select>
                                            </div>
                                            <div class="validation_error_message">
                                                &nbsp;
                                            </div>
                                            </spring:bind>
                                        </div>

                                        <spring:bind path="command.mutation_original_backg">
                                            <div class="field mutation_original_backg">
                                                <p><strong>Original genetic background</strong>&nbsp;<span class="tooltip" data-tooltip="<p>Please specify the genetic background of the founder strain (e.g., the genetic background of ES cells or oocytes used to generate a knockout or transgenic mouse respectively).</p>">? Help</span></p>
                                                <form:errors path="${status.expression}" cssClass="error" />
                                                <div class="input">
                                                    <!--TODO BACKGROUNDS FROM DATABASE PRODUCED LIST-->
                                                    <form:select path="${status.expression}" id="${status.expression}" title="">

                                                        <form:option value='7'>Please select...</form:option><%--  Set deafult to bg_id of 7 which is to be updated --%>
                                                        <c:forEach var="background" items="${BGDAO}">
                                                            <form:option value='${background[0]}'>${fn:escapeXml(background[1])}</form:option>
                                                        </c:forEach>        
                                                    </form:select>
                                                </div>
                                            </div>


                                        </spring:bind>
                                        <div id="mutOriginalBGText"  style="display: none">
                                            <p>
                                                <spring:bind path="command.current_backg_text">
                                                    <form:errors path="${status.expression}" cssClass="error" />
                                                    <input type="text" name="command.current_backg_text" maxlength="200" id="mutation_original_backg_text" />

                                                </spring:bind>
                                            </p>
                                        </div>
                                        <script>
                                            $("#mutation_original_backg").click(function() {
                                                // var optionValue = $("#list option[value='2']").text();
                                                var optionValue = $("select#mutation_original_backg").val();
                                                if (optionValue == '3284') {
                                                    //ID for Other (please specify below)
                                                    $("#mutOriginalBGText").show("slow");
                                                } else {
                                                    $("#mutOriginalBGText").hide("slow");
                                                }
                                            });
                                        </script>

                                        <div style="display: none" class="field mutation_chrom_anomaly_name conditional CH" id="field mutation_chrom_anomaly_name conditional CH">
                                            <spring:bind path="command.mutation_chrom_anomaly_name">
                                            <label class="label" for="mutation_chrom_anomaly_name"><strong>Chromosomal anomaly name</strong></label>
                                            <div class="input">
                                                <input type="text" name="mutation_chrom_anomaly_name" maxlength="100" value="" id="mutation_chrom_anomaly_name" />
                                            </div>
                                            <div class="validation_error_message">
                                                &nbsp;
                                            </div>
                                            </spring:bind>
                                        </div>
                                        <div style="display: none" class="field mutation_chrom_anomaly_descr conditional CH" id="field mutation_chrom_anomaly_descr conditional CH">
                                            <spring:bind path="command.mutation_chrom_anomaly_descr">
                                            <label class="label" for="mutation_chrom_anomaly_descr"><strong>Chromosomal anomaly description</strong></label>
                                            <div class="input">
                                                <input type="text" name="mutation_chrom_anomaly_descr" maxlength="100" value="" id="mutation_chrom_anomaly_descr" />
                                            </div>
                                            <div class="validation_error_message">
                                                &nbsp;
                                            </div>
                                            </spring:bind>
                                        </div>
                                        <div style="display: none" class="field mutation_es_cell_line conditional GT TM" id="field mutation_es_cell_line conditional GT TM">
                                            <spring:bind path="command.mutation_es_cell_line">
                                            <label class="label" for="mutation_es_cell_line"><strong>ES cell line used</strong></label>
                                            <div class="input">
                                                <input type="text" name="mutation_es_cell_line" maxlength="255" value="" id="mutation_es_cell_line" />
                                            </div>
                                            <div class="validation_error_message">
                                                &nbsp;
                                            </div>
                                            </spring:bind>
                                        </div>
                                        <div style="display: none" class="field mutation_mutagen conditional IN" id="field mutation_mutagen conditional IN">
                                            <spring:bind path="command.mutation_mutagen">
                                            <label class="label" for="mutation_mutagen"><strong>Mutagen used</strong></label>
                                            <div class="input">
                                                <input type="text" name="mutation_mutagen" value="" id="mutation_mutagen" />
                                            </div>
                                            <div class="validation_error_message">
                                                &nbsp;
                                            </div>
                                            </spring:bind>
                                        </div>
                                        <div style="display: none" class="field mutation_promoter conditional TG" id="field mutation_promoter conditional TG">
                                            <spring:bind path="command.mutation_promoter">
                                            <label class="label" for="mutation_promoter"><strong>Promoter</strong></label>
                                            <div class="input">
                                                <input type="text" name="mutation_promoter" maxlength="150" value="" id="mutation_promoter" />
                                            </div>
                                            <div class="validation_error_message">
                                                &nbsp;
                                            </div>
                                            </spring:bind>
                                        </div>
                                        <div style="display: none" class="field mutation_founder_line_number conditional TG" id="field mutation_founder_line_number conditional TG">
                                            <spring:bind path="command.mutation_founder_line_number">
                                            <label class="label" for="mutation_founder_line_number"><strong>Founder line number</strong></label>
                                            <div class="input">
                                                <input type="text" name="mutation_founder_line_number" maxlength="150" value="" id="mutation_founder_line_number" />
                                            </div>
                                            <div class="validation_error_message">
                                                &nbsp;
                                            </div>
                                            </spring:bind>
                                        </div>
                                        <div style="display: none" class="field mutation_plasmid conditional TG" id="field mutation_plasmid conditional TG">
                                            <spring:bind path="command.mutation_plasmid">
                                            <label class="label" for="mutation_plasmid"><strong>Plasmid/construct name or symbol</strong></label>
                                            <div class="input">
                                                <input type="text" name="mutation_plasmid" maxlength="150" value="" id="mutation_plasmid" onKeyPress="return disableEnterKey(event)"/>
                                            </div>
                                            <div class="validation_error_message">
                                                &nbsp;
                                            </div>
                                            </spring:bind>
                                        </div>
                                        <br/>
                                        <input type="hidden" name="id_mut" id="id_mut"  value=""/>
                                    </fieldset>
                                    <div class="box half first">&nbsp;</div>
                                    <div class="box half last">
                                        <p align="center">
                                        <input value="Clear mutation fields" type="button" class="btn big" id="clearMutation" onclick="clear_form_elements(document.getElementById('mutation'))" />
                                    </p>
                                    </div>
                                </div>
                                <p>&nbsp;</p>  

                                <c:choose><c:when test="${empty param.getprev}"><c:set var="action" value="get"/></c:when><c:otherwise><c:set var="action" value="get"/></c:otherwise></c:choose>
                                        <div id="subMutations" name="subMutations">
                                            <script type="text/javascript" >
                                                $('#subMutations').load('ajaxMutations.emma', {
                                                    action: "${action}",
                                                    Id_sub: $('#encID').val(),
                                                    IDFromSession: $('#sessencID').val()
                                                });
                                    </script>
                                </div>
                                <div class="boxcontainer">

                                    <p>
                                        <%@include file="submissionFormControlButtons_inc.jsp"%>
                                    </p>
                                    <div class="box half first">&nbsp;</div>
                                    <div class="box half last">
                                    </div><%--2015--%>
                                </div>
                                <%--      </form:form>  --%>

                            </div>
                        </div>
                    </div>
                </div>

            </form:form>  

            <jsp:include flush="true" page="submissionFormFooter_inc.jsp"/>
    </body>
</html>
