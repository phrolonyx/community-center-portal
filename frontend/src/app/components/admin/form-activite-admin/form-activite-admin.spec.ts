import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FormActiviteAdmin } from './form-activite-admin';

describe('FormActiviteAdmin', () => {
  let component: FormActiviteAdmin;
  let fixture: ComponentFixture<FormActiviteAdmin>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [FormActiviteAdmin],
    }).compileComponents();

    fixture = TestBed.createComponent(FormActiviteAdmin);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
