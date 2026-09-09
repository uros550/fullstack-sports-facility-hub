import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AthleteProfileComponent } from './athlete-profile-component';

describe('AthleteProfileComponent', () => {
  let component: AthleteProfileComponent;
  let fixture: ComponentFixture<AthleteProfileComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AthleteProfileComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AthleteProfileComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
