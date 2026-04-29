import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AnimateurSidebar } from './animateur-sidebar';

describe('AnimateurSidebar', () => {
  let component: AnimateurSidebar;
  let fixture: ComponentFixture<AnimateurSidebar>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [AnimateurSidebar],
    }).compileComponents();

    fixture = TestBed.createComponent(AnimateurSidebar);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
