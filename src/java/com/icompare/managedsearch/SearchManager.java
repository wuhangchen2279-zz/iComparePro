/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.icompare.managedsearch;

import com.ebay.services.client.ClientConfig;
import com.ebay.services.client.FindingServiceClientFactory;
import com.ebay.services.finding.FindItemsAdvancedRequest;
import com.ebay.services.finding.FindItemsAdvancedResponse;
import com.ebay.services.finding.FindItemsByKeywordsRequest;
import com.ebay.services.finding.FindItemsByKeywordsResponse;
import com.ebay.services.finding.FindingServicePortType;
import com.ebay.services.finding.PaginationInput;
import com.ebay.services.finding.SearchItem;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.ResourceId2;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import com.google.api.services.youtube.model.Video;
import com.google.api.services.youtube.model.VideoListResponse;
import com.icompare.amazon.ItemAttributes;
import com.icompare.amazon.ItemSearchResponse;
import com.icompare.amazon.Merchant;
import com.icompare.entity.Books;
import com.icompare.entity.Dvds;
import com.icompare.utility.SearchResultGoogle;
import com.icompare.ws.Flickr;
import com.icompare.utility.AmazonEbayResult;
import com.icompare.ws.ReviewResult;
import com.icompare.ws.SampleReview;
import com.icompare.ws.SignedRequestsHelper;
import com.icompare.wsclient.BookJerseyClient;
import com.icompare.wsclient.DvdJerseyClient;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.GenericType;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import oauth.signpost.OAuthConsumer;
import oauth.signpost.basic.DefaultOAuthConsumer;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.jsoup.Jsoup;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;
import org.netbeans.saas.RestConnection;
import org.netbeans.saas.RestResponse;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author hwu65
 */
public class SearchManager {
    private EntityManager em;
    private EntityManagerFactory emf;
    //Amazon API key info
    private static final String AWS_ACCESS_KEY_ID = "";
     private static final String AWS_SECRET_KEY = "";
    private static final String ASSOCIATE_TAG ="";
    private static final String ENDPOINT = "";
    
    //Google API key info
    private static final String DEV_KEY = "";
    private static final String CX_KEY_BOOK = ""; 
    private static final String CX_KEY_DVD = "";
    
    //eBay API key info
    private static final String EBAY_API_KEY = "";
    
    //Twitter API key info
    private static final String CONSUMER_KEY = "";
    private static final String CONSUMER_SECRET = "";
    private static final String OAUTH_TOKEN = "";
    private static final String OAUTH_TOKEN_SECRET = "";
    
    //words bag
    private static final List<String> positiveWordsBag = Arrays.asList("excellent","beautiful","cool","nice","perfect","better","wow!","no 1","star","think ahead",
                                                                        "royal","pride","proud","precious","joy","love","enjoy","luxurious","lovely","exciting",
                                                                        "essential","happy","famous","genius","impressive","interesting","legend","ideal","inspire","memorable",
                                                                        "mouthwatering","miracle","modern","paradise","sexy","strong","success","top","valued","v.i.p.",
                                                                        "really cool","save a packet","just like that","like","undoubtedly","spotless","spice","tiny","worth","surprise");
    private static final List<String> negativeWordsBag = Arrays.asList("awful","angry","bad","broken","damage","depressed","deny","dirty","disgusting","rude",
                                                                        "quit","dreadful","evil","frighten","hard","hate","horrible","hurt","hurtful","ignore",
                                                                        "impossible","imperfect","misunderstood","messy","malicious","missing","moldy","unsatisfactory","never","negative",
                                                                        "reject","nobody","offensive","old","poor","revenge","sad","scare","sick","sorry",
                                                                        "terrible","threatening","threatening","ugly","unhappy","upset","unwanted","unpleasant","disappoint","worthless");
    
    private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
    private static final JsonFactory JSON_FACTORY = new JacksonFactory();
    private static final long NUMBER_OF_VIDEOS_RETURNED = 1;
    private static YouTube youtube;
    private BookJerseyClient bookRestClient = new BookJerseyClient();
    GenericType<List<Books>> genericTypeBook = new GenericType<List<Books>>(){};
    DvdJerseyClient dvdRestClient = new DvdJerseyClient();
    GenericType<List<Dvds>> genericTypeDvd = new GenericType<List<Dvds>>(){};

    public SearchManager() {
        emf = Persistence.createEntityManagerFactory("iCompareProPU");
        em = emf.createEntityManager();
    }
    
    public List<Books> searchBookLocal(String query) {
        ClientResponse responseBookTitle = bookRestClient.searchBookWithTitle_JSON(ClientResponse.class, query);
        ClientResponse responseAuthor = bookRestClient.searchBookWithAuthor_JSON(ClientResponse.class, query);
        ClientResponse responseIsbn = bookRestClient.searchBookWithIsbn_JSON(ClientResponse.class, query);
        List<Books> books = responseBookTitle.getEntity(genericTypeBook);
        books.addAll(responseAuthor.getEntity(genericTypeBook));
        books.addAll(responseIsbn.getEntity(genericTypeBook));
        HashSet hs = new HashSet();
        hs.addAll(books);
        books.clear();
        books.addAll(hs);
        return books;
    }
    
    public List<SearchResultGoogle> searchBookGoogle(String query) throws MalformedURLException, IOException, JSONException {
        List<SearchResultGoogle> searchResults = new ArrayList<SearchResultGoogle>();
        List<String> searchLinks = this.searchGoogleOutput(query, "link" ,"", "" , CX_KEY_BOOK);
        List<String> searchTitles = this.searchGoogleOutput(query, "" ,"metatags","og:title" , CX_KEY_BOOK);
        if (searchTitles.isEmpty()) {
            searchTitles = this.searchGoogleOutput(query, "title", "", "", CX_KEY_BOOK);
        }
        List<String> searchSnippets = this.searchGoogleOutput(query, "snippet","", "" , CX_KEY_BOOK);        
        List<String> searchImages = this.searchGoogleOutput(query,"" ,"cse_image", "src", CX_KEY_BOOK);
        for(int i=0; i<searchLinks.size()  && i<searchSnippets.size() && i<searchImages.size(); i++) {
            SearchResultGoogle s = new SearchResultGoogle();
            s.setTitle(searchTitles.get(i));
            s.setLink(searchLinks.get(i));
            s.setDisplayLink(searchImages.get(i));
            s.setSnippet(searchSnippets.get(i));
            searchResults.add(s);
        }
        return searchResults;
    }

    public List<SearchResultGoogle> searchDvdGoogle(String query) throws MalformedURLException, IOException, JSONException {
        List<SearchResultGoogle> searchResults = new ArrayList<SearchResultGoogle>();
        List<String> searchLinks = this.searchGoogleOutput(query, "link" ,"", "" , CX_KEY_DVD);
        List<String> searchTitles = this.searchGoogleOutput(query, "","metatags","og:title" , CX_KEY_DVD);
        if (searchTitles.isEmpty()) {
            searchTitles = this.searchGoogleOutput(query, "title", "", "", CX_KEY_DVD);
        }
        List<String> searchSnippets = this.searchGoogleOutput(query, "snippet","", "" , CX_KEY_DVD);        
        List<String> searchImages = this.searchGoogleOutput(query, "","cse_image", "src", CX_KEY_DVD);
        for(int i=0; i<searchLinks.size()  && i<searchSnippets.size() && i<searchImages.size(); i++) {
            SearchResultGoogle s = new SearchResultGoogle();
            s.setTitle(searchTitles.get(i));
            s.setLink(searchLinks.get(i));
            s.setDisplayLink(searchImages.get(i));
            s.setSnippet(searchSnippets.get(i));
            searchResults.add(s);
        }
        return searchResults;
    }
    
    public List<String> searchGoogleOutput(String query, String itemsTag, String pagemapStartTag, String pagemapEndTag, String csKey) throws MalformedURLException, IOException, JSONException {
        URL url = new URL(
            "https://www.googleapis.com/customsearch/v1?key="+ DEV_KEY + "&cx=" + csKey + "&q=" + query.replaceAll("\\s", "+") + "&alt=json");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/json");
        BufferedReader br = new BufferedReader(new InputStreamReader(
        (conn.getInputStream())));
        List<String> searchResults = new ArrayList<String>();
        try{
            JSONTokener t = new JSONTokener(br); 
            JSONObject jo =new JSONObject(t);
            JSONArray a1 = jo.getJSONArray("items");
            if("".equals(itemsTag) && !"".equals(pagemapStartTag) && !"".equals(pagemapEndTag)) {
                for(int i=0; i<a1.length(); i++) {
                    JSONArray a2 = a1.getJSONObject(i).getJSONObject("pagemap").getJSONArray(pagemapStartTag);
                    for(int j=0; j< a2.length(); j++) {
                        String result = a2.getJSONObject(j).getString(pagemapEndTag);
                        searchResults.add(result);
                        if(searchResults.size()>5) {break;} 
                    }
                }
            }
            if( !"".equals(itemsTag) && "".equals(pagemapStartTag) && "".equals(pagemapEndTag)) {
                for(int k=0; k<a1.length(); k++) {
                    String result = a1.getJSONObject(k).getString(itemsTag);
                    searchResults.add(result);
                    if(searchResults.size()>2) {break;}
                }
            } 
        } catch (JSONException jex) {
            searchResults.clear();
        }
              
        return searchResults;
    }
    
    public List<Dvds> searchDvdLocal(String query){
        ClientResponse responseDvdTitle = dvdRestClient.searchDvdWithTitle_JSON(ClientResponse.class, query);
        ClientResponse responseDirctor = dvdRestClient.searchDvdWithCreator_JSON(ClientResponse.class, query);
        List<Dvds> dvds = responseDvdTitle.getEntity(genericTypeDvd);
        dvds.addAll(responseDirctor.getEntity(genericTypeDvd));
        HashSet hs = new HashSet();
        hs.addAll(dvds);
        dvds.clear();
        dvds.addAll(hs);
        return dvds;
    }
    
    public String searchCover(String keyWord) {
        try {
            URL result = Flickr.getInstance().search(keyWord);
            return result.toString();
        } catch (NullPointerException ne) {
            return "noavaimg.jpg";
        }
    }
    
    public String searchVideo(String keyWord) {
            String output = "";
            try {

          /*
           * The YouTube object is used to make all API requests.  The last argument is required, but
           * because we don't need anything initialized when the HttpRequest is initialized, we
           * override the interface and provide a no-op function.
           */
          youtube = new YouTube.Builder(HTTP_TRANSPORT, JSON_FACTORY, new HttpRequestInitializer() {
            public void initialize(HttpRequest request) throws IOException {}})
          .setApplicationName("search_video")
          .build();

          YouTube.Search.List search = youtube.search().list("id,snippet");
          /*
           * It is important to set your developer key from the Google Developer Console for
           * non-authenticated requests (found under the API Access tab at this link:
           * code.google.com/apis/). This is good practice and increased your quota.
           */
          search.setKey(DEV_KEY);
          search.setQ(keyWord);
          /*
           * We are only searching for videos (not playlists or channels).  If we were searching for
           * more, we would add them as a string like this: "video,playlist,channel".
           */
          search.setType("video");
          /*
           * This method reduces the info returned to only the fields we need and makes calls more efficient.
           */
          search.setFields("items(id/kind,id/videoId,snippet/title,snippet/thumbnails/default/url)");
          search.setMaxResults(NUMBER_OF_VIDEOS_RETURNED);
          SearchListResponse searchResponse = search.execute();

          List<SearchResult> searchResultList = searchResponse.getItems();

          if(!searchResultList.isEmpty()) {
                SearchResult singleVideo = searchResultList.get(0);
                ResourceId2 rId = singleVideo.getId();
                // Double checks the kind is video.
                if(rId.getKind().equals("youtube#video")) {
                      try {   
                          YouTube.Videos.List video = youtube.videos().list(rId.getVideoId(), "snippet,contentDetails,player");
                          video.setKey(DEV_KEY);
                          VideoListResponse videoSearch = video.execute();
                          List<Video> videoList = videoSearch.getItems();
                          Video aVideo = videoList.get(0);
                          //output.substring(output.indexOf("\"link\": \"")+("\"link\": \"").length(), output.indexOf("\","));
                          output = aVideo.getPlayer().getEmbedHtml();
                          output = output.substring(output.indexOf("src='") + ("src='").length(), output.indexOf("' width"));                          
                      } catch (IOException ex) {
                          Logger.getLogger(SearchManager.class.getName()).log(Level.SEVERE, null, ex);
                      }
                }
          } else {
              output = "";
          }
        } catch (GoogleJsonResponseException e) {
          System.err.println("There was a service error: " + e.getDetails().getCode() +
              " : " + e.getDetails().getMessage());
        } catch (IOException e) {
          System.err.println("There was an IO error: " + e.getCause() + " : " + e.getMessage());
        } catch (Throwable t) {
          t.printStackTrace();
        }
        return output;
    }
    
    public List<AmazonEbayResult> searchPriceResultAmazon(String keyWord, String searchIndex) throws MalformedURLException, IOException, IOException{
        List<AmazonEbayResult> listPriceResults = new ArrayList<AmazonEbayResult>();
        SignedRequestsHelper helper;
        try {
            helper = SignedRequestsHelper.getInstance(ENDPOINT, AWS_ACCESS_KEY_ID, AWS_SECRET_KEY);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        
        String requestUrlOfferFull = null;
        
        String requestUrlLarge = null;
        
        Map<String, String> paramsOfferFull = new HashMap<String, String>();
        paramsOfferFull.put("Service", "AWSECommerceService");
        paramsOfferFull.put("Version", "2009-03-31");
        paramsOfferFull.put("Operation", "ItemSearch");
        paramsOfferFull.put("SearchIndex", searchIndex);
        paramsOfferFull.put("Keywords", keyWord);
        paramsOfferFull.put("ResponseGroup", "OfferFull");
        paramsOfferFull.put("AssociateTag",ASSOCIATE_TAG);
        paramsOfferFull.put("MerchantId","All");
        
        Map<String, String> paramsLarge = new HashMap<String, String>();
        paramsLarge.put("Service", "AWSECommerceService");
        paramsLarge.put("Version", "2009-03-31");
        paramsLarge.put("Operation", "ItemSearch");
        paramsLarge.put("SearchIndex", searchIndex);
        paramsLarge.put("Keywords", keyWord);
        paramsLarge.put("ResponseGroup", "Large");
        paramsLarge.put("AssociateTag",ASSOCIATE_TAG);
        paramsLarge.put("MerchantId","All");
         
        requestUrlOfferFull = helper.sign(paramsOfferFull);
        requestUrlLarge = helper.sign(paramsLarge);
        
        return this.fetchAmazonItemXML(requestUrlOfferFull, requestUrlLarge);
    }
    
    public List<AmazonEbayResult> fetchAmazonItemXML(String requestUrlOfferFull, String requestUrlLarge) throws MalformedURLException, IOException{
        List<AmazonEbayResult> priceResults = new ArrayList<AmazonEbayResult>();
        ItemSearchResponse itemSearchResponseLarge = new ItemSearchResponse();
        ItemSearchResponse itemSearchResponseOfferFull = new ItemSearchResponse();
        URL urlOfferFull = new URL(requestUrlOfferFull);
        URL urlLarge = new URL(requestUrlLarge);
        
        urlLarge.openConnection();
        InputStream readerLarge = urlLarge.openStream();
        FileOutputStream writerLarge = new FileOutputStream("outLarge.xml");
        byte[] bufferLarger = new byte[153600];
        int bytesReadLarge = 0;
        while ((bytesReadLarge = readerLarge.read(bufferLarger)) > 0)
        {  
            writerLarge.write(bufferLarger, 0, bytesReadLarge);
            bufferLarger = new byte[153600];
        }
        writerLarge.close();
        readerLarge.close();
        
        urlOfferFull.openConnection();
        InputStream readerOfferFull = urlOfferFull.openStream();
        FileOutputStream writerOfferFull = new FileOutputStream("outOfferFull.xml");
        byte[] bufferOfferFull = new byte[153600];
        int bytesReadOfferFull = 0;
        while ((bytesReadOfferFull = readerOfferFull.read(bufferOfferFull)) > 0)
        {  
            writerOfferFull.write(bufferOfferFull, 0, bytesReadOfferFull);
            bufferOfferFull = new byte[153600];
        }
        writerOfferFull.close();
        readerOfferFull.close();
        
        try {
            javax.xml.bind.JAXBContext jaxbCtx = javax.xml.bind.JAXBContext.newInstance(itemSearchResponseLarge.getClass().getPackage().getName());
            javax.xml.bind.Unmarshaller unmarshaller = jaxbCtx.createUnmarshaller();
            itemSearchResponseLarge = (ItemSearchResponse) unmarshaller.unmarshal(new java.io.File("outLarge.xml")); //NOI18N
        } catch (javax.xml.bind.JAXBException ex) {
            // XXXTODO Handle exception
            java.util.logging.Logger.getLogger("global").log(java.util.logging.Level.SEVERE, null, ex); //NOI18N
        }
        
        try {
            javax.xml.bind.JAXBContext jaxbCtx = javax.xml.bind.JAXBContext.newInstance(itemSearchResponseOfferFull.getClass().getPackage().getName());
            javax.xml.bind.Unmarshaller unmarshaller = jaxbCtx.createUnmarshaller();
            itemSearchResponseOfferFull = (ItemSearchResponse) unmarshaller.unmarshal(new java.io.File("outOfferFull.xml")); //NOI18N
        } catch (javax.xml.bind.JAXBException ex) {
            // XXXTODO Handle exception
            java.util.logging.Logger.getLogger("global").log(java.util.logging.Level.SEVERE, null, ex); //NOI18N
        }
        
        
        for(int i=0; i<itemSearchResponseLarge.getItems().size(); i++) {
            for(int j=0; j<itemSearchResponseLarge.getItems().get(i).getItem().size(); j++) {
                AmazonEbayResult pr = new AmazonEbayResult();
                ItemAttributes itemAttributes = itemSearchResponseLarge.getItems().get(i).getItem().get(j).getItemAttributes();
                try {
                    String newPrice = itemSearchResponseLarge.getItems().get(i).getItem().get(j).getOfferSummary().getLowestNewPrice().getFormattedPrice().toString();
                    pr.setNewPrice(newPrice);
                } catch(NullPointerException ne) {
                    pr.setNewPrice("not available for this item!");
                }
                try {
                    String reviewsUrl = itemSearchResponseLarge.getItems().get(i).getItem().get(j).getCustomerReviews().getIFrameURL();
                    pr.setReviewsUrl(reviewsUrl);
                } catch(NullPointerException ne) {
                    pr.setReviewsUrl("");
                }
                try {
                    String limitedPrice = itemSearchResponseLarge.getItems().get(i).getItem().get(j).getOfferSummary().getLowestCollectiblePrice().getFormattedPrice().toString();
                    pr.setLimitedPrice(limitedPrice);
                } catch(NullPointerException ne) {
                    pr.setLimitedPrice("not available for this item!");
                }
                try {
                    String usedPrice = itemSearchResponseLarge.getItems().get(i).getItem().get(j).getOfferSummary().getLowestUsedPrice().getFormattedPrice().toString();
                    pr.setUsedPrice(usedPrice);
                } catch(NullPointerException ne) {
                    pr.setUsedPrice("not available for this item!");
                }
                
                try {
                    String title = itemAttributes.getTitle().toString();
                    pr.setTitle(title);
                } catch (NullPointerException ne) {
                    pr.setTitle("not available for this item!");
                }
                
           
                
                List<String> sellers = new ArrayList<String>();
                for(int k=0; k<itemSearchResponseOfferFull.getItems().get(i).getItem().get(j).getOffers().getOffer().size(); k++) {                   
                    sellers.add(itemSearchResponseOfferFull.getItems().get(i).getItem().get(j).getOffers().getOffer().get(k).getMerchant().getName());     
                }
                if(sellers.isEmpty()){
                    sellers.add("not available for this item!");
                }
                pr.setSellers(sellers);
                priceResults.add(pr);
                if(priceResults.size() == 3) {break;}
            }
        }
        
        return priceResults;
    }
    
    public ReviewResult searchTwitterCustomerReviews (String keyWord, String searchIndex) throws OAuthMessageSignerException, OAuthExpectationFailedException, OAuthCommunicationException, IOException, JSONException{
        ReviewResult reviewResult = new ReviewResult();
        List<SampleReview> sampleReviews = new ArrayList<SampleReview>();
        OAuthConsumer consumer = new DefaultOAuthConsumer(CONSUMER_KEY, CONSUMER_SECRET);
        consumer.setTokenWithSecret(OAUTH_TOKEN, OAUTH_TOKEN_SECRET);

        String query = keyWord + " " + searchIndex;
        query = query.replaceAll(" ", "%20");
        String URL="https://api.twitter.com/1.1/search/tweets.json?q=" + query + "&result_type=mixed&count=100&since_id=0&include_entities=true&lang=en";

        String[][] pathParams = new String[][]{};
        String[][] queryParams = new String[][]{};

        RestConnection conn = new RestConnection(consumer.sign(URL), pathParams, queryParams);
        RestResponse response = conn.get(); 
        
        JSONTokener t = new JSONTokener(response.getDataAsString()); 
        JSONObject jo =new JSONObject(t);
        JSONArray a1 = jo.getJSONArray("statuses");
        
        int positiveAmount = 0;
        int neturalAmount = 0;
        int negativeAmount = 0;
        for(int i=0; i<a1.length(); i++) {
            SampleReview sampleReview = new SampleReview();
            //twitter review text
            String reviewText = a1.getJSONObject(i).getString("text");
            sampleReview.setContent(reviewText);
            int positiveWordsCount = 0;
            int negativeWordsCount = 0;
            for(int j=0; j<positiveWordsBag.size(); j++) {
                if(reviewText.toLowerCase().contains(positiveWordsBag.get(j))) {
                    positiveWordsCount++;
                }
                if(reviewText.toLowerCase().contains(negativeWordsBag.get(j))) {
                    negativeWordsCount++;
                }
            }
            
            int minusResult = positiveWordsCount - negativeWordsCount;
            if(minusResult == 0) {
                neturalAmount++;
                sampleReview.setReviewType("netural");
            } else if(minusResult > 0) {
                positiveAmount++;
                sampleReview.setReviewType("positive");
            } else {
                negativeAmount++;
                sampleReview.setReviewType("negative");
            }
            
            //twitter user name
            String userName = a1.getJSONObject(i).getJSONObject("user").getString("name");
            sampleReview.setEidtor(userName);
            sampleReviews.add(sampleReview);
        }
        
        reviewResult.setPositiveAmount(positiveAmount);
        reviewResult.setNeturalAmount(neturalAmount);
        reviewResult.setNegativeAmount(negativeAmount);
        reviewResult.setSampleReviews(sampleReviews);
        
        return reviewResult;
    } 
    
    public List<ReviewResult> searchAmazonCustomerReviews (String keyWord, String searchIndex) throws MalformedURLException, MalformedURLException, IOException{
        List<ReviewResult> reviewResults = new ArrayList<ReviewResult>();
        List<AmazonEbayResult> amazonResults = this.searchPriceResultAmazon(keyWord, searchIndex);
        for(int i=0; i<amazonResults.size(); i++) {
            String reviewsUrl = amazonResults.get(i).getReviewsUrl();
            org.jsoup.nodes.Document doc;
            try{
                doc = Jsoup.connect(reviewsUrl).timeout(20000)
                .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_6_7) AppleWebKit/534.30 (KHTML, like Gecko) Chrome/12.0.742.91 Safari/534.30").get();
            } catch(IllegalArgumentException iae) {
                return null;
            }
            
            ReviewResult reviewResult = new ReviewResult();
            int positiveAmount = 0;
            int neturalAmount = 0;
            int negativeAmount = 0;
            Elements reviewAmounts = doc.select("td[style=font-family:Verdana,Arial,Helvetica,Sans-serif;;font-size:10px;][align=right]");
            for(int j=0; j<reviewAmounts.size(); j++) {
                if(j==0) {
                    //int f = reviewAmounts.get(j).text().replace("(", "").replace(",", "").replace(")", "").length();
                    positiveAmount = Integer.valueOf(reviewAmounts.get(j).text().replace("(", "").replace(",", "").replace(")", "").substring(1));
                }
                if(j==1 || j==2) {
                    neturalAmount += Integer.valueOf(reviewAmounts.get(j).text().replace("(", "").replace(",", "").replace(")", "").substring(1));
                }
                if(j==3 || j==4) {
                    negativeAmount += Integer.valueOf(reviewAmounts.get(j).text().replace("(", "").replace(",", "").replace(")", "").substring(1));
                }
            }
            
            String positiveUrl = "";
            String neturalUrl = "";
            String negativeUrl = "";
            Elements reviewUrls = doc.select("a[target=_top][style=font-family:Verdana,Arial,Helvetica,Sans-serif;]");
            for(int j=0; j<reviewUrls.size(); j++) {
                if(j==0) {
                    positiveUrl = reviewUrls.get(j).attr("href");
                }
                if(j==2) {
                    neturalUrl = reviewUrls.get(j).attr("href");
                }
                if(j==4) {
                    negativeUrl = reviewUrls.get(j).attr("href");
                }
            }
            
            List<SampleReview> samplePositiveReview = this.getAmazonSampleReviews(positiveUrl, "positive");
            List<SampleReview> sampleNeturalReview = this.getAmazonSampleReviews(neturalUrl, "netural");
            List<SampleReview> sampleNegativeReview = this.getAmazonSampleReviews(negativeUrl, "negative");
            List<SampleReview> allSampleReviews = new ArrayList<SampleReview>();
            allSampleReviews.addAll(samplePositiveReview);
            allSampleReviews.addAll(sampleNeturalReview);
            allSampleReviews.addAll(sampleNegativeReview);
            
            
            reviewResult.setSampleReviews(allSampleReviews);
            reviewResult.setPositiveAmount(positiveAmount);
            reviewResult.setNeturalAmount(neturalAmount);
            reviewResult.setNegativeAmount(negativeAmount);
            
            reviewResults.add(reviewResult);
        }
        
        return reviewResults;
    }
    
    public List<SampleReview> getAmazonSampleReviews(String sampleReviewsUrl, String reviewType) throws IOException {
        List<SampleReview> sampleReviews = new ArrayList<SampleReview>();
        org.jsoup.nodes.Document doc;
        try{
            doc = Jsoup.connect(sampleReviewsUrl).timeout(20000)
            .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_6_7) AppleWebKit/534.30 (KHTML, like Gecko) Chrome/12.0.742.91 Safari/534.30").get();
        } catch(IllegalArgumentException iae) {
            return null;
        }
        
        Elements reviewsContent = doc.select("div[style=margin-left:0.5em;]");
        Elements reviewsTitle = doc.select("span[style=vertical-align:middle;] b");
        Elements reviewsEditor = doc.select("span[style=font-weight: bold;]");
        
        for(int i=0; i<Math.max(Math.max(reviewsTitle.size(), reviewsEditor.size()),reviewsContent.size()); i++) {
            SampleReview sampleReview = new SampleReview();
            
            List<TextNode> textNodes = reviewsContent.get(i).textNodes();
            for(int j=0; j<textNodes.size(); j++) {
                if(!textNodes.get(j).toString().equals(" ")) {
                    sampleReview.setContent(textNodes.get(j).toString());
                }
            }

            sampleReview.setTitle(reviewsTitle.get(i).text());
            sampleReview.setReviewType(reviewType);
            sampleReview.setEidtor(reviewsEditor.get(i).text());
            
            sampleReviews.add(sampleReview);
            
            if(sampleReviews.size() == 3) {
                break;
            }
        }
        
        return sampleReviews;
    }
    
    public List<AmazonEbayResult> searchPriceResultEbay (String keyWord, String searchIndex) {
        List<AmazonEbayResult> priceResults = new ArrayList<AmazonEbayResult>();
        String err = "";
        ClientConfig config  = new ClientConfig();
        config.setApplicationId(EBAY_API_KEY);
        FindingServicePortType serviceport = FindingServiceClientFactory.getServiceClient(config);
        FindItemsAdvancedRequest req = new FindItemsAdvancedRequest(); 
        req.setKeywords(keyWord);
        if("Books".equals(searchIndex)) {
            req.getCategoryId().add("267");        
        } else if("DVD".equals(searchIndex)) {
            req.getCategoryId().add("11232");
        }
        PaginationInput pi = new PaginationInput(); 
        pi.setEntriesPerPage(30); 
        req.setPaginationInput(pi);
        FindItemsAdvancedResponse result = serviceport.findItemsAdvanced(req);
        err = result.getAck().toString();
        List<SearchItem> items = result.getSearchResult().getItem();
        
        for(int i=0; i< items.size(); i++) { 
            try {
                AmazonEbayResult priceResult = new AmazonEbayResult();
                priceResult.setTitle(items.get(i).getTitle()); 
                priceResult.setCondition(items.get(i).getCondition().getConditionDisplayName());
                priceResult.setPrice("$" + items.get(i).getSellingStatus().getCurrentPrice().getValue());
                priceResults.add(priceResult);
            } catch(NullPointerException ne) {
                continue;
            }
            if(priceResults.size() == 3) {break;}                                 
        }
        
        return priceResults;
    }
    
}
