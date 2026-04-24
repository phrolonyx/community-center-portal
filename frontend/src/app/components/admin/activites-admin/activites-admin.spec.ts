import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ActivitesAdmin } from './activites-admin';

describe('ActivitesAdmin', () => {
  let component: ActivitesAdmin;
  let fixture: ComponentFixture<ActivitesAdmin>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ActivitesAdmin],
    }).compileComponents();

    fixture = TestBed.createComponent(ActivitesAdmin);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
