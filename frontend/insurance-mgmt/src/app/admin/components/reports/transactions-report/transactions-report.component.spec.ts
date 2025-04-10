import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TransactionsReportComponent } from './transactions-report.component';

describe('TransactionsReportComponent', () => {
  let component: TransactionsReportComponent;
  let fixture: ComponentFixture<TransactionsReportComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [TransactionsReportComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(TransactionsReportComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
