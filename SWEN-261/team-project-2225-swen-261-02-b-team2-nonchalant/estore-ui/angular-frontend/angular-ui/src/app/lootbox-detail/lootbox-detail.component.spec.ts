import { ComponentFixture, TestBed } from '@angular/core/testing';

import { LootboxDetailComponent } from './lootbox-detail.component';

describe('LootboxDetailComponent', () => {
  let component: LootboxDetailComponent;
  let fixture: ComponentFixture<LootboxDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ LootboxDetailComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(LootboxDetailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
