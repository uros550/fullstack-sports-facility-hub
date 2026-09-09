import { Component, inject, OnInit } from '@angular/core';
import { User } from '../models/User';
import { SportsFacility } from '../models/SportsFacility';
import { SportsFacilityService } from '../services/sports-facility-service';
import { FacilityReviewService } from '../services/facility-review-service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { FacilityReview } from '../models/FacilityReview';

@Component({
  selector: 'app-athlete-review-component',
  imports: [CommonModule, FormsModule],
  templateUrl: './athlete-review-component.html',
  styleUrl: './athlete-review-component.css',
})
export class AthleteReviewComponent implements OnInit {

  currentUser: User | null = null;
  facilities: SportsFacility[] = [];

  showReview: boolean = false;
  selectedFacility: SportsFacility | null = null;
  reservations: number = 0;
  reviews: number = 0;
  isEligible: boolean = false;
  topReviews: FacilityReview[] = [];
  newLiked: boolean = true;
  newComment: string = '';

  private facilityService = inject(SportsFacilityService);
  private reviewService = inject(FacilityReviewService);

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
    }
  }

  loadFacilities() {
    this.facilityService.getAllActiveFacilities().subscribe(data => {
      this.facilities = data;
    })
  }

  openReview(facility: SportsFacility) {
    this.selectedFacility = facility;
    this.showReview = true;
    this.checkReviewPossibility();
    this.loadTopReviews();
  }

  closeReview() {
    this.showReview = false;
    this.selectedFacility = null;
    this.reservations = 0;
    this.reviews = 0;
    this.isEligible = false;
    this.topReviews = [];
    this.resetForm();
  }

  checkReviewPossibility() {
    if (!this.currentUser || !this.selectedFacility) return;

    this.reviewService.countConfirmedReservations(this.currentUser.id, this.selectedFacility.id).subscribe(data => {
      this.reservations = data;
      
      this.reviewService.countAthleteReviews(this.currentUser!.id, this.selectedFacility!.id).subscribe(data => {
        this.reviews = data;
        this.isEligible = this.reservations > this.reviews;
      });
    });
  }

  loadTopReviews() {
    if (!this.selectedFacility) return;
    this.reviewService.getTopReviewsByFacId(this.selectedFacility.id).subscribe(data => {
      this.topReviews = data;
    });
  }

  saveReview() {
    if (!this.currentUser || !this.selectedFacility || !this.newComment.trim()) return;

    const reviewToSave: FacilityReview = {
      id: 0,
      facilityId: this.selectedFacility.id,
      athleteId: this.currentUser.id,
      liked: this.newLiked,
      comment: this.newComment.trim(),
      reviewDate: new Date()
    };

    this.reviewService.saveReview(reviewToSave).subscribe(success => {
      if (success) {
        this.resetForm();
        this.checkReviewPossibility();
        this.loadTopReviews();
      }
      else {
        alert("error adding review");
      }
    });
  }
  
  cancelReview() {
    this.resetForm();
  }

  resetForm() {
    this.newLiked = true;
    this.newComment = '';
  }
}
