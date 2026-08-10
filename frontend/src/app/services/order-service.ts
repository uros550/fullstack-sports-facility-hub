import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Order } from '../models/Order';
import { OrderItem } from '../models/OrderItem';

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

  getOrderItems(orderId: number) {
    return this.http.get<OrderItem[]>(`${this.path}/${orderId}/items`);
  }

  addOrder(athleteId: number, items: OrderItem[]) {
    return this.http.post<boolean>(`${this.path}/add/${athleteId}`, items);
  }

  cancelOrder(orderId: number) {
    return this.http.put<boolean>(`${this.path}/cancel/${orderId}`, {});
  }

}
