import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminCoachesComponent } from './admin-coaches-component';

describe('AdminCoachesComponent', () => {
  let component: AdminCoachesComponent;
  let fixture: ComponentFixture<AdminCoachesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AdminCoachesComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AdminCoachesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
