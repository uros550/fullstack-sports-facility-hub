import { TestBed } from '@angular/core/testing';

import { FacilityReviewService } from './facility-review-service';

describe('FacilityReviewService', () => {
  let service: FacilityReviewService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(FacilityReviewService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
