import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WithdrawlsComponent } from './withdrawls.component';

describe('WithdrawlsComponent', () => {
  let component: WithdrawlsComponent;
  let fixture: ComponentFixture<WithdrawlsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [WithdrawlsComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(WithdrawlsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
