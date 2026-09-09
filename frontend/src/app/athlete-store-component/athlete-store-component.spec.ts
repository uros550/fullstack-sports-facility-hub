import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AthleteStoreComponent } from './athlete-store-component';

describe('AthleteStoreComponent', () => {
  let component: AthleteStoreComponent;
  let fixture: ComponentFixture<AthleteStoreComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AthleteStoreComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AthleteStoreComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
