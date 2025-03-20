package vttp2022.csf.assessment.server.services;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import vttp2022.csf.assessment.server.models.Comment;
import vttp2022.csf.assessment.server.models.LatLng;
import vttp2022.csf.assessment.server.models.Restaurant;
import vttp2022.csf.assessment.server.repositories.MapCache;
import vttp2022.csf.assessment.server.repositories.RestaurantRepository;

@Service
public class RestaurantService {
	@Autowired
	private RestaurantRepository restaurantRepository;

	// @Autowired
	// private S3Services s3Services;

	@Autowired
	private MapCache mapCache;

	RestTemplate template = new RestTemplate();

	// TODO Task 2 
	// Use the following method to get a list of cuisines 
	// You can add any parameters (if any) and the return type 
	// DO NOT CHNAGE THE METHOD'S NAME
	public List<String> getCuisines() {
		
		return restaurantRepository.getCuisines();
	}

	// TODO Task 3 
	// Use the following method to get a list of restaurants by cuisine
	// You can add any parameters (if any) and the return type 
	// DO NOT CHNAGE THE METHOD'S NAME
	public List<String> getRestaurantsByCuisine(String cuisine) {
		List<Document> restaurants = restaurantRepository.getRestaurantsByCuisine(cuisine);
		List<String> restaurantNames = new LinkedList<>();
		restaurants.forEach(d -> {
			String name = d.getString("name");
			restaurantNames.add(name);
		});

		return restaurantNames;
	}

	// TODO Task 4
	// Use this method to find a specific restaurant
	// You can add any parameters (if any) 
	// DO NOT CHNAGE THE METHOD'S NAME OR THE RETURN TYPE
	public Optional<Restaurant> getRestaurant(String name) throws IOException {
		Optional<Restaurant> opt = restaurantRepository.getRestaurant(name);
		if(opt.isEmpty()){
			return Optional.empty();
		} else {
			Restaurant res = opt.get();
			LatLng latLng = res.getCoordinates();
			// byte[] map = mapCache.getMap(latLng.getLatitude(), latLng.getLongitude());
			// String imgSrc = s3Services.upload(map);
	
			res.setMapURL("https://images.squarespace-cdn.com/content/v1/64b12e6b90f46f06f838da34/6dbee96b-9015-48c3-885f-d118763392b0/2024+risk+map.png");
			return Optional.of(res);
		}
		
	}

	// TODO Task 5
	// Use this method to insert a comment into the restaurant database
	// DO NOT CHNAGE THE METHOD'S NAME OR THE RETURN TYPE
	public void addComment(Comment comment) {
		restaurantRepository.addComment(comment);
	}
	//
	// You may add other methods to this class
	
}
