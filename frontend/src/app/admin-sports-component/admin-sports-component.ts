import { Component, inject, OnInit } from '@angular/core';
import { Sport } from '../models/Sport';
import { User } from '../models/User';
import { SportService } from '../services/sport-service';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-admin-sports-component',
  imports: [FormsModule],
  templateUrl: './admin-sports-component.html',
  styleUrl: './admin-sports-component.css',
})
export class AdminSportsComponent implements OnInit {

  currentUser: User | null = null;
  sports: Sport[] = [];

  newSportName: string = '';
  newSportReqPlayers: number = 1;
  
  successMessage: string = '';

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
      this.sportService.getAllSports().subscribe(data => {
        this.sports = data;
      })
    }
  }

  addSport() {
    if (!this.newSportName.trim()) {
      return;
    }

    const newSport: Sport = {
      id: 0,
      name: this.newSportName,
      requiredPlayers: this.newSportReqPlayers
    };

    this.sportService.addSport(newSport).subscribe(data => {
      if (data) {
        this.successMessage = 'Successfully added sport';
        this.sports.push(newSport);
      }
      else {
        this.successMessage = 'Error while adding sport';
      }
      this.cancelSport();
      this.clearMessageAfterDelay();
    })
  }

  cancelSport() {
    this.newSportName = '';
    this.newSportReqPlayers = 1;
  }
  
  clearMessageAfterDelay() {
    setTimeout(() => {
      this.successMessage = '';
    }, 5000);
  }

}
