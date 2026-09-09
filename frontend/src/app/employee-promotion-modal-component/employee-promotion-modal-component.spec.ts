import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EmployeePromotionModalComponent } from './employee-promotion-modal-component';

describe('EmployeePromotionModalComponent', () => {
  let component: EmployeePromotionModalComponent;
  let fixture: ComponentFixture<EmployeePromotionModalComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EmployeePromotionModalComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(EmployeePromotionModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
