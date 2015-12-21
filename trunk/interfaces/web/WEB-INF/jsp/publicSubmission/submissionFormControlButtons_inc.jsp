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
    Document   : submissionFormControlButtons_inc
    Created on : Apr 15, 2013, 2:37:39 PM
    Author     : phil
--%>
<%--<c:set var="stepCurrent" value="${(sessionScope.pageCount)}" scope="page" />
<c:set var="stepTotal" value="${(sessionScope.totalStepCount)}" scope="page" />
Control buttons, submit/cancel/previous for all active pages in the form submission
--%>
<%
        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", -1);
        response.setHeader("Cache-Control", "no-store");
%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib uri="http://jakarta.apache.org/taglibs/datetime-1.0" prefix="dt" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/request-1.0" prefix="req" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<style type="text/css">@import url(../css/default.css);</style>
<!DOCTYPE html>
            <div class="boxcontainer">
                <div class="box half first">
                    <p>
                        <c:choose>
                                   <c:when test="${stepCurrent != 0}"><input type="submit" class="btn big" value="Previous" name="_target${stepCurrent-1}" /></c:when>
                                   <c:otherwise></c:otherwise>
                               </c:choose>
                        &nbsp;&nbsp;
                        <input type="submit" class="btn big"
                               <c:choose>
                                   <c:when test="${stepCurrent == stepTotal}">value="Submit" name="_finish"</c:when>
                                   <c:otherwise>value="Next" name="_target${stepCurrent+1}"</c:otherwise>
                               </c:choose> />
                    </p>
                </div>
                <div class="box half last">
                    <p class="txtright">
                        <input type="submit" class="btn big" value="Cancel" name="_cancel" />
                    </p>
                </div>
                <div class="clear"></div>
            </div>