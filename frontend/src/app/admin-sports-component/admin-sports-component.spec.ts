import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminSportsComponent } from './admin-sports-component';

describe('AdminSportsComponent', () => {
  let component: AdminSportsComponent;
  let fixture: ComponentFixture<AdminSportsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AdminSportsComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AdminSportsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
