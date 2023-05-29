package com.resh.coders.springmvc.services;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.resh.coders.springmvc.models.restResponse;
import com.resh.coders.springmvc.models.searchResult;
import com.resh.coders.springmvc.models.videoThumbnails;
import com.resh.coders.springmvc.models.videos;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.LoggerFactory;

@Service
public class YoutubeAPIService{

    private static org.slf4j.Logger log = LoggerFactory.getLogger(YoutubeAPIService.class);
    private static String searchEndpoint = "/api/v1/search";
	private static String playlistEndpoint = "/api/v1/playlists/";
    private static String suggesEndpoint = "/api/v1/search/suggestions";

    public YoutubeAPIService(){
    }

    public List ytAPIURLs() throws Exception{
        List URLlist = new ArrayList();
        try{
            
            Document document = Jsoup.connect("https://redirect.invidious.io/api/v1/videos/aqz-KE-bpKQ").get();
            Elements elements = document.select(".list");
            if(null != elements && elements.size()>0){
                for(Element element : elements){
                    Elements URLtag = element.getElementsByTag("a");
                    if(URLtag != null && URLtag.size()>0){
                        for(Element ele : URLtag){
                            log.info("links element :: "+ele.text());
                            log.info("links element :: "+ele.tag());
                            if(null != ele.text() && !ele.text().equalsIgnoreCase("")){
                                URLlist.add("https://"+ele.text());
                            }
                        }
                    }
                }
            }

        }catch(Exception e){
            e.printStackTrace();
            log.info(" :: error occured while fetching YT api urls :: "+URLlist);
        }
        
        return URLlist;
    }

    public restResponse searchResult(String q, String page,String extraparameters){
        restResponse rest = new restResponse();
        try{
            if(extraparameters.equalsIgnoreCase("")){
                extraparameters = "&type=playlist";
            }else{
                //for comma saprarated param values
            }

            String urlString = searchEndpoint+"?q="+q+"&page="+page+extraparameters;
            urlString = urlString.replaceAll(" ", "%20");

            List resList = new ArrayList();
            resList.addAll(httpreq(false,urlString));
            
            rest.setMessage("success");
            rest.setStatusCode(200);
            rest.setResponse(resList);

        }catch(Exception e){
            e.printStackTrace();
        }
        return rest;
    }

    public restResponse youtubePlaylists(String playlistIds){
        restResponse rest = new restResponse();

        try{

            List resList = new ArrayList();

            for(int i=0;i<playlistIds.split(",").length;i++){
                String playlistUrl = playlistEndpoint+playlistIds.split(",")[i];
                resList.addAll(httpreq(true,playlistUrl));
            }
            
            rest.setMessage("success");
            rest.setStatusCode(200);
            rest.setResponse(resList);

            
        }catch(Exception e){
            e.printStackTrace();
            log.info("error occured while getting playlist data for :: "+playlistIds);
        }

        return rest;
    }

    public List httpreq(boolean includevideo,String methodUrl){
        List resList = new ArrayList();

        try{

            //getting all active URL for api
            ArrayList URLlist = new ArrayList<>();
            URLlist.addAll(ytAPIURLs());

            log.info(" :: Api URLs are :: "+URLlist);
            for(Object element : URLlist){
                try{
                    String apiUrl = ""+element;    
                    URL url = new URL(apiUrl+methodUrl.trim());
                    log.info("URI of search api ::: "+url);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                    connection.setRequestMethod("GET");
            
                    connection.setDoOutput(true);
                    System.setProperty("https.protocols", "TLSv1.2");

                    int responseCode = connection.getResponseCode();
                    System.out.println("Response Code: " + responseCode);

                    if(200 != responseCode){
                        log.info("response codee for search API is :: "+responseCode+" :: thus continuing");
                        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
                        String line;
                        StringBuilder responseBody = new StringBuilder();
                        while ((line = reader.readLine()) != null) {
                            responseBody.append(line);
                        }
                        reader.close();
                        log.info("error stream :: "+responseBody);
                        continue;
                    }

                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String line;
                    StringBuilder responseBody = new StringBuilder();
                    while ((line = reader.readLine()) != null) {
                        responseBody.append(line);
                    }
                    reader.close();
                    
                    
                    log.info(" :: response code :: "+responseCode+" :: of search api :: "+responseBody);

                    //deserialize the response to pojo
                    ObjectMapper objectMapper = new ObjectMapper();
                    //searchResult searchRes = new searchResult();
                    ArrayList playListArray = new ArrayList<>();


                    SimpleModule module = new SimpleModule();
                    module.addDeserializer(ArrayList.class, new CustomDeserializer(includevideo));
                    objectMapper.registerModule(module);

                    String resBody = responseBody.toString();

                    if(includevideo){
                        resBody = "["+resBody+"]";
                    }

                    playListArray = objectMapper.readValue(resBody,ArrayList.class);
                    
                    resList.addAll(playListArray);

                    //Print the response body
                    //System.out.println("after Body mapping with a custom deserializer :: " + resList.toString());
        
                    // Disconnect the connection
                    connection.disconnect();

                    break;

                }catch(Exception e){
                    e.printStackTrace();
                    Thread th = new Thread(() -> {
                        try{
                            Thread.sleep(5000);
                        }catch(InterruptedException ezx){
                            ezx.printStackTrace();
                        }
                    });

                    th.start();
                }
                
            } 

        }catch(Exception e){
            e.printStackTrace();
            log.info("exception while hitting http request :: includevide :: "+includevideo);
        }

        return resList;
    }

}

class CustomDeserializer extends JsonDeserializer<ArrayList<searchResult>> {
    private static org.slf4j.Logger log = LoggerFactory.getLogger(CustomDeserializer.class);

    private static boolean includeVideo = false;

    public CustomDeserializer(){
    }

    public CustomDeserializer(boolean includeVideo){
        this.includeVideo = includeVideo;
    }

    @Override
    public ArrayList<searchResult> deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);
        ArrayList<searchResult> playlistresult = new ArrayList<>();
        
        if(node == null){
            return playlistresult;
        }

        log.info("node size in deserializer :: "+node.size());

        for(int i=0;i<node.size();i++){
            try{

                //log.info("single  node  :: "+ node.get(i));

                // Deserialize other properties
                String playlistId= node.get(i).get("playlistId").asText();
                String title= node.get(i).get("title").asText();
                String author= node.get(i).get("author").asText();
                String authorUrl= node.get(i).get("authorUrl").asText();
                String playlistThumbnail= node.get(i).get("playlistThumbnail").asText();
                String videoCount= node.get(i).get("videoCount").asText();

                searchResult sr = new searchResult();
                sr.setAuthorUrl(authorUrl);
                sr.setAuthor(author);
                sr.setPlaylistId(playlistId);
                sr.setPlaylistThumbnail(playlistThumbnail);
                sr.setTitle(title);
                sr.setVideoCount(videoCount);

                log.info("include video :: "+includeVideo);

                if(this.includeVideo){
                    setVideo(node.get(i).get("videos"),sr); 
                }

                playlistresult.add(sr);
            }catch(NullPointerException e){
                log.info("continuing after NPE while deserialisation of Json :: ");
            }
        }

        return playlistresult;
    }

    public void setVideo(JsonNode node, searchResult sr){
        log.info("in set video :: ");
        if(node != null && node.size()>0){
            videos[] vids = new videos[node.size()];
            for(int j=0;j<node.size();j++){
                videos vid = new videos();
                try{
                    String title = node.get(j).get("title").asText();
                    String videoId = node.get(j).get("videoId").asText();;
                    String lengthSeconds = node.get(j).get("lengthSeconds").asText();;   
                    //videoThumbnails[] videoThumbnails;

                    vid.setTitle(title);
                    vid.setVideoId(videoId);
                    vid.setlengthSeconds(lengthSeconds);

                    setvidThumbnais(node.get(j).get("videoThumbnails"), vid);
                }catch(NullPointerException e){
                    e.printStackTrace();
                    log.info("NPE in setting video node in playlist :: ");
                }
                vids[j] = vid;
            }
            sr.setVideo(vids);
        }  
    }

    public void setvidThumbnais(JsonNode node, videos vid){
        log.info("in set thumbnails :: ");
        if(node != null && node.size()>0){
            videoThumbnails[] videothumbnails = new videoThumbnails[node.size()];
            for(int i=0;i<node.size();i++){
                videoThumbnails vidThumb = new videoThumbnails();
                try{
                    String quality = node.get(i).get("quality").asText();
                    String url = node.get(i).get("url").asText();;
                    String width = node.get(i).get("width").asText();;
                    String height = node.get(i).get("height").asText();;

                    vidThumb.setHeight(height);
                    vidThumb.setQuality(quality);
                    vidThumb.setUrl(url);
                    vidThumb.setWidth(width);
                }catch(NullPointerException e){
                    e.printStackTrace();
                    log.info("NPE in setting video thumbnails :: ");
                }
                videothumbnails[i] = vidThumb;
            }

            vid.setVideoThumbnails(videothumbnails);
        }
    }
}