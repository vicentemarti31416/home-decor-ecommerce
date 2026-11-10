import { ComponentFixture, TestBed } from '@angular/core/testing';

import { Dateselector } from './dateselector';

describe('Dateselector', () => {
  let component: Dateselector;
  let fixture: ComponentFixture<Dateselector>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [Dateselector]
    })
    .compileComponents();

    fixture = TestBed.createComponent(Dateselector);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
