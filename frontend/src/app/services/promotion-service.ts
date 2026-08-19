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

  getPromotionsByEmployeeId(employeeId: number) {
    return this.http.get<Promotion[]>(`${this.path}/employee/${employeeId}`);
  }

  createPromotion(promotion: Promotion) {
    return this.http.post(`${this.path}/create`, promotion, { responseType: 'text' });
  }

  updatePromotion(promotion: Promotion) {
    return this.http.put(`${this.path}/update`, promotion, { responseType: 'text' });
  }
  
}
