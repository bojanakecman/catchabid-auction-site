import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ProfileAuctionsListComponent } from './profile-auctions-list.component';

describe('ProfileAuctionsListComponent', () => {
  let component: ProfileAuctionsListComponent;
  let fixture: ComponentFixture<ProfileAuctionsListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ProfileAuctionsListComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ProfileAuctionsListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
