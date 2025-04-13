import { TestBed } from '@angular/core/testing';

import { AgentReportService } from './agent-report.service';

describe('AgentReportService', () => {
  let service: AgentReportService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AgentReportService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
