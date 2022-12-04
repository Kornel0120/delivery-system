import { TestBed } from '@angular/core/testing';

import { ProfileResolverService } from './profile-resolver.service';

describe('ProfileResolver', () => {
  let resolver: ProfileResolverService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    resolver = TestBed.inject(ProfileResolverService);
  });

  it('should be created', () => {
    expect(resolver).toBeTruthy();
  });
});
