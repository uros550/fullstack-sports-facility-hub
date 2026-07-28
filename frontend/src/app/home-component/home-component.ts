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

  private sportsFacilityService = inject(SportsFacilityService);
  private promotionService = inject(PromotionService);
  private sportService = inject(SportService);
  private router = inject(Router);

  ngOnInit(): void {

    this.sportsFacilityService.getAllActiveFacilities().subscribe(data => {
      this.displayedFacilities = data;
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
      cities: this.selectedCities
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
    this.router.navigate(['/facility', id]);
  }

}
