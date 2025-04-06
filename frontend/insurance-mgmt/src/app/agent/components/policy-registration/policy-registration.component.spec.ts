import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PolicyRegistrationComponent } from './policy-registration.component';

describe('PolicyRegistrationComponent', () => {
  let component: PolicyRegistrationComponent;
  let fixture: ComponentFixture<PolicyRegistrationComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [PolicyRegistrationComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PolicyRegistrationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
