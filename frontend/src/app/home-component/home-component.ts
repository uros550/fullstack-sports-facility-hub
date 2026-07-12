import { Component, inject, OnInit } from '@angular/core';
import { SportsFacility } from '../models/SportsFacility';
import { SportsFacilityService } from '../services/sports-facility-service';

@Component({
  selector: 'app-home-component',
  imports: [],
  templateUrl: './home-component.html',
  styleUrl: './home-component.css',
})
export class HomeComponent implements OnInit {

  facilities: SportsFacility[] = [];

  private sportsFacilityService = inject(SportsFacilityService);

  ngOnInit(): void {
    
    this.sportsFacilityService.getAllFacilities().subscribe( data => {
      this.facilities = data;
    });

  }

}
