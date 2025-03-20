package vttp2022.csf.assessment.server.repositories;

import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Repository
public class MapCache {

	// TODO Task 4
	// Use this method to retrieve the map
	// You can add any parameters (if any) and the return type 
	// DO NOT CHNAGE THE METHOD'S NAME
	public byte[] getMap(float lat, float lng) {


		final String URL_MAP = "http://map.chuklee.com/map";

		//correct
		String mapUrl = UriComponentsBuilder.fromUriString(URL_MAP)
					.queryParam("lat", lat)
					.queryParam("lng", lng)
					.toUriString();
		System.out.println(mapUrl);
		RequestEntity<Void> req = RequestEntity.get(mapUrl)
											.accept(MediaType.IMAGE_PNG)
											.build();

		RestTemplate template = new RestTemplate();
		ResponseEntity<byte[]> resp = template.exchange(req, byte[].class);
		byte[] payload = resp.getBody();

		System.out.println(payload);
		return payload;
		// JsonArray data = result.getJsonObject("data").getJsonArray("results");

	}

	// You may add other methods to this class

}
