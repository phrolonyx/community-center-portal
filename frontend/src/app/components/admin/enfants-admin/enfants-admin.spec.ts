import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EnfantsAdmin } from './enfants-admin';

describe('EnfantsAdmin', () => {
  let component: EnfantsAdmin;
  let fixture: ComponentFixture<EnfantsAdmin>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [EnfantsAdmin],
    }).compileComponents();

    fixture = TestBed.createComponent(EnfantsAdmin);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
