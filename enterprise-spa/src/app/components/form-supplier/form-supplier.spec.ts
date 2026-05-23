import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FormSupplier } from './form-supplier';

describe('FormSupplier', () => {
  let component: FormSupplier;
  let fixture: ComponentFixture<FormSupplier>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [FormSupplier]
    })
    .compileComponents();

    fixture = TestBed.createComponent(FormSupplier);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
