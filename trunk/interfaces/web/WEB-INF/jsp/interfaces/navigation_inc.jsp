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
<!-- 
    Document   : navigation_inc
    Created on : 02-Feb-2009, 10:30:08
    Author     : phil
-->

<center><img src="../images/5656emmalogo.gif" width="56" height="56" alt="EMMA" border="0" align="absmiddle">&nbsp;&nbsp;&nbsp;<a href="index.emma" ONMOUSEOVER="activate('first')" ONMOUSEOUT="deactivate('first')"><img src="../images/entrypointbutton.gif" NAME="first" width="117" height="25" border="0" alt="Entry Point"  align="absmiddle"/></a><a href="archiveListInterface.emma" ONMOUSEOVER="activate('second')" ONMOUSEOUT="deactivate('second')"><img src="../images/archivebutton.gif" NAME="second" width="117" height="25" border="0"  align="absmiddle" alt="Archive"/></a><a href="strainsInterface.emma?listAll=y" ONMOUSEOVER="activate('third')" ONMOUSEOUT="deactivate('third')"><img src="../images/strainsbutton.gif" NAME="third" width="117" height="25" border="0"  align="absmiddle" alt="Strains"/></a><a href="requestsInterface.emma?listAll=y" ONMOUSEOVER="activate('fourth')" ONMOUSEOUT="deactivate('fourth')"><img src="../images/requestsbutton.gif" NAME="fourth" width="117" height="25" border="0"  align="absmiddle" alt="Requests"/></a><a href="polistInterface.emma" ONMOUSEOVER="activate('fifth')" ONMOUSEOUT="deactivate('fifth')"><img src="../images/pobutton.gif" NAME="fifth" width="117" height="25" border="0"  align="absmiddle" alt="Project Office"/></a><a href="http://www.emmanet.org/internal/partners.php" ONMOUSEOVER="activate('sixth')" ONMOUSEOUT="deactivate('sixth')"><img src="../images/contactsbutton.gif" NAME="sixth" width="117" height="25" border="0"  align="absmiddle" alt="Contacts"/></a><a href="http://www.emmanet.org/internal/" ONMOUSEOVER="activate('seventh')" ONMOUSEOUT="deactivate('seventh')"><img src="../images/internalsitebutton.gif" NAME="seventh" width="117" height="25" border="0"  align="absmiddle" alt="Internal Site"/></a>&nbsp;&nbsp;&nbsp;<img src="../images/5656emmalogo.gif" width="56" height="56" alt="EMMA" border="0"  align="absmiddle"></center>
<c:if test="${sessionScope.SPRING_SECURITY_LAST_USERNAME == 'curator' || sessionScope.SPRING_SECURITY_LAST_USERNAME == 'super'}"><br/><center><a href="../2/" target="_BLANK">Curation Interfaces</a></center></c:if>


