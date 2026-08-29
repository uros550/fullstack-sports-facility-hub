import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminPendingUsersComponent } from './admin-pending-users-component';

describe('AdminPendingUsersComponent', () => {
  let component: AdminPendingUsersComponent;
  let fixture: ComponentFixture<AdminPendingUsersComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AdminPendingUsersComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AdminPendingUsersComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
