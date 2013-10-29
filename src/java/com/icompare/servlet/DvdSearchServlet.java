/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.icompare.servlet;

import com.icompare.entity.Dvds;
import com.icompare.managedsearch.SearchManager;
import com.icompare.utility.SearchResultGoogle;
import com.icompare.utility.AmazonEbayResult;
import com.icompare.ws.ReviewResult;
import com.icompare.ws.SampleReview;
import com.icompare.wsclient.DvdJerseyClient;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.GenericType;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import org.json.JSONException;

/**
 *
 * @author hwu65
 */
@WebServlet(name = "DvdSearchServlet", urlPatterns = {"/DvdSearchServlet"})
public class DvdSearchServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP
     * <code>GET</code> and
     * <code>POST</code> methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, MalformedURLException, JSONException, OAuthMessageSignerException, OAuthExpectationFailedException, OAuthCommunicationException{
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        String searchArea = request.getParameter("searchArea");
        List<Dvds> searchDvdsLocal = new ArrayList<Dvds>();
        SearchManager sbm = new SearchManager();
        searchDvdsLocal = sbm.searchDvdLocal(searchArea);
        List<AmazonEbayResult> searchDvdsAmazonPrice = new ArrayList<AmazonEbayResult>();
        searchDvdsAmazonPrice = sbm.searchPriceResultAmazon(searchArea, "DVD");
        List<AmazonEbayResult> searchDvdsEbayPrice = new ArrayList<AmazonEbayResult>();
        searchDvdsEbayPrice = sbm.searchPriceResultEbay(searchArea, "DVD");
        List<SearchResultGoogle> searchDvdsGoogle = new ArrayList<SearchResultGoogle>();
        searchDvdsGoogle = sbm.searchDvdGoogle(searchArea);
        List<ReviewResult> reviewResultAmazon = new ArrayList<ReviewResult>();
        reviewResultAmazon = sbm.searchAmazonCustomerReviews(searchArea, "DVD");
        ReviewResult reviewResultTwitter = new ReviewResult();
        reviewResultTwitter = sbm.searchTwitterCustomerReviews(searchArea, "Movie");
        int amazonPositiveNum = 0;
        int amazonNeturalNum = 0;
        int amazonNegativeNum = 0;
        String coverUrl = null;
        
        try {
            /* TODO output your page here. You may use following sample code. */  
            out.println("<html>");
            out.println("<head>");
            out.println("<title>iCompare</title>"); 
            out.println("<link href=\"http://fonts.googleapis.com/css?family=Open+Sans:400,300,600,700|Archivo+Narrow:400,700\" rel=\"stylesheet\" type=\"text/css\" />");
            out.println("<link href=\"default.css\" rel=\"stylesheet\" type=\"text/css\" media=\"all\" />");
            
            for(int i=0; i<reviewResultAmazon.size(); i++) {
                amazonPositiveNum += reviewResultAmazon.get(i).getPositiveAmount();
                amazonNeturalNum += reviewResultAmazon.get(i).getNeturalAmount();
                amazonNegativeNum += reviewResultAmazon.get(i).getNegativeAmount();
            }
            out.println("<script type=\"text/javascript\" src=\"https://www.google.com/jsapi\"></script>\n" +
"        <script type=\"text/javascript\">\n" +
"\n" +
"          // Load the Visualization API and the piechart package.\n" +
"          google.load('visualization', '1.0', {'packages':['corechart']});\n" +
"\n" +
"          // Set a callback to run when the Google Visualization API is loaded.\n" +
"          google.setOnLoadCallback(drawAmazonChart);\n" +
"\n" +
"          // Callback that creates and populates a data table, \n" +
"          // instantiates the pie chart, passes in the data and\n" +
"          // draws it.\n" +
"          function drawAmazonChart() {\n" +
"\n" +
"            // Create the data table.\n" +
"            var data = new google.visualization.DataTable();\n" +
"            data.addColumn('string', 'Topping');\n" +
"            data.addColumn('number', 'Slices');\n" +
"            data.addRows([\n" +
"              ['Positive', " + amazonPositiveNum + "], \n" +
"              ['Netural', "+ amazonNeturalNum +"],\n" +
"              ['Negative', "+ amazonNegativeNum +"]\n" +
"            ]);\n" +
"\n" +
"            // Set chart options\n" +
"            var options = {'title':'Amazon Review Analysis',\n" +
"                           'width':400,\n" +
"                           'height':300,\n" +
"                            backgroundColor: '#161616',\n" +
"                            titleColor:'white',\n" +
"                            legendTextColor:'white'};\n" +
"\n" +
"            // Instantiate and draw our chart, passing in some options.\n" +
"            var chart = new google.visualization.PieChart(document.getElementById('amazonchart_div'));\n" +
"\n" +
"            function selectHandler() {\n" +
"              var selectedItem = chart.getSelection()[0];\n" +
"              if (selectedItem) {\n" +
"                var topping = data.getValue(selectedItem.row, 0);\n" +
"                if(topping == \"Positive\") {\n" +
"                    document.getElementById('noteinfo').style.display = \"none\";\n" +
"                    document.getElementById('neturaltitle').style.display = \"none\";\n" +
"                    document.getElementById('neturalcontent').style.display = \"none\";\n" +
"                    document.getElementById('negativetitle').style.display = \"none\";\n" +
"                    document.getElementById('negativecontent').style.display = \"none\";\n" +
"                    document.getElementById('positivetitle').style.display = \"block\";\n" +
"                    document.getElementById('positivecontent').style.display = \"block\";\n" +
"                }\n" +
"                if(topping == \"Netural\") {\n" +
"                    document.getElementById('noteinfo').style.display = \"none\";\n" +
"                    document.getElementById('positivetitle').style.display = \"none\";\n" +
"                    document.getElementById('positivecontent').style.display = \"none\";\n" +
"                    document.getElementById('negativetitle').style.display = \"none\";\n" +
"                    document.getElementById('negativecontent').style.display = \"none\";\n" +
"                    document.getElementById('neturaltitle').style.display = \"block\";\n" +
"                    document.getElementById('neturalcontent').style.display = \"block\";\n" +
"                }\n" +
"                if(topping == \"Negative\") {\n" +
"                    document.getElementById('noteinfo').style.display = \"none\";\n" +
"                    document.getElementById('neturaltitle').style.display = \"none\";\n" +
"                    document.getElementById('neturalcontent').style.display = \"none\";\n" +
"                    document.getElementById('positivetitle').style.display = \"none\";\n" +
"                    document.getElementById('positivecontent').style.display = \"none\";\n" +
"                    document.getElementById('negativetitle').style.display = \"block\";\n" +
"                    document.getElementById('negativecontent').style.display = \"block\";\n" +
"                }" +
"              }\n" +
"            }\n" +
"\n" +
"            google.visualization.events.addListener(chart, 'select', selectHandler);    \n" +
"            chart.draw(data, options);\n" +
"          }\n" +
"\n" +
"        </script>\n" +
"        <script type=\"text/javascript\">\n" +
"\n" +
"          // Load the Visualization API and the piechart package.\n" +
"          google.load('visualization', '1.0', {'packages':['corechart']});\n" +
"\n" +
"          // Set a callback to run when the Google Visualization API is loaded.\n" +
"          google.setOnLoadCallback(drawTwitterChart);\n" +
"\n" +
"          // Callback that creates and populates a data table, \n" +
"          // instantiates the pie chart, passes in the data and\n" +
"          // draws it.\n" +
"          function drawTwitterChart() {\n" +
"\n" +
"            // Create the data table.\n" +
"            var data = new google.visualization.DataTable();\n" +
"            data.addColumn('string', 'Topping');\n" +
"            data.addColumn('number', 'Slices');\n" +
"            data.addRows([\n" +
"              ['Positive', " + reviewResultTwitter.getPositiveAmount() + "],\n" +
"              ['Netural', " + reviewResultTwitter.getNeturalAmount() + "],\n" +
"              ['Negative', " + reviewResultTwitter.getNegativeAmount() + "] \n" +
"            ]);\n" +
"\n" +
"            // Set chart options\n" +
"            var options = {'title':'Twitter Review Analysis',\n" +
"                           'width':400,\n" +
"                           'height':300,\n" +
"                            backgroundColor: '#161616',\n" +
"                            titleColor:'white',\n" +
"                        legendTextColor:'white'};\n" +
"\n" +
"            // Instantiate and draw our chart, passing in some options.\n" +
"            var chart = new google.visualization.PieChart(document.getElementById('twitterchart_div'));\n" +
"\n" +
"            function selectHandler() {\n" +
"              var selectedItem = chart.getSelection()[0];\n" +
"              if (selectedItem) {\n" +
"                var topping = data.getValue(selectedItem.row, 0);\n" +
"                if(topping == \"Positive\") {\n" +
"                    document.getElementById('noteinfotwitter').style.display = \"none\";\n" +
"                    document.getElementById('neturaltitletwitter').style.display = \"none\";\n" +
"                    document.getElementById('neturalcontenttwitter').style.display = \"none\";\n" +
"                    document.getElementById('negativetitletwitter').style.display = \"none\";\n" +
"                    document.getElementById('negativecontenttwitter').style.display = \"none\";\n" +
"                    document.getElementById('positivetitletwitter').style.display = \"block\";\n" +
"                    document.getElementById('positivecontenttwitter').style.display = \"block\";\n" +
"                }\n" +
"                if(topping == \"Netural\") {\n" +
"                    document.getElementById('noteinfotwitter').style.display = \"none\";\n" +
"                    document.getElementById('positivetitletwitter').style.display = \"none\";\n" +
"                    document.getElementById('positivecontenttwitter').style.display = \"none\";\n" +
"                    document.getElementById('negativetitletwitter').style.display = \"none\";\n" +
"                    document.getElementById('negativecontenttwitter').style.display = \"none\";\n" +
"                    document.getElementById('neturaltitletwitter').style.display = \"block\";\n" +
"                    document.getElementById('neturalcontenttwitter').style.display = \"block\";\n" +
"                }\n" +
"                if(topping == \"Negative\") {\n" +
"                    document.getElementById('noteinfotwitter').style.display = \"none\";\n" +
"                    document.getElementById('neturaltitletwitter').style.display = \"none\";\n" +
"                    document.getElementById('neturalcontenttwitter').style.display = \"none\";\n" +
"                    document.getElementById('positivetitletwitter').style.display = \"none\";\n" +
"                    document.getElementById('positivecontenttwitter').style.display = \"none\";\n" +
"                    document.getElementById('negativetitletwitter').style.display = \"block\";\n" +
"                    document.getElementById('negativecontenttwitter').style.display = \"block\";\n" +
"                }" +
"              }\n" +
"            }\n" +
"\n" +
"            google.visualization.events.addListener(chart, 'select', selectHandler);    \n" +
"            chart.draw(data, options);\n" +
"          }\n" +
"\n" +
"        </script>");
            out.println("</head>");
            out.println("<body>");
            out.println(
                    "<div id=\"header\" class=\"container\">\n" +
"            <div id=\"logo\">\n" +
"                <h1><a href=\"#\">iCompare Search</a></h1>\n" +
"            </div>\n" +
"            <div id=\"menu\">\n" +
"                <ul>\n" +
"                    <li><a href=\"index.jsp\" accesskey=\"1\" title=\"\">Book</a></li>\n" +
"                    <li class=\"active\"><a href=\"dvdsearch.jsp\" accesskey=\"2\" title=\"\">DVD</a></li>\n" +
"                </ul>\n" +
"            </div>\n" +
"        </div>\n" +
"        <div id=\"page\" class=\"container\">\n"  +
"                <form name=\"Test\" method=\"post\" action=\"DvdSearchServlet\"  style=\"padding-left: 250px;\">\n" +
"                    <b style=\"font-size: 18px;\">Search With Title/Creator:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</b>\n<br/>" +
"                    <input name=\"searchArea\" ID=\"searchArea\" style=\"width: 450px; height: 30px;\" value=\"" +searchArea + "\"/></input>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;\n" +
"                    <input type=\"submit\" value=\"Search\" name=\"searchButton\" class=\"link-style\"/>\n" +
"                </form>" +
"            <div id=\"content\">\n"
                    );
            
            out.println("<div id=\"onecolumntitle\">Compare Price</div>");
            
            /*
            if(searchDvdsLocal.isEmpty()) {
                out.println("<div id=\"onecolumn\">");
                out.println("<div style=\"font-weight: bolder; padding-left: 5px; font-size: 14px;\">This DVD does not exist in our database</div>");
                out.println("</div>");
            }
            * */
            
            out.println("<div id=\"onecolumn\">\n" +
"                    <table>\n" +
"                        <tr>\n" +
"                            <th style=\"width: 300px\">Our Price</th>\n" +
"                            <th style=\"width: 300px\">Amazon Price</th>\n" +
"                            <th style=\"width: 300px\">eBay Price</th>\n" +
"                        </tr>");
            
            for(int i=0; i< Math.max(Math.max(searchDvdsLocal.size(), searchDvdsAmazonPrice.size()),searchDvdsEbayPrice.size()); i++) {
                out.println("<tr>");
                try{
                    Dvds d = searchDvdsLocal.get(i);
                    coverUrl = sbm.searchCover(d.getDvdtitle());
                    out.println("<td style=\"width: 300px; vertical-align: text-top\">");
                    out.println("<div style=\"font-weight: bolder; padding-left: 5px; font-size: 20px;color:#0091E6;\">" + d.getDvdtitle()+"</div>");
                    out.println("<table>\n" +
    "                        <tr>\n" +
    "                            <td style=\"width: 140px; padding: 10px\"><img src=\"" + coverUrl+ "\" width=\"120\" height=\"160\"/></td>\n" +
    "                            <td>");
                    out.println("<div><b>Director: </b>" + d.getDirector() + "</div>");
                    //out.println("<div><b>Publication Time: </b>" + d.getPublicationtime() + "</div>");
                    out.println("<div><b>Retail Price: </b>" + d.getRetailprice() + "</div>");
                    out.println("<div><b>Currency Type: </b>" + d.getCurrencytype() + "</div>");
                    //out.println("<div><b>Snippet: </b>" + d.getSnippet() + "</div>");
                    out.println(" </td>\n" +
    "                        </tr>\n" +
    "                    </table>\n");
                    if(!sbm.searchVideo(searchDvdsLocal.get(i).getDvdtitle()).equals("")) {
                        out.println("<div style=\"padding-left:30px;font-weight:bolder;\"><b>Dvd Show: </b><br/>" + "<iframe width=\"200\" height=\"200\" src=\"" + sbm.searchVideo(searchDvdsLocal.get(i).getDvdtitle()) + "\"frameborder=\"0\" allowfullscreen=\"true\"></iframe><br/>" + "</div>");
                    } else {
                        out.println("<div style=\"padding-left:30px;font-weight:bolder;\"><b>Dvd Show: </b><br/>" + "<img width=\"290\" height=\"200\" src=\"" + "noavavideo.jpg" + "\"></img><br/>" + "</div>");
                    }
                    //out.println("<div style=\"padding-left:30px;font-weight:bolder;\"><b>Dvd Show: </b><br/>" + "<iframe width=\"200\" height=\"200\" src=\"" + sbm.searchVideo(searchDvdsLocal.get(i).getDvdtitle()) + "\"frameborder=\"0\" allowfullscreen=\"true\"></iframe><br/>" + "</div>");
                    out.println("</td>");
                } catch(IndexOutOfBoundsException ie){                    
                    out.println("<td style=\"width: 300px; vertical-align: text-top\">");
                    out.println("<div style=\"font-weight: bolder; padding-left: 5px; padding-right: 5px; font-size: 14px;\">No dvd available in our database</div>");
                    out.println("</td>");
                }
                
                try {
                    AmazonEbayResult priceAmazon = searchDvdsAmazonPrice.get(i);
                    coverUrl = sbm.searchCover(priceAmazon.getTitle());
                    out.println("<td style=\"width: 300px; vertical-align: text-top\">");
                    out.println("<div style=\"font-weight: bolder; padding-left: 5px; font-size: 20px;color:#0091E6;\">" + priceAmazon.getTitle() +"</div>");
                    out.println("<table>\n" +
    "                        <tr>\n" +
    "                            <td style=\"width: 140px; padding: 10px;border-radius: 10px 10px 0px 0px;\"><img src=\"" + coverUrl+ "\" width=\"120\" height=\"160\" style=\"border-radius: 10px 10px 10px 10px;\"/></td>\n" +
    "                            <td>");         
                    out.println("<div><b>New Price: </b>" + priceAmazon.getNewPrice() + "</div>");
                    out.println("<div><b>Used Price: </b>" + priceAmazon.getUsedPrice() + "</div>");
                    out.println("<div><b>Limited Price: </b>" + priceAmazon.getLimitedPrice() + "</div>");
                    out.println("<div><b>Sellers: </b>" + priceAmazon.getSellers().toString() + "</div>");
                    out.println(" </td>\n" +
    "                        </tr>\n" +
    "                    </table>\n");
                    if(!sbm.searchVideo(priceAmazon.getTitle()).equals("")) {
                        out.println("<div style=\"padding-left:30px;font-weight:bolder;\"><b>Dvd Show: </b><br/>" + "<iframe width=\"200\" height=\"200\" src=\"" + sbm.searchVideo(priceAmazon.getTitle()) + "\"frameborder=\"0\" allowfullscreen=\"true\"></iframe><br/>" + "</div>");
                    } else {
                        out.println("<div style=\"padding-left:30px;font-weight:bolder;\"><b>Dvd Show: </b><br/>" + "<img width=\"290\" height=\"200\" src=\"" + "noavavideo.jpg" + "\"></img><br/>" + "</div>");
                    }
                    //out.println("<div style=\"padding-left:30px;font-weight:bolder;\"><b>Dvd Show: </b><br/>" + "<iframe width=\"200\" height=\"200\" src=\"" + sbm.searchVideo(priceAmazon.getTitle()) + "\"frameborder=\"0\" allowfullscreen=\"true\"></iframe><br/>" + "</div>");
                    out.println("                    </td>");
                } catch(IndexOutOfBoundsException ie){                    
                    out.println("<td style=\"width: 300px; vertical-align: text-top\">");
                    out.println("<div style=\"font-weight: bolder; padding-left: 5px; padding-right: 5px; font-size: 14px;\">No DVD available from Amazon.com</div>");
                    out.println("</td>");
                }
                
                try {
                    AmazonEbayResult priceEbay = searchDvdsEbayPrice.get(i);
                    coverUrl = sbm.searchCover(priceEbay.getTitle());
                    out.println("<td style=\"width: 300px; vertical-align: text-top\">");
                    out.println("<div style=\"font-weight: bolder; padding-left: 5px; font-size: 20px;color:#0091E6;\">" + priceEbay.getTitle() +"</div>");
                    out.println("<table>\n" +
    "                        <tr>\n" +
    "                            <td style=\"width: 140px; padding: 10px;border-radius: 10px 10px 0px 0px;\"><img src=\"" + coverUrl+ "\" width=\"120\" height=\"160\" style=\"border-radius: 10px 10px 10px 10px;\"/></td>\n" +
    "                            <td>");         
                    out.println("<div><b>"+ priceEbay.getCondition() +" Price: </b>" + priceEbay.getPrice() + "</div>");
                    out.println(" </td>\n" +
    "                        </tr>\n" +
    "                    </table>\n");
                    if(!sbm.searchVideo(priceEbay.getTitle()).equals("")) {
                        out.println("<div style=\"padding-left:30px;font-weight:bolder;\"><b>Dvd Show: </b><br/>" + "<iframe width=\"200\" height=\"200\" src=\"" + sbm.searchVideo(priceEbay.getTitle()) + "\"frameborder=\"0\" allowfullscreen=\"true\"></iframe><br/>" + "</div>");
                    } else {
                        out.println("<div style=\"padding-left:30px;font-weight:bolder;\"><b>Dvd Show: </b><br/>" + "<img width=\"290\" height=\"200\" src=\"" + "noavavideo.jpg" + "\"></img><br/>" + "</div>");
                    }
                    out.println("                    </td>");
                } catch(IndexOutOfBoundsException ie){                    
                    out.println("<td style=\"width: 300px; vertical-align: text-top\">");
                    out.println("<div style=\"font-weight: bolder; padding-left: 5px; padding-right: 5px; font-size: 14px;\">No DVD available from eBay.com</div>");
                    out.println("</td>");
                }
                out.println("</tr>");
            }
            
            out.println("</table>");
            out.println("</div>");

            out.println("</div>");
            out.println("<div id=\"sidebar\">\n" +
"                <div id=\"sidebartitle\">Google Result</div>\n");
            if(searchDvdsGoogle.isEmpty()) {
                out.println("<div id=\"sbox1\">");
                out.println("<div style=\"font-weight: bolder; padding-left: 5px; padding-right: 5px; font-size: 14px;\">This Dvd does not exist in the google custom search result</div>");
                out.println("</div>");
            }
            
            for(int i=0; i< searchDvdsGoogle.size(); i++) { 
                SearchResultGoogle s = searchDvdsGoogle.get(i);
                try { 
                    coverUrl = sbm.searchCover(s.getTitle());
                    out.println("<div id=\"sbox1\">");
                    out.println("<div style=\"font-weight: bolder; padding-left: 5px; font-size: 20px;color:#0091E6;\"><a href=\"" + s.getLink() + "\"> " + s.getTitle()+"</a></div>");
                    out.println("<table>\n" +
    "                        <tr>\n" +
    "                            <td style=\"width: 140px; padding: 0px\"><img src=\"" + coverUrl + "\" width=\"120\" height=\"160\"/></td>\n" +
    "                            <td>");
                    out.println("<div><b>Snippet: </b>" + s.getSnippet().substring(0, 100) + "...." + "</div>");
                    out.println(" </td>\n" +
    "                        </tr>\n" +
    "                    </table>\n");
                    if(!sbm.searchVideo(s.getTitle()).equals("")) {
                        out.println("<div style=\"padding-left:30px;font-weight:bolder;\"><b>Dvd Show: </b><br/>" + "<iframe width=\"200\" height=\"200\" src=\"" + sbm.searchVideo(s.getTitle()) + "\"frameborder=\"0\" allowfullscreen=\"true\"></iframe><br/>" + "</div>");
                    } else {
                        out.println("<div style=\"padding-left:30px;font-weight:bolder;\"><b>Dvd Show: </b><br/>" + "<img width=\"290\" height=\"200\" src=\"" + "noavavideo.jpg" + "\"></img><br/>" + "</div>");
                    }
                    //out.println("<div style=\"padding-left:30px;font-weight:bolder;\"><b>Dvd Show: </b><br/>" + "<iframe width=\"200\" height=\"200\" src=\"" + sbm.searchVideo(s.getTitle()) + "\"frameborder=\"0\" allowfullscreen=\"true\"></iframe><br/>" + "</div>");
                    out.println("</div>");
                } catch(NullPointerException ex) {
                    out.println("<div id=\"sbox1\">");
                    out.println("<div style=\"font-weight: bolder; padding-left: 5px; font-size: 20px;color:#0091E6;\"><a href=\"" + s.getLink() + "\"> " + s.getTitle()+"</a></div>");
                    out.println("<table>\n" +
    "                        <tr>\n" +
    "                            <td style=\"width: 140px; padding: 0px\"><img src=\"" + s.getDisplayLink() + "\" width=\"120\" height=\"160\"/></td>\n" +
    "                            <td>");
                    out.println("<div><b>Snippet: </b>" + s.getSnippet().substring(0, 100) + "...." + "</div>");
                    out.println(" </td>\n" +
    "                        </tr>\n" +
    "                    </table>\n");
                    out.println("<div style=\"padding-left:30px;font-weight:bolder;\"><b>Dvd Show: </b><br/>" + "<iframe width=\"200\" height=\"200\" src=\"" + sbm.searchVideo(s.getTitle()) + "\"frameborder=\"0\" allowfullscreen=\"true\"></iframe><br/>" + "</div>");
                    out.println("</div>");
                }
            }
            
            out.println("</div>"+
"        </div>\n");
            
            out.println("<div id=\"footer\" class=\"container\">\n" +
"            <h2>Sentiment  Analysis</h2>\n" +
"            <div id=\"fbox1\">\n" +
"                    <h2>Amazon Result</h2>\n" +
"                    <div id=\"amazonchart_div\" style=\"width:400px; height:300px; color: #4E4E4E;\">                    \n" +
"                    </div>\n");
            out.println("<div id=\"noteinfo\" style=\"color: grey; padding-left: 20px;\">Note:To view sample review, please click the pie char element!</div>");
            out.println("<div id=\"positivetitle\" style=\"color: grey; padding-left: 20px; font-size: 17px; font-weight: bold;\" hidden=\"true\">Positive Review Sample</div>");
            out.println("<div id=\"neturaltitle\" style=\"color: grey; padding-left: 20px; font-size: 17px; font-weight: bold;\" hidden=\"true\">Neutral Review Sample</div>");
            out.println("<div id=\"negativetitle\" style=\"color: grey; padding-left: 20px; font-size: 17px; font-weight: bold;\" hidden=\"true\">Negative Review Sample</div>");
            
            out.println("<div id=\"positivecontent\" style=\"color: grey; padding-left: 20px;\" hidden=\"true\">");
            for(int i=0; i<reviewResultAmazon.size(); i++) {
                List<SampleReview> sampleReviews = reviewResultAmazon.get(i).getSampleReviews();
                int k=0;
                for(int j=0; j<sampleReviews.size(); j++) {
                    if(sampleReviews.get(j).getReviewType().equals("positive")) {
                        SampleReview sampleReview = sampleReviews.get(j);
                        out.println("<div style=\"padding-left: 10px\">\n" +
"                            <b>Review Title</b>:\n" + sampleReview.getTitle() +
"                        </div>\n" +
"                        <div style=\"padding-left: 10px\">\n" +
"                            <b>Review Editor:</b>\n" + sampleReview.getEidtor() +
"                        </div>\n" +
"                        <div style=\"padding-left: 10px\">\n" +
"                            <b>Review Content:</b><br/>\n" + sampleReview.getContent() +
"                        </div>\n" +
"                        <p/>");
                        k++;
                    }                    
                }
                if(k==3) {break;}
            }
            out.println("</div>");
            
            out.println("<div id=\"neturalcontent\" style=\"color: grey; padding-left: 20px;\" hidden=\"true\">");
            for(int i=0; i<reviewResultAmazon.size(); i++) {
                List<SampleReview> sampleReviews = reviewResultAmazon.get(i).getSampleReviews();
                int k=0;
                for(int j=0; j<sampleReviews.size(); j++) {
                    if(sampleReviews.get(j).getReviewType().equals("netural")) {
                        SampleReview sampleReview = sampleReviews.get(j);
                        out.println("<div style=\"padding-left: 10px\">\n" +
"                            <b>Review Title</b>:\n" + sampleReview.getTitle() +
"                        </div>\n" +
"                        <div style=\"padding-left: 10px\">\n" +
"                            <b>Review Editor:</b>\n" + sampleReview.getEidtor() +
"                        </div>\n" +
"                        <div style=\"padding-left: 10px\">\n" +
"                            <b>Review Content:</b><br/>\n" + sampleReview.getContent() +
"                        </div>\n" +
"                        <p/>");
                        k++;
                    }                    
                }
                if(k==3) {break;}
            }
            out.println("</div>");
            
            out.println("<div id=\"negativecontent\" style=\"color: grey; padding-left: 20px;\" hidden=\"true\">");
            for(int i=0; i<reviewResultAmazon.size(); i++) {
                List<SampleReview> sampleReviews = reviewResultAmazon.get(i).getSampleReviews();
                int k=0;
                for(int j=0; j<sampleReviews.size(); j++) {
                    if(sampleReviews.get(j).getReviewType().equals("negative")) {
                        SampleReview sampleReview = sampleReviews.get(j);
                        out.println("<div style=\"padding-left: 10px\">\n" +
"                            <b>Review Title</b>:\n" + sampleReview.getTitle() +
"                        </div>\n" +
"                        <div style=\"padding-left: 10px\">\n" +
"                            <b>Review Editor:</b>\n" + sampleReview.getEidtor() +
"                        </div>\n" +
"                        <div style=\"padding-left: 10px\">\n" +
"                            <b>Review Content:</b><br/>\n" + sampleReview.getContent() +
"                        </div>\n" +
"                        <p/>");
                        k++;
                    }                    
                }
                if(k==3) {break;}
            }
            out.println("</div>");
            
            out.println("            </div>\n" +
"            <div id=\"fbox2\">\n" +
"                    <h2>Twitter Result</h2>\n" +
"                    <div id=\"twitterchart_div\" style=\"width:400px; height:300px; color: #4E4E4E;\">                    \n" +
"                    </div>\n");
            
            out.println("<div id=\"noteinfotwitter\" style=\"color: grey; padding-left: 20px;\">Note:To view sample review, please click the pie char element!</div>");
            out.println("<div id=\"positivetitletwitter\" style=\"color: grey; padding-left: 20px; font-size: 17px; font-weight: bold;\" hidden=\"true\">Positive Review Sample</div>");
            out.println("<div id=\"neturaltitletwitter\" style=\"color: grey; padding-left: 20px; font-size: 17px; font-weight: bold;\" hidden=\"true\">Neutral Review Sample</div>");
            out.println("<div id=\"negativetitletwitter\" style=\"color: grey; padding-left: 20px; font-size: 17px; font-weight: bold;\" hidden=\"true\">Negative Review Sample</div>");
            List<SampleReview> sampleReviews = reviewResultTwitter.getSampleReviews();
            out.println("<div id=\"positivecontenttwitter\" style=\"color: grey; padding-left: 20px;\" hidden=\"true\">");
            int a = 0;
            for(int j=0; j<sampleReviews.size(); j++) {
                if(sampleReviews.get(j).getReviewType().equals("positive")) {
                    SampleReview sampleReview = sampleReviews.get(j);
                    out.println(
"                        <div style=\"padding-left: 10px\">\n" +
"                            <b>Review Editor:</b>\n" + sampleReview.getEidtor() +
"                        </div>\n" +
"                        <div style=\"padding-left: 10px\">\n" +
"                            <b>Review Content:</b><br/>\n" + sampleReview.getContent() +
"                        </div>\n" +
"                        <p/>");
                    a++;
                }
                if(a==3) {break;}
            }
            
            out.println("</div>");
            
            out.println("<div id=\"neturalcontenttwitter\" style=\"color: grey; padding-left: 20px;\" hidden=\"true\">");
            int b=0;
            for(int j=0; j<sampleReviews.size(); j++) {
                if(sampleReviews.get(j).getReviewType().equals("netural")) {
                    SampleReview sampleReview = sampleReviews.get(j);
                    out.println(
"                        <div style=\"padding-left: 10px\">\n" +
"                            <b>Review Editor:</b>\n" + sampleReview.getEidtor() +
"                        </div>\n" +
"                        <div style=\"padding-left: 10px\">\n" +
"                            <b>Review Content:</b><br/>\n" + sampleReview.getContent() +
"                        </div>\n" +
"                        <p/>");
                    b++;
                }  
                if(b==3) {break;}
            }
                
            out.println("</div>");
            
            out.println("<div id=\"negativecontenttwitter\" style=\"color: grey; padding-left: 20px;\" hidden=\"true\">");        
            int c=0;
            for(int j=0; j<sampleReviews.size(); j++) {
                if(sampleReviews.get(j).getReviewType().equals("negative")) {
                    SampleReview sampleReview = sampleReviews.get(j);
                    out.println(
"                        <div style=\"padding-left: 10px\">\n" +
"                            <b>Review Editor:</b>\n" + sampleReview.getEidtor() +
"                        </div>\n" +
"                        <div style=\"padding-left: 10px\">\n" +
"                            <b>Review Content:</b><br/>\n" + sampleReview.getContent() +
"                        </div>\n" +
"                        <p/>");
                    c++;
                }       
                if(c==3) {break;}
            }
                
            out.println("</div>");
            
            out.println("            </div>\n" +
"        </div> ");
            
            out.println("        <div id=\"copyright\" class=\"container\">\n" +
"                <p>Copyright (c) 2013 iCompare.com. All rights reserved.</p>\n" +
"        </div>");
            out.println("</body>");
            out.println("</html>");
        } finally {            
            out.close();
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP
     * <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, MalformedURLException {
        try {
                    try {
                        processRequest(request, response);
                    } catch (OAuthMessageSignerException ex) {
                        Logger.getLogger(DvdSearchServlet.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (OAuthExpectationFailedException ex) {
                        Logger.getLogger(DvdSearchServlet.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (OAuthCommunicationException ex) {
                        Logger.getLogger(DvdSearchServlet.class.getName()).log(Level.SEVERE, null, ex);
                    }
        } catch (JSONException ex) {
            Logger.getLogger(DvdSearchServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Handles the HTTP
     * <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, MalformedURLException {
        try {
                    try {
                        processRequest(request, response);
                    } catch (OAuthMessageSignerException ex) {
                        Logger.getLogger(DvdSearchServlet.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (OAuthExpectationFailedException ex) {
                        Logger.getLogger(DvdSearchServlet.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (OAuthCommunicationException ex) {
                        Logger.getLogger(DvdSearchServlet.class.getName()).log(Level.SEVERE, null, ex);
                    }
        } catch (MalformedURLException ex) {
            Logger.getLogger(DvdSearchServlet.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JSONException ex) {
            Logger.getLogger(DvdSearchServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
