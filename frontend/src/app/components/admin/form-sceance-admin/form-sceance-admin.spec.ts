import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FormSceanceAdmin } from './form-sceance-admin';

describe('FormSceanceAdmin', () => {
  let component: FormSceanceAdmin;
  let fixture: ComponentFixture<FormSceanceAdmin>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [FormSceanceAdmin],
    }).compileComponents();

    fixture = TestBed.createComponent(FormSceanceAdmin);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
