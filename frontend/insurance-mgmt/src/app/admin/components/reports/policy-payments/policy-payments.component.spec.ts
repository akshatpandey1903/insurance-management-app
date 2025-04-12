import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PolicyPaymentsComponent } from './policy-payments.component';

describe('PolicyPaymentsComponent', () => {
  let component: PolicyPaymentsComponent;
  let fixture: ComponentFixture<PolicyPaymentsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [PolicyPaymentsComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PolicyPaymentsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
