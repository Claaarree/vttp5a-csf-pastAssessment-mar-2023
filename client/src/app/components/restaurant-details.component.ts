import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { Restaurant, Comment } from '../models';
import { RestaurantService } from '../restaurant-service';

@Component({
  selector: 'app-restaurant-details',
  templateUrl: './restaurant-details.component.html',
  styleUrls: ['./restaurant-details.component.css']
})
export class RestaurantDetailsComponent implements OnInit {
	
	// TODO Task 4 and Task 5
	// For View 3
  form!: FormGroup
  name!: string;
  route$!: Subscription;
  restaurant!: Restaurant;
  comment!: Comment;


  constructor(private restaurantSvc: RestaurantService, private activatedRoute: ActivatedRoute, private fb: FormBuilder, private router: Router) {  }


  ngOnInit() {
    //initalise form
    this.form = this.createForm();
    //get the restaurant details
    this.route$=this.activatedRoute.params.subscribe(params=>{
      console.log(params['name'])
      this.name = params['name'];
    })
    this.getRestaurantDetails();
  }

  createForm(): FormGroup {
    return this.fb.group({
      name: this.fb.control('', [Validators.required]),
      rating: this.fb.control('', [Validators.required, Validators.min(1), Validators.max(5) ]),
      text: this.fb.control('', [Validators.required])
    })
  }

  getRestaurantDetails() {
    this.restaurantSvc.getRestaurant(this.name).then(response=>{
      console.log(response)
      this.restaurant = {
        restaurantId: response.restaurant_id,
        name: response.name,
        cuisine: response.cuisine,
        address: response.address,
        coordinates: response.coordinates,
        image: response.image
      }
      console.log(this.restaurant)
    }).catch(err=> {
      console.error(err);
    })
  }

  isFormValid(): boolean {
    return this.form.invalid;
  } 

  submitComment(restaurantId: string) {
    console.log(this.form.value);
    console.log(restaurantId);
    const name = this.form.get('name')?.value;
    const text = this.form.get('text')?.value
    const rating = this.form.get('rating')?.value
    
    this.comment = {
      name: name,
      rating: rating,
      restaurantId: restaurantId,
      text: text
    }

    this.restaurantSvc.postComment(this.comment).then(response => {
      console.log(response);
      this.router.navigate(['/'])
    }).catch(err=> {
      console.log(err);
    })
  }

}
