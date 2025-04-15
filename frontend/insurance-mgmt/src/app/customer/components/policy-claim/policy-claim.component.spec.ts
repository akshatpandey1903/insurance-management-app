import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PolicyClaimComponent } from './policy-claim.component';

describe('PolicyClaimComponent', () => {
  let component: PolicyClaimComponent;
  let fixture: ComponentFixture<PolicyClaimComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [PolicyClaimComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PolicyClaimComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
