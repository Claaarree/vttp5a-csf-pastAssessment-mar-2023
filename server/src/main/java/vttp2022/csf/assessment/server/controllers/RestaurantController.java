package vttp2022.csf.assessment.server.controllers;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;
import java.util.Optional;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import vttp2022.csf.assessment.server.models.Comment;
import vttp2022.csf.assessment.server.models.Restaurant;
import vttp2022.csf.assessment.server.services.RestaurantService;

@Controller
@RequestMapping(path="/api", produces = MediaType.APPLICATION_JSON_VALUE)
public class RestaurantController {

    @Autowired
    private RestaurantService restaurantService;

    @GetMapping("/cuisines")
    public ResponseEntity<String> getCuisines() {
        List<String> cuisines = restaurantService.getCuisines();
        JsonArrayBuilder jArrayBuilder = Json.createArrayBuilder();
        for (String s: cuisines) {
            s.replaceAll("/", "_");
            jArrayBuilder.add(s);
        }
        JsonArray jArray = jArrayBuilder.build();

        return ResponseEntity.ok(jArray.toString());
    }

    @GetMapping("/{cuisine}/restaurants")
    public ResponseEntity<String> getRestaurantByCuisine(@PathVariable String cuisine){
        cuisine.replace("_", "/");
        List<String> restaurantNames = restaurantService.getRestaurantsByCuisine(cuisine);
        JsonArrayBuilder jArrayBuilder = Json.createArrayBuilder();
        for (String s: restaurantNames) {
            jArrayBuilder.add(s);
        }
        JsonArray jArray = jArrayBuilder.build();

        return ResponseEntity.ok(jArray.toString());
    }

    @GetMapping("/restaurant/{name}")
    public ResponseEntity<String> getRestaurant(@PathVariable String name) {
        try {
            Optional<Restaurant> opt = restaurantService.getRestaurant(name);
            return opt.map(r -> new ResponseEntity<>(r.toJson().toString(), HttpStatus.valueOf(200)))
                .orElse(new ResponseEntity<String>("nope", HttpStatusCode.valueOf(404)));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return ResponseEntity.badRequest().body("boo");
        }
    }

    @PostMapping("/comments")
    public ResponseEntity<String> addComment(@RequestBody String comment) {
        JsonObject jObject = Json.createReader(new StringReader(comment)).readObject();
        Comment c = new Comment();
        c.setName(jObject.getString("name"));
        c.setRating(jObject.getInt("rating"));
        c.setRestaurantId(jObject.getString("restaurantId"));
        c.setText(jObject.getString("text"));
        restaurantService.addComment(c);

        JsonObject response = Json.createObjectBuilder()
            .add("message", "Comment Posted")
            .build();

        return ResponseEntity.status(201).body(response.toString());
    }

}
