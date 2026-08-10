import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Order } from '../models/Order';

@Injectable({
  providedIn: 'root',
})
export class OrderService {
  
  private http = inject(HttpClient);
  private path = 'http://localhost:8080/orders';

  getHistory(athleteId: number) {
    return this.http.get<Order[]>(`${this.path}/history/${athleteId}`);
  }

  getActiveOrders(athleteId: number) {
    return this.http.get<Order[]>(`${this.path}/active/${athleteId}`);
  }

  cancelOrder(orderId: number) {
    return this.http.put<boolean>(`${this.path}/cancel/${orderId}`, {});
  }

}
