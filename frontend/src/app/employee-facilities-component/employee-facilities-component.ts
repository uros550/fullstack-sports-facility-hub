import { CommonModule } from '@angular/common';
import { Component, inject, OnInit } from '@angular/core';
import { FormsModule, NgForm } from '@angular/forms';
import { User } from '../models/User';
import { SportsFacility } from '../models/SportsFacility';
import { Court } from '../models/Court';
import { SportsFacilityService } from '../services/sports-facility-service';
import { Sport } from '../models/Sport';
import { SportService } from '../services/sport-service';

@Component({
  selector: 'app-employee-facilities-component',
  imports: [CommonModule, FormsModule],
  templateUrl: './employee-facilities-component.html',
  styleUrl: './employee-facilities-component.css',
})
export class EmployeeFacilitiesComponent implements OnInit {

  currentUser: User | null = null;
  facilities: SportsFacility[] = [];
  selectedFacilityId: number | null = null;

  facility: SportsFacility = this.getEmptyFacility();
  courts: Court[] = [];
  sports: Sport[] = [];

  existingImages: string[] = [];
  newImageFiles: File[] = [];
  newImagePreviews: string[] = [];
  selectedJsonFile: File | null = null;

  imgUrl = 'http://localhost:8080/';

  private facilityService = inject(SportsFacilityService);
  private sportService = inject(SportService);

  ngOnInit() {
    const userJson = localStorage.getItem('loggedUser');
    if (userJson) {
      try {
        this.currentUser = JSON.parse(userJson);
      } catch (error) {
        console.error('Local storage getItem error:', error);
      }
    }

    if (this.currentUser) {
      this.loadFacilities();
      this.loadSports();
    }
  }

  loadFacilities() {
    this.facilityService.getFacilitiesForEmployee(this.currentUser!.id).subscribe(data => {
      this.facilities = data;
    });
  }

  loadSports() {
    this.sportService.getAllSports().subscribe(data => {
      this.sports = data;
    })
  }

  changeFacilitySelect() {
    if (!this.selectedFacilityId) {
      this.resetForm();
      return;
    }

    const found = this.facilities.find(f => f.id == this.selectedFacilityId);
    if (found) {
      this.facility = { ...found };
      this.loadFacilityImages(found.id);
      this.loadFacilityCourts(found.id);
    }
  }

  loadFacilityImages(facilityId: number) {
    this.facilityService.getFacilityImagesById(facilityId).subscribe(images => {
      this.existingImages = images;
    });
  }

  loadFacilityCourts(facilityId: number) {
    this.facilityService.getAllCourtsById(facilityId).subscribe(data => {
      this.courts = data;
    });
  }

  addCourt() {
    this.courts.push({
      id: 0,
      facilityId: this.facility.id || 0,
      sportId: 1,
      name: '',
      type: 'OPEN',
      capacity: 1,
      equipmentDescription: '',
      pricePerHour: 0,
      sportName: ''
    });
  }

  removeCourt(index: number) {
    this.courts.splice(index, 1);
  }

  selectJsonFile(event: any) {
    const file = event.target.files[0];
    if (file) {
      this.selectedJsonFile = file;
    }
  }

  uploadJson() {
    if (!this.selectedJsonFile) return;
  }

  getImageUrl(path: string): string {
    if (!path) return '';
    return `${this.imgUrl}${path}`;
  }

  onImageSelected(event: any) {
    const files: FileList = event.target.files;
    if (files) {
      for (let i = 0; i < files.length; i++) {
        const file = files[i];
        this.newImageFiles.push(file);

        const reader = new FileReader();
        reader.onload = (e: any) => {
          this.newImagePreviews.push(e.target.result);
        };
        reader.readAsDataURL(file);
      }
    }
  }

  removeExistingImage(index: number) {
    this.existingImages.splice(index, 1);
  }

  removeNewImage(index: number) {
    this.newImageFiles.splice(index, 1);
    this.newImagePreviews.splice(index, 1);
  }

  areCourtsValid(): boolean {
    if (this.courts.length === 0) return false;
    return this.courts.every(court => 
      court.name && court.name.trim() !== '' &&
      court.capacity > 0 && 
      court.pricePerHour > 0
    );
  }

  hasImages(): boolean {
    return (this.existingImages.length + this.newImageFiles.length) > 0;
  }

  saveFacility(form: NgForm) {
    //mark all form controls as touched to trigger css validation styles
    form.control.markAllAsTouched();

    const courtsValid = this.areCourtsValid();
    const imagesValid = this.hasImages();

    if (form.invalid || !courtsValid || !imagesValid) {
      let errors: string[] = [];

      if (form.invalid || !courtsValid) {
        errors.push('• Please fill in all fields correctly, all fields are required.');
      }
      if (!imagesValid) {
        errors.push('• You must upload at least one facility photo.');
      }
      if (this.courts.length === 0) {
        errors.push('• You must add at least one court.');
      }

      alert('Please fix the following errors:\n\n' + errors.join('\n'));

      //find the first invalid element in the dom, focus it, and scroll to it
      setTimeout(() => {
        const firstInvalidElement = document.querySelector('.ng-invalid') as HTMLElement;
        if (firstInvalidElement) {
          firstInvalidElement.focus();
          firstInvalidElement.scrollIntoView({ behavior: 'smooth', block: 'center' });
        }
      }, 50);

      return;
    }

    //all fields are valid, proceed with sending data to backend
    console.log('All fields are valid, sending data to backend...');
  }

  resetForm() {
    this.selectedFacilityId = null;
    this.facility = this.getEmptyFacility();
    this.courts = [];
    this.existingImages = [];
    this.newImageFiles = [];
    this.newImagePreviews = [];
    this.selectedJsonFile = null;
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
      status: 'ACTIVE'
    };
  }
}