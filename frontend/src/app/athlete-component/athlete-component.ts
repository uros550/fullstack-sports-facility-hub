import { Component, inject, OnInit } from '@angular/core';
import { User } from '../models/User';
import { AthleteProfileComponent } from '../athlete-profile-component/athlete-profile-component';
import { AthleteReservationComponent } from '../athlete-reservation-component/athlete-reservation-component';
import { AthleteTeammatesComponent } from '../athlete-teammates-component/athlete-teammates-component';

@Component({
  selector: 'app-athlete-component',
  imports: [AthleteProfileComponent, AthleteReservationComponent, AthleteTeammatesComponent],
  templateUrl: './athlete-component.html',
  styleUrl: './athlete-component.css',
})
export class AthleteComponent implements OnInit {

  currentUser: User | null = null;
  activeTab: string = 'profile'; //default on open

  ngOnInit(): void {
    const userJson = localStorage.getItem('loggedUser');

    if (userJson) {
      try {
        this.currentUser = JSON.parse(userJson);
        
      } catch (error) {
        console.error('Local storage getItem error:', error);
      }
    }
    //restore search session
    if (history.state.section === 'reservations') {
      this.activeTab = 'reservations';
    }
  }

  setActiveTab(tabName: string): void {
    this.activeTab = tabName;
  }

}
