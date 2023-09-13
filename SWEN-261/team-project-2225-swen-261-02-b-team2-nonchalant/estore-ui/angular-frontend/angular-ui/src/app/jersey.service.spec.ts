import { TestBed } from '@angular/core/testing';

import { JerseyService } from './jersey.service';

describe('JerseyService', () => {
  let service: JerseyService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(JerseyService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
