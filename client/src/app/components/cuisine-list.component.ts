import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { RestaurantService } from '../restaurant-service';

@Component({
  selector: 'app-cuisine-list',
  templateUrl: './cuisine-list.component.html',
  styleUrls: ['./cuisine-list.component.css']
})
export class CuisineListComponent implements OnInit {

	// TODO Task 2
	// For View 1
  cuisines: string[]=[];

  constructor(private restaurantSvc: RestaurantService, private router: Router) { }

  ngOnInit(): void {
    this.restaurantSvc.getCuisineList().then(result=> {
      console.log(result)
      this.cuisines = result;
    }).catch(err => {
      console.error(err)
    });
  }

  getRestaurants(cuisine: string) {
    this.router.navigate([`${cuisine}/restaurants`]);
    
  }

}
