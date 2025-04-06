import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WithdrawlApprovalComponent } from './withdrawl-approval.component';

describe('WithdrawlApprovalComponent', () => {
  let component: WithdrawlApprovalComponent;
  let fixture: ComponentFixture<WithdrawlApprovalComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [WithdrawlApprovalComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(WithdrawlApprovalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
