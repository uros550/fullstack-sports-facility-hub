import { AfterViewInit, Component, inject, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { SportsFacilityService } from '../services/sports-facility-service';
import { SportsFacility } from '../models/SportsFacility';
import { Court } from '../models/Court';
import { AuthenticationService } from '../services/authentication-service';
import { AvailabilitySlot } from '../models/AvailabilitySlot';
import { UserService } from '../services/user-service';
import { FormsModule } from '@angular/forms';
import { Reservation } from '../models/Reservation';
import * as L from 'leaflet';
import { Promotion } from '../models/Promotion';
import { PromotionService } from '../services/promotion-service';
import { DecimalPipe } from '@angular/common';


@Component({
  selector: 'app-facility-details-component',
  imports: [FormsModule, DecimalPipe],
  templateUrl: './facility-details-component.html',
  styleUrl: './facility-details-component.css',
})
export class FacilityDetailsComponent implements OnInit, AfterViewInit, OnDestroy {

  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private facilityService = inject(SportsFacilityService);
  private userService = inject(UserService);
  private authService = inject(AuthenticationService);
  private promoService = inject(PromotionService);

  facility!: SportsFacility;
  courts: Court[] = [];
  filteredCourts: Court[] = [];
  //athlete logged in
  isAthlete: boolean = false;
  athleteId: number = 0;
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
  //reservation
  showReservation: boolean = false;
  reservedCourt: Court | null = null;
  reservedDate: string = '';
  reservedStartTime: string = '';
  reservedEndTime: string = '';
  missingPlayers: number = 0;
  reservationSlots: AvailabilitySlot[] = [];
  selectedDuration: number = 1;
  availableDurations: number[] = [1];
  isAthleteBlocked: boolean = false;
  currentPromotion: Promotion | null = null;
  //images
  images: string[] = [];
  baseUrl: string = 'http://localhost:8080/';
  //map
  map: L.Map | null = null;
  //is employee logged in
  isEmployee: boolean = false;
  employeeId: number = 0;
  isAdmin: boolean = false;

  errorMessage: string = '';

  ngOnInit() {
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
        //map did not work without it
        setTimeout(() => {
          this.initMap();
        }, 10);
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
      this.athleteId = user.id;
      this.isAthlete = true;
      this.checkIfBlocked();
    }
    else if (user && user.role === 'EMPLOYEE') {
      this.employeeId = user.id;
      this.isEmployee = true;
    }
    else if (user && user.role === 'ADMIN') {
      this.isAdmin = true;
    }
  }

  checkIfBlocked() {
    const user = this.authService.currentUser();
    const id = Number(this.route.snapshot.paramMap.get('id'));

    if (user && id) {
      this.userService.checkIfBlocked(user.id, id).subscribe(data => {
        this.isAthleteBlocked = data;
      })
    }
  }

  //MAP
  ngAfterViewInit() {
    if (this.facility?.latitude && this.facility?.longitude) {
      this.initMap();
    }
  }

  ngOnDestroy() {
    if (this.map) {
      this.map.remove();
    }
  }

  initMap() {
    if (!this.facility || !this.facility.latitude || !this.facility.longitude) return;
    if (this.map) {
      this.map.remove(); //do not double initialize
    }
    //so they do not end up being strings while compiled
    const lat = Number(this.facility.latitude);
    const lng = Number(this.facility.longitude);
    //create a map centered around the object
    this.map = L.map('facility-map', {
      center: [lat, lng],
      zoom: 15,
      zoomControl: true
    });
    //open street map layer
    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
      maxZoom: 19,
      attribution: '© OpenStreetMap contributors'
    }).addTo(this.map);
    //custom icon fix
    const customIcon = L.icon({
      iconUrl: 'https://unpkg.com/leaflet@1.9.4/dist/images/marker-icon.png',
      iconRetinaUrl: 'https://unpkg.com/leaflet@1.9.4/dist/images/marker-icon-2x.png',
      shadowUrl: 'https://unpkg.com/leaflet@1.9.4/dist/images/marker-shadow.png',
      iconSize: [25, 41],
      iconAnchor: [12, 41],
      popupAnchor: [1, -34],
      shadowSize: [41, 41]
    });
    //marker and popup
    const marker = L.marker([lat, lng], { icon: customIcon }).addTo(this.map);
    marker.bindPopup(`
      <div class="map-popup-content">
        <strong class="map-popup-title">${this.facility.name}</strong>
        <span class="map-popup-subtitle">${this.facility.address}, ${this.facility.city}</span>
      </div>
    `);
  }
  //MAP

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

  openReservation(court: Court) {
    this.reservedCourt = court;
    if (this.filteredCourts.length === 0) return;
    this.showAvailability = false;
    this.showReservation = true;
    this.reservedDate = this.minDate;
    this.errorMessage = '';
    if (this.reservedCourt) this.getCurrentPromotion(this.facility.id, this.reservedCourt.sportId);
    this.loadReservationAvailability();
  }

  closeReservation() {
    this.showReservation = false;
    this.currentPromotion = null;
    this.reservedCourt = null;
    this.reservationSlots = [];
    this.reservedStartTime = '';
    this.reservedEndTime = '';
    this.missingPlayers = 0; 
    this.errorMessage = '';
  }

  getCurrentPromotion(facilityId: number, sportId: number) {
    this.promoService.getCurrentPromotion(facilityId, sportId).subscribe(data => {
      this.currentPromotion = data;
    })
  }

  loadReservationAvailability() {
    if (!this.reservedDate || !this.reservedCourt) return;
    this.userService.getCourtAvailability(this.reservedCourt.id, this.reservedDate).subscribe(data => {
      let slots = data;

      const todayStr = new Date().toISOString().split('T')[0];
      if (this.reservedDate === todayStr) {
        const nowStr = new Date().toTimeString().substring(0, 5);
        slots = data.filter(slot => slot.startTime >= nowStr); //show only future slots
      } 

      this.reservationSlots = slots.filter(slot => slot.available); //show only available
      if (this.reservationSlots.length > 0) {
        this.reservedStartTime = this.reservationSlots[0].startTime;
        this.changeReservationStartTime();
      } else {
        this.reservedStartTime = '';
        this.availableDurations = [];
      }
    })
  }

  changeReservationDate() {
    this.errorMessage = '';
    this.loadReservationAvailability();
  }

  changeReservationStartTime() {
    if (!this.reservedStartTime || this.reservationSlots.length === 0) {
      this.availableDurations = [];
      return;
    }

    //available continuous hours
    const startIndex = this.reservationSlots.findIndex(s => s.startTime === this.reservedStartTime);
    if (startIndex === -1) {
      this.availableDurations = [1];
      this.selectedDuration = 1;
      return;
    }
    let maxHours = 1;
    for (let i = startIndex; i < this.reservationSlots.length - 1; i++) {
      const currentStart = parseInt(this.reservationSlots[i].startTime.split(':')[0], 10);
      const nextStart = parseInt(this.reservationSlots[i + 1].startTime.split(':')[0], 10);

      if (nextStart === currentStart + 1) {
        maxHours++;
      } else {
        break;
      }
    }
    this.availableDurations = Array.from({ length: maxHours }, (_, index) => index + 1); // [1,2,3,...,maxHours]
    this.selectedDuration = 1;
  }

  submitReservation() {
    if (!this.reservedCourt || !this.reservedDate || !this.reservedStartTime) {
      this.errorMessage = 'Please select a valid date and start time.';
      return;
    }
    const startHour = parseInt(this.reservedStartTime.split(':')[0], 10);
    const endHour = startHour + Number(this.selectedDuration); 
    //start and end times in correct syntax for backend
    const startSyntax = `${this.reservedDate}T${String(startHour).padStart(2, '0')}:00:00`; 
    const endSyntax = `${this.reservedDate}T${String(endHour).padStart(2, '0')}:00:00`;

    const reservation: Reservation = new Reservation();
    reservation.facilityId = this.facility.id;
    reservation.facilityName = this.facility.name;
    reservation.city = this.facility.city;
    reservation.courtId = this.reservedCourt.id;
    reservation.courtName = this.reservedCourt.name;
    reservation.athleteId = this.athleteId;
    reservation.sportId = this.reservedCourt.sportId;
    reservation.sportName = this.reservedCourt.sportName;
    reservation.startTime = startSyntax;
    reservation.endTime = endSyntax;
    reservation.status = 'PENDING';
    reservation.missingPlayers = 0;

    this.userService.addReservation(reservation).subscribe(data => {
      if (data === 'Success') {
        this.errorMessage = 'Reservation created, pending approval.';
      }
      else {
        this.errorMessage = 'Reservation error.';
      }
    })
  }

  getEstimatedPrice() {
    if (!this.reservedCourt) return 0;

    let price = this.reservedCourt.pricePerHour * this.selectedDuration;

    if (this.currentPromotion) {
      if (this.currentPromotion.discountType === 'PERCENTAGE') {
        price = price - (price * this.currentPromotion.discountValue / 100);
      } else if (this.currentPromotion.discountType === 'FIXED') {
        price = price - this.currentPromotion.discountValue;
      }
      if (price < 0) price = 0;
    }

    return price;
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
    else if (this.isEmployee) {
      this.router.navigate(['/employee-dashboard']);
    }
    else if (this.isAdmin) {
      this.router.navigate(['/admin-dashboard'], {
        state: {
          section: 'facilities'
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
