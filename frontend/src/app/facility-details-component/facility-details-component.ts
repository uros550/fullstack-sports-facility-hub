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
  isAthlete: boolean = false;

  images: string[] = [];
  baseUrl: string = 'http://localhost:8080/';

  ngOnInit(): void {
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

  return() {
    if (this.isAthlete) {
      this.router.navigate(['/athlete-dashboard'])
    }
    else {
      this.router.navigate(['/home']);
    }
  }
}
