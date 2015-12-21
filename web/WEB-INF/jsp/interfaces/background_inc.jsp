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
    Document   : background_inc.jsp
    Created on : 12-May-2009, 16:26:45
    Author     : phil
--%>

<%@page import="org.emmanet.util.WebmasterAlertMsg"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib uri="http://jakarta.apache.org/taglibs/datetime-1.0" prefix="dt" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/request-1.0" prefix="req" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="org.emmanet.util.Configuration" %>
<%@page import="java.io.*"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">

<%
//OK resigned to creating a scriptlet to read in values from output file

 String bgname = request.getParameter("bgname");
            String selected = "";
            String optionDisplay="";
            String optionDisplayClose="";
             if(request.getParameter("optionDisplay") != null){
                     optionDisplay = request.getParameter("optionDisplay");  
optionDisplay ="<form:option value=\"";
                optionDisplayClose="</form:option>";                          
             }else {
                optionDisplay ="<option value=\"";
                optionDisplayClose="</option>";
             }
            try {
                BufferedReader in = new BufferedReader(new FileReader(Configuration.get("BGNAMESLIST")));
                String str;
 outerloop:
                while ((str = in.readLine()) != null) {
                   int i = str.indexOf("||");
                   if(i<0){
                       //could be a problem with created bg list, send a mail to webmaster as an alert
                       WebmasterAlertMsg alertMsg = new WebmasterAlertMsg();
                       System.out.println("alert message object # is " + alertMsg.hashCode());
                       alertMsg.sendAlert(" Alert generated, possible issue with BG names file."," -- There is a problem with the generated BG list. Check ASAP.");
                       break outerloop;
                   }
                   String id = str.substring(0, i);
                   str = str.replace(id + "||", "");
                   str = org.apache.commons.lang.StringEscapeUtils.escapeHtml(str);
                   String param = request.getParameter("bgname");
                    if (id.equals(param)){
                        selected="selected";
                    } else {
                       selected="";
                    }
                //   System.out.println("<option value=\""+ id + "\"  "+selected +">"+ str+"</option>");
%>

<%=optionDisplay %><%= id %>" <%= selected %>><%= str%><%=optionDisplayClose %>

<%


                }

                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

%>