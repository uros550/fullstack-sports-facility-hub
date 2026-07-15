import { Component, inject } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { SportsFacilityService } from '../services/sports-facility-service';
import { SportsFacility } from '../models/SportsFacility';
import { Court } from '../models/Court';

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

  facility!: SportsFacility;
  courts: Court[] = [];

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    
    if (id) {
      this.facilityService.getFacilityById(id).subscribe(data => {
        this.facility = data;
      });
      this.facilityService.getAllCourtsById(id).subscribe(data => {
        this.courts = data;
      })
    }
  }

  return() {
    this.router.navigate(['/home']);
  }
}
