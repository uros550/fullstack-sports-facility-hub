import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EmployeeFacilitiesComponent } from './employee-facilities-component';

describe('EmployeeFacilitiesComponent', () => {
  let component: EmployeeFacilitiesComponent;
  let fixture: ComponentFixture<EmployeeFacilitiesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EmployeeFacilitiesComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(EmployeeFacilitiesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
