<%-- 
    Document   : submissionFormControlButtons_inc
    Created on : Apr 15, 2013, 2:37:39 PM
    Author     : phil
--%>
<%--<c:set var="stepCurrent" value="${(sessionScope.pageCount)}" scope="page" />
<c:set var="stepTotal" value="${(sessionScope.totalStepCount)}" scope="page" />
Control buttons, submit/cancel/previous for all active pages in the form submission
--%>
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