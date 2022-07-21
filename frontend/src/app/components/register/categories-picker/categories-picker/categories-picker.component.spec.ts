import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CategoriesPickerComponent } from './categories-picker.component';

describe('CategoriesPickerComponent', () => {
  let component: CategoriesPickerComponent;
  let fixture: ComponentFixture<CategoriesPickerComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CategoriesPickerComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(CategoriesPickerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
