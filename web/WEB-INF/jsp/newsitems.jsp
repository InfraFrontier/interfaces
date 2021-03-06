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
    Document   : newsitems
    Created on : 29-Oct-2009, 14:24:20
    Author     : phil
--%>

<%@page contentType="text/html" pageEncoding="UTF-8" session="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib uri="http://jakarta.apache.org/taglibs/datetime-1.0" prefix="dt" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/request-1.0" prefix="req" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>EMMA News Items</title>
    </head>
    <body>
        <form:form>
            <table>
                <tr>
                    <td>Title:</td>
                    <td><input type="text" size="50" maxlength="150" name="title"/></td>
                </tr>
                <tr>
                    <td>URL:</td>
                    <td><input type="text" size="50" maxlength="100" name="link"/></td>
                </tr>
                <tr>
                    <td>Description:</td>
                    <td><textarea  name="description"  cols="50" rows="4" maxlength="350"></textarea></td>
                </tr>
                <tr><td colspan="2"><input name="update" value="Update" class="button" type="submit">
                            <input name="Clear updates" value="Clear updates" class="button" type="reset">
                        </td>
                        </tr>
            </table>

        </form:form>
    </body>
</html>
