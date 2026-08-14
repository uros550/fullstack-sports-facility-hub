import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EmployeeReservationsComponent } from './employee-reservations-component';

describe('EmployeeReservationsComponent', () => {
  let component: EmployeeReservationsComponent;
  let fixture: ComponentFixture<EmployeeReservationsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EmployeeReservationsComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(EmployeeReservationsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
