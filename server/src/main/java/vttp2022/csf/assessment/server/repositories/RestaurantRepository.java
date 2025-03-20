package vttp2022.csf.assessment.server.repositories;

import java.util.List;
import java.util.Optional;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.MongoExpression;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationExpression;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import vttp2022.csf.assessment.server.models.Comment;
import vttp2022.csf.assessment.server.models.LatLng;
import vttp2022.csf.assessment.server.models.Restaurant;

@Repository
public class RestaurantRepository {

	@Autowired
	private MongoTemplate template;

	public final String C_DETAILS = "details";
	public final String F_CUISINE = "cuisine";
	public final String F_NAME = "name";
	public final String F_RESTAURANT_ID = "restaurant_id";



	// TODO Task 2
	// Use this method to retrive a list of cuisines from the restaurant collection
	// You can add any parameters (if any) and the return type 
	// DO NOT CHNAGE THE METHOD'S NAME
	// Write the Mongo native query above for this method
	// 
	// db.details.distinct("cuisine");
	//  
	public List<String> getCuisines() {
		// Implmementation in here
		List<String> cuisinesList = template
				.findDistinct(new Query(), F_CUISINE, C_DETAILS, String.class);

		return cuisinesList;
	}

	// TODO Task 3
	// Use this method to retrive a all restaurants for a particular cuisine
	// You can add any parameters (if any) and the return type 
	// DO NOT CHNAGE THE METHOD'S NAME
	// Write the Mongo native query above for this method
	//  
	// db.details.find({cuisine: "African"})
		
	public List<Document> getRestaurantsByCuisine(String cuisine) {
		// Implmementation in here
		Criteria criteria = Criteria
				.where(F_CUISINE)
				.regex(cuisine, "i");
		
		Query query = Query.query(criteria);
		
		query.with(Sort.by(Direction.ASC, F_NAME));

		List<Document> restaurants = template
				.find(query, Document.class, C_DETAILS);

		return restaurants;
	}

	// TODO Task 4
	// Use this method to find a specific restaurant
	// You can add any parameters (if any) 
	// DO NOT CHNAGE THE METHOD'S NAME OR THE RETURN TYPE
	// Write the Mongo native query above for this method
	/*
	 * db.details.aggregate([
			{$match: {name: "African Terrace"}},
			{$project: {
				_id: 0,
				restaurant_id: 1,
				name: 1,
				cuisine: 1,
				address: {$concat:['$address.building', ', ', 
									'$address.street', ', ',
									'$address.zipcode', ', ',
									'$borough']},
				coorrdinates: "$address.coord"
				}
			}
		])
	 */
		
	public Optional<Restaurant> getRestaurant(String name) {
		// Build query
		Criteria criteria = Criteria.where(F_NAME)
		.is(name);
		// .regex(name, "i");
		MatchOperation matchOperation = Aggregation.match(criteria);
		ProjectionOperation projectionOperation = Aggregation
			.project(F_NAME, F_CUISINE, F_RESTAURANT_ID)
			.andExclude("_id")
			.and("address.coord").as("coordinates")
			.and(
				AggregationExpression.from(
					MongoExpression.create("""
						$concat:['$address.building', ', ', 
									'$address.street', ', ',
									'$address.zipcode', ', ',
									'$borough']
					""")
				)
				// StringOperators.Concat
				// 	.valueOf("address.building").concat(", ")
				// 	.valueOf("address.street").concat(", ")
				// 	.valueOf("address.zipcode").concat(", ")
				// 	.valueOf("borough")
			).as("address");

		Aggregation pipeline = Aggregation.newAggregation(matchOperation, projectionOperation);
		Document d = template.aggregate(pipeline, C_DETAILS, Document.class).getUniqueMappedResult();

		if (d.isEmpty()){
			return Optional.empty();
		} else {
			Restaurant r = new Restaurant();
			r.setRestaurantId(d.getString(F_RESTAURANT_ID));
			r.setName(d.getString(F_NAME));
			r.setCuisine(d.getString(F_CUISINE));
			r.setAddress(d.getString("address"));
			List<Double> coords = d.getList("coordinates", Double.class);
			LatLng latLng = new LatLng();
			latLng.setLatitude(coords.get(1).floatValue());
			latLng.setLongitude(coords.get(0).floatValue());
			r.setCoordinates(latLng);

			return Optional.of(r);
		}
	}


	// // TODO Task 5
	// // Use this method to insert a comment into the restaurant database
	// // DO NOT CHNAGE THE METHOD'S NAME OR THE RETURN TYPE
	// // Write the Mongo native query above for this method
	// //  
	public void addComment(Comment comment) {
		// Implmementation in here
		Document d = new Document();
		d.append("name", comment.getName());
		d.append("rating", comment.getRating());
		d.append("restaurant_id", comment.getRestaurantId());
		d.append("text", comment.getText());
		template.insert(d, "comments");
	}
	
	// You may add other methods to this class
	

}
