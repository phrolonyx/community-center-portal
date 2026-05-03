import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SceancesAnimateur } from './sceances-animateur';

describe('SceancesAnimateur', () => {
  let component: SceancesAnimateur;
  let fixture: ComponentFixture<SceancesAnimateur>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [SceancesAnimateur],
    }).compileComponents();

    fixture = TestBed.createComponent(SceancesAnimateur);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
