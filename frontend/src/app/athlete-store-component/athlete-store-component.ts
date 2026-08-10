import { Component, inject, OnInit } from '@angular/core';
import { User } from '../models/User';
import { OrderService } from '../services/order-service';
import { Order } from '../models/Order';
import { DatePipe } from '@angular/common';
import { OrderItem } from '../models/OrderItem';
import { SportService } from '../services/sport-service';
import { Sport } from '../models/Sport';
import { EquipmentService } from '../services/equipment-service';
import { Equipment } from '../models/Equipment';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-athlete-store-component',
  imports: [FormsModule, DatePipe],
  templateUrl: './athlete-store-component.html',
  styleUrl: './athlete-store-component.css',
})
export class AthleteStoreComponent implements OnInit {

  currentUser: User | null = null;
  activeOrders: Order[] = [];
  historyOrders: Order[] = [];
  sports: Sport[] = [];
  orderItems: OrderItem[] = [];
  equipment: Equipment[] = [];
  selectedSportId: number = 0;
  cart: Equipment[] = [];

  showHistory: boolean = false;
  showActive: boolean = false;
  showItems: boolean = false;
  showCart: boolean = false;

  successMessage: string = '';

  private orderService = inject(OrderService);
  private sportService = inject(SportService);
  private equipmentService = inject(EquipmentService);

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
    this.loadEquipment();
    this.loadSports();
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

  loadEquipment() {
   this.equipmentService.getEquipmentBySport(this.selectedSportId).subscribe(data => {
      this.equipment = data;
    })
  }

  loadSports() {
    this.sportService.getAllSports().subscribe(data => {
      this.sports = data;
    })
  }

  changeSport() {
    this.loadEquipment();
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

  openCart() {
    this.showCart = true;
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
    this.showCart = false;
    this.successMessage = '';
  }

  openItems(orderId: number) {
    this.showItems = true;
    this.orderItems = [];
    this.orderService.getOrderItems(orderId).subscribe(data => {
      this.orderItems = data;
    })
  }

  closeItems() {
    this.showItems = false;
  }

}
