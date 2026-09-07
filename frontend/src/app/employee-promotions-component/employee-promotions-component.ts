import { Component, inject, OnInit } from '@angular/core';
import { User } from '../models/User';
import { Promotion } from '../models/Promotion';
import { PromotionService } from '../services/promotion-service';
import { DatePipe, NgClass } from '@angular/common';
import { EmployeePromotionModalComponent } from '../employee-promotion-modal-component/employee-promotion-modal-component';
import { EquipmentService } from '../services/equipment-service';
import { Equipment } from '../models/Equipment';
import { Order } from '../models/Order';
import { OrderService } from '../services/order-service';
import { UserService } from '../services/user-service';
import { FormsModule } from '@angular/forms';
import { SportService } from '../services/sport-service';
import { Sport } from '../models/Sport';

@Component({
  selector: 'app-employee-promotions-component',
  imports: [DatePipe, EmployeePromotionModalComponent, NgClass, FormsModule],
  templateUrl: './employee-promotions-component.html',
  styleUrl: './employee-promotions-component.css',
})
export class EmployeePromotionsComponent implements OnInit {
  
  currentUser: User | null = null;
  promotions: Promotion[] = [];
  equipment: Equipment[] = [];
  orders: Order[] = [];
  selectedView: 'Promo' | 'Equip' | 'Order' = 'Promo';
  showModal: boolean = false;
  currentEmployeeId: number = 0;
  selectedPromotionForEdit: Promotion | null = null;
  selectedEquipmentId: number = 0;
  newPrice: number = 0;
  newStock: number = 0;
  showAdd: boolean = false;
  //new equipment
  sports: Sport[] = [];
  selectedSportId: number = 0;
  newEqName: string = '';
  newEqPrice: number | null = null;
  newEqStock: number | null = null;
  selectedFile: File | null = null;

  successMessage: string = '';

  private promoService = inject(PromotionService);
  private equipmentService = inject(EquipmentService);
  private orderService = inject(OrderService);
  private userService = inject(UserService);
  private sportService = inject(SportService);

  ngOnInit() {
    const userJson = localStorage.getItem('loggedUser');

    if (userJson) {
      try {
        this.currentUser = JSON.parse(userJson);
      } catch (error) {
        console.error('Local storage getItem error:', error);
      }
    }

    if (this.currentUser) {
      this.currentEmployeeId = this.currentUser.id;
      this.loadData();
    }
  }

  loadData() {
    this.loadPromotions();
    this.loadEquipment();
    this.loadOrders();
    this.loadSports();
  }

  loadPromotions() {
    this.promoService.getPromotionsByEmployeeId(this.currentUser!.id).subscribe(data => {
      this.promotions = data;
    })
  }

  loadEquipment() {
    this.equipmentService.getEquipmentBySport(0).subscribe(data => {
      this.equipment = data;
    })
  }

  loadOrders() {
    this.orderService.getAllOrders().subscribe(data => {
      this.orders = data;
      this.orders.forEach(o => {
        this.userService.getProfileById(o.athleteId).subscribe(user => {
            o.athleteName = user.username;
        })
      })
    })
  }

  loadSports() {
    this.sportService.getAllSports().subscribe(data => {
      this.sports = data;
      if (this.sports.length > 0) {
        this.selectedSportId = this.sports[0].id;
      }
    })
  }

  toggleView(view: 'Promo' | 'Equip' | 'Order') {
    this.selectedView = view;
  }

  openCreate() {
    this.selectedPromotionForEdit = null;
    this.showModal = true;
  }

  openUpdate(promo: Promotion) {
    this.selectedPromotionForEdit = promo;
    this.showModal = true;
  }

  closeModal(isSuccess: boolean) {
    this.showModal = false;
    this.selectedPromotionForEdit = null;

    if (isSuccess) {
      this.loadPromotions();
    }
  }

  changeStatus(orderId: number, status: 'PICKED_UP' | 'CANCELLED') {
    if (status === 'CANCELLED') {
      this.orderService.cancelOrder(orderId).subscribe(data => {
        if (data) {
          this.successMessage = 'Successfully changed';
          this.loadOrders();
        }
        else {
          this.successMessage = 'Error';
        }
        this.clearMessageAfterDelay();
      })
    }
    else {
      this.orderService.pickedOrder(orderId).subscribe(data => {
        if (data) {
          this.successMessage = 'Successfully changed';
          this.loadOrders();
        }
        else {
          this.successMessage = 'Error';
        }
        this.clearMessageAfterDelay();
      })
    }
  }

  openEdit(item: Equipment) {
    this.selectedEquipmentId = item.id;
    this.newPrice = item.price;
    this.newStock = item.stock;
  }

  confirmEdit(item: Equipment) {
    if (this.newPrice < 0 || this.newStock < 0) return;

    const updatedEquipment = {...item, price: this.newPrice, stock: this.newStock};

    this.equipmentService.updatePriceStock(updatedEquipment).subscribe(data => {
      if (data) {
        this.loadEquipment();
        this.selectedEquipmentId = 0;
        this.successMessage = 'Successfully updated';
      }
      else {
        this.successMessage = 'Error';
      }
      this.clearMessageAfterDelay();
    })
  }

  cancelEdit() {
    this.selectedEquipmentId = 0;
  }

  openAdd() {
    this.showAdd = true;
  }

  closeAdd() {
    this.showAdd = false;
    this.newEqName = '';
    this.newEqPrice = null;
    this.newEqStock = null;
    this.selectedFile = null;
    if (this.sports.length > 0) {
      this.selectedSportId = this.sports[0].id;
    }
  }

  changeFile(event: Event) {
    const input = event.target as HTMLInputElement;
    if (input.files && input.files.length > 0) {
      this.selectedFile = input.files[0];
    }
  }

  createEquipment() {
    if (
      !this.newEqName.trim() ||
      !this.selectedSportId ||
      this.newEqPrice === null ||
      this.newEqPrice <= 0 ||
      this.newEqStock === null ||
      this.newEqStock < 0 ||
      !this.selectedFile
    ){
      this.successMessage = 'Error: Fill in all required fields correctly';
      this.clearMessageAfterDelay();
      return;
    }

    const formData = new FormData();
    formData.append('name', this.newEqName);
    formData.append('sportId', this.selectedSportId.toString());
    formData.append('price', this.newEqPrice.toString());
    formData.append('stock', this.newEqStock.toString());
    formData.append('image', this.selectedFile);

    this.equipmentService.addEquipment(formData).subscribe(data => {
      if (data) {
        this.successMessage = 'Successfully added equipment';
        this.loadEquipment();
        this.closeAdd();
      } 
      else {
        this.successMessage = 'Error while adding equipment';
      }
      this.clearMessageAfterDelay();
    });
  }

  clearMessageAfterDelay() {
    setTimeout(() => {
      this.successMessage = '';
    }, 5000);
  }

}
