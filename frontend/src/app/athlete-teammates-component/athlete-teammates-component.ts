import { Component, inject, OnInit } from '@angular/core';
import { User } from '../models/User';
import { Reservation } from '../models/Reservation';
import { UserService } from '../services/user-service';
import { DatePipe } from '@angular/common';
import { SportService } from '../services/sport-service';
import { FormsModule } from '@angular/forms';
import { AthleteAdsComponent } from '../athlete-ads-component/athlete-ads-component';

@Component({
  selector: 'app-athlete-teammates-component',
  imports: [DatePipe, FormsModule, AthleteAdsComponent],
  templateUrl: './athlete-teammates-component.html',
  styleUrl: './athlete-teammates-component.css',
})
export class AthleteTeammatesComponent implements OnInit {

  currentUser: User | null = null;
  reservations: Reservation[] = [];
  activeReservationId: number = 0;
  requiredPlayersOptions: number[] = [];
  selectedMissingPlayers: number = 1;
  
  showAds: boolean = false;
  adsMode: 'my' | 'explore' = 'my';

  successMessage = '';

  private userService = inject(UserService);
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
      this.userService.getActiveReservationsByAthleteId(this.currentUser.id).subscribe(data => {
        if (data) {
          this.reservations = data;
        }
      })
    }
  }
  
  postAnAd(reservation: Reservation) {
    this.activeReservationId = reservation.id;

    this.sportService.getMaxMissingPlayers(reservation.sportId).subscribe(data => {
      const max = data - 1;
      this.requiredPlayersOptions = Array.from({ length: max > 0 ? max : 0 }, (_, i) => i + 1);
      this.selectedMissingPlayers = 1;
    });
  }

  closePostAnAd() {
    this.activeReservationId = 0;
    this.requiredPlayersOptions = [];
    this.selectedMissingPlayers = 1;
  }

  confirmPostAnAd() {
    this.userService.updateMissingPlayers(this.selectedMissingPlayers, this.activeReservationId).subscribe(data => {
      if (data) {
        this.successMessage = 'Successfully posted';
      }
      else {
        this.successMessage = 'Error';
      }
    })
    console.log("ResId: " + this.activeReservationId + " count: " + this.selectedMissingPlayers);
    this.closePostAnAd();
  }

  openMyAds() {
    this.adsMode = 'my';
    this.showAds = true;
  }

  openExploreAds() {
    this.adsMode = 'explore';
    this.showAds = true;
  }

  closeAds() {
    this.showAds = false;
  }

}
