import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AuctionsSearchFormComponent } from './auctions-search-form.component';

describe('AuctionsSearchFormComponent', () => {
  let component: AuctionsSearchFormComponent;
  let fixture: ComponentFixture<AuctionsSearchFormComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AuctionsSearchFormComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AuctionsSearchFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
