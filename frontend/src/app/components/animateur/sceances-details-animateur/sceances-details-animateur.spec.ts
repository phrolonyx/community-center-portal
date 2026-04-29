import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SceancesDetailsAnimateur } from './sceances-details-animateur';

describe('SceancesDetailsAnimateur', () => {
  let component: SceancesDetailsAnimateur;
  let fixture: ComponentFixture<SceancesDetailsAnimateur>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [SceancesDetailsAnimateur],
    }).compileComponents();

    fixture = TestBed.createComponent(SceancesDetailsAnimateur);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
