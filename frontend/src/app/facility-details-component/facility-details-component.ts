import { Component, inject } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { SportsFacilityService } from '../services/sports-facility-service';
import { SportsFacility } from '../models/SportsFacility';
import { Court } from '../models/Court';
import { AuthenticationService } from '../services/authentication-service';
import { AvailabilitySlot } from '../models/AvailabilitySlot';
import { UserService } from '../services/user-service';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-facility-details-component',
  imports: [FormsModule],
  templateUrl: './facility-details-component.html',
  styleUrl: './facility-details-component.css',
})
export class FacilityDetailsComponent {

  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private facilityService = inject(SportsFacilityService);
  private userService = inject(UserService);
  private authService = inject(AuthenticationService);

  facility!: SportsFacility;
  courts: Court[] = [];
  filteredCourts: Court[] = [];
  //athlete logged in
  isAthlete: boolean = false;
  requestSportId: number = 0; //ids start from 1
  requestCourtType: string = '';
  requestFreeToday: boolean = false;
  //not important just for restoring search
  requestName: string = '';
  requestCities: any[] = [];
  //availability
  showAvailability: boolean = false;
  selectedCourtIndex: number = 0;
  selectedDate: string = '';
  minDate: string = ''; //to not be able to select dates before today
  availabilitySlots: AvailabilitySlot[] = [];

  images: string[] = [];
  baseUrl: string = 'http://localhost:8080/';

  ngOnInit(): void {
    const today = new Date();
    this.minDate = today.toISOString().split('T')[0]; //set todays date to yyyy-mm-dd
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
    this.filteredCourts = [];
    for (const court of this.courts) {
      if (this.requestSportId !== 0 && court.sportId !== this.requestSportId) {
        continue; //is not in sport filter
      }
      if (this.requestCourtType !== '' && court.type !== this.requestCourtType) {
        continue; //is not in courtType filer
      }
      this.filteredCourts.push(court);
    }
    return this.filteredCourts;
  }

  openAvailability() {
    if (this.filteredCourts.length === 0) {
      return;
    }
    this.showAvailability = true;
    this.selectedCourtIndex = 0;
    //default date is today
    const today = new Date();
    //today to string
    this.selectedDate = today.getFullYear() + '-' + String(today.getMonth() + 1).padStart(2, '0') + '-' + String(today.getDate()).padStart(2, '0');
    this.loadAvailability();
  }

  closeAvailability() {
    this.showAvailability = false;
    this.availabilitySlots = [];
  }

  loadAvailability() {
    const selectedCourt = this.filteredCourts[this.selectedCourtIndex];
    this.userService.getCourtAvailability(selectedCourt.id, this.selectedDate).subscribe(data => {
      //console.log('API RESPONSE SLOTS:', data); //TEST
      const todayStr = new Date().toISOString().split('T')[0];
      if (this.selectedDate === todayStr) {
        const nowStr = new Date().toTimeString().substring(0, 5);
        this.availabilitySlots = data.filter(slot => slot.startTime >= nowStr);
      }
      else {
        this.availabilitySlots = data;
      }
    })
  }

  previousCourt() {
    if (this.filteredCourts.length === 0) {
      return;
    }
    if (this.selectedCourtIndex > 0) {
      this.selectedCourtIndex--;
    }
    else {
      this.selectedCourtIndex = this.filteredCourts.length - 1; //circular selection
    }
    this.loadAvailability();
  }

  nextCourt() {
    if (this.filteredCourts.length === 0) {
      return;
    }
    if (this.selectedCourtIndex < this.filteredCourts.length - 1) {
      this.selectedCourtIndex++;
    }
    else {
      this.selectedCourtIndex = 0;
    }
    this.loadAvailability();
  }

  changeDate() {
    this.loadAvailability();
  }

  getSelectedCourt(): Court | null {
    if (this.filteredCourts.length === 0) {
      return null;
    }
    return this.filteredCourts[this.selectedCourtIndex];
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
