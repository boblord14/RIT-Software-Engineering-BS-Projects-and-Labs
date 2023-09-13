import { ComponentFixture, TestBed } from '@angular/core/testing';

import { LootboxesComponent } from './lootboxes.component';

describe('LootboxesComponent', () => {
  let component: LootboxesComponent;
  let fixture: ComponentFixture<LootboxesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ LootboxesComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(LootboxesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
