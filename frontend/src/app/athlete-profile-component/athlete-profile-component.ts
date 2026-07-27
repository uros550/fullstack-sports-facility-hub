import { Component, inject, OnInit } from '@angular/core';
import { AthleteProfile } from '../models/AthleteProfile';
import { UserService } from '../services/user-service';
import { FormsModule } from '@angular/forms';
import { User } from '../models/User';
import { SportService } from '../services/sport-service';
import { Sport } from '../models/Sport';
import { AuthenticationService } from '../services/authentication-service';

@Component({
  selector: 'app-athlete-profile-component',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './athlete-profile-component.html',
  styleUrl: './athlete-profile-component.css',
})
export class AthleteProfileComponent implements OnInit {

  //profile and user
  currentUser: User | null = null;
  originalProfile: AthleteProfile | null = null;
  profile: AthleteProfile | null = null;
  //sport
  allSports: Sport[] = [];
  originalSports: number[] = [];
  selectedSports: number[] = [];
  //pfp and preview
  selectedImage: File | null = null;
  imagePreview: string | null = null;
  isGeneratedAvatar: boolean = false;
  isAvatarSaved: boolean =false;
  isRemoveRequested: boolean = false;
  showPhotoDialog: boolean = false;
  currentSeed: string = '';

  successMessage: string = '';

  private userService = inject(UserService);
  private sportService = inject(SportService);
  private authService = inject(AuthenticationService);

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
          this.originalProfile = structuredClone(data);
          if (this.profile.profilePicture) {
            //get preview if exists
            this.imagePreview = 'http://localhost:8080/' + this.profile.profilePicture;
          }
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

  //main methods
  saveChanges() {
    if (this.profile) {
      this.successMessage = '';
      //update profile
      this.userService.updateProfile(this.profile).subscribe(data => {
        if (data === 'Success') {
          //update sports
          this.userService.updateUserSports(this.profile!.id, this.selectedSports).subscribe(data => {
                if (data === 'Success') {
                  //change pfp
                  this.finishPhotoUpdate();
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
    this.resetImageState();
  }

  //sport
  toggleSport(sportId: number) {

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

  //photo pretty much pasted from auth.ts
  openPhotoDialog() {
    this.showPhotoDialog = true;
  }

  closePhotoDialog() {
    this.resetImageState();
  }

  onFileSelected(event: any) {
    const file = event.target.files[0];
    if (file) {
      this.selectedImage = file;
      this.isGeneratedAvatar = false;
      this.isAvatarSaved = false;
      this.isRemoveRequested = false;

      const reader = new FileReader();
      reader.onload = () => {
        this.imagePreview = reader.result as string;
      };
      reader.readAsDataURL(file);
    }
  }

  generateAvatar() {
    this.currentSeed = Math.random().toString(36).substring(7);
    this.imagePreview = `https://api.dicebear.com/9.x/bottts/png?seed=${this.currentSeed}&size=128`;
    this.isGeneratedAvatar = true;
    this.isAvatarSaved = false;
    this.isRemoveRequested = false;
    this.selectedImage = null;
  }

  async saveAvatarAsFile() {
    if (!this.imagePreview) return;

    try {
      const response = await fetch(this.imagePreview);
      const blob = await response.blob();
      const fileName = `avatar-${this.currentSeed}.png`;
      this.selectedImage = new File([blob], fileName, { type: 'image/png' });
      this.isAvatarSaved = true;
    } catch (error) {
      console.error('Error processing generated avatar:', error);
    }
  }

  removePhoto() {
    this.selectedImage = null;
    this.imagePreview = 'default-pfp.jpg';
    this.isGeneratedAvatar = false;
    this.isRemoveRequested = true;
  }

  getCurrentProfilePicture(): string {

    if (this.imagePreview) {
      return this.imagePreview;
    }

    if (this.profile?.profilePicture) {
      return 'http://localhost:8080/' + this.profile.profilePicture;
    }

    return '/default-pfp.jpg';
  }

  //helper
  private resetImageState() {
    this.selectedImage = null;
    this.imagePreview = null;
    this.isGeneratedAvatar = false;
    this.isAvatarSaved = false;
    this.isRemoveRequested = false;
    this.showPhotoDialog = false;

    if (this.originalProfile?.profilePicture) {
      this.imagePreview = 'http://localhost:8080/' + this.originalProfile.profilePicture;
    }
    else {
      this.imagePreview = null;
    }
  }

  private finishPhotoUpdate() {
    if (!this.profile) return;

    if (this.isRemoveRequested) {
        this.userService.removeProfilePicture(this.profile.username).subscribe(data => {
              if (data === 'Success') {
                this.profile!.profilePicture = null;
                this.finishSaving();
              }
            });
        return;
    }

    if (this.selectedImage) {
        this.authService.uploadProfilePicture(this.profile.username, this.selectedImage).subscribe(data => {
          if (data === 'Success') {
            //get new pfp path
            this.userService.getProfileById(this.currentUser!.id).subscribe(freshProfile => {
              if (freshProfile) {
                this.profile!.profilePicture = freshProfile.profilePicture;
              }
              this.finishSaving();
            });
          }
        });
        return;
    }

    this.finishSaving();
  }

  private finishSaving() {
    this.originalProfile = structuredClone(this.profile);
    this.originalSports = [...this.selectedSports];

    this.showPhotoDialog = false;

    //header pfp change
    const currentAuthUser = this.authService.currentUser();

    if (currentAuthUser && this.profile) {
      const updatedPicturePath = this.profile.profilePicture ? this.profile.profilePicture : null;

      const updatedUser: User = {
          ...currentAuthUser,
          profilePicture: this.profile.profilePicture
      };
      
      //update signal
      this.authService.currentUser.set(updatedUser);
      //update local storage
      localStorage.setItem('loggedUser', JSON.stringify(updatedUser));
    }

    this.selectedImage = null;
    this.isGeneratedAvatar = false;
    this.isAvatarSaved = false;
    this.isRemoveRequested = false;

    this.successMessage = 'Profile successfully updated';
  }

} 