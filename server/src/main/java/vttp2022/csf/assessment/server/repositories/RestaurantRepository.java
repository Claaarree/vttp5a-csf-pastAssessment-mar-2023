package vttp2022.csf.assessment.server.repositories;

import java.util.List;
import java.util.Optional;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import vttp2022.csf.assessment.server.models.Comment;
import vttp2022.csf.assessment.server.models.Restaurant;

@Repository
public class RestaurantRepository {

	// TODO Task 2
	// Use this method to retrive a list of cuisines from the restaurant collection
	// You can add any parameters (if any) and the return type 
	// DO NOT CHNAGE THE METHOD'S NAME
	// Write the Mongo native query above for this method

		/* Get all cuisine mongo query: 
		 * db.restaurants.distinct("cuisine")
		 */

	@Autowired
	private MongoTemplate mongoTemplate;

	private final String COLLECTION_NAME = "restaurants";


	//  
	public List<String> getCuisines() {
		// Implmementation in here
		List<String> cuisines = mongoTemplate.findDistinct(new Query(), "cuisine", COLLECTION_NAME, String.class);
		return cuisines;


	}

	// TODO Task 3
	// Use this method to retrive a all restaurants for a particular cuisine
	// You can add any parameters (if any) and the return type 
	// DO NOT CHNAGE THE METHOD'S NAME
	// Write the Mongo native query above for this method
	//  
		/*Query to get restaurants
		 * db.restaurants.find({
				cuisine: "Asian"
			}, {name:1}).sort({name:1})
		 */
	public List<Document> getRestaurantsByCuisine(String cuisine) {
		// Implmementation in here
		//Build criteria
		Criteria c = Criteria.where("cuisine").is(cuisine);
		//create query
		Query q = Query.query(c).with(Sort.by(Sort.Direction.ASC, "name"));
		q.fields().include("name");
		List<Document> restaurants = mongoTemplate.find(q, Document.class, COLLECTION_NAME);
		return restaurants;

	}

	// TODO Task 4
	// Use this method to find a specific restaurant
	// You can add any parameters (if any) 
	// DO NOT CHNAGE THE METHOD'S NAME OR THE RETURN TYPE
	// Write the Mongo native query above for this method
	//  
		/* Query for restaurant
		 * db.restaurants.find({
				name: "Ajisen Ramen"
			})
		 */
	public Optional<Restaurant> getRestaurant(String name) {
		// Build query
		Criteria c = Criteria.where("name").is(name);
		Query q = Query.query(c);
		List<Document> result = mongoTemplate.find(q, Document.class, COLLECTION_NAME);
		if (result.size()<1) {
			return Optional.empty();
		}

		Document restaurantDoc = result.get(0);

		Restaurant r = Restaurant.createFromDoc(restaurantDoc);
		return Optional.of(r);

		
	}


	// // TODO Task 5
	// // Use this method to insert a comment into the restaurant database
	// // DO NOT CHNAGE THE METHOD'S NAME OR THE RETURN TYPE
	// // Write the Mongo native query above for this method
	// //  
	public void addComment(Comment comment) {
		// Implmementation in here
		Document doc = new Document();
		doc.put("restaurant_id", comment.getRestaurantId());
		doc.put("name", comment.getName());
		doc.put("rating", comment.getRating());
		doc.put("text", comment.getText());
		mongoTemplate.insert(doc, "comments");
		System.out.printf("Inserted document %s", doc);
		
	}
	
	// You may add other methods to this class
	

}
