import { Component, EventEmitter, inject, Input, OnInit, Output } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Promotion } from '../models/Promotion';
import { SportsFacilityService } from '../services/sports-facility-service';
import { SportService } from '../services/sport-service';
import { PromotionService } from '../services/promotion-service';
import { SportsFacility } from '../models/SportsFacility';
import { Sport } from '../models/Sport';

@Component({
  selector: 'app-employee-promotion-modal-component',
  imports: [ReactiveFormsModule],
  templateUrl: './employee-promotion-modal-component.html',
  styleUrl: './employee-promotion-modal-component.css',
})
export class EmployeePromotionModalComponent  implements OnInit {

  @Input() employeeId!: number;
  @Input() promotionToEdit: Promotion | null = null; //if exists -> editing
  @Output() close = new EventEmitter<boolean>();
  
  private fb = inject(FormBuilder);
  private facilityService = inject(SportsFacilityService);
  private sportService = inject(SportService);
  private promotionService = inject(PromotionService);

  facilities: SportsFacility[] = [];
  sports: Sport[] = [];
  promoForm!: FormGroup;
  minDate: string = '';

  get isEditMode(): boolean {
    if (this.promotionToEdit) {
      return true;
    }
    else {
      return false;
    }
  }

  ngOnInit() {
    const today = new Date();
    this.minDate = today.toISOString().split('.')[0]; //set todays date to yyyy-mm-dd

    this.initForm();

    if (this.isEditMode && this.promotionToEdit) {
      this.setupEditMode(this.promotionToEdit);
    } 
    else {
      this.loadFacilities();
    }
  }

  private initForm() {
    this.promoForm = this.fb.group({
      id: [null],
      facilityId: ['', Validators.required], //on init only this available to select
      sportId: [{ value: '', disabled: true }, Validators.required],
      name: [{ value: '', disabled: true }, Validators.required],
      discountType: [{ value: 'PERCENTAGE', disabled: true }, Validators.required],
      discountValue: [{ value: '', disabled: true }, [Validators.required, Validators.min(1)]],
      startDate: [{ value: '', disabled: true }, Validators.required],
      endDate: [{ value: '', disabled: true }, Validators.required]
    });
  }

  private setupEditMode(promo: Promotion) {
    //get sports for selected facility
    this.sportService.getSportsByFacilityId(promo.facilityId).subscribe(data => {
      this.sports = data;
      
      this.promoForm.patchValue({
        id: promo.id,
        facilityId: promo.facilityId,
        sportId: promo.sportId,
        name: promo.name,
        discountType: promo.discountType,
        discountValue: promo.discountValue,
        startDate: promo.startDate,
        endDate: promo.endDate
      });

      this.promoForm.enable();
      this.promoForm.get('facilityId')?.disable(); 
    });
  }

  loadFacilities() {
    this.facilityService.getFacilitiesForEmployee(this.employeeId).subscribe(data => {
      this.facilities = data;
    });
  }

  changeFacility() {
    if (this.isEditMode) return;

    const facilityId = this.promoForm.get('facilityId')?.value;

    this.sports = [];
    this.promoForm.patchValue({ //updates only sent fields
      sportId: '',
      name: '',
      discountType: 'PERCENTAGE',
      discountValue: '',
      startDate: '',
      endDate: ''
    });

    //if changed facility must select sport again
    this.disableFieldsFrom('sportId');

    if (facilityId) {
      this.sportService.getSportsByFacilityId(facilityId).subscribe(data => {
        this.sports = data;
        this.promoForm.get('sportId')?.enable();
      });
    }
  }

  changeSport() {
    if (this.isEditMode) return;

    const sportId = this.promoForm.get('sportId')?.value;

    this.promoForm.patchValue({
      name: '',
      discountType: 'PERCENTAGE',
      discountValue: '',
      startDate: '',
      endDate: ''
    });

    if (sportId) {
      this.enableFieldsFrom('name');
    } else {
      this.disableFieldsFrom('name');
    }
  }

  changeDate() {
    const start = this.promoForm.get('startDate')?.value;
    const end = this.promoForm.get('endDate')?.value;

    //if endDate chosen and before new startDate, reset
    if (start && end && new Date(end) < new Date(start)) {
      this.promoForm.patchValue({ endDate: '' });
    }
  }

  confirm() {
    if (this.promoForm.invalid) return;

    const formData = this.promoForm.getRawValue(); //gets all fields (locked too)

    //choose which function to call
    const request = this.isEditMode ? this.promotionService.updatePromotion(formData) : this.promotionService.createPromotion(formData);
    request.subscribe(data => {
      if (data === 'Success') {
        this.close.emit(true);
      }
      else {
        alert("error");
      }
    });
  }

  cancel() {
    this.close.emit(false);
  }

  private disableFieldsFrom(fieldName: string) {
    const fields = ['sportId', 'name', 'discountType', 'discountValue', 'startDate', 'endDate'];
    const startIndex = fields.indexOf(fieldName);
    for (let i = startIndex; i < fields.length; i++) {
      this.promoForm.get(fields[i])?.disable();
    }
  }

  private enableFieldsFrom(fieldName: string) {
    const fields = ['name', 'discountType', 'discountValue', 'startDate', 'endDate'];
    const startIndex = fields.indexOf(fieldName);
    for (let i = startIndex; i < fields.length; i++) {
      this.promoForm.get(fields[i])?.enable();
    }
  }

}
