import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AllAgentsComponent } from './all-agents.component';

describe('AllAgentsComponent', () => {
  let component: AllAgentsComponent;
  let fixture: ComponentFixture<AllAgentsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [AllAgentsComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AllAgentsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
