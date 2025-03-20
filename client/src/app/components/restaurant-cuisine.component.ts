import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { RestaurantService } from '../restaurant-service';

@Component({
  selector: 'app-restaurant-cuisine',
  templateUrl: './restaurant-cuisine.component.html',
  styleUrls: ['./restaurant-cuisine.component.css']
})
export class RestaurantCuisineComponent implements OnInit {

  route$!: Subscription;
  cuisine!: string;
  restaurants: string[]=[];

  constructor(private restaurantSvc: RestaurantService, private activatedRoute: ActivatedRoute, private router: Router) { }
	
	// TODO Task 3
	// For View 2
  ngOnInit(): void {
      this.route$ = this.activatedRoute.params.subscribe(params => {
        console.log(params['cuisine']);
        this.cuisine=params['cuisine'];
      })
      this.getRestaurantsByCuisine();
  }

  getRestaurantsByCuisine() {
    this.restaurantSvc.getRestaurantsByCuisine(this.cuisine).then(response=>{
      console.log(response);
      this.restaurants=response;
    }).catch(err=>{
      console.error(err);
    })
  }

  getRestaurantDetails(name: string) {
    console.log(name);
    this.router.navigate([`${this.cuisine}/restaurants/${name}`]);
  }

}
