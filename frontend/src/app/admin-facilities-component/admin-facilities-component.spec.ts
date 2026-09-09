import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminFacilitiesComponent } from './admin-facilities-component';

describe('AdminFacilitiesComponent', () => {
  let component: AdminFacilitiesComponent;
  let fixture: ComponentFixture<AdminFacilitiesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AdminFacilitiesComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AdminFacilitiesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
