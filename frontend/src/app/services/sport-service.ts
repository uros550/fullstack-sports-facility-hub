import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Sport } from '../models/Sport';

@Injectable({
  providedIn: 'root',
})

export class SportService {

  private http = inject(HttpClient);
  private path = 'http://localhost:8080/sports';

  getAllSports() {
    return this.http.get<Sport[]>(this.path);
  }

}
