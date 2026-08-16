import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Training } from '../models/Training';

@Injectable({
  providedIn: 'root',
})
export class TrainingService {
  
  private http = inject(HttpClient);
  private path = 'http://localhost:8080/trainings';

  getAllTrainingsById(athleteId: number) {
    return this.http.get<Training[]>(`${this.path}/athlete/${athleteId}`);
  }

  getActiveTrainingsByEmployeeId(employeeId: number) {
    return this.http.get<Training[]>(`${this.path}/employee/${employeeId}`);
  }

  reserveTraining(newTraining: Training) {
    return this.http.post(`${this.path}/create`, newTraining, { responseType: 'text' });
  }

  acceptTraining(id: number) {
    return this.http.post(`${this.path}/accept/${id}`, null, { responseType: 'text' });
  }

  rejectTraining(id: number) {
    return this.http.post(`${this.path}/reject/${id}`, null, { responseType: 'text' });
  }

  noShowTraining(id: number, athleteId: number, facilityId: number) {
    return this.http.post(`${this.path}/noShow/${id}`, null, { params: { athleteId, facilityId }, responseType: 'text'});
  }  
  
}
