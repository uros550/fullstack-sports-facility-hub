import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AthleteAdsComponent } from './athlete-ads-component';

describe('AthleteAdsComponent', () => {
  let component: AthleteAdsComponent;
  let fixture: ComponentFixture<AthleteAdsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AthleteAdsComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AthleteAdsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
