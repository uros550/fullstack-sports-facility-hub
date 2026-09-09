import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EmployeePDFComponent } from './employee-pdfcomponent';

describe('EmployeePDFComponent', () => {
  let component: EmployeePDFComponent;
  let fixture: ComponentFixture<EmployeePDFComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EmployeePDFComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(EmployeePDFComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
