import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { FacilityReview } from '../models/FacilityReview';

@Injectable({
  providedIn: 'root',
})
export class FacilityReviewService {
  
  private http = inject(HttpClient);
  private path = 'http://localhost:8080/reviews';

  countConfirmedReservations(athleteId: number, facilityId: number) {
    return this.http.get<number>(`${this.path}/count/reservations/${athleteId}/${facilityId}`);
  }

  countAthleteReviews(athleteId: number, facilityId: number) {
    return this.http.get<number>(`${this.path}/count/${athleteId}/${facilityId}`);
  }

  getTopReviewsByFacId(facilityId: number) {
    return this.http.get<FacilityReview[]>(`${this.path}/top5/${facilityId}`);
  }

  saveReview(review: FacilityReview) {
    return this.http.post<boolean>(`${this.path}/save`, review);
  }

}
