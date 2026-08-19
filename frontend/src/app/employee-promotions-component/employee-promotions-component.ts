import { Component, inject, OnInit } from '@angular/core';
import { User } from '../models/User';
import { Promotion } from '../models/Promotion';
import { PromotionService } from '../services/promotion-service';
import { DatePipe } from '@angular/common';
import { EmployeePromotionModalComponent } from '../employee-promotion-modal-component/employee-promotion-modal-component';

@Component({
  selector: 'app-employee-promotions-component',
  imports: [DatePipe, EmployeePromotionModalComponent],
  templateUrl: './employee-promotions-component.html',
  styleUrl: './employee-promotions-component.css',
})
export class EmployeePromotionsComponent implements OnInit {
  
  currentUser: User | null = null;
  promotions: Promotion[] = [];
  showPromo: boolean = true;
  showModal: boolean = false;
  currentEmployeeId: number = 0;
  selectedPromotionForEdit: Promotion | null = null;

  private promoService = inject(PromotionService);

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
    //load equipment
  }

  loadPromotions() {
    this.promoService.getPromotionsByEmployeeId(this.currentUser!.id).subscribe(data => {
      this.promotions = data;
    })
  }

  toggleView(isPromo: boolean) {
    this.showPromo = isPromo;
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
}
