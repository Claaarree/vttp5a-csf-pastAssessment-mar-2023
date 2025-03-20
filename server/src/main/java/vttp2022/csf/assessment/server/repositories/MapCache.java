package vttp2022.csf.assessment.server.repositories;

import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Repository
public class MapCache {

	private RestTemplate restTemplate = new RestTemplate();

	// TODO Task 4
	// Use this method to retrieve the map
	// You can add any parameters (if any) and the return type 
	// DO NOT CHNAGE THE METHOD'S NAME
	public byte[] getMap(float lat, float lng) {

		String baseUrl = "http://map.chuklee.com";
		String url = UriComponentsBuilder
				.fromUriString(baseUrl)
				.queryParam("lat", lat)
				.queryParam("lng", lng)
				.toUriString();
		RequestEntity<Void> req = RequestEntity.get(url)
				.accept(MediaType.IMAGE_PNG)
				.build();

		ResponseEntity<byte[]> response = restTemplate.exchange(req, byte[].class);
		return response.getBody();	}

	// You may add other methods to this class

}
