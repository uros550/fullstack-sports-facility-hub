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

  reserveTraining(newTraining: Training) {
    return this.http.post(`${this.path}/create`, newTraining, { responseType: 'text' });
  }
  
}
