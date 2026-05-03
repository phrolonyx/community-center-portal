import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EnfantsAnimateur } from './enfants-animateur';

describe('EnfantsAnimateur', () => {
  let component: EnfantsAnimateur;
  let fixture: ComponentFixture<EnfantsAnimateur>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [EnfantsAnimateur],
    }).compileComponents();

    fixture = TestBed.createComponent(EnfantsAnimateur);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
