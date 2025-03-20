import { HttpClient } from '@angular/common/http'
import { Injectable } from '@angular/core'
import { firstValueFrom, lastValueFrom } from 'rxjs'
import { Restaurant, Comment } from './models'

@Injectable()
export class RestaurantService {

	restaurant!: Restaurant;

	constructor(private http: HttpClient) {	}

	// TODO Task 2 
	// Use the following method to get a list of cuisines
	// You can add any parameters (if any) and the return type 
	// DO NOT CHNAGE THE METHOD'S NAME
	public getCuisineList():Promise<any> {
		// Implememntation in here
		return firstValueFrom(this.http.get("/api/cuisines"));

	}

	// TODO Task 3 
	// Use the following method to get a list of restaurants by cuisine
	// You can add any parameters (if any) and the return type 
	// DO NOT CHNAGE THE METHOD'S NAME
	public getRestaurantsByCuisine(cuisine: string): Promise<any> {
		return firstValueFrom(this.http.get(`/api/${cuisine}/restaurants`));

	}
	
	// TODO Task 4
	// Use this method to find a specific restaurant
	// You can add any parameters (if any) 
	// DO NOT CHNAGE THE METHOD'S NAME OR THE RETURN TYPE

	//change back rmb
	public getRestaurant(name: string): Promise<any> {
		// Implememntation in here
		return firstValueFrom(this.http.get(`/api/restaurant/${name}`));

	}

	// TODO Task 5
	// Use this method to submit a comment
	// DO NOT CHANGE THE METHOD'S NAME OR SIGNATURE
	public postComment(comment: Comment): Promise<any> {
		// Implememntation in here
		const body = {
			restaurantId: comment.restaurantId,
			name: comment.name,
			rating: comment.rating,
			text: comment.text
		}
		return lastValueFrom(this.http.post(`/api/comments`, body))

	}
}
