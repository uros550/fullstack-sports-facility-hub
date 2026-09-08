import { Component, inject, OnInit } from '@angular/core';
import { User } from '../models/User';
import { AthleteProfileComponent } from '../athlete-profile-component/athlete-profile-component';
import { AthleteReservationComponent } from '../athlete-reservation-component/athlete-reservation-component';
import { AthleteTeammatesComponent } from '../athlete-teammates-component/athlete-teammates-component';
import { AthleteTrainingComponent } from '../athlete-training-component/athlete-training-component';
import { AthleteStoreComponent } from '../athlete-store-component/athlete-store-component';
import { AthleteStatsComponent } from '../athlete-stats-component/athlete-stats-component';
import { AthleteReviewComponent } from '../athlete-review-component/athlete-review-component';

@Component({
  selector: 'app-athlete-component',
  imports: [AthleteProfileComponent, AthleteReservationComponent, AthleteTeammatesComponent, AthleteTrainingComponent, AthleteStoreComponent, AthleteStatsComponent, AthleteReviewComponent],
  templateUrl: './athlete-component.html',
  styleUrl: './athlete-component.css',
})
export class AthleteComponent implements OnInit {

  currentUser: User | null = null;
  activeTab: string = 'profile'; //default on open

  ngOnInit() {
    const userJson = localStorage.getItem('loggedUser');

    if (userJson) {
      try {
        this.currentUser = JSON.parse(userJson);
        
      } catch (error) {
        console.error('Local storage getItem error:', error);
      }
    }

    //if refresh go back to correct tab
    if (history.state?.section) {
      this.activeTab = history.state.section;
    }
  }

  setActiveTab(tabName: string): void {
    this.activeTab = tabName;
    history.replaceState({ ...history.state, section: tabName }, '');
  }

}
