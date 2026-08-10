import { Component, inject, OnInit } from '@angular/core';
import { User } from '../models/User';
import { OrderService } from '../services/order-service';
import { Order } from '../models/Order';

@Component({
  selector: 'app-athlete-store-component',
  imports: [],
  templateUrl: './athlete-store-component.html',
  styleUrl: './athlete-store-component.css',
})
export class AthleteStoreComponent implements OnInit {

  currentUser: User | null = null;
  activeOrders: Order[] = [];
  historyOrders: Order[] = [];

  showHistory: boolean = false;
  showActive: boolean = false;

  successMessage: string = '';

  private orderService = inject(OrderService);

  ngOnInit() {
    const userJson = localStorage.getItem('loggedUser');

    if (userJson) {
      try {
        this.currentUser = JSON.parse(userJson);
      } catch (error) {
        console.error('Local storage getItem error:', error);
      }
    }
    
    this.loadOrders();
  }

  loadOrders() {
    if (!this.currentUser) return;

    this.orderService.getActiveOrders(this.currentUser.id).subscribe(data => {
      this.activeOrders = data;
    });

    this.orderService.getHistory(this.currentUser.id).subscribe(data => {
      this.historyOrders = data;
    });
  }

  cancelOrder(orderId: number) {
    const isConfirmed = window.confirm('Are you sure you want to cancel this order?');

    if (isConfirmed) {
      this.orderService.cancelOrder(orderId).subscribe(data => {
        if (data) {
          this.successMessage = 'Succesfully cancelled';
          this.loadOrders();
          //show message only for 3 secs
          setTimeout(() => {
            this.successMessage = '';
          }, 3000);
        }
        else {
          this.successMessage = 'Order could not be cancelled';
        }
      })
    }
  }

  openHistory() {
    this.showHistory = true;
  }

  openActive() {
    this.showActive = true;
  }

  close() {
    this.showHistory = false;
    this.showActive = false;
    this.successMessage = '';
  }
}
