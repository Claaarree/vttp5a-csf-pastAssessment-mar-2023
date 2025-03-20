package vttp2022.csf.assessment.server.models;

import java.util.ArrayList;
import org.bson.Document;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;

// Do not modify this class
public class Restaurant {
	
	private String restaurantId;
	private String name;
	private String cuisine;
	private String address;
	private LatLng coordinates;
	private String mapUrl;


	public void setRestaurantId(String restaurantId) {
		this.restaurantId = restaurantId;
	}
	public String getRestaurantId() {
		return this.restaurantId;
	}

	public void setName(String name) {
		this.name = name;
	}
	public String getName() {
		return this.name;
	}

	public void setCuisine(String cuisine) {
		this.cuisine = cuisine;
	}
	public String getCuisine() {
		return this.cuisine;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	public String getAddress() {
		return this.address;
	}

	public void setCoordinates(LatLng coordinates) {
		this.coordinates = coordinates;
	}
	public LatLng getCoordinates() {
		return this.coordinates;
	}

	public void setMapURL(String mapUrl) {
		this.mapUrl = mapUrl;
	}
	public String getMapURL() {
		return this.mapUrl;
	}

	public static Restaurant createFromDoc(Document d) {
		Restaurant r = new Restaurant();
		r.setRestaurantId(d.getString("restaurant_id")); //watch this for the int if error
		r.setName(d.getString("name"));
		r.setCuisine(d.getString("cuisine"));
		
		//get the array of the address
		//Build the address
		Document addressDoc = d.get("address", Document.class);
		String building = addressDoc.getString("building");
		String street = addressDoc.getString("street");
		String zipcode = addressDoc.getString("zipcode");
		String borough =d.getString("borough");

		r.setAddress(building + ", " + street + ", " + zipcode + ", " + borough);
	
		ArrayList<Double> coords =  (ArrayList<Double>) addressDoc.get("coord");
			System.out.println(coords);

		float latitude = (coords.get(0)).floatValue();
		float longitude = (coords.get(1)).floatValue();
		LatLng coordinates = new LatLng();
		coordinates.setLatitude(latitude);
		coordinates.setLongitude(longitude);
		r.setCoordinates(coordinates);

		return r;
	}

	public JsonObject toJson() {

		
		JsonObjectBuilder json = Json.createObjectBuilder()
				.add("restaurant_id", restaurantId)
				.add("name", name)
				.add("cuisine", cuisine)
				.add("address", address)
				.add("image", mapUrl);

		//create the lat lng
		JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
		float latitude =  coordinates.getLatitude();
		float longitude = coordinates.getLongitude();
		
		arrayBuilder.add(latitude);
		arrayBuilder.add(longitude);

		JsonArray coordinatesJson = arrayBuilder.build();

		json.add("coordinates", coordinatesJson);

		return json.build();
		

				//need add coord


	}
}
