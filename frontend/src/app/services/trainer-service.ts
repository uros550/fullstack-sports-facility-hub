import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Trainer } from '../models/Trainer';

@Injectable({
  providedIn: 'root',
})
export class TrainerService {

  private http = inject(HttpClient);
  private path = 'http://localhost:8080/trainers';

  getAllTrainers() {
    return this.http.get<Trainer[]>(this.path);
  }

  getAllActiveTrainers() {
    return this.http.get<Trainer[]>(`${this.path}/active`);
  }

  searchTrainers(request: any) {
    return this.http.post<Trainer[]>(`${this.path}/active/search`, request);
  }

  changeIsActive(trainerId: number, activate: boolean) {
    return this.http.put<boolean>(`${this.path}/change/isActive`, null, {
      params: {
        trainerId: trainerId,
        activate: activate
      }
    })
  }

}
