import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AthleteReviewComponent } from './athlete-review-component';

describe('AthleteReviewComponent', () => {
  let component: AthleteReviewComponent;
  let fixture: ComponentFixture<AthleteReviewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AthleteReviewComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AthleteReviewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
