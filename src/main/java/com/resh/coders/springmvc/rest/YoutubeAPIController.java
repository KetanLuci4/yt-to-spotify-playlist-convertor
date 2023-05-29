package com.resh.coders.springmvc.rest;
import com.resh.coders.springmvc.models.restResponse;
import com.resh.coders.springmvc.services.YoutubeAPIService;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/youtube")
public class YoutubeAPIController {

	private static org.slf4j.Logger log = LoggerFactory.getLogger(YoutubeAPIController.class);
	

	public YoutubeAPIController(){
		//apiURLs();
	}

    @GetMapping(value = "/searchResult")
	public restResponse searchYoutube(@RequestParam("q") String search,@RequestParam("page") String page,@RequestParam("extras") String extraparameters) {

		YoutubeAPIService youtubeAPIService = new YoutubeAPIService();
		restResponse response = new restResponse();

		try{	
			return youtubeAPIService.searchResult(search,page,extraparameters);
		}catch(Exception e){
			e.printStackTrace();
		}
		return response;
	}

	@GetMapping(value = "/playlistids/{playlists}")
	public restResponse playlistinfo(@PathVariable String playlists){
		YoutubeAPIService youtubeAPIService = new YoutubeAPIService();
		restResponse response = new restResponse();

		try{	
			return youtubeAPIService.youtubePlaylists(playlists);
		}catch(Exception e){
			e.printStackTrace();
		}
		return response;
	}

}
