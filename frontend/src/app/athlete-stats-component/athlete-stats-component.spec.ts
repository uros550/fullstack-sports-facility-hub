import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AthleteStatsComponent } from './athlete-stats-component';

describe('AthleteStatsComponent', () => {
  let component: AthleteStatsComponent;
  let fixture: ComponentFixture<AthleteStatsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AthleteStatsComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AthleteStatsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
