import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PendingAgentApprovalComponent } from './pending-agent-approval.component';

describe('PendingAgentApprovalComponent', () => {
  let component: PendingAgentApprovalComponent;
  let fixture: ComponentFixture<PendingAgentApprovalComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [PendingAgentApprovalComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PendingAgentApprovalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
