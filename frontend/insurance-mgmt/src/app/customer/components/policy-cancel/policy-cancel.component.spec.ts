import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PolicyCancelComponent } from './policy-cancel.component';

describe('PolicyCancelComponent', () => {
  let component: PolicyCancelComponent;
  let fixture: ComponentFixture<PolicyCancelComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [PolicyCancelComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PolicyCancelComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
