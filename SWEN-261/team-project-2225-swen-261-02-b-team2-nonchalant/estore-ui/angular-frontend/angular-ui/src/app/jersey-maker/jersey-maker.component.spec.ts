import { ComponentFixture, TestBed } from '@angular/core/testing';

import { JerseyMakerComponent } from './jersey-maker.component';

describe('JerseyMakerComponent', () => {
  let component: JerseyMakerComponent;
  let fixture: ComponentFixture<JerseyMakerComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ JerseyMakerComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(JerseyMakerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
