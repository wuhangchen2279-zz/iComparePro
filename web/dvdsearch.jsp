<%-- 
    Document   : dvdsearch
    Created on : Apr 9, 2013, 9:37:32 PM
    Author     : hwu65
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link href="http://fonts.googleapis.com/css?family=Open+Sans:400,300,600,700|Archivo+Narrow:400,700" rel="stylesheet" type="text/css">
        <link href="default.css" rel="stylesheet" type="text/css" media="all" />
        <title>iCompare</title>
    </head>
    <body>
        <div id="header" class="container">
            <div id="logo">
                <h1><a href="#">iCompare Search</a></h1>
            </div>
            <div id="menu">
                <ul>
                    <li><a href="index.jsp" accesskey="1" title="">Book</a></li>
                    <li  class="active"><a href="dvdsearch.jsp" accesskey="2" title="">DVD</a></li>
                </ul>
            </div>
        </div>
        <div id="page" class="container">
            <form name="Test" method="post" action="DvdSearchServlet" style="padding-left: 250px;">
                    <b style="font-size: 18px;">Search With Title/Creator:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</b><br/> 
                    <input rows="1" name="searchArea" cols="40" ID="searchArea" style="width: 450px; height: 30px;"/></input>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    <input type="submit" value="Search" name="searchButton" class="link-style">
            </form>
        </div>
        <div id="copyright" class="container">
                <p>Copyright (c) 2013 iCompare.com. All rights reserved.</p>
        </div>
    </body>
</html>
