import { Component, inject, OnInit } from '@angular/core';
import { User } from '../models/User';
import { AthleteProfileComponent } from '../athlete-profile-component/athlete-profile-component';

@Component({
  selector: 'app-athlete-component',
  imports: [AthleteProfileComponent],
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
  }

  setActiveTab(tabName: string): void {
    this.activeTab = tabName;
  }

}
