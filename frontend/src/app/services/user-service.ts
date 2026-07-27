import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { AthleteProfile } from '../models/AthleteProfile';

@Injectable({
  providedIn: 'root',
})
export class UserService {

  private http = inject(HttpClient);
  private path = "http://localhost:8080/users";

  getProfileById(id: number) {
    return this.http.get<AthleteProfile>(`${this.path}/profile/${id}`);
  }

  getUserSportIds(id: number) {
    return this.http.get<number[]>(`${this.path}/sports/${id}`);
  }

  updateProfile(profile: AthleteProfile) {
    return this.http.put(`${this.path}/profile`, profile, { responseType: 'text' });
  }

  removeProfilePicture(username: string) {
    return this.http.put(`${this.path}/remove-image`, username, { responseType: 'text' })
  }
  
  updateUserSports(userId: number, sportIds: number[]) {
    return this.http.put(`${this.path}/sports/${userId}`, sportIds, { responseType: 'text' });
  }

}
