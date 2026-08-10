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
  cart: OrderItem[] = [];
  selectedQuantity: number = 1;
  selectedItemId: number = 0;

  showHistory: boolean = false;
  showActive: boolean = false;
  showItems: boolean = false;
  showCart: boolean = false;
  showQuantitySelection: boolean = false;

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

    const savedCart = localStorage.getItem('cart');
    
    if (savedCart) {
      try {
        this.cart = JSON.parse(savedCart);
      } catch (error) {
        console.error('Error parsing cart from localStorage:', error);
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

  getQuantityRange(stock: number): number[] {
    return Array.from({ length: stock }, (_, i) => i + 1);
  }

  openAddition(itemId: number) {
    this.selectedQuantity = 1;
    this.selectedItemId = itemId;
    this.showQuantitySelection = true;
  }

  closeAddition() {
    this.selectedItemId = 0;
    this.showQuantitySelection = false;
  }

  addToCart(item: Equipment) {
    if (!item || this.selectedQuantity <= 0) return;

    //if item already exists in cart
    const existingItem = this.cart.find(cartItem => cartItem.equipmentId === item.id);
    const currentInCart = existingItem ? existingItem.quantity : 0;

    //if exceeds stock
    if (currentInCart + this.selectedQuantity > item.stock) {
      alert(`Exceeds stock limit. Available: ${item.stock}, already in cart: ${currentInCart}.`);
      return;
    }

    //if exists just update quantity
    if (existingItem) {
      existingItem.quantity += this.selectedQuantity;
    }
    //create new orderItem add to cart
    else {
      const newOrderItem: OrderItem = {
        id: 0,
        orderId: 0,
        equipmentId: item.id,
        equipmentName: item.name,
        equipmentImage: item.imageUrl,
        quantity: this.selectedQuantity,
        priceAtPurchase: item.price
      };

      this.cart.push(newOrderItem);
    }

    //save cart so it does not disappear when refreshed
    localStorage.setItem('cart', JSON.stringify(this.cart));
    this.closeAddition();
  }

  removeFromCart(item: OrderItem) {
    if (!item || this.selectedQuantity <= 0) return;

    if (item.quantity > this.selectedQuantity) {
      //subtract quantity
      item.quantity -= this.selectedQuantity;
    }
    else if (item.quantity === this.selectedQuantity) {
      //remove from cart
      this.cart = this.cart.filter(c => c.equipmentId !== item.equipmentId);
    }
    else {
      alert(`Can not remove ${this.selectedQuantity} item(s). In cart only: ${item.quantity}.`);
      return;
    }

    localStorage.setItem('cart', JSON.stringify(this.cart));
    this.closeAddition();
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

  getCartTotal(): number {
    return this.cart.reduce((sum, item) => sum + (item.priceAtPurchase * item.quantity), 0);
  }

  pay() {
    if (!this.currentUser || this.cart.length === 0) return;

    this.orderService.addOrder(this.currentUser.id, this.cart).subscribe(success => {
      if (success) {
        this.successMessage = 'Order placed successfully!';
        this.cart = [];
        localStorage.removeItem('cart');
        this.loadEquipment();
        this.loadOrders();
        this.showCart = false;

        setTimeout(() => {
          this.successMessage = '';
        }, 3000);
      } else {
        alert('Failed to place order. Some items might be out of stock.');
      }
    });
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
