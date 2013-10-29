<%-- 
    Document   : index
    Created on : Apr 9, 2013, 12:21:29 PM
    Author     : hwu65
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link href="http://fonts.googleapis.com/css?family=Open+Sans:400,300,600,700|Archivo+Narrow:400,700" rel="stylesheet" type="text/css">
        <link href="default.css" rel="stylesheet" type="text/css" media="all" />
         <!--Load the AJAX API-->
        <script type="text/javascript" src="https://www.google.com/jsapi"></script>
        <script type="text/javascript">

          // Load the Visualization API and the piechart package.
          google.load('visualization', '1.0', {'packages':['corechart']});

          // Set a callback to run when the Google Visualization API is loaded.
          google.setOnLoadCallback(drawAmazonChart);

          // Callback that creates and populates a data table, 
          // instantiates the pie chart, passes in the data and
          // draws it.
          function drawAmazonChart() {

            // Create the data table.
            var data = new google.visualization.DataTable();
            data.addColumn('string', 'Topping');
            data.addColumn('number', 'Slices');
            data.addRows([
              ['Mushrooms', 3],
              ['Onions', 1],
              ['Olives', 1], 
              ['Zucchini', 1],
              ['Pepperoni', 2]
            ]);

            // Set chart options
            var options = {'title':'How Much Pizza I Ate Last Night',
                           'width':400,
                           'height':300,
                            backgroundColor: '#161616',
                            titleColor:'white',
                            legendTextColor:'white'};

            // Instantiate and draw our chart, passing in some options.
            var chart = new google.visualization.PieChart(document.getElementById('amazonchart_div'));

            function selectHandler() {
              var selectedItem = chart.getSelection()[0];
              if (selectedItem) {
                var topping = data.getValue(selectedItem.row, 0);
                if(topping == "Positive") {
                    document.getElementById('noteinfo').style.display = "none";
                    document.getElementById('neturaltitle').style.display = "none";
                    document.getElementById('neturalcontent').style.display = "none";
                    document.getElementById('negativetitle').style.display = "none";
                    document.getElementById('negativecontent').style.display = "none";
                    document.getElementById('positivetitle').style.display = "block";
                    document.getElementById('positivecontent').style.display = "block";
                }
                if(topping == "Netural") {
                    document.getElementById('noteinfo').style.display = "none";
                    document.getElementById('positivetitle').style.display = "none";
                    document.getElementById('positivecontent').style.display = "none";
                    document.getElementById('negativetitle').style.display = "none";
                    document.getElementById('negativecontent').style.display = "none";
                    document.getElementById('neturaltitle').style.display = "block";
                    document.getElementById('neturalcontent').style.display = "block";
                }
                if(topping == "Negative") {
                    document.getElementById('noteinfo').style.display = "none";
                    document.getElementById('neturaltitle').style.display = "none";
                    document.getElementById('neturalcontent').style.display = "none";
                    document.getElementById('positivetitle').style.display = "none";
                    document.getElementById('positivecontent').style.display = "none";
                    document.getElementById('negativetitle').style.display = "block";
                    document.getElementById('negativecontent').style.display = "block";
                }
              }
            }

            google.visualization.events.addListener(chart, 'select', selectHandler);    
            chart.draw(data, options);
          }

        </script>
        <script type="text/javascript">

          // Load the Visualization API and the piechart package.
          google.load('visualization', '1.0', {'packages':['corechart']});

          // Set a callback to run when the Google Visualization API is loaded.
          google.setOnLoadCallback(drawTwitterChart);

          // Callback that creates and populates a data table, 
          // instantiates the pie chart, passes in the data and
          // draws it.
          function drawTwitterChart() {

            // Create the data table.
            var data = new google.visualization.DataTable();
            data.addColumn('string', 'Topping');
            data.addColumn('number', 'Slices');
            data.addRows([
              ['Mushrooms', 3],
              ['Onions', 1],
              ['Olives', 1], 
              ['Zucchini', 1],
              ['Pepperoni', 2]
            ]);

            // Set chart options
            var options = {'title':'How Much Pizza I Ate Last Night',
                           'width':400,
                           'height':300,
                            backgroundColor: '#161616',
                            titleColor:'white',
                        legendTextColor:'white'};

            // Instantiate and draw our chart, passing in some options.
            var chart = new google.visualization.PieChart(document.getElementById('twitterchart_div'));

            function selectHandler() {
              var selectedItem = chart.getSelection()[0];
              if (selectedItem) {
                var topping = data.getValue(selectedItem.row, 0);
                alert('The user selected ' + topping);
              }
            }

            google.visualization.events.addListener(chart, 'select', selectHandler);    
            chart.draw(data, options);
          }

        </script>
        <title>iCompare</title>
    </head>
    <body>
        <div id="header" class="container">
            <div id="logo">
                <h1><a href="#">iCompare Search</a></h1>
            </div>
            <div id="menu">
                <ul>
                    <li class="active"><a href="index.jsp" accesskey="1" title="">Book</a></li>
                    <li><a href="dvdsearch.jsp" accesskey="2" title="">DVD</a></li>
                </ul>
            </div>
        </div>
        <div id="page" class="container">
            <form name="Test" method="post" action="BookSearchServlet" style="padding-left: 250px;">
                    <b style="font-size: 18px;">Search With Title/Author/ISBN:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</b><br/> 
                    <input rows="1" name="searchArea" cols="40" ID="searchArea" style="width: 450px; height: 30px;"/></input>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    <input type="submit" value="Search" name="searchButton" class="link-style">
            </form>
            <!--
            <div id="content">
                <div id="onecolumntitle">Compare Price</div>
                <div id="onecolumn">
                    <table>
                        <tr style="border-bottom-width: 10px; border-bottom-color: black;">
                            <th style="width: 300px;">Our Prices</th>
                            <th style="width: 300px">Amazon Price</th>
                            <th style="width: 300px">eBay Price</th>
                        </tr>
                        <tr>
                            <td style="width: 300px; vertical-align: text-top">sd</td>
                            <td style="width: 300px">sd</td>
                            <td style="width: 300px">sd</td>
                        </tr>
                        <tr>
                            <td style="width: 300px">sd</td>
                            <td style="width: 300px">sd</td>
                            <td style="width: 300px">sd</td>
                        </tr>
                    </table>
                </div>
            </div>
            <div id="sidebar">
                <div id="sidebartitle">Google Result</div>
                <div id="sbox1">
                    <h2>Fusce fringilla</h2>
                    <ul class="list-style1">
                            <li class="first">
                                    <p>Etiam non felis. Donec ut ante. In id eros. Suspendisse lacus, cursus egestas at sem. </p>
                                    <p><a href="#" class="link-style">Read More</a></p>
                            </li>
                    </ul>
                </div>
                <div id="sbox1">
                    <h2>Fusce fringilla</h2>
                    <ul class="list-style1">
                            <li class="first">
                                    <p>Etiam non felis. Donec ut ante. In id eros. Suspendisse lacus, cursus egestas at sem. </p>
                                    <p><a href="#" class="link-style">Read More</a></p>
                            </li>
                    </ul>
                </div>
            </div>
            -->
        </div>
        
        <!--
        <div id="footer" class="container">
            <h2>Sentiment  Analysis</h2>
            <div id="fbox1">
                    <h2>Amazon Result</h2>
                    <div id="amazonchart_div" style="width:400px; height:300px; color: #4E4E4E;">                    
                    </div>
                    <div id="noteinfo" style="color: grey; padding-left: 20px;">Note:To view sample review, please click the pie char element!</div>
                    <div id="positivetitle" style="color: grey; padding-left: 20px; font-size: 17px; font-weight: bold;" hidden="true">Positive Review Sample</div>
                    <div id="positivecontent" style="color: grey; padding-left: 20px;" hidden="true">
                        <div style="padding-left: 10px">
                            Review Title:
                        </div>
                        <div style="padding-left: 10px">
                            Review Editor:
                        </div>
                        <div style="padding-left: 10px">
                            Review Content:
                        </div>
                        <p/>
                    </div>
                    <div id="neturaltitle" style="color: grey; padding-left: 20px; font-size: 17px; font-weight: bold;" hidden="true">Neutral Review Sample</div>
                    <div id="neturalcontent" style="color: grey; padding-left: 20px;" hidden="true">
                        <div style="padding-left: 10px">
                            Review Title:
                        </div>
                        <div style="padding-left: 10px">
                            Review Editor:
                        </div>
                        <div style="padding-left: 10px">
                            Review Content:
                        </div>
                        <p/>
                    </div>
                    <div id="negativetitle" style="color: grey; padding-left: 20px; font-size: 17px; font-weight: bold;" hidden="true">Negative Review Sample</div>
                    <div id="negativecontent" style="color: grey; padding-left: 20px;" hidden="true">
                        <div style="padding-left: 10px">
                            Review Title:
                        </div>
                        <div style="padding-left: 10px">
                            Review Editor:
                        </div>
                        <div style="padding-left: 10px">
                            Review Content:
                        </div>
                        <p/>
                    </div>
            </div>
            <div id="fbox2">
                    <h2>Twitter Result</h2>
                    <div id="twitterchart_div" style="width:400px; height:300px; color: #4E4E4E;">                    
                    </div>
            </div>
        </div>   
        -->
        
        <div id="copyright" class="container">
                <p>Copyright (c) 2013 iCompare.com. All rights reserved. </p>
        </div>
    
    </body>
</html>
