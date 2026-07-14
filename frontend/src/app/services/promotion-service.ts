import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Promotion } from '../models/Promotion';

@Injectable({
  providedIn: 'root',
})
export class PromotionService {

  private http = inject(HttpClient);
  private path = 'http://localhost:8080/promotions';

  getActivePromotions() {
    return this.http.get<Promotion[]>(`${this.path}/active`);
  }

}
