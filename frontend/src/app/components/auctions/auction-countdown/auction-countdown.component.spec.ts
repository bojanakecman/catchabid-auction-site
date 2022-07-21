import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AuctionCountdownComponent } from './auction-countdown.component';

describe('AuctionCountdownComponent', () => {
  let component: AuctionCountdownComponent;
  let fixture: ComponentFixture<AuctionCountdownComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AuctionCountdownComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AuctionCountdownComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
