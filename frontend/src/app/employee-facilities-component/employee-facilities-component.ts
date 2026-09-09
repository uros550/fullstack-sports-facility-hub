import { Component, inject, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { User } from '../models/User';
import { SportsFacility } from '../models/SportsFacility';
import { Court } from '../models/Court';
import { Sport } from '../models/Sport';
import { SportsFacilityService } from '../services/sports-facility-service';
import { SportService } from '../services/sport-service';
import { NgClass } from '@angular/common';

@Component({
  selector: 'app-employee-facilities-component',
  imports: [FormsModule, NgClass],
  templateUrl: './employee-facilities-component.html',
  styleUrl: './employee-facilities-component.css',
})
export class EmployeeFacilitiesComponent implements OnInit {

  currentUser: User | null = null;
  facilities: SportsFacility[] = [];
  sports: Sport[] = [];
  
  newFacility: SportsFacility = this.getEmptyFacility();
  newFacilityCourts: Court[] = [];
  tempCourt: Court = this.getEmptyCourt();

  selectedFacility: SportsFacility | null = null;
  selectedFacilityCourts: Court[] = [];

  showAdd: boolean = true;

  message: string = '';

  private facilityService = inject(SportsFacilityService);
  private sportService = inject(SportService);

  ngOnInit() {
    const userJson = localStorage.getItem('loggedUser');
    if (userJson) {
      try {
        this.currentUser = JSON.parse(userJson);
      } catch (error) {
        console.error(error);
      }
    }

    if (this.currentUser) {
      this.loadFacilities();
      this.loadSports();
    }
  }

  loadFacilities() {
    if (!this.currentUser) return;
    this.facilityService.getFacilitiesForEmployee(this.currentUser.id).subscribe(data => {
      this.facilities = data;
      if (this.facilities.length > 0) {
        this.selectedFacility = this.facilities[0];
        this.loadCourtsForFacility(this.selectedFacility.id);
      }
    })
  }

  loadSports() {
    this.sportService.getAllSports().subscribe(data => {
      this.sports = data;
    });
  }

  changeView(showAdd: boolean) {
    this.showAdd = showAdd;
    this.message = '';
    if (!showAdd) {
      this.loadFacilities();
    }
  }

  JsonFileUpload(event: Event) {
    const input = event.target as HTMLInputElement;
    if (!input.files || input.files.length === 0) return;

    const file = input.files[0];
    const reader = new FileReader();

    reader.onload = (e: ProgressEvent<FileReader>) => {
      try {
        const parsed = JSON.parse(e.target?.result as string);
        
        if (parsed.facility) {
          this.newFacility = { ...this.getEmptyFacility(), ...parsed.facility };
        } else {
          this.newFacility = { ...this.getEmptyFacility(), ...parsed };
        }

        if (parsed.courts && Array.isArray(parsed.courts)) {
          this.newFacilityCourts = parsed.courts;
        }

        this.message = 'JSON file successfully loaded! Form fields populated.';
      } catch (err) {
        this.message = 'Error reading JSON file. Check format.';
      }
      this.clearMessageAfterDelay();
    };

    reader.readAsText(file);
  }

  addTempCourt() {
    if (!this.tempCourt.name || !this.tempCourt.sportId) {
      this.message = 'Error: Enter court name and select a sport.';
      return;
    }
    this.newFacilityCourts.push({ ...this.tempCourt });
    this.tempCourt = this.getEmptyCourt();
    this.clearMessageAfterDelay();
  }

  removeTempCourt(index: number) {
    this.newFacilityCourts.splice(index, 1);
  }

  confirmAddFacility() {
    if (!this.currentUser) {
      this.message = 'Error: User is not authenticated.';
      return;
    }

    const f = this.newFacility;

    if (!f.name?.trim() || !f.address?.trim() || !f.city?.trim() || !f.mb?.trim() || !f.pib?.trim() || !f.workingHours?.trim()) {
      this.message = 'Error: All required fields must be filled.';
      return;
    }

    const mbRegex = /^\d{8}$/;
    if (!mbRegex.test(f.mb.trim())) {
      this.message = 'Error: Registry number (MB) must contain exactly 8 digits.';
      return;
    }

    const pibRegex = /^\d{9}$/;
    if (!pibRegex.test(f.pib.trim())) {
      this.message = 'Error: Tax ID (PIB) must contain exactly 9 digits.';
      return;
    }

    const timeRegex = /^([01]\d|2[0-3]):[0-5]\d\s*-\s*([01]\d|2[0-3]):[0-5]\d$/;
    if (!timeRegex.test(f.workingHours.trim())) {
      this.message = 'Error: Working hours must be in HH:MM - HH:MM format (e.g. 08:00 - 23:00).';
      return;
    }

    if (f.description && f.description.length > 500) {
      this.message = 'Error: Description cannot exceed 500 characters.';
      return;
    }

    if (this.newFacilityCourts.length === 0) {
      this.message = 'Error: You must add at least one court for the facility.';
      return;
    }

    this.facilityService.addFacility(this.newFacility, this.newFacilityCourts, this.currentUser.id).subscribe(data => {
      this.message = data;
      this.loadFacilities();
      if (!data.includes('Error')) {
        this.resetForm();
      }
    })
  }

  getEmptyFacility(): SportsFacility {
    return {
      id: 0,
      name: '',
      address: '',
      city: '',
      mb: '',
      pib: '',
      description: '',
      latitude: 0,
      longitude: 0,
      workingHours: '',
      maxPenalties: 3,
      likesCount: 0,
      status: 'PENDING'
    };
  }

  getEmptyCourt(): Court {
    return {
      id: 0,
      facilityId: 0,
      sportId: 0,
      name: '',
      type: 'OPEN',
      capacity: 0,
      equipmentDescription: '',
      pricePerHour: 0,
      sportName: ''
    };
  }

  resetForm() {
    this.newFacility = this.getEmptyFacility();
    this.newFacilityCourts = [];
    this.tempCourt = this.getEmptyCourt();
  }

  selectFacility(facility: SportsFacility) {
    this.selectedFacility = { ...facility };
    this.tempCourt = this.getEmptyCourt();
    this.loadCourtsForFacility(facility.id);
  }

  loadCourtsForFacility(facilityId: number) {
    this.facilityService.getAllCourtsById(facilityId).subscribe(data => {
      this.selectedFacilityCourts = data;
    })
  }

  confirmUpdateFacility() {
    if (!this.selectedFacility) return;
    const f = this.selectedFacility;

    if (!f.name?.trim() || !f.address?.trim() || !f.city?.trim() || !f.mb?.trim() || !f.pib?.trim() || !f.workingHours?.trim()) {
      this.message = 'Error: All required fields must be filled.';
      return;
    }

    const mbRegex = /^\d{8}$/;
    if (!mbRegex.test(f.mb.trim())) {
      this.message = 'Error: Registry number (MB) must contain exactly 8 digits.';
      return;
    }

    const pibRegex = /^\d{9}$/;
    if (!pibRegex.test(f.pib.trim())) {
      this.message = 'Error: Tax ID (PIB) must contain exactly 9 digits.';
      return;
    }

    const timeRegex = /^([01]\d|2[0-3]):[0-5]\d\s*-\s*([01]\d|2[0-3]):[0-5]\d$/;
    if (!timeRegex.test(f.workingHours.trim())) {
      this.message = 'Error: Working hours must be in HH:MM - HH:MM format (e.g. 08:00 - 23:00).';
      return;
    }

    if (f.description && f.description.length > 500) {
      this.message = 'Error: Description cannot exceed 500 characters.';
      return;
    }

    this.facilityService.updateFacility(f).subscribe(data => {
      if (data) {
        this.message = 'Facility details updated successfully.';
        this.loadFacilities();
      }
      else {
        this.message = 'Error updating facility.';
      }
    });
  }

  confirmAddSingleCourt() {
    if (!this.selectedFacility) return;
    const c = this.tempCourt;

    if (!c.name?.trim() || !c.sportId) {
      this.message = 'Error: Enter court name and select a sport.';
      return;
    }

    c.facilityId = this.selectedFacility.id;

    this.facilityService.addCourt(c).subscribe(data => {
      if (data) {
        this.message = 'Court added successfully.';
        this.loadCourtsForFacility(this.selectedFacility!.id);
        this.tempCourt = this.getEmptyCourt();
      }
      else {
        this.message = 'Error adding court.';
      }
    });
  }

  clearMessageAfterDelay() {
    setTimeout(() => {
      this.message = '';
    }, 5000);
  }

}