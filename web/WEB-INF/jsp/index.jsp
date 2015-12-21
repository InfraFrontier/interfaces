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
<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://"
            + request.getServerName() + ":" + request.getServerPort()
            + path + "/";
%>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<!--[if lt IE 7 ]> <html lang="en" class="no-js ie6"> <![endif]-->
<!--[if IE 7 ]>    <html lang="en" class="no-js ie7"> <![endif]-->
<!--[if IE 8 ]>    <html lang="en" class="no-js ie8"> <![endif]-->
<!--[if IE 9 ]>    <html lang="en" class="no-js ie9"> <![endif]-->
<!--[if (gt IE 9)|!(IE)]><!-->
<html lang="en" class="no-js"><!--<![endif]-->
    <head>

        <!-- Infrafrontier -->
        <title>EMMA - the European Mouse Mutant Archive</title>
        <meta charset="utf-8">
        <meta name="author" content="Infrafrontier" />
        <meta name="description" content="EMMA is a non-profit repository for the collection, archiving (via cryopreservation) and distribution of relevant mutant strains essential for basic biomedical" />
        <meta name="keywords" content="EMMA, Infrafrontier" />
        <meta name="copyright" content="Infrafrontier" />
        <link rel="canonical" href="https://www.infrafrontier.eu" />
        <base href="<%=path%>/" />

        <!-- css -->
        <link href="http://dev.infrafrontier.eu/fonts.googleapis.com/css?family=Open+Sans:400,600" rel="stylesheet" type="text/css" />
        <link href="http://dev.infrafrontier.eu/sites/infrafrontier.eu/themes/custom/infrafrontier/css/default.css" rel="stylesheet" type="text/css" />
        <link href="https://dev.infrafrontier.eu/sites/infrafrontier.eu/themes/custom/infrafrontier/css/ebi.css" rel="stylesheet" type="text/css" />
        <link href="<%=path%>/css/default.css" rel="stylesheet" type="text/css" />
        <link href="<%=path%>/css/default_frontpage.css" rel="stylesheet" type="text/css" />

        <!-- js
        <script type="text/javascript" src="http://dev.infrafrontier.eu/misc/jquery.js?v=1.4.4"></script> -->

        <script src="http://jquery-ui.googlecode.com/svn/tags/1.8.18/jquery-1.7.1.js"></script>
        <script src="http://jquery-ui.googlecode.com/svn/tags/1.8.18/ui/jquery-ui.js"></script>

     
    </head>

    <body class="html front not-logged-in no-sidebars emma">	

        <div id="wrapper">

            <header id="header">        
                <div id="innerheader">    
                    <div id="logo">
                        <a href="<%=path%>/" title="Infrafrontier"><img src="<%=path%>/images/logo-emma.png" /></a>
                    </div>      
                    <div class="clear"></div>        
                </div>    
            </header>

            <footer id="footer">
                <div class="innerfooter">        
                           	           
                    <div id="tn">
                        <div class="region region-usernavi">
                            <div id="block-menu-block-4" class="block block-menu-block">
                                <div class="menu-block-wrapper menu-block-4 menu-name-user-menu parent-mlid-0 menu-level-1">
                                </div>
                            </div>
                        </div>
                    </div>        
                </div>
                <div id="footerline">        	
                    <div class="innerfooter">
                        <div class="splithalf">
                            <div class="half">
                                &copy; INFRAFRONTIER 2013 - all rights reserved
                            </div>
                            <div class="half">
                                <div id="bn">
                                    <div class="region region-usernavi">
                                        <div class="region region-usernavi">
                                            <div id="block-menu-block-4" class="block block-menu-block">

                                                <div class="menu-block-wrapper menu-block-4 menu-name-user-menu parent-mlid-0 menu-level-1">
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>            
                </div>

            </footer>

        </div></body>
</html>