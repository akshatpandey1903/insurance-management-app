import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AgentCommisionReportComponent } from './agent-commision-report.component';

describe('AgentCommisionReportComponent', () => {
  let component: AgentCommisionReportComponent;
  let fixture: ComponentFixture<AgentCommisionReportComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [AgentCommisionReportComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AgentCommisionReportComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
