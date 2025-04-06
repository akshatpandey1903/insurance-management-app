import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CommisionReportsComponent } from './commision-reports.component';

describe('CommisionReportsComponent', () => {
  let component: CommisionReportsComponent;
  let fixture: ComponentFixture<CommisionReportsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [CommisionReportsComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CommisionReportsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
