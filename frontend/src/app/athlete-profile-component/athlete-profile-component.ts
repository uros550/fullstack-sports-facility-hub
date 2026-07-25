import { Component, inject, OnInit } from '@angular/core';
import { AthleteProfile } from '../models/AthleteProfile';
import { UserService } from '../services/user-service';
import { FormsModule } from '@angular/forms';
import { User } from '../models/User';
import { SportService } from '../services/sport-service';
import { Sport } from '../models/Sport';

@Component({
  selector: 'app-athlete-profile-component',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './athlete-profile-component.html',
  styleUrl: './athlete-profile-component.css',
})
export class AthleteProfileComponent implements OnInit {

  originalProfile: AthleteProfile | null = null;
  profile: AthleteProfile | null = null;
  allSports: Sport[] = [];
  originalSports: number[] = [];
  selectedSports: number[] = [];
  selectedImage: File | null = null;

  currentUser: User | null = null;

  successMessage: string = '';

  private userService = inject(UserService);
  private sportService = inject(SportService);

  ngOnInit(): void {
    const userJson = localStorage.getItem('loggedUser');

    if (userJson) {
      try {
        this.currentUser = JSON.parse(userJson);
      } catch (error) {
        console.error('Local storage getItem error:', error);
      }
    }

    if (this.currentUser) {

      this.userService.getProfileById(this.currentUser.id).subscribe(data => {
        if(data) {
          this.profile = data;
          this.originalProfile = data;
        }
      });

      this.sportService.getAllSports().subscribe(data => {
        this.allSports = data;
      });

      this.userService.getUserSportIds(this.currentUser.id).subscribe(data => {
        this.selectedSports = data;
        this.originalSports = data;
      });

    }
  }

  toggleSport(sportId: number): void {

    const index = this.selectedSports.indexOf(sportId);

    if (index !== -1) {
      //already selected -> remove
      this.selectedSports.splice(index, 1);
    } 
    else {
      //not selected -> add (already checked if >5 in html)
      this.selectedSports.push(sportId);
    }

  }

  saveChanges() {
    if (this.profile) {
      this.successMessage = '';
      
      this.userService.updateProfile(this.profile).subscribe(data => {
        if (data === 'Success') {
          this.successMessage = 'Successfully changed profile';

          this.userService.updateUserSports(this.profile!.id, this.selectedSports).subscribe(data => {
            if (data === 'Success') {
              this.successMessage = 'Successfully change profile and sports'
              this.originalProfile = structuredClone(this.profile);
              this.originalSports = [...this.selectedSports];
            }
            else {
              this.successMessage = 'Error changing sports';
            }
          })

        }
        else {
          this.successMessage = 'Error changing profile';
        }
      })
    }
  }

  cancelChanges() {
    this.profile = structuredClone(this.originalProfile);
    this.selectedSports = [...this.originalSports];
  }
}