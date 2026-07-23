import { Component, OnInit } from '@angular/core';
import { User } from '../models/User';

@Component({
  selector: 'app-athlete-component',
  imports: [],
  templateUrl: './athlete-component.html',
  styleUrl: './athlete-component.css',
})
export class AthleteComponent implements OnInit {

  currentUser: User | null = null;

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

}
