import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AuctionCancellationConfirmationComponent } from './auction-cancellation-confirmation.component';

describe('AuctionCancellationConfirmationComponent', () => {
  let component: AuctionCancellationConfirmationComponent;
  let fixture: ComponentFixture<AuctionCancellationConfirmationComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AuctionCancellationConfirmationComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AuctionCancellationConfirmationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
