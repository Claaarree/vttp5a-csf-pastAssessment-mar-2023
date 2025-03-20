package vttp2022.csf.assessment.server.controllers;

import java.io.IOException;
import java.io.StringReader;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import vttp2022.csf.assessment.server.models.Comment;
import vttp2022.csf.assessment.server.models.Restaurant;
import vttp2022.csf.assessment.server.repositories.MapCache;
import vttp2022.csf.assessment.server.services.RestaurantService;
import vttp2022.csf.assessment.server.services.S3Services;

@Controller
@RequestMapping(path="/api", produces = MediaType.APPLICATION_JSON_VALUE)
public class RestaurantController {

    @Autowired
    private RestaurantService restaurantSvc;

    @Autowired
    private S3Services s3Service;

    @Autowired
    private MapCache mapCache;
    
    @GetMapping(path="/cuisines")
    @ResponseBody
    public ResponseEntity<String> getCuisines() {   
        //Query for the List
        List<String> cuisinesList=restaurantSvc.getCuisines();
        //convert / to underscore
        List<String> cuisinesListUnderscore = new LinkedList<>();
        for (String c: cuisinesList) {
            c=c.replace("/", "_");
            System.out.println(c);
            cuisinesListUnderscore.add(c);
        }
        //convert list to json array
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        for (String c: cuisinesListUnderscore) {
            arrayBuilder.add(c);
        }
        JsonArray cuisines = arrayBuilder.build();
        return ResponseEntity.ok(cuisines.toString());
        
    }

    @GetMapping(path="/{cuisine}/restaurants") 
    @ResponseBody
    public ResponseEntity<String> getRestaurantsByCuisine(@PathVariable String cuisine) {
        //Create the list of restaurants
        List<String> restaurants = restaurantSvc.getRestaurantsByCuisine(cuisine);
        System.out.println(restaurants);
        //convert list to json array
        JsonArrayBuilder arrBuilder = Json.createArrayBuilder();
        for (String r: restaurants) {
            System.out.println(r);
            if (r.contains("/"))
            r = r.replace("/", "_");
            arrBuilder.add(r);
        }
        JsonArray restaurantsJson = arrBuilder.build();
        return ResponseEntity.ok(restaurantsJson.toString());

    }

    @GetMapping(path="/restaurant/{name}")
    @ResponseBody
    public ResponseEntity<String> getRestaurant(@PathVariable String name) throws IOException {
        //Make query
        Optional<Restaurant> opt = restaurantSvc.getRestaurant(name);
        if (opt.isEmpty()) {
            JsonObject error = Json.createObjectBuilder()
                                .add("error", "Restaurant not found for %s".formatted(name))
                                .build();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error.toString());
        }
        Restaurant r = opt.get();

        //Retrieve image from s3

        //get image from chuk
        byte[] image = mapCache.getMap(r.getCoordinates().getLatitude(), r.getCoordinates().getLongitude());
        //save it to s3
        String imgUrl = s3Service.upload(image);
        System.out.println(imgUrl);
        r.setMapURL("https://drewkwan.sgp1.digitaloceanspaces.com/csfAssessment%2F"+imgUrl);
        return ResponseEntity.ok(r.toJson().toString());
        // System.out.println(r);
        // return null;
    }


    @PostMapping(path="/comments", consumes=MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<String> postComments(@RequestBody String body) {
        //Initialise new comment
        Comment comment = new Comment();
        //Get comment from payload
        JsonReader reader = Json.createReader(new StringReader(body));
        JsonObject json = reader.readObject();
        String name = json.getString("name");
        String id = json.getString("restaurantId");
        System.out.println(id);
        int rating = json.getInt("rating");
        String text = json.getString("text");
        System.out.println(name);
        System.out.println(id);
        System.out.println(rating);
        System.out.println(text);

        comment.setRestaurantId(id);
        comment.setName(name);
        comment.setRating(rating);
        comment.setText(text);

        //insert the comment into the db
        restaurantSvc.addComment(comment);

        JsonObject payload = Json.createObjectBuilder()
                                .add("message", "Comment posted")
                                .build();
        
        return ResponseEntity.ok(payload.toString());
        
    }

}
