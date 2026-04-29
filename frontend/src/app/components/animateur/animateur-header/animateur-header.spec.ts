import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AnimateurHeader } from './animateur-header';

describe('AnimateurHeader', () => {
  let component: AnimateurHeader;
  let fixture: ComponentFixture<AnimateurHeader>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [AnimateurHeader],
    }).compileComponents();

    fixture = TestBed.createComponent(AnimateurHeader);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
