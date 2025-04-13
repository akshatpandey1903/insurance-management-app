import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CommissionReportComponent } from './commission-report.component';

describe('CommissionReportComponent', () => {
  let component: CommissionReportComponent;
  let fixture: ComponentFixture<CommissionReportComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [CommissionReportComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CommissionReportComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
