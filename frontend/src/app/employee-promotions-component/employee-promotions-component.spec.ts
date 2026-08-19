import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EmployeePromotionsComponent } from './employee-promotions-component';

describe('EmployeePromotionsComponent', () => {
  let component: EmployeePromotionsComponent;
  let fixture: ComponentFixture<EmployeePromotionsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EmployeePromotionsComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(EmployeePromotionsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
