import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { AthleteProfile } from '../models/AthleteProfile';
import { Reservation } from '../models/Reservation';
import { AvailabilitySlot } from '../models/AvailabilitySlot';
import { Application } from '../models/Application';

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

  //RESERVATIONS
  getReservationsByAthleteId(id: number) {
    return this.http.get<Reservation[]>(`${this.path}/reservations/${id}`);
  }

  getActiveReservationsByAthleteId(id: number) {
    return this.http.get<Reservation[]>(`${this.path}/reservations/active/${id}`);
  }

  getActiveReservationsByEmployeeId(id: number) {
    return this.http.get<Reservation[]>(`${this.path}/reservations/employee/${id}`);
  }

  acceptReservation(id: number) {
    return this.http.post(`${this.path}/reservations/accept/${id}`, null, { responseType: 'text' });
  }

  rejectReservation(id: number) {
    return this.http.post(`${this.path}/reservations/reject/${id}`, null, { responseType: 'text' });
  }

  cancelReservation(id: number) {
    return this.http.post(`${this.path}/reservations/cancel/${id}`, null, { responseType: 'text' });
  }

  noShowReservation(id: number, athleteId: number, facilityId: number) {
    return this.http.post(`${this.path}/reservations/noShow/${id}`, null, { params: { athleteId, facilityId }, responseType: 'text'});
  }

  getCourtAvailability(courtId: number, date: string) {
    return this.http.get<AvailabilitySlot[]>(`${this.path}/reservations/availability/${courtId}`, { params: {date: date} });
  }

  addReservation(reservation: Reservation) {
    return this.http.post(`${this.path}/reservations/create`, reservation, { responseType: 'text' });
  }

  updateMissingPlayers(missingPlayers: number, resId: number) {
    return this.http.post<number>(`${this.path}/reservations/updateMP/${resId}/${missingPlayers}`, {});
  } 

  getReservationAds(athleteId: number) {
    return this.http.get<Reservation[]>(`${this.path}/reservations/exploreAds/${athleteId}`);
  }

  getAllApplicationsForAthlete(athleteId: number) {
    return this.http.get<Application[]>(`${this.path}/reservations/checkStatus/${athleteId}`);
  }

  applyToAd(reservationId: number, athleteId: number) {
    return this.http.put<boolean>(`${this.path}/reservations/apply/${reservationId}/${athleteId}`, {});
  }

  changeStatus(reservationId: number, athleteId: number, accept: boolean) {
    return this.http.post<boolean>(`${this.path}/reservations/changeStatus`, null, {
      params: {
        reservationId: reservationId, 
        athleteId: athleteId, 
        accept: accept
      }
    });
  }

  getAllApplicationsByAthlete(athleteId: number) {
    return this.http.get<Application[]>(`${this.path}/reservations/applications/${athleteId}`);
  }
  
}
