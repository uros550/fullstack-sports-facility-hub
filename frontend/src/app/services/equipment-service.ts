import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Equipment } from '../models/Equipment';

@Injectable({
  providedIn: 'root',
})
export class EquipmentService {
  
  private http = inject(HttpClient);
  private path = 'http://localhost:8080/equipment';

  getEquipmentBySport(sportId: number) {
    return this.http.get<Equipment[]>(`${this.path}/${sportId}`);
  }

  updatePriceStock(equipment: Equipment) {
    return this.http.post<boolean>(`${this.path}/update`, equipment);
  }

  addEquipment(formData: FormData) {
    return this.http.post<boolean>(`${this.path}/add`, formData);
  }

}
