import { TestBed } from '@angular/core/testing';

import { SportsFacilityService } from './sports-facility-service';

describe('SportsFacilityService', () => {
  let service: SportsFacilityService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SportsFacilityService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
