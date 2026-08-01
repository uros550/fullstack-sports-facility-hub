import { Component, inject, OnInit } from '@angular/core';
import { SportsFacility } from '../models/SportsFacility';
import { SportsFacilityService } from '../services/sports-facility-service';
import { PromotionService } from '../services/promotion-service';
import { Promotion } from '../models/Promotion';
import { Sport } from '../models/Sport';
import { SportService } from '../services/sport-service';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { DatePipe } from '@angular/common';
import { User } from '../models/User';
import { AuthenticationService } from '../services/authentication-service';

@Component({
  selector: 'app-home-component',
  imports: [FormsModule, DatePipe],
  templateUrl: './home-component.html',
  styleUrl: './home-component.css',
})
export class HomeComponent implements OnInit {

  displayedFacilities: SportsFacility[] = [];
  topFacilities: SportsFacility[] = [];
  activePromotions: Promotion[] = [];
  allSports: Sport[] = [];
  allCities: string[] = [];
  activeFacilitiesCount = 0;

  //search
  searchName = '';
  selectedCities: string[] = [];  
  selectedSportId = 0;
  selectedCourtType = '';

  //sort
  sortColumn: string = '';
  sortDir: 'asc' | 'desc' = 'asc';

  //logged athlete
  currentUser: User | null = null;
  isAthlete = false;
  onlyFreeToday = false;

  private sportsFacilityService = inject(SportsFacilityService);
  private promotionService = inject(PromotionService);
  private sportService = inject(SportService);
  private authService = inject(AuthenticationService);
  private router = inject(Router);

  ngOnInit(): void {

    //if athlete logged in
    const user = this.authService.currentUser();
    const search = history.state.searchRequest;
    if (user && user.role === 'ATHLETE') {
      this.currentUser = user;
      this.isAthlete = true;
      const search = history.state.searchRequest;
      if (search) {
        this.searchName = search.name;
        this.selectedCities = search.cities;
        this.selectedSportId = search.sportId;
        this.selectedCourtType = search.courtType;
        this.onlyFreeToday = search.freeToday;
      }
    }
    else {
      if (search) {
        this.searchName = search.name;
        this.selectedCities = search.cities;
        this.selectedSportId = search.sportId;
        this.selectedCourtType = search.courtType;
      }
    }

    this.sportsFacilityService.getAllActiveFacilities().subscribe(data => {
      this.displayedFacilities = data;
      if (search) {
        this.searchFacilities(); //restore previous search
      }
    })

    this.sportsFacilityService.getActiveFacilitiesCount().subscribe(data => {
      this.activeFacilitiesCount = data;
    })

    this.sportsFacilityService.getTop3Facilities().subscribe(data => {
      this.topFacilities = data;
    })

    this.promotionService.getActivePromotions().subscribe(data => {
      this.activePromotions = data;
    })

    this.sportService.getAllSports().subscribe(data => {
      this.allSports = data;
    });

    this.sportsFacilityService.getAllCities().subscribe(data => {
      this.allCities = data;
    });

  }

  searchFacilities() {

    const request = {
      name: this.searchName,
      sportId: this.selectedSportId,
      courtType: this.selectedCourtType,
      cities: this.selectedCities,
      freeToday: this.onlyFreeToday
    };

    this.sportsFacilityService.searchFacilities(request).subscribe(data => {
      this.displayedFacilities = data;
    });

  }

  sortFacilities(column: string) {

    if (this.sortColumn === column) {
      if(this.sortDir === 'asc') {
        this.sortDir = 'desc';
      } else{
        this.sortDir = 'asc';
      }
    } else {
      this.sortColumn = column;
      this.sortDir = 'asc';
    }

    this.displayedFacilities.sort((a, b) => {
      const valA = a[column as keyof SportsFacility];
      const valB = b[column as keyof SportsFacility];

      if (valA == null) return 1;
      if (valB == null) return -1;

      if (typeof valA === 'string' && typeof valB === 'string') {
        return this.sortDir === 'asc' 
          ? valA.localeCompare(valB) 
          : valB.localeCompare(valA);
      } else {
        return this.sortDir === 'asc'
          ? (valA as number) - (valB as number)
          : (valB as number) - (valA as number);
      }
    });

  }

  goToDetails(id: number) {

    const request = {
      name: this.searchName,
      cities: this.selectedCities,
      sportId: this.selectedSportId,
      courtType: this.selectedCourtType,
      freeToday: this.onlyFreeToday
    };

    this.router.navigate(['/facility', id], {
      state: {
        searchRequest: request //send request params through navigate
      }
    });

  }
}
