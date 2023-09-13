import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddToCartPopupComponentComponent } from './add-to-cart-popup-component.component';

describe('AddToCartPopupComponentComponent', () => {
  let component: AddToCartPopupComponentComponent;
  let fixture: ComponentFixture<AddToCartPopupComponentComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AddToCartPopupComponentComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AddToCartPopupComponentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
