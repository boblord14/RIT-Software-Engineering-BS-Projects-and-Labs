import { ComponentFixture, TestBed } from '@angular/core/testing';

import { JerseyImageComponent } from './jersey-image.component';

describe('JerseyImageComponent', () => {
  let component: JerseyImageComponent;
  let fixture: ComponentFixture<JerseyImageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ JerseyImageComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(JerseyImageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
