import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SceancesAdmin } from './sceances-admin';

describe('SceancesAdmin', () => {
  let component: SceancesAdmin;
  let fixture: ComponentFixture<SceancesAdmin>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [SceancesAdmin],
    }).compileComponents();

    fixture = TestBed.createComponent(SceancesAdmin);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
