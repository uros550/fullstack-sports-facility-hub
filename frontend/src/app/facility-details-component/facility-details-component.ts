import { Component, inject } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { SportsFacilityService } from '../services/sports-facility-service';
import { SportsFacility } from '../models/SportsFacility';
import { Court } from '../models/Court';
import { AuthenticationService } from '../services/authentication-service';

@Component({
  selector: 'app-facility-details-component',
  imports: [],
  templateUrl: './facility-details-component.html',
  styleUrl: './facility-details-component.css',
})
export class FacilityDetailsComponent {

  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private facilityService = inject(SportsFacilityService);
  private authService = inject(AuthenticationService);

  facility!: SportsFacility;
  courts: Court[] = [];
  //athlete logged in
  isAthlete: boolean = false;
  requestSportId: number = 0; //ids start from 1
  requestCourtType: string = '';
  requestFreeToday: boolean = false;
  //not important just for restoring search
  requestName: string = '';
  requestCities: any[] = [];

  images: string[] = [];
  baseUrl: string = 'http://localhost:8080/';

  ngOnInit(): void {
    //get params
    const request = history.state.searchRequest;
    if (request) {
      this.requestSportId = request.sportId;
      this.requestCourtType = request.courtType;
      this.requestFreeToday = request.freeToday;
      //not important
      this.requestName = request.name;
      this.requestCities = request.cities;
    }
    const id = Number(this.route.snapshot.paramMap.get('id'));
    
    if (id) {
      this.facilityService.getFacilityById(id).subscribe(data => {
        this.facility = data;
      });
      this.facilityService.getAllCourtsById(id).subscribe(data => {
        this.courts = data;
      })
      this.facilityService.getFacilityImagesById(id).subscribe(data => {
        this.images = data;
      })
    }

    //if athlete logged in
    const user = this.authService.currentUser();
    if (user && user.role === 'ATHLETE') {
      this.isAthlete = true;
    }
  }

  getFilteredCourts(): Court[] {
    const filteredCourts: Court[] = [];

    for (const court of this.courts) {
      if (this.requestSportId !== 0 && court.sportId !== this.requestSportId) {
        continue; //is not in sport filter
      }
      if (this.requestCourtType !== '' && court.type !== this.requestCourtType) {
        continue; //is not in courtType filer
      }
      filteredCourts.push(court);
    }

    return filteredCourts;
  }

  return() {
    if (this.isAthlete) {
      this.router.navigate(['/athlete-dashboard'], {
        //return to exact search in reservation
        state: {
          section: 'reservations',
          searchRequest: history.state.searchRequest
        }
      });
    }
    else {
      this.router.navigate(['/home'], {
        state: {
          searchRequest: history.state.searchRequest
        }
      });
    }
  }
}
